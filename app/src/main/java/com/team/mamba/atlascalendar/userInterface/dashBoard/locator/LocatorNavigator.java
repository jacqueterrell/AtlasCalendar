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

    void addFavoriteUser(String profileId);

    void removeFavoriteUser(String profileId);

    void onFavoriteAddedSuccessfully(String profileId);

    void onFavoriteRemovedSuccessfully(String profileId);

    void updateFavoritesList();

    List<UserProfile> getPermProfileList();

}
