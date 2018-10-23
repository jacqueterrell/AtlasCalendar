package com.team.mamba.atlascalendar.userInterface.welcome._container_activity;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class WelcomeActivityViewModel extends BaseViewModel<WelcomeActivityNavigator> {


    private boolean businessLogin = false;


    public void setBusinessLogin(boolean businessLogin) {
        this.businessLogin = businessLogin;
    }

    public boolean isBusinessLogin() {
        return businessLogin;
    }
}
