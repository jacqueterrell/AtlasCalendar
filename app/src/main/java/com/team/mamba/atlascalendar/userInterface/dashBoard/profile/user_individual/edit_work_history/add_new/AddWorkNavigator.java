package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new;

import java.util.List;

public interface AddWorkNavigator {

    void onAutoCompleteReturned(List<String> decriptionList);

    void handleError(String msg);

    void onRowClicked(String text);
}
