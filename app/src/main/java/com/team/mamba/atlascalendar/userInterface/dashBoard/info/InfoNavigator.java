package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;

import java.util.List;

public interface InfoNavigator {

    void onSettingsClicked();

    void handleError(String msg);

    void onCrmClicked();

    void onNotificationsClicked();

    void onContactsClicked();

}
