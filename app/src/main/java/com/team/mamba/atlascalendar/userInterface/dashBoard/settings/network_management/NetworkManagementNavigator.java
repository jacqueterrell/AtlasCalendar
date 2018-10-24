package com.team.mamba.atlascalendar.userInterface.dashBoard.settings.network_management;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;

public interface NetworkManagementNavigator {

    void onContactListReceived();

    void handleError(String msg);

    void onRowClicked(UserConnections connection);

    void onContactDeleted();
}
