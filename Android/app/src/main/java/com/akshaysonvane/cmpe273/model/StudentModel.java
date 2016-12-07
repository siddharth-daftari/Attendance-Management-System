package com.akshaysonvane.cmpe273.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 11/27/2016.
 */

public class StudentModel
{
    @SerializedName("lastName")
    private String lastName;

    @SerializedName("classId")
    private String classId;

    @SerializedName("studentId")
    private String studentId;

    @SerializedName("macAddress")
    private String macAddress;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("emailId")
    private String emailId;


    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getClassId()
    {
        return classId;
    }

    public void setClassId(String classId)
    {
        this.classId = classId;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getMacAddress()
    {
        return macAddress;
    }

    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getEmailId()
    {
        return emailId;
    }

    public void setEmailId(String emailId)
    {
        this.emailId = emailId;
    }

}
