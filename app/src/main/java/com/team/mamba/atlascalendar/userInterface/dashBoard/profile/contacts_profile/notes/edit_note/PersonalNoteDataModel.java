package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.PersonalNotes;
import com.team.mamba.atlascalendar.utils.AppConstants;

import javax.inject.Inject;

public class PersonalNoteDataModel {

    private AppDataManager dataManager;


    @Inject
    public PersonalNoteDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void sendUserNote(PersonalNotesViewModel viewModel,PersonalNotes personalNotes){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newNotesRef = db.collection(AppConstants.USER_NOTES_COLLECTION).document();
        Long timeStamp = System.currentTimeMillis() / 1000;
        String id = newNotesRef.getId();

        personalNotes.setTimestamp(timeStamp);
        personalNotes.setId(id);

        newNotesRef.set(personalNotes)
                .addOnSuccessListener(aVoid -> {

                    Logger.i("Note successfully updated");
                    viewModel.getNavigator().onNoteSentSuccessfully();
                })
                .addOnFailureListener(e -> {
                    //
                    Logger.e("Error updating note");
                });

    }
}
