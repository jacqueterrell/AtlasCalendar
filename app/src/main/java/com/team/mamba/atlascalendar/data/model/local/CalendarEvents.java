package com.team.mamba.atlascalendar.data.model.local;

import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarEvents {

    private String title;
    private String location;
    private long startTime;
    private long endTime;
    private boolean allDayEvent;


    public CalendarEvents(Builder builder){

        this.title = builder.getTitle();
        this.location = builder.getLocation();
        this.startTime = builder.getStartTime();
        this.endTime = builder.getEndTime();
        this.allDayEvent = builder.isAllDayEvent();
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

    public void setAllDayEvent(boolean allDayEvent) {
        this.allDayEvent = allDayEvent;
    }

    public boolean isAllDayEvent() {
        return allDayEvent;
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


    public static class Builder{

        private String title;
        private String location;
        private long startTime;
        private long endTime;
        private boolean allDayEvent;

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getLocation() {
            return location;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public long getStartTime() {
            return startTime;
        }

        public Builder setStartTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public long getEndTime() {
            return endTime;
        }

        public Builder setEndTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        public boolean isAllDayEvent() {
            return allDayEvent;
        }

        public Builder setAllDayEvent(boolean allDayEvent) {
            this.allDayEvent = allDayEvent;
            return this;
        }

        public CalendarEvents build(){

            return new CalendarEvents(this);
        }
    }
}
