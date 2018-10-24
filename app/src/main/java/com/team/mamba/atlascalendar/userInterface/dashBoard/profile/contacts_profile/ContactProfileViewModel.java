package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class ContactProfileViewModel extends BaseViewModel<ContactProfileNavigator> {


    private ContactProfileDataModel dataModel;
    private String profilePhone;
    private boolean streetAddress = false;
    private boolean cityStateZip = false;
    private boolean workStreet = false;
    private boolean workCityStateZip = false;

    /*************Getters and Setters*****************/

    public void setDataModel(ContactProfileDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setProfilePhone(String profilePhone) {
        this.profilePhone = profilePhone;
    }

    public String getProfilePhone() {
        return profilePhone;
    }

    public void setStreetAddress(boolean streetAddress) {
        this.streetAddress = streetAddress;
    }

    public boolean isStreetAddress() {
        return streetAddress;
    }

    public void setCityStateZip(boolean cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    public boolean isCityStateZip() {
        return cityStateZip;
    }

    public void setWorkStreet(boolean workStreet) {
        this.workStreet = workStreet;
    }

    public boolean isWorkStreet() {
        return workCityStateZip;
    }

    public void setWorkCityStateZip(boolean workCityStateZip) {
        this.workCityStateZip = workCityStateZip;
    }

    public boolean isWorkCityStateZip() {
        return workCityStateZip;
    }

    /**************OnClick Listeners**********************/

    public void contactCellPhoneClicked() {

        getNavigator().contactCellPhoneClicked();
    }


    public void contactOnOfficePhoneClicked() {

        getNavigator().contactOnOfficePhoneClicked();
    }


    public void contactOnHomePhoneClicked() {

        getNavigator().contactOnHomePhoneClicked();
    }


    public void contactOnPersonalPhoneClicked() {

        getNavigator().contactOnPersonalPhoneClicked();
    }


    public void contactOnFaxClicked() {

        getNavigator().contactOnFaxClicked();
    }


    public void contactOnPersonalEmailClicked() {

        getNavigator().contactOnPersonalEmailClicked();
    }


    public void contactOnWorkEmailClicked() {

        getNavigator().contactOnWorkEmailClicked();
    }


    public void contactOnHomeAddressClicked() {

        getNavigator().contactOnHomeAddressClicked();
    }


    public void contactOnWorkAddressClicked() {

        getNavigator().contactOnWorkAddressClicked();
    }


    public void contactOnWorkHistoryClicked() {

        getNavigator().contactOnWorkHistoryClicked();
    }


    public void contactOnEducationClicked() {

        getNavigator().contactOnEducationClicked();
    }


    /****************DataModel Requests***************/

}
