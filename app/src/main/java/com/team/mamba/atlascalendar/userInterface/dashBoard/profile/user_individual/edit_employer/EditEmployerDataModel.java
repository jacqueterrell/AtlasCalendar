package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_employer;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import javax.inject.Inject;

public class EditEmployerDataModel {

    private AppDataManager dataManager;

    @Inject
    public EditEmployerDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void updateUser(EditEmployerViewModel viewModel, UserProfile profile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.USERS_COLLECTION)
                .document(profile.getId())
                .set(profile)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        viewModel.getNavigator().onProfileUpdated();

                    } else {

                        Logger.e("Could not update address: " + task.getException());
                    }

                });
    }
}
