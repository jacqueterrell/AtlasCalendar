package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts;

import com.google.firebase.auth.FirebaseAuth;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class AdminAccountsViewModel extends BaseViewModel<AdminAccountsNavigator> {


    private AdminAccountsDataModel dataModel;
    private List<UserProfile> adminProfileList = new ArrayList<>();

    /************Getters and Setters**************/

    public void setDataModel(AdminAccountsDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public List<UserProfile> getAdminProfileList() {
        return adminProfileList;
    }

    public void setAdminProfileList(List<UserProfile> adminProfileList) {
        this.adminProfileList = adminProfileList;
    }


    /***************DataModel Requests*****************/

    public void getAllAdminProfiles(AdminAccountsViewModel viewModel) {

        dataModel.getAllAdminProfiles(viewModel);
    }

    public void signInAnonymously(AdminAccountsViewModel viewModel, FirebaseAuth firebaseAuth){

        dataModel.signInAnonymously(viewModel,firebaseAuth);
    }
}
