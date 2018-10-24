package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class SuggestedContactsViewModel extends BaseViewModel<SuggestedContactsNavigator> {

    private SuggestedContactsDataModel dataModel;
    private List<UserProfile> suggestedProfileList = new ArrayList<>();
    private UserProfile requestingUserProfile;
    private BusinessProfile requestingBusinessProfile;
    private List<String> connectionIdList = new ArrayList<>();
    private List<UserProfile> allUsers = new ArrayList<>();

    /****Getters and Setters******/

    public void setDataModel(SuggestedContactsDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setSuggestedProfileList(List<UserProfile> suggestedProfileList) {
        this.suggestedProfileList = suggestedProfileList;
    }

    public List<UserProfile> getSuggestedProfileList() {
        return suggestedProfileList;
    }

    public void setRequestingUserProfile(UserProfile requestingUserProfile) {
        this.requestingUserProfile = requestingUserProfile;
    }

    public UserProfile getRequestingUserProfile() {
        return requestingUserProfile;
    }

    public void setRequestingBusinessProfile(BusinessProfile requestingBusinessProfile) {
        this.requestingBusinessProfile = requestingBusinessProfile;
    }

    public BusinessProfile getRequestingBusinessProfile() {
        return requestingBusinessProfile;
    }

    public void setConnectionIdList(List<String> connectionIdList) {
        this.connectionIdList = connectionIdList;
    }

    public List<String> getConnectionIdList() {
        return connectionIdList;
    }

    public void setAllUsers(List<UserProfile> allUsers) {
        this.allUsers = allUsers;
    }

    public List<UserProfile> getAllUsers() {
        return allUsers;
    }

    /*****Onclick Listeners*******/

    public void onSearchButtonClicked(){

        getNavigator().onSearchButtonClicked();
    }

    /*********DataModel Requests**********/

    public void requestSuggestedContacts(SuggestedContactsViewModel viewModel){

        dataModel.requestSuggestedContacts(viewModel);
    }

    public void initiateNewUserRequest(SuggestedContactsViewModel viewModel, UserProfile profile){

        dataModel.initiateNewUserRequest(viewModel,profile);
    }
}
