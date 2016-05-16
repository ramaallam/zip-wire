package com.deltarail.schedule.factory;

import com.deltarail.schedule.beans.Header;
import com.deltarail.schedule.util.DatesUtility;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAllam on 08/05/2016.
 */
public class HeaderFactory implements Serializable{
    public static Header createHeader(String line) {
        String trainUId = line.substring(3, 9);

        List<LocalDate> datesOfJourney = getDatesOfJourney(line);
        List<String> strDatesOfJourney = getStrDatesOfJourney(datesOfJourney);
        List<String> compositeIds = new ArrayList<>();
        for(String date: strDatesOfJourney){
            compositeIds.add(trainUId+date);
        }

        Header header = new Header();
        header.setTrainID(trainUId);
        header.setDatesRunning(strDatesOfJourney);
        header.setCompositeIDs(compositeIds);

        return header;
    }

    private static List<String> getStrDatesOfJourney(List<LocalDate> datesOfJourney) {
        List<String> retDatesOfJourney = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        for(LocalDate date: datesOfJourney){
            retDatesOfJourney.add(date.format(formatter));
        }
        return retDatesOfJourney;
    }

    private static List<LocalDate> getDatesOfJourney(String line) {
        String runsOnDays = line.substring(21, 28);//0000011 - sat and sun
        LocalDate now = LocalDate.now();
        int currentDayNumber = now.get(ChronoField.DAY_OF_WEEK);
        String todayPlusTwoDays = DatesUtility.getWeekString(currentDayNumber);//W - 0011100
        List<LocalDate> retDates = new ArrayList<>();
        //i represents the day of the week.
        int i = 0;
        for (char c : todayPlusTwoDays.toCharArray()) {
            i++;
            if (c == '0') continue;
            if (runsOnDays.charAt(i - 1) == '1') {
                LocalDate dateToBeConsidered;
                if(i<currentDayNumber){
                    dateToBeConsidered = now.plus(i + 7 - currentDayNumber, ChronoUnit.DAYS);
                } else {
                    dateToBeConsidered = now.plus(i - currentDayNumber, ChronoUnit.DAYS);
                }
                retDates.add(dateToBeConsidered);
            }
        }
        return retDates;
    }
}
