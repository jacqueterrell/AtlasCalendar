package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoViewModel extends BaseViewModel<InfoNavigator> {

    private InfoDataModel dataModel;
    private static Map<Integer,Integer> networksMap = new LinkedHashMap<>();
    private static Map<Integer,Integer> opportunitiesMap = new LinkedHashMap<>();
    private static boolean networkChartSelected = true;
    private static UserProfile userProfile;
    private static BusinessProfile businessProfile;
    private static List<UserProfile> allUsersList = new ArrayList<>();
    private static List<BusinessProfile>businessProfileList = new ArrayList<>();
    private static List<UserConnections> connectionRecordList = new ArrayList<>();

    private static List<String> userStatsList = new ArrayList<>();
    private static List<UserConnections> recentActivityConnections = new ArrayList<>();
    private static String savedUserId = "";

    /***************view logic************/


    /***************getters and setters************/

    public void setDataModel(InfoDataModel dashBoardHomeDataModel) {
        this.dataModel = dashBoardHomeDataModel;
    }

    public void setSavedUserId(String savedUserId) {
        InfoViewModel.savedUserId = savedUserId;
    }

    public String getSavedUserId() {
        return savedUserId;
    }

    public void setNetworksMap(Map<Integer,Integer> connectionsMap) {
        networksMap = connectionsMap;
    }

    public Map<Integer,Integer> getNetworksMap() {
        return networksMap;
    }

    public void setOpportunitiesMap(Map<Integer, Integer> opportunitiesMap) {
        InfoViewModel.opportunitiesMap = opportunitiesMap;
    }

    public Map<Integer, Integer> getOpportunitiesMap() {
        return opportunitiesMap;
    }

    public void setNetworkChartSelected(boolean networkChartSelected) {
        InfoViewModel.networkChartSelected = networkChartSelected;
    }

    public boolean isNetworkChartSelected() {
        return networkChartSelected;
    }

    public void setUserProfile(UserProfile userProfile) {
        InfoViewModel.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        InfoViewModel.businessProfile = businessProfile;
    }

    public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }

    public void setUserConnections(List<UserConnections> connectionRecordList) {
        InfoViewModel.connectionRecordList = connectionRecordList;
    }

    public List<UserConnections> getUserConnections() {
        return connectionRecordList;
    }

    public void setBusinessProfileList(List<BusinessProfile> businessProfileList) {
        InfoViewModel.businessProfileList = businessProfileList;
    }

    public List<BusinessProfile> getBusinessProfileList() {
        return businessProfileList;
    }

    public void setUserStatsList(List<String> userStatsList) {
        InfoViewModel.userStatsList = userStatsList;
    }

    public List<String> getUserStatsList() {
        return userStatsList;
    }

    public void setRecentActivityConnections(List<UserConnections> recentActivityConnections) {
        InfoViewModel.recentActivityConnections = recentActivityConnections;
    }

    public List<UserConnections> getRecentActivityConnections() {
        return recentActivityConnections;
    }

    public void setAllUsersList(List<UserProfile> allUsersList) {
        InfoViewModel.allUsersList = allUsersList;
    }

    public List<UserProfile> getAllUsersList() {
        return allUsersList;
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

    public void onNetworkButtonClicked(){

        getNavigator().onNetworkButtonClicked();
    }

    public void onOpportunitiesButtonClicked(){

        getNavigator().onOpportunitiesButtonClicked();
    }

    public void onAddButtonClicked(){

        getNavigator().onAddContactClicked();
    }

    public void onUserProfileClicked(){

        getNavigator().onUserProfileClicked();
    }

    public void onSettingsClicked(){

        getNavigator().onSettingsClicked();
    }

    public void onUserStatsInfoClicked(){

        getNavigator().onUserStatsInfoClicked();
    }

    public void onRecentActivityInfoClicked(){

        getNavigator().onRecentActivityInfoClicked();
    }

    public void hideRecentActivityInfoDialog(){

        getNavigator().hideRecentActivityInfoDialog();
    }

    public void hideUserStatusInfoDialog(){

        getNavigator().hideUserStatusInfoDialog();
    }

    /********* Datamodel Calls********/

    public void getAllUsers(InfoViewModel viewModel){

        dataModel.getAllUsers(viewModel);
    }

}
