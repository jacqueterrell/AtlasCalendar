package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_accounts_recycler;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;

public interface BusinessAccountsNavigator {

    void onAccountSelected(BusinessProfile profile);
}
