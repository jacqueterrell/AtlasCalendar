package com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.CrmFilter;

public interface DashBoardActivityNavigator {


    void openAddContactDialog();

    void openSettingsScreen();

    void openUserProfile(UserProfile userProfile);

    void openBusinessProfile(BusinessProfile businessProfile);

    void setUserProfile(UserProfile userProfile);

    UserProfile getUserProfile();

    CrmFilter getCrmFilter();

    void setCrmFilter(CrmFilter crmFilter);

    void resetToFirstFragment();

    void refreshInfoFragment();

    boolean wasContactRecentlyDeleted();

    boolean wasContactRecentlyAdded();

    void setContactRecentlyAdded(boolean wasAdded);

    void setContactRecentlyDeleted(boolean wasDeleted);

}
