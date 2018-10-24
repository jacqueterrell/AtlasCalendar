package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view;

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

public class FindUsersRecyclerDataModel {

    AppDataManager dataManager;

    @Inject
    public FindUsersRecyclerDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }



    public void initiateNewUserRequest(FindUsersRecyclerViewModel viewModel,UserProfile profile){

        if (dataManager.getSharedPrefs().isBusinessAccount()){

            requestNewUserBusiness(viewModel,profile);

        } else {

            requestNewUserIndividual(viewModel,profile);
        }

    }

    /**
     *
     * Looks through all Users and finds the profile for the logged in user, and gets a
     * list of all of their current connections.
     *
     * @param selectedProfile the selected UserProfile
     */
    private void requestNewUserIndividual(FindUsersRecyclerViewModel viewModel,UserProfile selectedProfile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> connectionIdList = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .whereEqualTo("id",dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserProfile> userProfileList = task.getResult().toObjects(UserProfile.class);
                        UserProfile requestingProfile = userProfileList.get(0);

                        viewModel.setRequestingUserProfile(requestingProfile);

                        for (Map.Entry<String,String> entry : requestingProfile.getConnections().entrySet()){//get a list of all current connections

                            String userId = entry.getKey();
                            connectionIdList.add(userId);
                        }

                        if (connectionIdList.contains(selectedProfile.getId())){

                            //show already a contact
                            viewModel.getNavigator().showAlreadyAContactAlert();

                        } else {

                            addNewConnectionIndividual(viewModel,selectedProfile);
                        }

                    } else {

                        Logger.e(task.getException().getMessage());
                    }
                });

    }

    /**
     * Creates a new connection for the requesting profile and the
     * consenting profile
     *
     * @param consentingProfile the logged in User's profile
     */
    private void addNewConnectionIndividual(FindUsersRecyclerViewModel viewModel,UserProfile consentingProfile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newUserRef = db.collection(AppConstants.CONNECTIONS_COLLECTION).document();
        UserProfile requestingProfile = viewModel.getRequestingUserProfile();

        Long timeStamp = System.currentTimeMillis() / 1000;
        String requestingToken = dataManager.getSharedPrefs().getFirebaseDeviceToken();
        String id = newUserRef.getId();

        String consentingName = consentingProfile.getFirstName() + " " + consentingProfile.getLastName();
        String requestingName = requestingProfile.getFirstName() + " " + requestingProfile.getLastName();

        UserConnections userConnections = new UserConnections();
        userConnections.setConDeviceToken(consentingProfile.getDeviceToken());
        userConnections.setConnectionType(9999);
        userConnections.setConsentingUserID(consentingProfile.getId());
        userConnections.setConsentingUserName(consentingName);
        userConnections.setId(id);
        userConnections.setConfirmed(false);
        userConnections.setOrgBus(false);
        userConnections.setReqDeviceToken(requestingToken);
        userConnections.setRequestingUserName(requestingName);
        userConnections.setRequestingUserID(requestingProfile.getId());
        userConnections.setTimestamp(timeStamp);

        newUserRef.set(userConnections)
                .addOnSuccessListener(documentReference -> {

                    String first = consentingProfile.getFirstName();
                    String last = consentingProfile.getLastName();

                    viewModel.getNavigator().showInvitationSentAlert(first,last);

                })
                .addOnFailureListener(e -> {

                    viewModel.getNavigator().handleError(e.getMessage());
                    Logger.e(e.getMessage());
                });

    }

    /**********************User Logged in as a Business Account***************/

    /**
     *
     * Looks through all Users and finds the profile for the logged in user, and gets a
     * list of all of their current connections.
     *
     * @param selectedProfile the selected UserProfile
     */
    private void requestNewUserBusiness(FindUsersRecyclerViewModel viewModel,UserProfile selectedProfile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> connectionIdList = new ArrayList<>();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .whereEqualTo("id",dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects((BusinessProfile.class));

                        BusinessProfile profile = businessProfiles.get(0);

                        viewModel.setRequestingBusinessProfile(profile);

                        for (Map.Entry<String,String> entry : profile.getContacts().entrySet()){//get a list of all current connections

                            String userId = entry.getKey();
                            connectionIdList.add(userId);
                        }

                        if (connectionIdList.contains(selectedProfile.getId())){

                            //show already a contact
                            viewModel.getNavigator().showAlreadyAContactAlert();

                        } else {

                            addNewConnectionBusiness(viewModel,selectedProfile);
                        }

                    } else {

                        Logger.e(task.getException().getMessage());
                    }
                });

    }



    /**
     * Creates a new connection for the requesting profile and the
     * consenting profile
     *
     * @param consentingProfile the logged in User's profile
     */
    private void addNewConnectionBusiness(FindUsersRecyclerViewModel viewModel,UserProfile consentingProfile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newUserRef = db.collection(AppConstants.CONNECTIONS_COLLECTION).document();
        BusinessProfile businessProfile = viewModel.getRequestingBusinessProfile();

        Long timeStamp = System.currentTimeMillis() / 1000;
        String requestingToken = dataManager.getSharedPrefs().getFirebaseDeviceToken();
        String id = newUserRef.getId();

        String consentingName = consentingProfile.getFirstName() + " " + consentingProfile.getLastName();
        String requestingName = businessProfile.getName();

        UserConnections userConnections = new UserConnections();
        userConnections.setConDeviceToken(consentingProfile.getDeviceToken());
        userConnections.setConnectionType(9999);
        userConnections.setConsentingUserID(consentingProfile.getId());
        userConnections.setConsentingUserName(consentingName);
        userConnections.setId(id);
        userConnections.setConfirmed(false);
        userConnections.setOrgBus(true);
        userConnections.setReqDeviceToken(requestingToken);
        userConnections.setRequestingUserName(requestingName);
        userConnections.setRequestingUserID(businessProfile.getId());
        userConnections.setTimestamp(timeStamp);

        newUserRef.set(userConnections)
                .addOnSuccessListener(documentReference -> {

                    String first = consentingProfile.getFirstName();
                    String last = consentingProfile.getLastName();

                    viewModel.getNavigator().showInvitationSentAlert(first,last);

                })
                .addOnFailureListener(e -> {

                    viewModel.getNavigator().handleError(e.getMessage());
                    Logger.e(e.getMessage());
                });

    }
}
