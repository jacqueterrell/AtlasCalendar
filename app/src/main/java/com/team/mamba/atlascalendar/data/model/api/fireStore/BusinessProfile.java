package com.team.mamba.atlascalendar.data.model.api.fireStore;

import androidx.annotation.Keep;

import com.google.firebase.firestore.Exclude;

import java.util.LinkedHashMap;
import java.util.Map;

@Keep
public class BusinessProfile {

    private String id = "...";
    private String code = "...";
    private String name = "...";
    private String contactName = "...";
    private String email = "...";
    private String phone = "...";
    private String fax = "...";
    private String street = "...";
    private String cityStateZip = "...";
    private String imageUrl = "...";
    private String searchName = "";
    private Map<String,String> announcements = new LinkedHashMap<>();
    private Map<String,String> children = new LinkedHashMap<>();
    private Map<String,String> contacts = new LinkedHashMap<>();
    private String shareNeeds = "...";
    private String businessRepId = "";
    @Exclude private boolean isDirectory = false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = String.valueOf(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String,String> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Map<String,String> announcements) {
        this.announcements = announcements;
    }

    public Map<String, String> getChildren() {
        return children;
    }

    public void setChildren(Map<String, String> children) {
        this.children = children;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public String getShareNeeds() {
        return shareNeeds;
    }

    public void setShareNeeds(String shareNeeds) {
        this.shareNeeds = shareNeeds;
    }

    public String getBusinessRepId() {
        return businessRepId;
    }

    public void setBusinessRepId(String businessRepId) {
        this.businessRepId = businessRepId;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchName() {
        return searchName;
    }

    @Exclude public boolean isDirectory() {
        return isDirectory;
    }

    @Exclude public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}
