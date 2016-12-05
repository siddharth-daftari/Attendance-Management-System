
package com.akshaysonvane.cmpe273.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceModel {

    @SerializedName("macAddress")
    @Expose
    private String macAddress;
    @SerializedName("classId")
    @Expose
    private String classId;

    /**
     * 
     * @return
     *     The macAddress
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * 
     * @param macAddress
     *     The macAddress
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * 
     * @return
     *     The classId
     */
    public String getClassId() {
        return classId;
    }

    /**
     * 
     * @param classId
     *     The classId
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }

}
