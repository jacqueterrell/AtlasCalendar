package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main;

import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import java.util.List;

public interface CrmNavigator {

    void onCrmDataReturned();

    void onRowClicked(CrmNotes notes);

    void onSettingsClicked();

    void onShareClicked();

    void onCreateNewNoteClicked();

    void onFilterClicked();

    void hideCrmInfoDialog();

    boolean isInfoDialogShown();

    void onExportButtonClicked();

    boolean isExportScreenShown();

    void hideExportScreen();

    void handleError(String error);

    List<CrmNotes> getPerCrmNotesList();

    void onCrmInfoButtonClicked();

    void onInfoClicked();

    void onNotificationsClicked();

    void onContactsClicked();

    void onExportContactsCheckboxClicked();

    void onExportOpportunitiesCheckboxClicked();
}
