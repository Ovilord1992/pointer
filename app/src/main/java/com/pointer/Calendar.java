package com.pointer;

import java.text.SimpleDateFormat;


public class Calendar {
    public static int getDate(){
        java.util.Calendar c = java.util.Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String formattedDate = df.format(c.getTime());
        return Integer.parseInt (formattedDate);
    }
}
