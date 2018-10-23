package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;

public interface FindUsersRecyclerNavigator {

    void onUsersRowClicked(UserProfile profile);

    void showAlreadyAContactAlert();

    void showInvitationSentAlert(String first,String last);

    void handleError(String msg);

}
