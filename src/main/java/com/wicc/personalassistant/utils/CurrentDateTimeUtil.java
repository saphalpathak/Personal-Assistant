package com.wicc.personalassistant.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Component
public class CurrentDateTimeUtil {

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

    public boolean isGreater(Date date) {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        //is (date) is greater or not if greater return true
        int i = date.compareTo(c.getTime());
        return i > 0 || i == 0;
    }
}
