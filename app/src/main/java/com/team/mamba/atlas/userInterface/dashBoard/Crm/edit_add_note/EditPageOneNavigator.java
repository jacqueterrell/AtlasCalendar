package com.team.mamba.atlas.userInterface.dashBoard.Crm.edit_add_note;

import com.team.mamba.atlas.data.model.api.UserProfile;

public interface EditPageOneNavigator {

    void onRowClicked(UserProfile userProfile);

    boolean isContactsScreenShown();

    void closeContactsScreen();
}
