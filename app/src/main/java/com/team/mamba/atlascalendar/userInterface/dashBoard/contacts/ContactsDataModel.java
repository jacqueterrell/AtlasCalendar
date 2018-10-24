package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ContactsDataModel {

    private AppDataManager dataManager;
    private boolean isSuccesful = true;
    private Exception exception;
    private List<UserConnections> defaultConnectionsList = new ArrayList<>();


    @Inject
    public ContactsDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    public void requestContactsInfo(ContactsViewModel viewModel) {

        viewModel.setSavedUserId(dataManager.getSharedPrefs().getUserId());
        requestUserProfiles(viewModel);
    }

    /**
     * Gets a list of all profiles and saves the user's profile
     */
    private void requestUserProfiles(ContactsViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> adjustedProfileList = new ArrayList<>();


        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles) {

                            if (profile.getId() != null) {

                                if (profile.getId().equals(dataManager.getSharedPrefs().getUserId())) {

                                    viewModel.setUserProfile(profile);
                                }

                                adjustedProfileList.add(profile);
                            }
                        }

                        viewModel.setUserProfileList(adjustedProfileList);
                        requestAllConnections(viewModel);

                    } else {

                        exception = task.getException();
                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }

                });
    }


    /**
     * Gets a list of all of the user's approved connections
     */
    private void requestAllConnections(ContactsViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserConnections> userConnections = new ArrayList<>();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .whereEqualTo("isConfirmed", true)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                        viewModel.setAllConnectionsList(connectionsList);

                        for (UserConnections connections : connectionsList) {

                            if (dataManager.getSharedPrefs().isBusinessAccount()){

                                if (connections.getConsentingUserID().equals(dataManager.getSharedPrefs().getUserId())){

                                    connections.setOverrideBusinessProfile(true);
                                    userConnections.add(connections);
                                }

                            } else {

                                if (connections.getRequestingUserID().equals(dataManager.getSharedPrefs().getUserId())){

                                    userConnections.add(connections);
                                }
                            }
                        }

                        defaultConnectionsList = userConnections;
                        getAllConnectionTypes(viewModel);

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }

                });
    }

    /**
     * This finds the connection type and assigns it to the appropriate
     * UserProfile
     * @param viewModel
     */
    private void getAllConnectionTypes(ContactsViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .whereEqualTo("consentingUserID", dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                        for (UserConnections connections : connectionsList) {

                            if (connections.isConfirmed){

                                for (UserProfile profile : viewModel.getUserProfileList()){

                                    if (profile.getId().equals(connections.getRequestingUserID())){

                                        profile.setConnectionType(connections.getConnectionType());
                                    }
                                }
                            }
                        }

                        requestAllBusinessProfiles(viewModel);

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }

                });
    }


    /**
     * Save a list of all business profiles and sets the user's business
     * profile if they are a business.
     */
    private void requestAllBusinessProfiles(ContactsViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String savedUserId = dataManager.getSharedPrefs().getUserId();


        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (!viewModel.getNavigator().isActivityVisible()){

                            return;
                        }

                        isSuccesful = true;
                        List<BusinessProfile> businessProfiles = task.getResult().toObjects(BusinessProfile.class);

                        viewModel.setBusinessProfileList(businessProfiles);

                        for (BusinessProfile profile : businessProfiles) {

                            if (profile.getId().equals(savedUserId)) {

                                viewModel.setBusinessProfile(profile);

                            }
                        }

                        setConnectionsProfiles(viewModel);

                    } else {

                        Logger.e(task.getException().getMessage());

                            viewModel.getNavigator().handleError("Error requesting business collections");

                    }
                });

    }



    /**
     * Sets the correlating profile (business or user) for our UserConnections list
     *
     */
    private void setConnectionsProfiles(ContactsViewModel viewModel){

        List<UserConnections> individualConnections = new ArrayList<>();
        List<UserConnections> businessConnections = new ArrayList<>();

        String userId = dataManager.getSharedPrefs().getUserId();
        List<UserConnections> adjustedConnections = new ArrayList<>();


            if (dataManager.getSharedPrefs().isBusinessAccount()){//logged in as a business

                for (UserConnections connections : defaultConnectionsList) {

                    for (UserProfile profile : viewModel.getUserProfileList()) {

                        if (connections.requestingUserID.equals(profile.getId())
                                && connections.consentingUserID.equals(userId)) {

                            profile.setShareNeeds(viewModel.getBusinessProfile().getShareNeeds());
                            connections.setUserProfile(profile);
                            connections.setConnectionType(profile.getConnectionType());
                            individualConnections.add(connections);
                        }
                    }
                }

            } else {

                for (UserConnections connections : defaultConnectionsList) {

                    if (connections.isOrgBus && !connections.isOverrideBusinessProfile()) {

                        for (BusinessProfile profile : viewModel.getBusinessProfileList()) {

                            if (connections.requestingUserID.equals(userId)
                                    && connections.consentingUserID.equals(profile.getId())) {

                                connections.setBusinessProfile(profile);
                                businessConnections.add(connections);
                            }
                        }

                    } else {

                        for (UserProfile profile : viewModel.getUserProfileList()) {

                            if (connections.requestingUserID.equals(userId)
                                    && connections.consentingUserID.equals(profile.getId())) {

                                connections.setUserProfile(profile);
                                connections.setConnectionType(profile.getConnectionType());
                                individualConnections.add(connections);

                            }
                        }

                    }
                }
            }

        Collections.sort(individualConnections, (o1, o2) -> o1.getUserProfile().getLastName().compareTo(o2.getUserProfile().getLastName()));
        Collections.sort(businessConnections, (o1, o2) -> Boolean.compare(o1.isIsOrgBus(), o2.isOrgBus));
        adjustedConnections.addAll(individualConnections);
        adjustedConnections.addAll(businessConnections);


        int totalDirectories = 0;

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            for (UserConnections connections : adjustedConnections) {
                if (connections.isOrgBus && connections.isDirectory) {
                    totalDirectories += 1;
                }
            }
        }


        viewModel.setTotalDirectories(totalDirectories);
        viewModel.setUserConnectionsList(adjustedConnections);
        viewModel.getNavigator().onDataValuesReturned(adjustedConnections);
    }



}
