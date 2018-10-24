package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login;

import android.app.Activity;

public interface BusinessLoginNavigator {


    void onBusinessScreenLoginClicked();

    void onBusinessScreenLearnMoreClicked();

    Activity getParentActivity();

    void showBusinessNotFoundAlert();

    void openDashBoard();

    void showMultipleBusinessLogin();

}
