package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;

public interface EditPageOneNavigator {

    void onRowClicked(UserProfile userProfile);

    boolean isContactsScreenShown();

    void closeContactsScreen();
}
