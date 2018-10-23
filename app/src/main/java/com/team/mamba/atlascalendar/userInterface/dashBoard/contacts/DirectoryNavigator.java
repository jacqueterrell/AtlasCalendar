package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;

public interface DirectoryNavigator {

    void onRowClicked(UserConnections connections);
}
