package com.team.mamba.atlascalendar.data.model.api.fireStore;

import android.text.Html;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.team.mamba.atlascalendar.utils.formatData.AppFormatter;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@IgnoreExtraProperties
public class UserProfile {


    private String id = "";
    private String deviceToken = "";
    private String code = "";
    private int score;
    private String firstName= "";
    private String lastName= "";
    private String email= "";
    private String workEmail= "";
    private String homePhone= "";
    private String personalPhone= "";
    private String workPhone= "";
    private String fax= "";
    private String street= "";
    private String cityStateZip= "";
    private List<Map<String,String>> workHistory = new ArrayList<>();
    private List<Map<String,String>> education = new ArrayList<>();
    private String currentEmployer= "";
    private String currentPosition= "";
    private String workStreet= "";
    private String workCityStateZip= "";
    private String imageUrl= "";
    private Map<String,String> connections = new LinkedHashMap<>();
    private int connectionsCount;
    private double timestamp;
    private double dob;
    private String phone= "";

    @Exclude private int connectionType = 3;
    @Exclude private String shareNeeds = "...";



    public UserProfile() {


    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {

        return phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(String cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    public List<Map<String,String>> getWorkHistory() {

        return workHistory;
    }


    @Exclude public void setShareNeeds(String shareNeeds) {
        this.shareNeeds = shareNeeds;
    }

   @Exclude public String getShareNeeds() {
        return shareNeeds;
    }

    @Exclude public String getWorkHistoryString(){

        StringBuilder workHistoryBuilder = new StringBuilder();

        for (Map<String, String> map : workHistory) {

            String location = "";
            String title = "";

            for (Map.Entry<String,String> entry : map.entrySet()){

                String key = entry.getKey();
                String value = entry.getValue();

                if (key.toLowerCase().contains("title")){

                    title = value + "\n";

                } else if (key.toLowerCase().contains("where")){

                    location = value + "\n";
                }
            }

            workHistoryBuilder.append(location)
                    .append(title)
                    .append("\n");
        }

        return workHistoryBuilder.toString();
    }



    public void setWorkHistory(List<Map<String,String>> workHistory) {
        this.workHistory = workHistory;
    }

    public List<Map<String,String>> getEducation() {

       return education;
    }

  @Exclude  public String getEducationString(){

        StringBuilder educationBuilder = new StringBuilder();

        for (Map<String, String> map : education) {

            String schoolName = "";
            String degree = "";
            String field = "";

            for (Map.Entry<String, String> entry : map.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue();

                if (key.toLowerCase().equals("school")){

                    String boldSchool = "<b>" + value + "</b>";

                    schoolName = Html.fromHtml(boldSchool) + "\n";

                } else if (key.toLowerCase().equals("degree")){

                    degree = value + "\n";

                } else if (key.toLowerCase().contains("study")){

                    field = value + "\n";
                }

            }

            educationBuilder.append(schoolName)
                    .append(degree)
                    .append(field)
                    .append("\n");
        }

        return educationBuilder.toString();
    }

    public void setEducation(List<Map<String,String>> education) {
        this.education = education;
    }

    public String getCurrentEmployer() {
        return currentEmployer;
    }

    public void setCurrentEmployer(String currentEmployer) {
        this.currentEmployer = currentEmployer;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getWorkStreet() {
        return workStreet;
    }

    public void setWorkStreet(String workStreet) {
        this.workStreet = workStreet;
    }

    public String getWorkCityStateZip() {
        return workCityStateZip;
    }

    public void setWorkCityStateZip(String workCityStateZip) {
        this.workCityStateZip = workCityStateZip;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String,String> getConnections() {
        return connections;
    }

    public void setConnections(Map<String,String> connections) {
        this.connections = connections;
    }

    public int getConnectionsCount() {
        return connectionsCount;
    }

    public void setConnectionsCount(int connectionsCount) {
        this.connectionsCount = connectionsCount;
    }

    public long getTimestamp() {

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);

        return Long.parseLong(adjustedTime);
    }


    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public long getDob() {

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);

        return Long.parseLong(adjustedTime);
    }

    public void setDob(double dob) {
        this.dob = dob;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

   @Exclude public long getAdjustedTimeStamp(){

        String adjustedTime = AppFormatter.timeStampFormatter.format(timestamp);

        return Long.parseLong(adjustedTime);
    }

    @Exclude public int getConnectionType() {
        return connectionType;
    }

    @Exclude public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }



    @Exclude public String getDateToString(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getAdjustedTimeStamp() * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        String stamp = String.valueOf(getAdjustedTimeStamp());

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
