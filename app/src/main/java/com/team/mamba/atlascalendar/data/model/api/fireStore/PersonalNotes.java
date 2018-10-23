package com.team.mamba.atlascalendar.data.model.api.fireStore;

import com.google.firebase.firestore.Exclude;
import com.team.mamba.atlascalendar.utils.formatData.AppFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersonalNotes {

    public String authorID = "";
    public List<String> details = new ArrayList<>();
    public String howMet = "";
    public String id = "";
    public String subjectID = "";
    public double timestamp;
    public String whereMet = "";


    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public String getHowMet() {
        return howMet;
    }

    public void setHowMet(String howMet) {
        this.howMet = howMet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public double getTimestamp() {

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);
        return Long.parseLong(adjustedTime);
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getWhereMet() {
        return whereMet;
    }

    public void setWhereMet(String whereMet) {
        this.whereMet = whereMet;
    }


    @Exclude public String getDetailsToString(){

        StringBuilder stringBuilder = new StringBuilder();

        for (String detail : details){

            stringBuilder.append(detail)
                    .append("\n\n");
        }

        return stringBuilder.toString();
    }
}
