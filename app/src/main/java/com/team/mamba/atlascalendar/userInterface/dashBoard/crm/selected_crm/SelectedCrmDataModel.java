package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.selected_crm;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.utils.AppConstants;

import javax.inject.Inject;

public class SelectedCrmDataModel {



    private AppDataManager dataManager;

    @Inject
    public SelectedCrmDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void deleteCrmNote(SelectedCrmViewModel viewMode, CrmNotes notes){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.BUS_NOTES_COLLECTION)
                .document(notes.getId())
                .delete()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        viewMode.getNavigator().onSuccess();
                        SelectedCrmFragment.isNoteDeleted = true;

                    } else {

                        Logger.e(task.getException().getMessage());
                    }

                });

    }


}
