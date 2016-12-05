package com.akshaysonvane.cmpe273.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay on 11/27/2016.
 */

public class ResponseModel
{
    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private String result;

    @SerializedName("data")
    private String data;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
