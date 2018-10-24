package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;

public class BusinessLoginDataModel {


    private AppDataManager dataManager;


    @Inject
    public BusinessLoginDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void firebaseAuthenticateByEmail(BusinessLoginViewModel viewModel, String email, String password) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(viewModel.getNavigator().getParentActivity(), task -> {

                    if (task.isSuccessful()) {

                        checkAllBusinesses(viewModel, email);

                    } else {

                        viewModel.getNavigator().showBusinessNotFoundAlert();
                    }
                });

    }


    /**
     * Query the DB to look for the email, if a match is found
     * login in as the business. If multiple are found a recyclerview
     * gives the user options on which business to represent
     *
     * @param email the business email address
     */
    public void checkAllBusinesses(BusinessLoginViewModel viewModel, String email) {

        //query database to look for the email
        //if match found login as the business
        //if multiple matched recylerview to select the business to represent

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects(BusinessProfile.class);

                        viewModel.setBusinessProfileList(businessProfiles);

                        if (viewModel.getBusinessProfileList().isEmpty()) {

                            //todo can this situation exist
                            viewModel.getNavigator().showBusinessNotFoundAlert();

                        } else if (viewModel.getBusinessProfileList().size() == 1) {

                            dataManager.getSharedPrefs().setUserId(businessProfiles.get(0).getId());
                            viewModel.getNavigator().openDashBoard();

                        } else {

                            viewModel.getNavigator().showMultipleBusinessLogin();
                        }

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                    }
                });

    }
}
