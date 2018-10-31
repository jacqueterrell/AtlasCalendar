package com.team.mamba.atlascalendar.data.model.local;

import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarEvents {

    private String title = "";
    private String location = "";
    private long startTime;
    private long endTime;


    public CalendarEvents(String title, String location, long startTime,long endTime){

        this.title = title;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStartDateToString(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getStartTime() * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String stamp = String.valueOf(getStartTime());

        if (stamp.matches(RegEx.ALLOW_DIGITS_AND_DECIMALS)){

            return monthName + "/" + day + "/" + year;

        } else {

            return "";
        }

    }

    public long getDuration(){

        long end = getEndTime() * 1000;
        long start = getStartTime() * 1000;
        long diff = end - start;

        return TimeUnit.MILLISECONDS.toDays(diff);
    }


    private String getMonth(int index) {

        List<String> monthsList = new ArrayList<>();

        monthsList.add("");
        monthsList.add("January");
        monthsList.add("February");
        monthsList.add("March");
        monthsList.add("April");
        monthsList.add("May");
        monthsList.add("June");
        monthsList.add("July");
        monthsList.add("August");
        monthsList.add("September");
        monthsList.add("October");
        monthsList.add("November");
        monthsList.add("December");

        return monthsList.get(index);
    }
}
