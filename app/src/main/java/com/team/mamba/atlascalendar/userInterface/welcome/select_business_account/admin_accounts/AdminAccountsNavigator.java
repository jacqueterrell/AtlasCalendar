package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;

public interface AdminAccountsNavigator {


    void handleError(String errorMsg);

    void onAdminProfileSelected(UserProfile userProfile);

    void onAdminProfilesReturned();

    void openDashBoard();
}
