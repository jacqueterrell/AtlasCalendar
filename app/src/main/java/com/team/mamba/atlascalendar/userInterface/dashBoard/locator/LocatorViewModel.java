package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

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
    private List<String> favoritesIdList = new ArrayList<>();
    private List<UserProfile> favoritesProfileList = new ArrayList<>();

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

    public void setEmployeeProfilesList(
            List<UserProfile> employeeProfilesList) {
        this.employeeProfilesList = employeeProfilesList;
    }

    public void setFavoritesIdList(List<String> favoritesIdList) {
        this.favoritesIdList = favoritesIdList;
    }

    public List<String> getFavoritesIdList() {
        return favoritesIdList;
    }

    public void setFavoritesProfileList(List<UserProfile> favoritesProfileList) {
        this.favoritesProfileList = favoritesProfileList;
    }

    public List<UserProfile> getFavoritesProfileList() {
        return favoritesProfileList;
    }

    /***************onclick listeners************/

    public void onContactsClicked(){

        getNavigator().onContactsClicked();
    }

    public void onCrmClicked(){

        getNavigator().onCrmClicked();
    }

    public void onNotificationsClicked(){

        getNavigator().onNotificationsClicked();
    }


    /********* Datamodel Calls********/

    public void requestContactsInfo(LocatorViewModel viewModel){

        dataModel.requestContactsInfo(viewModel);
    }
}
