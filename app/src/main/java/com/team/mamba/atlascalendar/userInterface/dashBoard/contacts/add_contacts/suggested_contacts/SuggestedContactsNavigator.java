package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;

public interface SuggestedContactsNavigator {

    void onSearchButtonClicked();

    void onSuggestedContactsFound();

    void onUsersRowClicked(UserProfile profile);

    void showInvitationSentAlert(String first,String last);

    void handleError(String msg);


}
