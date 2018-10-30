package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersEntity;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class LocatorViewModel extends BaseViewModel<LocatorNavigator> {

    private LocatorDataModel dataModel;
    private UserProfile selectedUserProfile;
    private BusinessProfile selectedBusinessProfile;
    private List<UserProfile> employeeProfilesList = new ArrayList<>();
    private List<UserProfile> favoritesProfileList = new ArrayList<>();
    private List<FavoriteUsersEntity> favoriteUsersEntityList = new ArrayList<>();

    private static String calendarCompanyId = "";


    /***************view logic************/

    /***************getters and setters************/

    public void setDataModel(LocatorDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public UserProfile getSelectedUserProfile() {
        return selectedUserProfile;
    }

    public void setSelectedUserProfile(UserProfile selectedUserProfile) {
        this.selectedUserProfile = selectedUserProfile;
    }

    public BusinessProfile getSelectedBusinessProfile() {
        return selectedBusinessProfile;
    }

    public void setSelectedBusinessProfile(
            BusinessProfile selectedBusinessProfile) {
        this.selectedBusinessProfile = selectedBusinessProfile;
    }

    public List<UserProfile> getEmployeeProfilesList() {
        return employeeProfilesList;
    }

    public void setEmployeeProfilesList(List<UserProfile> employeeProfilesList) {
        this.employeeProfilesList = employeeProfilesList;
    }


    public void setFavoritesProfileList(List<UserProfile> favoritesProfileList) {
        this.favoritesProfileList = favoritesProfileList;
    }

    public List<UserProfile> getFavoritesProfileList() {


        return favoritesProfileList;
    }

    public void setFavoriteUsersEntityList(List<FavoriteUsersEntity> favoriteUsersEntityList) {
        this.favoriteUsersEntityList = favoriteUsersEntityList;
    }

    public List<FavoriteUsersEntity> getFavoriteUsersEntityList() {
        return favoriteUsersEntityList;
    }



    //fixme remove in place of action Firebase field

    public static void setCalendarCompanyId(String calendarCompanyId) {
        LocatorViewModel.calendarCompanyId = calendarCompanyId;
    }

    public static String getCalendarCompanyId() {
        return calendarCompanyId;
    }

    /***************onclick listeners************/

    public void onContactsClicked() {

        getNavigator().onContactsClicked();
    }

    public void onCrmClicked() {

        getNavigator().onCrmClicked();
    }

    public void onNotificationsClicked() {

        getNavigator().onNotificationsClicked();
    }

    public void onHamburgerClicked(){

        getNavigator().onHamburgerClicked();
    }

    public void onAccountManagementClicked(){

        getNavigator().onAccountManagementClicked();
    }

    public void onLogOutClicked(){

        getNavigator().onLogOutClicked();
    }

    public void onAddConnectionClicked(){

        getNavigator().onAddConnectionClicked();
    }


    /********* Datamodel Calls********/

    public void requestContactsInfo(LocatorViewModel viewModel) {

        dataModel.requestContactsInfo(viewModel);
    }

    public void removeFavorteUser(LocatorViewModel viewModel, String profileId) {

        dataModel.removeFavoriteUser(viewModel, profileId);
    }

    public void addFavoriteUser(LocatorViewModel viewModel, String profileId) {

        dataModel.addFavoriteUser(viewModel, profileId);
    }


    //client id
    //450285275359-7iqk4stuc54gbbkcbsqrdh78jhoaq8jq.apps.googleusercontent.com

    //client secret
    //jQKi8GXvmYF-jLPol7aeDqOy
}
