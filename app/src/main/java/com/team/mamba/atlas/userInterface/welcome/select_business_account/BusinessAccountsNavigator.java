package com.team.mamba.atlas.userInterface.welcome.select_business_account;

import com.team.mamba.atlas.data.model.api.fireStore.BusinessProfile;

public interface BusinessAccountsNavigator {

    void onAccountSelected(BusinessProfile profile);
}
