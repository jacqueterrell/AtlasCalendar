package com.team.mamba.atlas.userInterface.dashBoard.settings.network_management;

import com.team.mamba.atlas.data.model.api.BusinessProfile;
import com.team.mamba.atlas.data.model.api.UserConnections;
import com.team.mamba.atlas.data.model.api.UserProfile;
import com.team.mamba.atlas.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class NetworkManagementViewModel extends BaseViewModel<NetworkManagementNavigator> {


    private NetworkManagementDataModel dataModel;
    private List<UserConnections> userConnectionsList = new ArrayList<>();
    private BusinessProfile loggedInBusinessProfile;
    private UserProfile loggedInUserProfile;


    public NetworkManagementDataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(NetworkManagementDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setUserConnectionsList(List<UserConnections> userConnectionsList) {
        this.userConnectionsList = userConnectionsList;
    }

    public List<UserConnections> getUserConnectionsList() {
        return userConnectionsList;
    }

    public BusinessProfile getLoggedInBusinessProfile() {
        return loggedInBusinessProfile;
    }

    public void setLoggedInBusinessProfile(BusinessProfile loggedInBusinessProfile) {
        this.loggedInBusinessProfile = loggedInBusinessProfile;
    }

    public UserProfile getLoggedInUserProfile() {
        return loggedInUserProfile;
    }

    public void setLoggedInUserProfile(UserProfile loggedInUserProfile) {
        this.loggedInUserProfile = loggedInUserProfile;
    }

    /*****************DataModel Requests*****************/

    public void getUsersContacts(NetworkManagementViewModel viewModel){

        dataModel.getUsersContacts(viewModel);
    }

    public void deleteUserConnection(NetworkManagementViewModel viewModel,UserConnections userConnections){

        dataModel.deleteUserConnection(viewModel,userConnections);
    }

    public void undoConnectionDeletion(){


    }
}
