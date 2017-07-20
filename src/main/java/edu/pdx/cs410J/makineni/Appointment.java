package edu.pdx.cs410J.makineni;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Appointment class extends AbstractAppointment
 */
public class Appointment extends AbstractAppointment implements java.lang.Comparable<Appointment> {
    private Date bdate;
    private Date edate;
    private String desc;
    private DateFormat ShortDateFormat;

    /**
     * This is an empty constructor
     * @param s String
     *
     */
    public Appointment(String s)
    {}

    public Appointment (){}

    /**
     * Function to setDate
     * @param start start date
     * @param end end date
     */
    public void setDate(String start, String end){
//       System.out.println("No start");
//        System.out.println(start+"[passed]");
        ShortDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.ENGLISH);
        try {
            this.bdate = ShortDateFormat.parse(start);
            //System.out.println(bdate);
            this.edate = ShortDateFormat.parse(end);
        }
        catch(ParseException ex){
            System.out.println("Error Parsing the time, please enter valid time, dont forget to include am/pm " +ex.getMessage());
            System.exit(1);
        }
    }
    /**
     * This is a constructor for the Appoinment class with the following parameters
     * @param desc description of appt
     * @param begTime begin time
     * @param endTime end time
     */
    public Appointment(String desc, String begTime,String endTime) {

        ShortDateFormat = new SimpleDateFormat("MM/dd/yyy hh:mm a", Locale.ENGLISH);
        //Check for bad data
        try{
            if(begTime.contains("\"")||endTime.contains("\""))
                throw new IllegalArgumentException("Date and time cannot contain quotes ");


            String[] tempStart = begTime.split(" ");
            String[] tempEnd= endTime.split(" ");

            if(!tempStart[0].matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)")||!tempEnd[0].matches("(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)")) {
                throw new IllegalArgumentException("Date format must follow mm/dd/yyyy");
            }

            if(!tempStart[1].matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")||!tempEnd[1].matches("([01]?[0-9]|2[0-3]):[0-5][0-9]"))
                throw new IllegalArgumentException("Time format must follow mm:hh (12 hour time)");
            if(!tempStart[2].matches("(am|pm|AM|PM)")&&!tempEnd[2].matches("(am|pm|AM|PM)"))
                throw new IllegalArgumentException("Time must include am/pm");
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }

        this.desc = desc;
        setDate(begTime,endTime);
    }


    /**
     * This method gets and returns the begin date
     * @return bdate
     */
    @Override
    public String getBeginTimeString() {

        return (ShortDateFormat.format(bdate));
    }

    /**
     * This method gets and returns the end date
     * @return edate
     */
    @Override
    public String getEndTimeString()
    {

        return (ShortDateFormat.format(edate));
        //  throw new UnsupportedOperationException("This method is not implemented yet");
    }


    /**
     * This method gets and returns the description of the appointment
     * @return desc
     */
    @Override
    public String getDescription()
    {
        return desc;
        //  return "This method is not implemented yet";
    }

    /**
     * Method to get begindate
     * @return bdate
     */
    @Override
    public Date getBeginTime() {
        return bdate;
    }

    /**
     * Method to get endDate
     * @return edate
     */
    @Override
    public Date getEndTime() {
        return edate;
    }

    /**
     * Method to calculate duration
     * @return duration
     */
    public String duration(){
        String diff = "";

        long duration = Math.abs(bdate.getTime()- edate.getTime());
        diff = String.format("%d min(s)", TimeUnit.MILLISECONDS.toMinutes(duration));        return diff;}

    /**
     * This method is used to sort the appointments based on begin date, end date and description
     * @param o of type Appointment
     * @return 0 or 1
     *
     */
    public int compareTo(Appointment o) {
        try {
            if (this.bdate == null) {
                throw new NullPointerException("Empty start time ");
            }
            if (o.getBeginTime() == null) {
                throw new NullPointerException("Empty begin time ");
            }
            long diff = this.bdate.getTime()-o.getBeginTime().getTime();

            if (diff > 0) {
                return 1;
            }
            if (diff < 0) {
                return -1;
            }
            if (diff == 0) {
                long enddiff = this.edate.getTime()-o.getEndTime().getTime();

                if(enddiff >0){
                    return 1;
                }
                if(enddiff<0){
                    return -1;
                }
                if(enddiff == 0){
                    int descriptiondiff = this.desc.compareTo(o.getDescription());
                    if(descriptiondiff >0){
                        return 1;
                    }
                    if(descriptiondiff<0){
                        return -1;
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        return 0;
    }


}
