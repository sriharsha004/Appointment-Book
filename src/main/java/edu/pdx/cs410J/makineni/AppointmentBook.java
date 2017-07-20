package edu.pdx.cs410J.makineni;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by sriharsha on 7/25/2016.
 */
public class AppointmentBook extends AbstractAppointmentBook{
    private String ownerName;
    Appointment searchAppt;
    Appointment singleAppt;
    ArrayList<Appointment> appointmentDetails;

    /**
     * This constructor takes the owner name as parameter and assigns it to the ownerName String
     * @param owner owner name
     */
    AppointmentBook(String owner)
    {
        this.ownerName = owner;
        appointmentDetails = new ArrayList<Appointment>();
        searchAppt=null;
        singleAppt = null;
    }


    /**
     * One of the constructor
     * @param ownerName name of the owner
     * @param appts appointments
     */
    AppointmentBook(String ownerName, Appointment appts)
    {
        this.ownerName = ownerName;
        appointmentDetails = new ArrayList<Appointment>();
        addAppointment(appts);
        searchAppt=null;
        singleAppt = null;
    }


    /**
     * Empty appointment book
     */
    AppointmentBook()
    {
        //Create an empty AppointmentBook
        ownerName = "";
        appointmentDetails = new ArrayList<Appointment>();
        searchAppt=null;
        singleAppt = null;
    }


    /**
     * One constructor with
     * @param ownerName ownerName
     * @param appointments Appointments
     * @param s String single or search
     */
    public AppointmentBook(String ownerName, Appointment appointments, String s) {
        if(s.equals("-search")) {
            this.ownerName = ownerName;
            appointmentDetails = null;
            searchAppt = appointments;
        }
        if(s.equals("-single")) {
            this.ownerName = ownerName;
            appointmentDetails = null;
            singleAppt = appointments;

           // System.out.println(singleAppt);
            //System.out.println("in -single");
        }
    }



    /**
     * This  method returns the owner name
     * @return ownerName
     */
    @Override
    public String getOwnerName()
    {
        return this.ownerName;
    }

    /**
     * This method displays all the appointments of the owner
     * @return appointmentDetails
     */
    @Override
    public Collection<Appointment> getAppointments()
    {
        return appointmentDetails;
    }


    /**
     * This method adds the appointment to the owner's appointment book
     * @param var1 of tye AbstractAppointment
     */
    @Override
    public void addAppointment(AbstractAppointment var1)
    {

        boolean addAppointment = true;
        for(AbstractAppointment call:appointmentDetails){
            if(call.toString().equals(var1.toString())) {
                addAppointment = false;
            }
        }
        if(addAppointment) {
            appointmentDetails.add((Appointment)var1);
        }

        Collections.sort(appointmentDetails);


    }
       // appointmentDetails.add((Appointment) var1);
     //   Collections.sort(appointmentDetails);
    }

