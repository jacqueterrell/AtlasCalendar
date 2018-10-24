package com.team.mamba.atlascalendar.userInterface.dashBoard.settings;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.utils.AppConstants;
import javax.inject.Inject;


/**
 * This class handles all API calls for {@link SettingsFragment}
 */
public class SettingsDataModel {


    private AppDataManager dataManager;


    @Inject
    public SettingsDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    /**
     * Sets up the method to use for deleting the Signed in account
     * (Individual or Business)
     *
     */
    public void deleteUser() {

        if (dataManager.getSharedPrefs().isBusinessAccount()){

            deleteBusinessAccount();

        } else {

            deleteIndividualAccount();
        }
    }


    /**
     * Deletes the logged in Individual Account from the DB
     *
     */
    private void deleteIndividualAccount(){

        String userId = dataManager.getSharedPrefs().getUserId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .delete()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Logger.i("Account successfully deleted");

                    } else {

                        Logger.e("Could not delete user " + task.getException().getLocalizedMessage());
                    }
                });
    }


    /**
     * Deletes the logged in Business Account from the DB
     *
     */
    private void deleteBusinessAccount(){

        String userId = dataManager.getSharedPrefs().getUserId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .document(userId)
                .delete()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Logger.i("Account successfully deleted");

                    } else {

                        Logger.e("Could not delete user " + task.getException().getLocalizedMessage());
                    }
                });
    }

}
