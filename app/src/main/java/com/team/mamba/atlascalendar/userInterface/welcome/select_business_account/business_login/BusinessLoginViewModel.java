package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;
import com.team.mamba.atlascalendar.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class BusinessLoginViewModel extends BaseViewModel<BusinessLoginNavigator> {


    private BusinessLoginDataModel dataModel;
    private List<BusinessProfile> businessProfileList = new ArrayList<>();


    /*************Getters and Setters***************/

    public void setDataModel(BusinessLoginDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setBusinessProfileList(List<BusinessProfile> businessProfileList) {
        this.businessProfileList = businessProfileList;
    }

    public List<BusinessProfile> getBusinessProfileList() {
        return businessProfileList;
    }



    /****************App Logid***************/

    public boolean isEmailValid(String email){

        return  CommonUtils.isEmailValid(email);

    }

    public boolean isPasswordValid(String password){

        return !password.isEmpty();
    }

    /*********OnClick Listeners******************/

    public void onBusinessScreenLoginClicked(){

        getNavigator().onBusinessScreenLoginClicked();
    }

    public void onBusinessScreenLearnMoreClicked(){

        getNavigator().onBusinessScreenLearnMoreClicked();
    }


    /************DataModel Requests*****************/


    public void firebaseAuthenticateByEmail(BusinessLoginViewModel viewModel, String email, String password){

        dataModel.firebaseAuthenticateByEmail(viewModel,email,password);
    }

}
