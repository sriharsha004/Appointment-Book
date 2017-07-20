package edu.pdx.cs410J.makineni;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.family.PrettyPrinter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet provides a REST API for working with an
 * AppointmentBook.  It is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet {
    private final Map<String, AppointmentBook> data = new HashMap<String, AppointmentBook>();
    private AppointmentBook appointmentBook = null;

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String owner = getParameter("owner", request);
        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);
        if (owner != null && beginTime != null && endTime != null) {
            Appointment appointment = new Appointment();
            appointment.setDate(beginTime, endTime);
            //client is performing a search
            //return all appointments that begin between the start and end time
            writeSearchValue(new AppointmentBook(owner, appointment, "-search"), response);
        } else if (owner != null && beginTime == null && endTime == null) {
            //Client is trying writevalue
            writeValue(owner, response);
        } else {
            writeAllMappings(response);
        }
    }


    /**
     * To write all the mappings
     * @param response response
     * @throws IOException IOException
     */
    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( data.size() ));

        for (Map.Entry<String, AppointmentBook> entry : this.data.entrySet()) {
            pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue().toString()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }


    /**
     * Method to write search values
     * @param owner name
     * @param response response
     * @throws IOException exception
     */
    private void writeValue( String owner, HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        if(data.get(owner)!= null) {
            pw.println(data.get(owner).toString());
            int counter=0;
            Collection<Appointment> appts = data.get(owner).getAppointments();
            //pw.println(owner);
            for(Appointment appt: appts){
                pw.println(++counter +" "+ owner+ "  "+appt.getDescription()+ "  "+appt.getBeginTimeString()+"  "+appt.getEndTimeString()+  "   "+appt.duration()+"\n");
            }
        }
        else
            pw.println("Customer does not exists");

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Method to write search values
     * @param book type AppointmentBook
     * @param response response
     * @throws IOException Exception
     */
    private void writeSearchValue(AppointmentBook book, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String ownerName = book.getOwnerName();
        Long begin = book.searchAppt.getBeginTime().getTime();
        Long end = book.searchAppt.getEndTime().getTime();

        if (data.get(ownerName) != null) {
            pw.println("Searching for: " + data.get(ownerName).toString());
            Collection<Appointment> appts = data.get(ownerName).getAppointments();
            boolean flag = true;
            int counter = 0;

            for (Appointment appt : appts) {
                Long bdate = appt.getBeginTime().getTime();
                Long edate = appt.getEndTime().getTime();
                if (end >= edate && begin <= bdate  && begin <= edate) {
                    flag = false;
                    pw.println(++counter + " " + ownerName + "  " + appt.getDescription() + "  " + appt.getBeginTimeString() + "   " + appt.getEndTimeString() + "   " + appt.duration() + "\n");
                }
            }
            if (flag) {

               // pw.println("123");
                pw.println("No appointments match for: " + ownerName);
            }
        } else
            pw.println("Customer does not exists");

        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }


    /**
     * Method to handle missing parameters
     * @param response response
     * @param parameterName Parameter name
     * @throws IOException exception
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
            throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(parameterName));
        pw.flush();

        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");


        String owner = getParameter("owner", request);
        if (owner == null) {
            missingRequiredParameter(response, "customer");
            return;
        }
        String desc = getParameter("description", request);
        if (desc == null) {
            missingRequiredParameter(response, "description");
            return;
        }

        String beginTime = getParameter("beginTime", request);
        if (beginTime == null) {
            missingRequiredParameter(response, "beginTime");
            return;
        }
        String endTime = getParameter("endTime", request);
        if (endTime == null) {
            missingRequiredParameter(response, "endTime");
            return;
        }

        PrintWriter pw = response.getWriter();
        //Now we have all relavent information about customers and their appointments
        if (data != null && data.get(owner) != null) {
            //Customer exists, just add a new appointment
            appointmentBook = data.get(owner);
            appointmentBook.addAppointment(new Appointment(desc,beginTime, endTime));
            data.put(owner, appointmentBook);
            System.out.println("attempting to add new appointmentbook");


        } else {
            //Customer doesn't exist, create a new appointmentbook.
            data.put(owner, new AppointmentBook(owner, new Appointment(desc, beginTime, endTime)));
            System.out.println("new owner added");
           // pw.println("attempting to add a new owner");

        }

        int counter = 0;
        Collection<Appointment> appts = data.get(owner).getAppointments();

            //pw.println(owner);
        for (Appointment appt : appts) {
            pw.println(++counter + " " + owner + "  " + appt.getDescription() + "  " + appt.getBeginTimeString() + "  " + appt.getEndTimeString() + "   " + appt.duration() + "\n");
        }

        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * One method to get the parameters
     * @param name parameter name
     * @param request http request
     * @return null/value
     */
    private String getParameter(String name, HttpServletRequest request) {
        String value = request.getParameter(name);
        if (value == null || "".equals(value)) {
            return null;

        } else {
            return value;
        }
    }
}