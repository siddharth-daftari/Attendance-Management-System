package com.akshaysonvane.cmpe273.utils;

/**
 * Created by Akshay on 11/24/2016.
 */

public class Utils
{
    private static Utils utils = null;
    public String BASE_URL = "http://de14c93f.ngrok.io";
    public static String PI_MAC = "B8:27:EB:9B:90:FD";
    public static String ServerUUID = "94f39d29-7d6d-437d-973b-fba39e49d4ee";

    public static Utils getInstance()
    {
        if(utils == null)
        {
            utils = new Utils();
        }

        return utils;
    }
}
