package com.team.mamba.atlascalendar.data.model.api.fireStore;

import com.google.firebase.firestore.Exclude;
import com.team.mamba.atlascalendar.utils.formatData.AppFormatter;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Announcements {

    public String id = "";
    public boolean isEvent;
    public String orgBusID = "";
    public String orgBusName = "";
    public String text = "";
    public double timestamp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public String getOrgBusID() {
        return orgBusID;
    }

    public void setOrgBusID(String orgBusID) {
        this.orgBusID = orgBusID;
    }

    public String getOrgBusName() {
        return orgBusName;
    }

    public void setOrgBusName(String orgBusName) {
        this.orgBusName = orgBusName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude public long getAdjustedTimeStamp(){

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);

        return Long.parseLong(adjustedTime);
    }


    @Exclude
    public String getDateToString(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getAdjustedTimeStamp() * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);


        String stamp = String.valueOf(getAdjustedTimeStamp());

        if (stamp.matches(RegEx.ALLOW_DIGITS_AND_DECIMALS)){

            return monthName + " " + day + ", " + year;

        } else {

            return "";
        }
    }

    @Exclude private String getMonth(int index) {

        List<String> monthsList = new ArrayList<>();

        monthsList.add("Jan");
        monthsList.add("Feb");
        monthsList.add("Mar");
        monthsList.add("Apr");
        monthsList.add("May");
        monthsList.add("Jun");
        monthsList.add("Jul");
        monthsList.add("Aug");
        monthsList.add("Sep");
        monthsList.add("Oct");
        monthsList.add("Nov");
        monthsList.add("Dec");

        return monthsList.get(index);
    }
}
