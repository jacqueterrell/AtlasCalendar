package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class DescribeConnectionsDataModel {

    private AppDataManager dataManager;

    @Inject
    public DescribeConnectionsDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    /**
     * Based on the account type of business or individual, makes a
     * request to add a user based on received credentials
     */
    public void initiateNewUserRequest(DescribeConnectionsViewModel viewModel, String lastName, String code,
            int connectionType) {
        //query users and search for a match...

        if (dataManager.getSharedPrefs().isBusinessAccount()) {

            getBusinessProfile(viewModel, lastName, code, connectionType);

        } else {

            requestNewUserIndividual(viewModel, lastName, code, connectionType);
        }
    }


    /**
     * Looks through all Users and finds the profile for the logged in user, and gets a
     * list of all of their current connections.
     *
     * @param lastName       the selected last name
     * @param code           the selected code
     * @param connectionType the passed in connection type
     */
    private void requestNewUserIndividual(DescribeConnectionsViewModel viewModel, String lastName, String code,
            int connectionType) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> selectedProfileList = new ArrayList<>();
        List<String> connectionIdList = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles) {

                            if (profile.getId().equals(dataManager.getSharedPrefs().getUserId())) {

                                viewModel.setLoggedInProfileIndividual(profile);

                                for (Map.Entry<String, String> entry : profile.getConnections()
                                        .entrySet()) {//get a list of all current connections

                                    String userId = entry.getKey();
                                    connectionIdList.add(userId);
                                }

                            } else if (profile.getLastName().toLowerCase().equals(lastName.toLowerCase().trim())
                                    && profile.getCode().equals(code)) {

                                selectedProfileList.add(profile);
                            }
                        }

                        if (selectedProfileList.isEmpty()) {//no user found

                            viewModel.getNavigator().showUserNotFoundAlert();

                        } else {

                            UserProfile selectedProfile = selectedProfileList.get(0);

                            if (connectionIdList.contains(selectedProfile.getId())) {//already a contact

                                viewModel.getNavigator().showAlreadyAContactAlert();

                            } else {

                                addNewConnectionIndividual(viewModel, selectedProfile, connectionType);
                            }
                        }

                    } else {

                        handleError(task,viewModel);
                    }
                });
    }


    /**
     * Creates a new Connection for the User Request
     */
    private void addNewConnectionIndividual(DescribeConnectionsViewModel viewModel, UserProfile consentingProfile,
            int connectionType) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newUserRef = db.collection(AppConstants.CONNECTIONS_COLLECTION).document();
        UserProfile requestingProfile = viewModel.getLoggedInProfileIndividual();

        Long timeStamp = System.currentTimeMillis() / 1000;
        String requestingToken = dataManager.getSharedPrefs().getFirebaseDeviceToken();
        String id = newUserRef.getId();

        String consentingName = consentingProfile.getFirstName() + " " + consentingProfile.getLastName();
        String requestingName = requestingProfile.getFirstName() + " " + requestingProfile.getLastName();

        UserConnections userConnections = new UserConnections();
        userConnections.setConDeviceToken(consentingProfile.getDeviceToken());
        userConnections.setConnectionType(connectionType);
        userConnections.setConsentingUserID(consentingProfile.getId());
        userConnections.setConsentingUserName(consentingName);
        userConnections.setId(id);
        userConnections.setConfirmed(viewModel.isApprovingConnection());
        userConnections.setOrgBus(false);
        userConnections.setReqDeviceToken(requestingToken);
        userConnections.setRequestingUserName(requestingName);
        userConnections.setRequestingUserID(requestingProfile.getId());
        userConnections.setTimestamp(timeStamp);

        //Ensures that duplicate connections never exist
        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .whereEqualTo("requestingUserID", requestingProfile.getId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<String> consentingIdList = new ArrayList<>();

                        List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                        for (UserConnections connection : connectionsList) {

                            if (connection.getConsentingUserID().equals(consentingProfile.getId())) {

                                consentingIdList.add(consentingProfile.getId());
                            }
                        }

                        //if the connection does not already exists
                        if (consentingIdList.isEmpty()) {

                            newUserRef.set(userConnections);

                        }

                        if (viewModel.isApprovingConnection()) {

                            updateInitialConnection(viewModel);

                        } else {

                            viewModel.getNavigator().onRequestSent();
                        }

                    } else {

                        handleError(task,viewModel);
                    }

                });

    }

    /**
     * If the user is approving a connection request, this updates the original request
     * setting the confirmed field to 'true' and updating the timestamp
     */
    private void updateInitialConnection(DescribeConnectionsViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Long timeStamp = System.currentTimeMillis() / 1000;

        UserConnections connections = viewModel.getRequestingConnection();
        connections.setConfirmed(true);
        connections.setTimestamp(timeStamp);

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .document(connections.getId())
                .set(connections)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        viewModel.getNavigator().onConnectionRequestApproved();
                        Logger.i("Connection updated successfully");
                        addToUsersContactList(viewModel);

                    } else {

                        if (task.getException() != null) {

                            viewModel.getNavigator().handleError(
                                    "Failed to update connection " + task.getException().getLocalizedMessage());
                        }
                    }
                });


    }


    /**
     * Adds the the consenting user and the requesting user to each other's
     * contact list.
     */
    private void addToUsersContactList(DescribeConnectionsViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserConnections connections = viewModel.getRequestingConnection();

        if (!dataManager.getSharedPrefs().isBusinessAccount()) {

            //add the contact to the users contacts

            UserProfile loggedInUserProfile = viewModel.getLoggedInProfileIndividual();
            Map<String, String> connectionsMap = loggedInUserProfile.getConnections();
            connectionsMap.put(connections.getRequestingUserName(), connections.getRequestingUserID());

            db.collection(AppConstants.USERS_COLLECTION)
                    .document(loggedInUserProfile.getId())
                    .set(loggedInUserProfile)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Logger.i("New contact added successfully");

                        } else {

                            handleError(task,viewModel);
                        }
                    });

            //add the user to the consenting contact's list
            db.collection(AppConstants.USERS_COLLECTION)
                    .whereEqualTo("id", connections.getRequestingUserID())
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            UserProfile profile = task.getResult().toObjects(UserProfile.class).get(0);
                            Map<String, String> contactsMap = profile.getConnections();
                            contactsMap.put(loggedInUserProfile.getId(), loggedInUserProfile.getId());

                            //update the consenting profile with the new contact added
                            db.collection(AppConstants.USERS_COLLECTION)
                                    .document(profile.getId())
                                    .set(profile)
                                    .addOnCompleteListener(result -> {

                                        if (result.isSuccessful()) {

                                            Logger.i("New contact added successfully");

                                        } else {

                                            handleError(task, viewModel);
                                        }
                                    });

                        } else {

                            if (task.getException() != null) {

                                viewModel.getNavigator().handleError(task.getException().getLocalizedMessage());
                            }
                        }

                    });

        }

    }


    public void updateContactsConnection(DescribeConnectionsViewModel viewModel, UserConnections connection) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .document(connection.getId())
                .set(connection)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Logger.i("Connection updated successfully");
                        viewModel.getNavigator().onConnectionUpdated();

                    } else {

                        if (task.getException() != null) {

                            viewModel.getNavigator().handleError(
                                    "Failed to update connection " + task.getException().getLocalizedMessage());
                        }
                    }
                });


    }

    /**********For Business Accounts******************/


    /**
     * Retrieves the business profile for the logged in Business
     * Also adds all of their contacts to a list.
     */
    public void getBusinessProfile(DescribeConnectionsViewModel viewModel, String lastName, String code,
            int connectionType) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> connectionIdList = new ArrayList<>();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .whereEqualTo("id", dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects((BusinessProfile.class));

                        BusinessProfile profile = businessProfiles.get(0);

                        viewModel.setLoggedInProfileBusiness(businessProfiles.get(0));

                        for (Map.Entry<String, String> entry : profile.getContacts()
                                .entrySet()) {//get a list of all current connections

                            String userId = entry.getKey();
                            connectionIdList.add(userId);
                        }

                        //get all users
                        getAllUsersForBusiness(viewModel, lastName, code, connectionType, connectionIdList);

                    } else {

                        handleError(task, viewModel);
                    }
                });

    }

    /**
     * uses the received credentials to query our Users Collection for a match
     */
    private void getAllUsersForBusiness(DescribeConnectionsViewModel viewModel, String lastName, String code,
            int connectionType, List<String> idList) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> selectedProfileList = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles) {

                            if (profile.getLastName().toLowerCase().equals(lastName.toLowerCase().trim())
                                    && profile.getCode().equals(code)) {

                                selectedProfileList.add(profile);
                            }
                        }

                        if (selectedProfileList.isEmpty()) {//no user found

                            viewModel.getNavigator().showUserNotFoundAlert();

                        } else {

                            UserProfile selectedProfile = selectedProfileList.get(0);

                            if (idList.contains(selectedProfile.getId())) {//already a contact

                                viewModel.getNavigator().showAlreadyAContactAlert();

                            } else {

                                addNewConnectionBusiness(viewModel, selectedProfile, connectionType);
                            }
                        }

                    } else {

                        handleError(task, viewModel);
                    }

                });

    }


    /**
     * Creates a new Connection for the User Request
     */
    private void addNewConnectionBusiness(DescribeConnectionsViewModel viewModel, UserProfile consentingProfile,
            int connectionType) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newUserRef = db.collection(AppConstants.CONNECTIONS_COLLECTION).document();
        BusinessProfile businessProfile = viewModel.getLoggedInProfileBusiness();

        Long timeStamp = System.currentTimeMillis() / 1000;
        String requestingToken = dataManager.getSharedPrefs().getFirebaseDeviceToken();
        String id = newUserRef.getId();

        String consentingName = consentingProfile.getFirstName() + " " + consentingProfile.getLastName();
        String requestingName = businessProfile.getName();

        UserConnections userConnections = new UserConnections();
        userConnections.setConDeviceToken(consentingProfile.getDeviceToken());
        userConnections.setConnectionType(connectionType);
        userConnections.setConsentingUserID(consentingProfile.getId());
        userConnections.setConsentingUserName(consentingName);
        userConnections.setId(id);
        userConnections.setConfirmed(viewModel.isApprovingConnection());
        userConnections.setOrgBus(false);
        userConnections.setReqDeviceToken(requestingToken);
        userConnections.setRequestingUserName(requestingName);
        userConnections.setRequestingUserID(businessProfile.getId());
        userConnections.setTimestamp(timeStamp);

        newUserRef.set(userConnections)
                .addOnSuccessListener(documentReference -> {

                    viewModel.getNavigator().onRequestSent();

                    if (viewModel.isApprovingConnection()) {

                        updateInitialConnection(viewModel);
                    }

                })
                .addOnFailureListener(e -> {

                    viewModel.getNavigator().handleError(e.getMessage());
                    Logger.e(e.getMessage());
                });
    }


    private void handleError(Task task, DescribeConnectionsViewModel viewModel) {

        if (task.getException() != null) {

            viewModel.getNavigator().handleError(task.getException().getLocalizedMessage());
        }

    }
}
