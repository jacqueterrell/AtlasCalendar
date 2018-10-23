package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContactsViewModel extends BaseViewModel<ContactsNavigator> {

    ContactsDataModel dataModel;


    private List<UserProfile> userProfileList = new ArrayList<>();
    private static List<BusinessProfile> businessProfileList = new ArrayList<>();
    private static UserProfile userProfile;
    private static BusinessProfile businessProfile;
    private static List<UserConnections> userConnectionsList = new ArrayList<>();
    private static List<UserConnections> allConnectionsList = new ArrayList<>();
    private static BusinessProfile selectedDirectory;
    private static String savedUserId = "";
    private static boolean businessContactsShown = false;
    private static List<UserConnections> directoryConnections = new ArrayList<>();
    private static BusinessProfile selectedBusinessProfile;
    private static int totalDirectories = 0;


    public void setDataModel(ContactsDataModel dataModel) {
        this.dataModel = dataModel;
    }

    /******Getters and Setters********/

    public List<UserProfile> getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(List<UserProfile> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public List<BusinessProfile> getBusinessProfileList() {
        return businessProfileList;
    }

    public void setBusinessProfileList(List<BusinessProfile> businessProfileList) {
        ContactsViewModel.businessProfileList = businessProfileList;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        ContactsViewModel.userProfile = userProfile;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        ContactsViewModel.businessProfile = businessProfile;
    }

    public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }

    public void setUserConnectionsList(List<UserConnections> userConnectionsList) {
        ContactsViewModel.userConnectionsList = userConnectionsList;
    }

    public List<UserConnections> getUserConnectionsList() {
        return userConnectionsList;
    }

    public void setSavedUserId(String savedUserId) {
        ContactsViewModel.savedUserId = savedUserId;
    }

    public String getSavedUserId() {
        return savedUserId;
    }

    public void setSelectedBusinessProfile(BusinessProfile selectedBusinessProfile) {
        ContactsViewModel.selectedBusinessProfile = selectedBusinessProfile;
    }

    public BusinessProfile getSelectedBusinessProfile() {
        return selectedBusinessProfile;
    }

    public void setBusinessContactsShown(boolean businessContactsShown) {
        ContactsViewModel.businessContactsShown = businessContactsShown;
    }

    public boolean isBusinessContactsShown() {
        return businessContactsShown;
    }

    public void setAllConnectionsList(List<UserConnections> allConnectionsList) {
        ContactsViewModel.allConnectionsList = allConnectionsList;
    }

    private List<UserConnections> getAllConnectionsList() {
        return allConnectionsList;
    }

    private void setSelectedDirectory(BusinessProfile selectedDirectory) {
        ContactsViewModel.selectedDirectory = selectedDirectory;
    }

    private BusinessProfile getSelectedDirectory() {
        return selectedDirectory;
    }

    public List<UserConnections> getDirectoryConnections() {
        return ContactsViewModel.directoryConnections;
    }

    public void setTotalDirectories(int totalDirectories) {
        ContactsViewModel.totalDirectories = totalDirectories;
    }

    public int getTotalDirectories() {
        return totalDirectories;
    }

    /********Onclick Listeners********/

    public void onInfoClicked() {

        getNavigator().onInfoClicked();
    }

    public void onCrmClicked() {

        getNavigator().onCrmClicked();
    }

    public void onNotificationsClicked() {

        getNavigator().onNotificationsClicked();
    }

    public void onAddNewContactClicked() {

        getNavigator().onAddNewContactClicked();
    }

    public void onSycnContactsClicked() {

        getNavigator().onSyncContactsClicked();
    }

    public void onSettingsClicked() {

        getNavigator().onSettingsClicked();
    }

    public void onProfileImageClicked() {

        getNavigator().onProfileImageClicked();
    }

    public void onGroupFilterClicked() {

        getNavigator().onBusinessFilterClicked();
    }

    public void onIndividualFilterClicked() {

        getNavigator().onIndividualFilterClicked();
    }

    public void onDirectoryCountClicked(){

        getNavigator().onDirectoryCountClicked();
    }

    /**************App Logic**************/


    public void setBusinessContactProfiles() {

        getListOfAllDirectories();
    }


    /**
     * Gets the most current business directory for the user
     */
    private void getListOfAllDirectories() {

        directoryConnections.clear();

        for (UserConnections connections : getUserConnectionsList()) {

            if (connections.isOrgBus && connections.isDirectory) {

                directoryConnections.add(connections);
            }
        }

        Collections.sort(directoryConnections, (o1, o2) -> Double.compare(o2.getTimestamp(), o1.getTimestamp()));

        if (!directoryConnections.isEmpty()) {

            UserConnections selectedConnection = directoryConnections.get(0);
            setSelectedBusinessProfile(selectedConnection.getBusinessProfile());
            getAllBusinessConnections(selectedConnection);
        }
    }


    public void getAllBusinessConnections(UserConnections selectedConnections){

        List<UserConnections> requestingConnections = new ArrayList<>();

        for (UserConnections connections: getAllConnectionsList()){

            if (connections.getConsentingUserID().equals(selectedConnections.getConsentingUserID())){

                requestingConnections.add(connections);
            }
        }

        for (BusinessProfile profile : getBusinessProfileList()){

            if (selectedConnections.getConsentingUserID().equals(profile.getId())){

                setSelectedDirectory(profile);
            }
        }

        for (UserConnections connections : requestingConnections) {

            for (UserProfile profile : getUserProfileList()) {

                if (connections.requestingUserID.equals(profile.getId())) {

                    profile.setShareNeeds(getSelectedDirectory().getShareNeeds());
                    connections.setUserProfile(profile);
                    connections.setOverrideBusinessProfile(true);
                    connections.setConnectionType(profile.getConnectionType());

                }
            }
        }

            setBusinessContactsList(selectedConnections,requestingConnections);

    }


    /**
     * Gets a list of business contacts for the most recently connected organization
     * and saves the correlating UserConnection object
     *
     * @param selectedConnections
     */
    private void setBusinessContactsList(UserConnections selectedConnections,List<UserConnections> connectionsList){

        List<String> businessContactList = new ArrayList<>();
        List<UserConnections> businessAssociatesList = new ArrayList<>();

        for (Map.Entry<String, String> entry : selectedConnections.getBusinessProfile().getContacts().entrySet()) {

            String userId = entry.getKey();

            if (!businessContactList.contains(userId)) {

                businessContactList.add(userId);
            }
        }

        for (UserConnections connections : connectionsList) {

            try{

                if (businessContactList.contains(connections.getUserProfile().getId())) {

                    businessAssociatesList.add(connections);
                }

            }catch (Exception e){

                Logger.e(e.getMessage());
            }

        }



        Collections.sort(businessAssociatesList,
                (o1, o2) -> o1.getUserProfile().getLastName().compareTo(o2.getUserProfile().getLastName()));

        getNavigator().onBusinessContactsSet(businessAssociatesList);
    }


        /*********DataModel Requests*******/

        public void requestContactsInfo (ContactsViewModel viewModel){

            dataModel.requestContactsInfo(viewModel);
        }
    }
