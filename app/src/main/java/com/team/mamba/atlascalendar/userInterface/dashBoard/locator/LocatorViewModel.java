package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersEntity;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class LocatorViewModel extends BaseViewModel<LocatorNavigator> {

    private LocatorDataModel dataModel;
    private static UserProfile selectedUserProfile;
    private static BusinessProfile selectedBusinessProfile;
    private static BusinessProfile calendarContactProfile;
    private static List<UserProfile> allUserProfiles = new ArrayList<>();
    private static List<UserProfile> employeeProfilesList = new ArrayList<>();
    private static List<UserProfile> favoritesProfileList = new ArrayList<>();
    private static List<UserConnections> needsApprovalConnections = new ArrayList<>();
    private static List<FavoriteUsersEntity> favoriteUsersEntityList = new ArrayList<>();
    public static boolean wasUpdated = false;

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
        LocatorViewModel.selectedUserProfile = selectedUserProfile;
    }

    public BusinessProfile getSelectedBusinessProfile() {
        return selectedBusinessProfile;
    }

    public void setSelectedBusinessProfile(
            BusinessProfile selectedBusinessProfile) {
        LocatorViewModel.selectedBusinessProfile = selectedBusinessProfile;
    }

    public List<UserProfile> getEmployeeProfilesList() {
        return employeeProfilesList;
    }

    public void setEmployeeProfilesList(List<UserProfile> employeeProfilesList) {
        LocatorViewModel.employeeProfilesList = employeeProfilesList;
    }


    public void setFavoritesProfileList(List<UserProfile> favoritesProfileList) {
        LocatorViewModel.favoritesProfileList = favoritesProfileList;
    }

    public List<UserProfile> getFavoritesProfileList() {

        return favoritesProfileList;
    }

    public void setFavoriteUsersEntityList(List<FavoriteUsersEntity> favoriteUsersEntityList) {
        LocatorViewModel.favoriteUsersEntityList = favoriteUsersEntityList;
    }

    public List<FavoriteUsersEntity> getFavoriteUsersEntityList() {
        return favoriteUsersEntityList;
    }


    public void setCalendarCompanyId(String calendarCompanyId) {
        LocatorViewModel.calendarCompanyId = calendarCompanyId;
    }

    public String getCalendarCompanyId() {
        return calendarCompanyId;
    }

    public void setCalendarContactProfile(BusinessProfile calendarContactProfile) {
        LocatorViewModel.calendarContactProfile = calendarContactProfile;
    }

    public BusinessProfile getCalendarContactProfile() {
        return calendarContactProfile;
    }

    public void setAllUserProfiles(List<UserProfile> allUserProfiles) {
        LocatorViewModel.allUserProfiles = allUserProfiles;
    }

    public List<UserProfile> getAllUserProfiles() {
        return allUserProfiles;
    }

    public void setNeedsApprovalConnections(List<UserConnections> needsApprovalConnections) {
        LocatorViewModel.needsApprovalConnections = needsApprovalConnections;
    }

    public List<UserConnections> getNeedsApprovalConnections() {
        return needsApprovalConnections;
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

    public void onHamburgerClicked() {

        getNavigator().onHamburgerClicked();
    }

    public void onAccountManagementClicked() {

        getNavigator().onAccountManagementClicked();
    }

    public void onLogOutClicked() {

        getNavigator().onLogOutClicked();
    }

    public void onAddConnectionClicked() {

        getNavigator().onAddConnectionClicked();
    }

    public void onUsersCalendarClicked() {

        getNavigator().onUsersCalendarClicked();
    }

    public void onGlobalMapClicked() {

        getNavigator().onGlobalMapClicked();
    }

    public void onConnectionRequestsClicked(){

        getNavigator().onConnectionRequestsClicked();
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

    public void setPrivacyMode(LocatorViewModel viewModel,boolean privacyMode) {

        dataModel.setPrivacyMode(viewModel,privacyMode);
    }

    public void setVacationMode(LocatorViewModel viewModel,boolean vacationMode) {

        dataModel.setVacationMode(viewModel,vacationMode);
    }
}
