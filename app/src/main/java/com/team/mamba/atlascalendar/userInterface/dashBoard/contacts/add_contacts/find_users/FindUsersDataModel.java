package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class FindUsersDataModel {

    private AppDataManager dataManager;

    @Inject
    public FindUsersDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    /**
     * Queries all users for the first and last name provided by the user
     *
     * @param first the contact's first name
     * @param last the contact's last name
     */
    public void queryUsers(FindUsersViewModel viewModel,String first,String last){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> queriedProfiles = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles){

                            if (profile.getFirstName().toLowerCase().trim().equals(first.toLowerCase().trim())
                                    && profile.getLastName().toLowerCase().trim().equals(last.toLowerCase().trim())) {

                                queriedProfiles.add(profile);
                            }
                        }

                        if (queriedProfiles.isEmpty()){

                            //show could not find
                            viewModel.getNavigator().showUsersNotFoundAlert();

                        } else {

                            //set recyclerview
                            viewModel.setQueriedProfiles(queriedProfiles);
                            viewModel.getNavigator().onUsersFound();
                        }

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                    }

                });

    }
}
