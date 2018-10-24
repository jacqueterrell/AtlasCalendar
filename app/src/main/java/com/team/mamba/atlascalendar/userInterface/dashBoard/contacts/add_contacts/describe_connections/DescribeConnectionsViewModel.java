package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class DescribeConnectionsViewModel extends BaseViewModel<DescribeConnectionsNavigator> {


    private DescribeConnectionsDataModel dataModel;
    private UserProfile loggedInProfileIndividual;
    private BusinessProfile loggedInProfileBusiness;
    private boolean approvingConnection = false;
    private UserConnections requestingConnection;



    /******Getters and Setters******/

    public void setDataModel(DescribeConnectionsDataModel dataModel) {
        this.dataModel = dataModel;
    }


    public void setLoggedInProfileIndividual(UserProfile loggedInProfileIndividual) {
        this.loggedInProfileIndividual = loggedInProfileIndividual;
    }

    public UserProfile getLoggedInProfileIndividual() {
        return loggedInProfileIndividual;
    }

    public void setLoggedInProfileBusiness(BusinessProfile loggedInProfileBusiness) {
        this.loggedInProfileBusiness = loggedInProfileBusiness;
    }

    public BusinessProfile getLoggedInProfileBusiness() {
        return loggedInProfileBusiness;
    }

    public void setApprovingConnection(boolean approvingConnection) {
        this.approvingConnection = approvingConnection;
    }

    public boolean isApprovingConnection() {
        return approvingConnection;
    }

    public void setRequestingConnection(UserConnections requestingConnection) {
        this.requestingConnection = requestingConnection;
    }

    public UserConnections getRequestingConnection() {
        return requestingConnection;
    }

    /*****Onclick Listeners*********/

    public void onFinishButtonClicked(){

        getNavigator().onFinishButtonClicked();
    }

    public void onInfoClicked(){

        getNavigator().onInfoClicked();
    }

    public void hideConnectionsInfo(){

        getNavigator().hideConnectionsInfo();
    }

    /*****DataModel calls******/

    public void addUserRequest(DescribeConnectionsViewModel viewModel,String lastName, String userCode,int connectionType){

        dataModel.initiateNewUserRequest(viewModel,lastName,userCode,connectionType);
    }

    public void updateConnection(DescribeConnectionsViewModel viewModel,UserConnections connections){

        dataModel.updateContactsConnection(viewModel,connections);
    }
}
