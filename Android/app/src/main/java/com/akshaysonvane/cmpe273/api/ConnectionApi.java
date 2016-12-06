package com.akshaysonvane.cmpe273.api;

import com.akshaysonvane.cmpe273.model.AttendanceModel;
import com.akshaysonvane.cmpe273.model.ResponseModel;
import com.akshaysonvane.cmpe273.model.StudentModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;


/**
 * Created by Akshay on 11/20/2016.
 */

public interface ConnectionApi
{
    @POST("/checkIfAttendanceMarked/")
    void checkAttendance(@Body AttendanceModel attendanceModel, Callback<ResponseModel> callback);


    @POST("/register/")
    void registerStudent(@Body StudentModel studentModel, Callback<ResponseModel> callback);
}
