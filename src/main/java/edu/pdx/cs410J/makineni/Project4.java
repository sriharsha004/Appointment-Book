package edu.pdx.cs410J.makineni;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";
    static ArrayList<String> commands; //used to keep track of all the commands that will be run at the end of the program
    static AppointmentBook ApptSearch;//keep a local copy of the customer that we are looking for, only used for -search command
    static AppointmentBook ApptAdd;
    private static Appointment Appt;
    //AbstractAppointment Appt;
    private static String date;
    private static String date1;

    private static String date_am;
    private static String date1_am;
    private static boolean pflag = false;


    public static void main(String... args) {
        setGlobalsToNull();
        commands = new ArrayList<String>();
        int element = parseCommandsAtBeginning(args);
       //System.out.println(element +" "+args[element]);
        parseCustomerIfExists(args, element);
        executeCommands();

        System.exit(0);
    }


    /**
     * -----PARSE COMMANDS AT BEGINNING-----
     *
     * @param args parse all of the commands from args
     * @throws IllegalArgumentException if there are missing args
     */
    private static int parseCommandsAtBeginning(String[] args) {
        int element = 0;
        try {
            if (args.length == 0) {
                throw new IllegalArgumentException(MISSING_ARGS);
            }

            boolean flag = false;
            for (; element < args.length; element++) {
                //check if -print, -README, -textFile filename

                switch (args[element]) {
                    case "-README":
                        //add readme to the command list
                        addArgumentCommand("-README");
                        break;
                    case "-print":
                        //add print to the list
                        addArgumentCommand("-print");
                        pflag = true;
                        break;
                    case "-host":
                        //check for ++element
                        if (args.length > element + 1) {
                            //save -textfile Filename
                            addArgumentCommand("-host");
                            if (!dashExists(args[element + 1])){
                                addArgumentCommand(args[++element]);
                            //System.out.println(args[element]);
                            }
                            else
                                throw new IllegalArgumentException("-host must contains a valid <hostname>. No dashes allowed");
                        } else {
                            //throw error
                            throw new IllegalArgumentException("-host argument must be followed by <hostname>");
                        }
                        break;
                    case "-port":
                        //check for ++element
                        if (args.length > element + 1) {
                            //save -textfile Filename
                            addArgumentCommand("-port");
                            if (!dashExists(args[element + 1])){
                                addArgumentCommand(args[++element]);
                           // System.out.println(args[element]);
                            }
                            else
                                throw new IllegalArgumentException("-port must contains a valid <port>. No dashes allowed");
                        } else {
                            //throw error
                            throw new IllegalArgumentException("-port argument must be followed by <port>");
                        }
                        break;
                    case "-search":
                        //add print to the list
                        addArgumentCommand("-search");
                        break;
                    default:
                        if (dashExists(args[element])) {
                            throw new IllegalArgumentException("Non-Valid Argument");
                        }
                     //   System.out.println(element);
                        return element;
                }
            }
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null)
                usage(ex.getMessage());
            else
                usage("");
            System.exit(1);
        }
       // System.out.println(element);
        return element;
    }

    /**
     * -----Check if a dash is in the beggning of the argument-----
     *
     * @return true or false if it starts with a dash
     */
    private static boolean dashExists(String arg) {
        //Check if a dash exists in the arg
        return arg.startsWith("-");
    }

    /**
     * -----PARSE Owner IF THEY EXIST-----
     *
     * @param args    more parsing and stuff
     * @param element is a counter to keep track of which arg we are parsing
     * @throws IllegalArgumentException if not enough args are provided
     */
    private static void parseCustomerIfExists(String[] args, int element) {
      //  if (pflag == false) {
            try {

                if (args.length > element + 7) {

                    //System.out.println(args[element]);


                    Appt = new Appointment(args[element + 1], args[element + 2] + " " + args[element + 3] + " " + args[element + 4], args[element + 5] + " " + args[element + 6] + " " + args[element + 7]);
                    ApptAdd = new AppointmentBook(args[element], Appt, "-single");

                } else {
                    // System.out.println(element+"Hi");
                    if (args.length > element) {
                        if (args.length > element + 5)
                            parseSearch(args, element);
                        else {

                            throw new IllegalArgumentException("Not enough arguments provided");
                        }
                    }
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                System.exit(1);
            }
        //}
    }

    /**
     * -----Method to check if enough args exist to be a owner, used for searching only, not creating a new one-----
     *
     * @param element is a counter to keep track of which arg we are parsing
     * @param args    is the list of args passed in to parse
     */
    private static void parseSearch(String[] args, int element) {
        try {
            if (commands.contains("-search")) {
                Appointment appointments = new Appointment();
                String owner = args[element++];
               // System.out.println(element);
                date = checkDateTime(args[element++]+" "+args[element++]);
                //System.out.println(date);
                date_am = checkAmPm(args[element++]);
                //System.out.println(date_am);
                date1 = checkDateTime(args[element++]+" "+args[element++]);
               // System.out.println(date1);
                date1_am = checkAmPm(args[element++]);
                //System.out.println(date1_am);

                //checkDateTime(date);


                appointments.setDate(date + " " + date_am, date1 + " " + date1_am);
                ApptSearch = new AppointmentBook(owner, appointments, "-search");
            } else {
                //Throw exception, trying to add a new Appointment or owner with not enough args.
                throw new IllegalArgumentException("Not enough arguments to add customer or Appointment");
            }
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null)
                usage(ex.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * -----ADD AN ARGUMENT TO THE LIST-----
     *
     * @param arg is a single string to be added to the list of commands
     */
    private static void addArgumentCommand(String arg) {

        if (!commands.contains(arg)) {

            commands.add(arg);
        }
    }

    /**
     * Method to execute commands
     */
    private static void executeCommands() {
       // System.out.println("in exec");
        AppointmentBookRestClient client; //Client for all the HTTP commands
        HttpRequestHelper.Response response; //Response from client command
        ArrayList<HttpRequestHelper.Response> listOfResponses; //List of Response from client command
       // System.out.println("in exec2");
        boolean printFlag = false;
        boolean textFileFlag = false;
        boolean ReadmeFlag = false;
        boolean pretty = false;
        boolean host = false;
        boolean port = false;
        boolean search = false;
        String fileName = null;
        String prettyName = null;
        String hostName = null;
        int portNumber = 0;


        try {
            for (String comm : commands) {
                switch (comm) {

                    case "-README":
                        ReadmeFlag = true;
                        Readme();
                        break;
                    case "-print":
                        printFlag = true;
                        break;

                    case "-host":
                        host = true;
                        hostName = commands.get(commands.indexOf(comm) + 1);
                       // System.out.println(hostName);
                        break;
                    case "-port":
                        port = true;
                        try {
                            portNumber = Integer.parseInt(commands.get(commands.indexOf(comm) + 1));

                        } catch (NumberFormatException ex) {
                            usage("Port \"" + commands.get(commands.indexOf(comm) + 1) + "\" must be an integer");
                            return;
                        }
                       // System.out.println(portNumber);
                        break;
                    case "-search":
                        search = true;
                    default:
                        //fileName = comm;
                        break;

                }
            }
            if (host == false) {
                //throw new exception
                throw new Exception("No host to connect to");
            } else {
                //execute host stuff
                if (port == false) {
                    //get mad
                    throw new Exception("No port to connect through");
                } else {
                    //be happy, do all the connection things here
                    client = new AppointmentBookRestClient(hostName, portNumber);
                }
            }
            //Web app stuff, this is where it all happens
            try {

                if (search == true) {
                    //check that either ApptSearch is != null
                    if (ApptSearch != null) {
                        //do a GET ApptAdd
                        response = client.getValues(ApptSearch);
                    } else if (ApptAdd != null) {
                        //do a GET ApptAdd
                        response = client.getValues(ApptAdd);
                    } else {
                        throw new Exception("No data to search for");
                    }
                    System.out.println(response.getContent());
                } else if (ApptAdd != null && ApptAdd.singleAppt != null) {
                 // System.out.println(ApptAdd.getOwnerName());

                    response = client.addKeyValuePair(ApptAdd.getOwnerName(), ApptAdd.singleAppt);

                } else {
                    //They supplied no commands, check the server and getAllKeysAndValues???????

                    response = client.getAllKeysAndValues();
                }
                checkResponseCode(HttpURLConnection.HTTP_OK, response);
                //System.out.println("2");
            } catch (IOException ex) {
                error("While contacting server: " + ex.getMessage() + ",\nPlease try again with a a valid host name\n");
                return;
            }

           // System.out.println(response.getContent());



            if (printFlag) {
                if (ApptAdd != null) {

                    System.out.println("\n"+ ApptAdd.getOwnerName() + "  " + ApptAdd.singleAppt.getDescription() + "  " + ApptAdd.singleAppt.getBeginTimeString() + "   " + ApptAdd.singleAppt.getEndTimeString() + "   " + ApptAdd.singleAppt.duration() + "\n");


                    printFlag = true;
                } else {

                    throw new Exception("Must provide a appointment with the -print command");
                }
            }
//            if (ReadmeFlag) {
//                Readme();
//            }

        } catch (Exception ex) {
            if (ex.getMessage() != null) {
                usage(ex.getMessage());
            } else {
                usage("Empty Exception");
            }

            System.exit(1);
        }
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     *
     * @param code     The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode(int code, HttpRequestHelper.Response response) {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                    response.getCode(), response.getContent()));
        }
    }

    /**
     * @param message is used to nicely display stuff
     */
    private static void error(String message) {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * -----README FUNCTION WITH ERRORS-----
     * Prints usage information for this program and exits
     *
     * @param message An error message to print
     */
    private static void usage(String message) {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }

    /**
     * setting globals to null
     */
    public static void setGlobalsToNull() {
        commands = null;
        ApptAdd = null;
        ApptSearch = null;
    }

    /**
     * Readme function contains the readme of all useful information the user may need to know.
     */
    private static void Readme() {
        //System.out.println("README has been called");
        System.out.println("\nREADME FILE - \n \nProject4 by Sriharsha Makineni creates an appointment book for each member with the respective appointments list.\n" +
                "usage: java edu.pdx.cs410J.makineni.Project1 [options] <args>\n" +
                "args are (in this order):\n" +
                "owner - The person whose owns appt book\n" +
                "description - A description of the appointment\n" +
                "beginTime - When the appt begins (12-hour time)\n" +
                "endTime - When the appt ends (12-hour time)\n" +
                "\noptions (options may appear in any order):\n" +
                "-host hostname Host computer on which the server runs\n"+
                "-port port Port on which the server is listening\n"+
                "-search Appointments should be searched for\n"+
                "-print : Prints a description of the new appointment\n" +
                "-README : Prints a README for this project and exits\n" +
                "Date and time should be in this format: mm/dd/yyyy hh:mm\n" +
                "Description should not be empty");
        System.exit(1);
    }









    /**
     * This method checks whether the appointment has description or not
     * @param description
     * takes the parameter as description
     * @return true
     * return true
     * @throws Exception
     * throws exception if there is no description
     */
    public static boolean checkNullDescription(String description) throws Exception{
        if(description.trim()!=null&&!description.trim().isEmpty()){

        return true;
        }else{

        throw new Exception("Empty string in description");

        }
        }


    /**
     * This method helps in validating the am/pm part of the date
     * @param ampm input
     * @return ampm valid if accurate
     * @throws Exception
     * throwa exception if the format is not as specified.
     */
    public static String checkAmPm(String ampm) throws Exception{
        if(ampm.equalsIgnoreCase("am") || ampm.equalsIgnoreCase("pm"))
        {

        }
        else {
            System.out.println("The date and time format must be : mm/dd/yyyy hh:mm am/pm");
            System.exit(1);
        }

        return ampm;
    }
    /**
     * This method checks the date and time format of the appointment begin and end date
     * @param dateTime
     * takes dateTime as parameter
     * @return dateTime
     * returns dateTime if valid
     * @throws ParseException
     * throws exception if not valid
     */
    public static String checkDateTime(String dateTime)throws ParseException{
        //System.out.println(dateTime);

        if(dateTime == null || !dateTime.matches("^\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{2}$")){
            System.out.println(dateTime);
            System.out.println("The date and time format must be : mm/dd/yyyy hh:mm am/pm");
            System.exit(1);
        }
        else {

            SimpleDateFormat formt = new SimpleDateFormat("MM/dd/yyyy hh:mm");
            formt.setLenient(false);
            formt.parse(dateTime);

        }
        return dateTime;
    }




}