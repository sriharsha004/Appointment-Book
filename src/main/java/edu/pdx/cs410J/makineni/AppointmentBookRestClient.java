package edu.pdx.cs410J.makineni;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper {
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";

    private final String url;


    /**
     * Creates a client to the appointment book REST service running on the given host and port
     *
     * @param hostName The name of the host
     * @param port     The port
     */
    public AppointmentBookRestClient(String hostName, int port) {
        this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
    }

    /**
     * Returns all keys and values from the server
     */
    public Response getAllKeysAndValues() throws IOException {
        return get(this.url);
    }

    /**
     * Returns all values for the given key
     */
    public Response getValues(AppointmentBook book) throws IOException {
        try {
            //Search was properly handled.
            if (book.searchAppt != null)
                return get(this.url, "owner", book.getOwnerName(), "beginTime", book.searchAppt.getBeginTimeString(), "endTime", book.searchAppt.getEndTimeString());
            else {
                if (book.appointmentDetails != null)
                    throw new Exception("Please specify search parameters");
                else {
                    return get(this.url, "owner", book.getOwnerName(), "beginTime", book.singleAppt.getBeginTimeString(), "endTime", book.singleAppt.getEndTimeString());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
            return null;
        }
    }

    /**
     * Method to add key value pairs
     * @param owner ownername
     * @param apt appointments
     * @return post
     * @throws IOException exception
     */
    public Response addKeyValuePair(String owner, Appointment apt) throws IOException {
        return post(this.url, "owner", owner, "description", apt.getDescription(), "beginTime", apt.getBeginTimeString(), "endTime", apt.getEndTimeString());
    }

//    @VisibleForTesting
//    Response postToMyURL(String... keysAndValues) throws IOException {
//        return post(this.url, keysAndValues);
//    }

    public Response removeAllMappings() throws IOException {
        return delete(this.url);
    }

}
