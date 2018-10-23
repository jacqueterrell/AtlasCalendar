package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class CrmDataModel {

    private AppDataManager dataManager;

    @Inject
    public CrmDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    /**
     * Retrieves a list of business notes authored by the user
     */
    public void getAllOpportunities(CrmViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        viewModel.setSavedUserId(dataManager.getSharedPrefs().getUserId());

        getUserDetails(viewModel);

        db.collection(AppConstants.BUS_NOTES_COLLECTION)
                .whereEqualTo("authorID", dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<CrmNotes> crmNotes = task.getResult().toObjects(CrmNotes.class);

                        Collections.sort(crmNotes,
                                (o1, o2) -> Double.compare(o2.getAdjustedTimeStamp(), o1.getAdjustedTimeStamp()));

                        viewModel.setCrmNotesList(crmNotes);
                        viewModel.getNavigator().onCrmDataReturned();

                    } else {

                        viewModel.getNavigator().handleError(task.getException().getMessage());
                        Logger.e(task.getException().getMessage());
                    }

                });
    }


    /**
     * Gets a list of all profiles
     */
    private void getUserDetails(CrmViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> adjustedProfileList = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles) {

                            if (profile.getId() != null) {

                                adjustedProfileList.add(profile);
                            }
                        }
                        getAllConnections(viewModel, adjustedProfileList);

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());

                    }

                });

    }


    /**
     * Retrieves all confirmed contacts for the logged in user
     *
     * @param userProfiles list of all user profiles
     */
    private void getAllConnections(CrmViewModel viewModel, List<UserProfile> userProfiles) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> usersContactProfiles = new ArrayList<>();
        String userId = dataManager.getSharedPrefs().getUserId();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                        if (dataManager.getSharedPrefs().isBusinessAccount()){

                            for (UserProfile profile : userProfiles) {

                                for (UserConnections connection : connectionsList) {

                                    if (connection.isConfirmed
                                            && connection.getConsentingUserID().equals(userId)
                                            && profile.getId().equals(connection.getRequestingUserID())) {

                                        usersContactProfiles.add(profile);
                                    }

                                }
                            }

                        } else {

                            for (UserProfile profile : userProfiles) {

                                for (UserConnections connection : connectionsList) {

                                    if (connection.isConfirmed
                                            && connection.getRequestingUserID().equals(userId)
                                            && profile.getId().equals(connection.getConsentingUserID())) {

                                            usersContactProfiles.add(profile);
                                    }

                                }
                            }
                        }

                        getAllConnectionTypes(viewModel, usersContactProfiles);

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }

                });

    }


    /**
     * Sets the connection type for each UserProfile
     * @param viewModel
     * @param userProfiles
     */
    private void getAllConnectionTypes(CrmViewModel viewModel, List<UserProfile> userProfiles) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .whereEqualTo("consentingUserID", dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                        for (UserProfile profile : userProfiles) {

                            for (UserConnections connection : connectionsList) {

                                if (connection.isConfirmed) {

                                    if (profile.getId().equals(connection.getRequestingUserID())) {

                                        profile.setConnectionType(connection.getConnectionType());
                                    }
                                }

                            }
                        }

                        viewModel.setUsersContactProfiles(userProfiles);

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }

                });

    }

}
