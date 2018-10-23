package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number;

import android.app.Activity;
import com.google.firebase.auth.PhoneAuthProvider;

public interface EnterPhoneNavigator {

    void onPhoneSubmitClicked();

    PhoneAuthProvider.OnVerificationStateChangedCallbacks getPhoneCallBacks();

    Activity getParentActivity();

    void handleError(String msg);

    void openDashBoard();

    void onPhoneSubmitPreviousClicked();

    void onEnterSmsCancelClicked();

    void onEnterSmsContinueClicked();
}
