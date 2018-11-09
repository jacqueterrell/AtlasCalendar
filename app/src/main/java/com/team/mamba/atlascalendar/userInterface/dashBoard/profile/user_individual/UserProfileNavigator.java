package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual;

public interface UserProfileNavigator {

    void onUserProfileClicked();

    void onSettingsClicked();

    void editEmployer();

    void editPhoneInfo();

    void editEmailInfo();

    void editAddressInfo();

    void editWorkHistoryInfo();

    void editEductionInfo();

    void onProfileUpdated();

    void openCamera();

    void openGallery();

    String getImagePath();

    void handleError(String errMsg);
}
