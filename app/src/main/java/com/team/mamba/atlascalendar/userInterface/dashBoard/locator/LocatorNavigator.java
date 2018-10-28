package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import java.util.List;

public interface LocatorNavigator {

    void onSettingsClicked();

    void handleError(String msg);

    void onCrmClicked();

    void onNotificationsClicked();

    void onContactsClicked();

    void onEmployeeContactsReturned();

    List<UserProfile> getPermProfileList();

    List<UserProfile> getFavoriteProfileList();

}
