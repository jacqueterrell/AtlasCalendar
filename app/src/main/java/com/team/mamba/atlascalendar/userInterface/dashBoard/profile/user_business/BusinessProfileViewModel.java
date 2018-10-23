package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_business;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class BusinessProfileViewModel extends BaseViewModel<BusinessProfileNavigator> {

    private static BusinessProfile businessProfile;
    private BusinessProfileDataModel dataModel;



    /*******Getters and Setters************/
    public void setDataModel(BusinessProfileDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        BusinessProfileViewModel.businessProfile = businessProfile;
    }


    /*******Onclick Listeners*******/
    public void onProfileImageClicked(){

        getNavigator().onProfileImageClicked();
    }

    public void callBusinessContact(){

        getNavigator().callBusinessContact();
    }

    public void contactOnNameClicked(){

        getNavigator().contactOnNameClicked();
    }

    public void contactOnEmailClicked(){

        getNavigator().contactOnEmailClicked();
    }

    public void contactOnFaxClicked(){

        getNavigator().contactOnFaxClicked();
    }

    public void contactOnAddressClicked(){

        getNavigator().contactOnAddressClicked();
    }

    public void contactOnCodeClicked(){

        getNavigator().contactOnCodeClicked();
    }

    public void contactOnContactNameClicked(){

        getNavigator().contactOnContactNameClicked();
    }
}
