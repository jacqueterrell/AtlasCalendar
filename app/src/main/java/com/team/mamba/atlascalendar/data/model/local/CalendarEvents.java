package com.team.mamba.atlascalendar.data.model.local;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarEvents {

    private String title;
    private String location;
    private String description;
    private long startTime;
    private long endTime;
    private boolean allDayEvent;
    private boolean holiday;
    private CalendarDay calendarDay;


    public CalendarEvents(Builder builder){

        this.title = builder.getTitle();
        this.description = builder.getDescription();
        this.location = builder.getLocation();
        this.startTime = builder.getStartTime();
        this.endTime = builder.getEndTime();
        this.allDayEvent = builder.isAllDayEvent();
        this.calendarDay = builder.getCalendarDay();
        this.holiday = builder.isHoliday();
    }


    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isAllDayEvent() {
        return allDayEvent;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }

    public String getDescription() {
        return description;
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

        private String title = "";
        private String location = "";
        private String description = "";
        private long startTime;
        private long endTime;
        private boolean allDayEvent;
        private boolean holiday = false;
        private CalendarDay calendarDay;


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

        public CalendarDay getCalendarDay() {
            return calendarDay;
        }

        public Builder setCalendarDay(CalendarDay calendarDay) {
            this.calendarDay = calendarDay;
            return this;
        }

        public boolean isAllDayEvent() {
            return allDayEvent;
        }

        public Builder setAllDayEvent(boolean allDayEvent) {
            this.allDayEvent = allDayEvent;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setHoliday(boolean holiday) {
            this.holiday = holiday;
            return this;
        }

        public boolean isHoliday() {
            return holiday;
        }

        public CalendarEvents build(){

            return new CalendarEvents(this);
        }
    }
}
