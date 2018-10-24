package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts;

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

public class SuggestedContactsDataModel {

    private AppDataManager dataManager;

    @Inject
    public SuggestedContactsDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }

    public void requestSuggestedContacts(SuggestedContactsViewModel viewModel){

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            getLoggedInIndividualDetails(viewModel);

        } else {


        }
    }

        /**
         * Retrieves the logged in User's account and gets a list of all of their
         * contacts
         *
         * @param viewModel
         */
    private void getLoggedInIndividualDetails(SuggestedContactsViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> connectionIdList = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        viewModel.setAllUsers(userProfiles);

                        for (UserProfile profile : userProfiles){

                            if (profile.getId().equals(dataManager.getSharedPrefs().getUserId())){

                                viewModel.setRequestingUserProfile(profile);

                                for (Map.Entry<String, String> entry : profile.getConnections()
                                        .entrySet()) {//get a list of all current connections

                                    String userId = entry.getKey();

                                    if (!connectionIdList.contains(userId)){

                                        connectionIdList.add(userId);
                                    }
                                }
                            }
                        }

                        viewModel.setConnectionIdList(connectionIdList);
                        getBusinessContactsDetails(viewModel);

                    } else {

                        Logger.e(task.getException().getMessage());
                    }
                });

    }


    /**
     * Gets a list of all business the user is connected to, and retrieves
     * all contacts relating to those businesses
     *
     * @param viewModel
     */
    private void getBusinessContactsDetails(SuggestedContactsViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<BusinessProfile> connectedBusinessProfiles = new ArrayList<>();
        List<String> businessContactsIdList = new ArrayList<>();
         List<UserProfile> suggestedProfileList = new ArrayList<>();


        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<BusinessProfile> profileList = task.getResult().toObjects(BusinessProfile.class);

                        for (BusinessProfile profile : profileList){

                            if (viewModel.getConnectionIdList().contains(profile.getId())){

                                connectedBusinessProfiles.add(profile);

                                for (Map.Entry<String,String> entry : profile.getContacts().entrySet()){

                                    String userId = entry.getKey();
                                    businessContactsIdList.add(userId);
                                }
                            }
                        }

                        //loops through all profiles to find those relating to our business contact ids
                        //and not already in the user's contact list and not the user
                        for (UserProfile userProfile : viewModel.getAllUsers()){

                            for (String businessContactId : businessContactsIdList){

                                if (businessContactId.equals(userProfile.getId())
                                        && !viewModel.getConnectionIdList().contains(userProfile.getId())
                                        && !userProfile.getId().equals(dataManager.getSharedPrefs().getUserId())){

                                    suggestedProfileList.add(userProfile);
                                }
                            }
                        }

                        viewModel.setSuggestedProfileList(suggestedProfileList);
                        viewModel.getNavigator().onSuggestedContactsFound();

                    } else {

                        Logger.e(task.getException().getMessage());
                    }
                });
    }





    public void initiateNewUserRequest(SuggestedContactsViewModel viewModel,UserProfile profile){

        if (dataManager.getSharedPrefs().isBusinessAccount()){

          //  requestNewUserBusiness(viewModel,profile);

        } else {

            addNewConnectionIndividual(viewModel,profile);
        }

    }



    /**
     * Creates a new connection for the requesting profile and the
     * consenting profile
     *
     * @param consentingProfile the logged in User's profile
     */
    private void addNewConnectionIndividual(SuggestedContactsViewModel viewModel,UserProfile consentingProfile){

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

}
