package com.team.mamba.atlascalendar.userInterface.dashBoard.settings;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class SettingsViewModel extends BaseViewModel<SettingsNavigator> {


    private SettingsDataModel dataModel;


    public void setDataModel(SettingsDataModel dataModel) {
        this.dataModel = dataModel;
    }


    /**************Getters and Setters******************/

    public void onCorporateDirectoryClicked(){

        getNavigator().onCorporateDirectoryClicked();
    }

    public void onOrganizationalOutreachClicked(){

        getNavigator().onOrganizationalOutreachClicked();
    }

    public void onAlumniNetworkingClicked(){

        getNavigator().onAlumniNetworkingClicked();
    }

    public void onPrivacyPolicyClicked(){

        getNavigator().onPrivacyPolicyClicked();
    }

    public void onTermsOfServiceClicked(){

        getNavigator().onTermsOfServiceClicked();
    }

    public void onNetworkManagementClicked(){

        getNavigator().onNetworkManagementClicked();
    }

    public void onLogOutClicked(){

        getNavigator().onLogOutClicked();
    }

    public void onDeleteMyAccountClicked(){

        getNavigator().onDeleteMyAccountClicked();
    }

    public void onSiteLinkClicked(){

        getNavigator().onSiteLinkClicked();;
    }

    /**************DataModel Requests**********************/

    public void deleteUser(){

        dataModel.deleteUser();
    }
}
