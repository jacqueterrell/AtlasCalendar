package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections;

public interface DescribeConnectionsNavigator {

    void onFinishButtonClicked();

    void onInfoClicked();

    void hideConnectionsInfo();

    void showUserNotFoundAlert();

    void showAlreadyAContactAlert();

    void onRequestSent();

    void onConnectionRequestApproved();

    void onConnectionUpdated();

    void handleError(String errorMsg);
}
