package com.example.memberboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilClass {
    public static String dateTimeFormat(LocalDateTime dateTime){
        if(dateTime == null)
            return null;
        else
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
