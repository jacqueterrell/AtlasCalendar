package com.team.mamba.atlascalendar.data.model.api.fireStore;


import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.team.mamba.atlascalendar.utils.formatData.AppFormatter;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@IgnoreExtraProperties
public class CrmNotes {

    public String id= "";
    public String subjectID= "";
    public String authorID= "";
    public String noteName= "";
    public String poc= "";
    public String whereMetCitySt= "";
    public String whereMetEventName= "";
    public int howMet;
    public int stage;
    public int type;
    public int oppGoal;
    public String desc= "";
    public int nextStep;
    public double timestamp; //creation time
    public double closeTimestamp; //close time


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

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getPoc() {
        return poc;
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }

    public String getWhereMetCitySt() {
        return whereMetCitySt;
    }

    public void setWhereMetCitySt(String whereMetCitySt) {
        this.whereMetCitySt = whereMetCitySt;
    }

    public String getWhereMetEventName() {
        return whereMetEventName;
    }

    public void setWhereMetEventName(String whereMetEventName) {
        this.whereMetEventName = whereMetEventName;
    }

    public int getHowMet() {
        return howMet;
    }

    public void setHowMet(int howMet) {
        this.howMet = howMet;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOppGoal() {
        return oppGoal;
    }

    public void setOppGoal(int oppGoal) {
        this.oppGoal = oppGoal;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNextStep() {
        return nextStep;
    }

    public void setNextStep(int nextStep) {
        this.nextStep = nextStep;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public double getCloseTimestamp() {
        return closeTimestamp;
    }


    public void setCloseTimestamp(double closeTimestamp) {
        this.closeTimestamp = closeTimestamp;
    }


   @Exclude public long getAdjustedTimeStamp(){

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);
        return Long.parseLong(adjustedTime);
    }

    @Exclude public long getAdjustedCloseTimeStamp(){

        String adjustedTime = AppFormatter.timeStampFormatter.format(closeTimestamp);
        return Long.parseLong(adjustedTime);
    }


   @Exclude public String getHowMetToString(){

        if (howMet == 0){

            return "Exhibit Booth";

        } else if ( howMet == 1){

            return "Networking";

        } else {

            return "Meeting";
        }
    }

    @Exclude public String getStageToString(){

        if (stage == 0){

            return "New";

        } else if ( stage == 1){

            return "Qualified";

        } else if (stage == 2){

            return "Proposal";
        } else if (stage == 3){

            return "Negotiation";

        } else if (stage == 4){

            return "Closed Won";

        } else {

            return "Closed Lost";
        }
    }

    @Exclude public String getTypeToString(){

        if (type == 0){

            return "Commercial";

        } else if ( type == 1){

            return "Non-Profit";

        } else if (type == 2){

            return "Federal";

        } else if (type == 3){

            return "Local";

        } else {

            return "State";
        }
    }

    @Exclude public String getOpportunityGoalToString(){

        if (oppGoal == 0){

            return "Solicitation";

        } else if ( oppGoal == 1){

            return "Teaming";

        } else {

            return "Direct Sell";
        }
    }

    @Exclude public String getNextStepToString(){

        if (nextStep == 0){

            return "Email";

        } else if ( nextStep == 1){

            return "Phone Call";

        } else if (nextStep == 2){

            return "Teleconference";

        } else if (nextStep == 3){

            return "Meeting";

        } else {

            return "Proposal";
        }
    }


    @Exclude public String getDateCreatedToString(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getAdjustedTimeStamp() * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);


        String stamp = String.valueOf(getAdjustedTimeStamp());

        if (stamp.matches(RegEx.ALLOW_DIGITS_AND_DECIMALS)
                && stamp.length() > 1){

            return monthName + " " + day + " " + year;

        } else {

            return "";
        }
    }

    @Exclude public String getDateClosedToString(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getAdjustedCloseTimeStamp() * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String stamp = String.valueOf(getAdjustedCloseTimeStamp());

        if (stamp.matches(RegEx.ALLOW_DIGITS_AND_DECIMALS)
                && stamp.length() > 1){

            return monthName + " " + day + " " + year;

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
