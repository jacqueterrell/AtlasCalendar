package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class LocatorDataModel {


    private AppDataManager dataManager;

    @Inject
    public LocatorDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    public void requestContactsInfo(LocatorViewModel viewModel){

        requestUserProfiles(viewModel);
    }

    private void requestUserProfiles(LocatorViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> adjustedProfileList = new ArrayList<>();
        String savedUserId = dataManager.getSharedPrefs().getUserId();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserProfile> userProfileList = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfileList){

                            if (profile.getId() != null) {

                                if (profile.getId().equals(savedUserId)) {

                                    viewModel.setSelectedUserProfile(profile);
                                }

                                adjustedProfileList.add(profile);
                            }
                        }

                        if (!dataManager.getSharedPrefs().isBusinessAccount()){

                            requestBusinessProfiles(viewModel,adjustedProfileList);
                        }

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }
                });
    }

    private void requestBusinessProfiles(LocatorViewModel viewModel,List<UserProfile> userProfileList){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String employer = viewModel.getSelectedUserProfile().getCurrentEmployer();
        List<UserProfile> selectedProfilesList = new ArrayList<>();
        BusinessProfile employerProfile = new BusinessProfile();
        List<String> connectionsIdList = new ArrayList<>();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects(BusinessProfile.class);

                        for (BusinessProfile profile : businessProfiles){

                            if (profile.getName().equalsIgnoreCase(employer)){//retrieve the employers contacts

                                for (Map.Entry<String, String> entry : profile.getContacts().entrySet()){

                                    String contactId = entry.getKey();
                                    connectionsIdList.add(contactId);
                                }
                            }
                        }

                        for (UserProfile profile : userProfileList){//assigns the necessary profile for the contact id

                            for (String contactId : connectionsIdList){

                                if (profile.getId().equals(contactId)) {

                                    selectedProfilesList.add(profile);
                                }
                            }
                        }

                        viewModel.setEmployeeProfilesList(selectedProfilesList);
                        viewModel.getNavigator().onEmployeeContactsReturned();

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }
                });
    }


    private void onPause(LocatorViewModel viewModel){

    }
}
