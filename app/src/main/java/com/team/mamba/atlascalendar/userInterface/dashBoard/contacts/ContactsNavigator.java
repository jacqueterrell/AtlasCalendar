package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;

import java.util.List;

public interface ContactsNavigator {

    void onAddNewContactClicked();

    void onSyncContactsClicked();

    void onSettingsClicked();

    void onProfileImageClicked();

    void onBusinessFilterClicked();

    void onIndividualFilterClicked();

    void onRowClicked(UserConnections userConnections);

    void onDirectoryCountClicked();

    void onDirectoryRowClicked(UserConnections userConnections);

    void handleError(String errorMsg);

    void onDataValuesReturned(List<UserConnections> connectionsList);

    void onBusinessContactsSet(List<UserConnections> businessAssociatesList);

    boolean isActivityVisible();

    List<UserConnections> getPermConnectionList();

    void onInfoClicked();

    void onNotificationsClicked();

    void onCrmClicked();
}
