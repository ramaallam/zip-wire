package com.deltarail.schedule.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by RAllam on 06/05/2016.
 */
public class DatesUtility implements Serializable{
    public static boolean isValidPeriod(String record) {
        try {
            String startDateStr = record.substring(9, 15);
            String endDateStr = record.substring(15, 21);
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
            LocalDate runsFrom = (formatter.parse(startDateStr)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();//*/Instant.ofEpochMilli(formatter.parse(startDateStr).getTime());
            LocalDate runsTill = (formatter.parse(endDateStr)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();//Instant.ofEpochMilli(formatter.parse(endDateStr).getTime());

            LocalDate now = LocalDate.now();
            LocalDate tomorrow = now.plus(1, ChronoUnit.DAYS);
            LocalDate dayAfter = now.plus(2, ChronoUnit.DAYS);
            if (((runsFrom.isBefore(now) || runsFrom.isEqual(now)) && (runsTill.isAfter(now) || runsTill.isEqual(now))) || ((runsFrom.isBefore(tomorrow) || runsFrom.isEqual(tomorrow)) && (runsTill.isAfter(tomorrow) || runsTill.isEqual(tomorrow))) || ((runsFrom.isBefore(dayAfter) || runsFrom.isEqual(dayAfter)) && (runsTill.isAfter(dayAfter) || runsTill.isEqual(dayAfter)))) {
                String runsOnDays = record.substring(21, 28);//0000011 - sat and sun
                String todayPlusTwoDays = getWeekString(now.get(ChronoField.DAY_OF_WEEK));//W - 0011100
                if (isCountingTowardsJourney(runsOnDays, todayPlusTwoDays)) {
                    return true;
                }
            }
        } catch (Exception e) {
            //TODO specific exceptions
            return false;
        }
        return false;
    }

    private static boolean isCountingTowardsJourney(String runsOnDays, String todayPlusTwoDays) {
        int i = 0;
        for (char c : todayPlusTwoDays.toCharArray()) {
            i++;
            if (c == '0') continue;
            if (runsOnDays.charAt(i - 1) == '1') {
                return true;
            }
        }
        return false;
    }

    public static String getWeekString(int dayOfTheWeek) {
        String out = "0000000";
        if (dayOfTheWeek >= 1 && dayOfTheWeek <= 5) {
            String tripleOne = "111";
            out = out.substring(0, dayOfTheWeek - 1) + tripleOne + out.substring(dayOfTheWeek + 2);
        }
        if (dayOfTheWeek == 6) {
            out = "1000011";
        }
        if (dayOfTheWeek == 7) {
            out = "1100001";
        }
        return out;
    }
}
