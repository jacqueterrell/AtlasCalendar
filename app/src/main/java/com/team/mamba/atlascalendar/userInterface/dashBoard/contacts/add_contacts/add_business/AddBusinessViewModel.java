package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddBusinessViewModel extends BaseViewModel<AddBusinessNavigator> {


    private AddBusinessDataModel dataModel;
    private BusinessProfile requestingBusinessProfile;
    private UserProfile requestingUserProfile;
    List<String> connectionIdList = new ArrayList<>();


    /******Getters and Setters******/

    public void setDataModel(AddBusinessDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setRequestingBusinessProfile(BusinessProfile requestingProfile) {
        this.requestingBusinessProfile = requestingProfile;
    }

    public BusinessProfile getRequestingBusinessProfile() {
        return requestingBusinessProfile;
    }

    public void setRequestingUserProfile(UserProfile requestingUserProfile) {
        this.requestingUserProfile = requestingUserProfile;
    }

    public UserProfile getRequestingUserProfile() {
        return requestingUserProfile;
    }

    public void setConnectionIdList(List<String> connectionIdList) {
        this.connectionIdList = connectionIdList;
    }

    public List<String> getConnectionIdList() {
        return connectionIdList;
    }

    /****Onclick Listeners******/

    public void onFinishButtonClicked(){

        getNavigator().onFinishButtonClicked();
    }

    public void addBusinessRequest(AddBusinessViewModel viewModel,String name,String code){

        dataModel.addBusinessRequest(viewModel,name,code);
    }
}
