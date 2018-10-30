package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business;

public interface AddBusinessNavigator {

    void onFinishButtonClicked();

    void showUserNotFoundAlert();

    void showAlreadyAContactAlert();

    void onRequestSent();

    void onCalendarConnectionSaved();
}
