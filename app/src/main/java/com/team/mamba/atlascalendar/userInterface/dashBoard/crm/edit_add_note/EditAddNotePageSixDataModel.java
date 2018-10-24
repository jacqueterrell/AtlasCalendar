package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;

import com.team.mamba.atlascalendar.utils.AppConstants;
import javax.inject.Inject;

public class EditAddNotePageSixDataModel {


    private AppDataManager dataManager;
    public static boolean shouldReloadCrm = false;

    @Inject
    public EditAddNotePageSixDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void setNoteRequest(EditAddNotePageSixViewModel viewModel, CrmNotes notes){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.BUS_NOTES_COLLECTION)
                .document(notes.id)
                .set(notes)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        viewModel.getNavigator().onNoteSubmitted();
                        shouldReloadCrm = true;

                    } else {

                        Logger.e("Could not update address: " + task.getException());
                    }

                });
    }
}
