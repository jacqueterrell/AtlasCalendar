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

    void onHamburgerClicked();

    void onAccountManagementClicked();

    void onLogOutClicked();

    void onAddConnectionClicked();

    void addFavoriteUser(UserProfile userProfile);

    void removeFavoriteUser(UserProfile userProfile);

    void onCalendarClicked(UserProfile userProfile);

    void updateFavoritesList();

    void showAddCalendarMessage();

    List<UserProfile> getPermProfileList();

}
