package com.team.mamba.atlascalendar.userInterface.dashBoard.locator.connectionsRequest;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;

public interface ConnectionsNavigator {

    void onRowClicked(UserConnections userConnections);
}
