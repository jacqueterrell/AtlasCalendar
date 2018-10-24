package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AdminAccountsDataModel {


    private AppDataManager dataManager;


    @Inject
    public AdminAccountsDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void signInAnonymously(AdminAccountsViewModel viewModel, FirebaseAuth firebaseAuth){

        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        getAllAdminProfiles(viewModel);

                    } else {

                        viewModel.getNavigator().handleError(task.getException().getLocalizedMessage());
                    }

                });
    }


    public void getAllAdminProfiles(AdminAccountsViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> adminProfiles = new ArrayList<>();
        List<UserProfile> testAccounts = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles){

                            String first = profile.getFirstName().toLowerCase();
                            String last = profile.getLastName().toLowerCase();

                            if (first.equals("matt") && last.equals("maher")){

                                adminProfiles.add(profile);

                            } else if (last.equals("testacct")){

                                testAccounts.add(profile);
                            }
                        }

                        adminProfiles.addAll(testAccounts);

                        viewModel.setAdminProfileList(adminProfiles);
                        viewModel.getNavigator().onAdminProfilesReturned();

                    } else {

                        Logger.e("Failed to get admin profiles " + task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getLocalizedMessage());
                    }

                });
    }
}
