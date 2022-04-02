package com.wicc.personalassistant.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Component
public class CurrentDateTimeUtil {

    //return current day and month as a string
    public String getDayAndMonth() {
        LocalDate localDate = LocalDate.now();
        String dayOfWeek = localDate.getDayOfWeek().toString().toLowerCase();
        String month = localDate.getMonth().toString().toLowerCase();
        int year = localDate.getYear();
        return dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1)
                + ", "
                + month.substring(0, 1).toUpperCase() + month.substring(1)
                + " "
                + year;

    }

    //return boolean according to current date
    public boolean isGreater(Date date) {
        Calendar c = Calendar.getInstance();
        //for first day of month
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        // if date is greater return true and vice versa
        int i = date.compareTo(c.getTime());
        return i > 0 || i == 0;
    }
}
