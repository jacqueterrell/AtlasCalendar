package com.team.mamba.atlascalendar.data.model.api.fireStore;

import com.google.firebase.firestore.Exclude;
import com.team.mamba.atlascalendar.utils.formatData.AppFormatter;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class UserConnections {


    public String id= "";
    public String requestingUserID= "";
    public String requestingUserName= "";
    public String consentingUserID= "";
    public String consentingUserName= "";
    public String conDeviceToken = "";
    public String reqDeviceToken = "";
    public int connectionType;


    public boolean isOrgBus;



    public boolean isConfirmed;
    public boolean isViewed;
    public boolean isDirectory;
    public double timestamp;

    @Exclude private boolean needsApproval;
    @Exclude private boolean recentActivity;
    @Exclude private UserProfile userProfile;
    @Exclude private BusinessProfile businessProfile;
    @Exclude private boolean overrideBusinessProfile;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsOrgBus() {
        return isOrgBus;
    }

    public void setOrgBus(boolean isOrgBus) {
        this.isOrgBus = isOrgBus;
    }

    public boolean isIsConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.isConfirmed = confirmed;
    }

    public boolean isIsViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public boolean isIsDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        this.isDirectory = directory;
    }



    public String getRequestingUserID() {
        return requestingUserID;
    }

    public void setRequestingUserID(String requestingUserID) {
        this.requestingUserID = requestingUserID;
    }

    public String getRequestingUserName() {
        return requestingUserName;
    }

    public void setRequestingUserName(String requestingUserName) {
        this.requestingUserName = requestingUserName;
    }

    public String getConsentingUserID() {
        return consentingUserID;
    }

    public void setConsentingUserID(String consentingUserID) {
        this.consentingUserID = consentingUserID;
    }

    public void setReqDeviceToken(String reqDeviceToken) {
        this.reqDeviceToken = reqDeviceToken;
    }

    public String getReqDeviceToken() {
        return reqDeviceToken;
    }

    public String getConsentingUserName() {
        return consentingUserName;
    }

    public void setConsentingUserName(String consentingUserName) {
        this.consentingUserName = consentingUserName;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }


    public void setConDeviceToken(String conDeviceToken) {
        this.conDeviceToken = conDeviceToken;
    }

    public String getConDeviceToken() {
        return conDeviceToken;
    }

    public long getTimestamp() {

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);

        return Long.parseLong(adjustedTime);
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }


    @Exclude public void setOverrideBusinessProfile(boolean overrideBusinessProfile) {
        this.overrideBusinessProfile = overrideBusinessProfile;
    }

   @Exclude public boolean isOverrideBusinessProfile() {
        return overrideBusinessProfile;
    }

    @Exclude public boolean isNeedsApproval() {
        return needsApproval;
    }

    public void setNeedsApproval(boolean needsApproval) {
        this.needsApproval = needsApproval;
    }

   @Exclude public boolean isRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(boolean recentActivity) {
        this.recentActivity = recentActivity;
    }


   @Exclude public UserProfile getUserProfile() {
        return userProfile;
    }

    @Exclude public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Exclude public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }

    @Exclude public void setBusinessProfile(BusinessProfile businessProfile) {
        this.businessProfile = businessProfile;
    }

    @Exclude public String getConnectionTypeToString(){

        if (connectionType == 0){

            return "Family Member";

        } else if (connectionType == 1){

            return "Personal Friend";

        } else if (connectionType == 2){

            return "New Acquaintance";

        } else if (connectionType == 3){

            return "Business Contact";

        } else if (connectionType == 4){

            return "Colleague";

        } else {

            return "Client";
        }

    }


    @Exclude public String getDateToString(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getTimestamp() * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String stamp = String.valueOf(getTimestamp());

        if (stamp.matches(RegEx.ALLOW_DIGITS_AND_DECIMALS)){

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
