package com.team.mamba.atlas.userInterface.welcome.welcomeScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlas.data.AppDataManager;
import com.team.mamba.atlas.utils.AppConstants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class WelcomeDataModel {

    private AppDataManager dataManager;

    @Inject
    public WelcomeDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    public void fireBaseVerifyPhoneNumber(WelcomeViewModel viewModel, String phoneNumber) {

        int timeOut = 60;

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                timeOut,
                TimeUnit.SECONDS,
                viewModel.getNavigator().getParentActivity(),
                viewModel.getNavigator().getPhoneCallBacks());
    }

    /**
     * Use the received Firebase credential to attempt a sign in
     */
    public void signInWithPhoneAuthCredential(WelcomeViewModel viewModel) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(viewModel.getPhoneAuthCredential())
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (isCurrentUser(viewModel)) {

                            viewModel.getNavigator().openDashBoard();

                        } else {

                            addUserToFirebaseDatabase(viewModel);
                            viewModel.getNavigator().handleError("Adding New User");

                        }
                        Logger.d("signInWithCredential:success");

                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            viewModel.getNavigator().handleError("Invalid verification code");
                        }

                        Logger.e("signInWithCredential:failure", task.getException());
                    }
                });

    }

    public void addUserToFirebaseDatabase(WelcomeViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //convert dob to time stamp
        Long timeStamp = System.currentTimeMillis() / 1000;
        String token = FirebaseInstanceId.getInstance().getToken();
        DocumentReference newUserRef = db.collection(AppConstants.USERS_COLLECTION).document();
        String myId = newUserRef.getId();

        Map<String, Object> user = new HashMap<>();
        user.put("firstName", viewModel.getFirstName());
        user.put("lastName", viewModel.getLastName());
        user.put("dob", viewModel.getDateOfBirth());
        user.put("phone", viewModel.getPhoneNumber());
        user.put("timestamp", timeStamp);
        user.put("deviceToken", token);
        user.put("id", myId);
        user.put("badgeCount", 0);
        user.put("code", 1);
        user.put("score", 0);

        newUserRef.set(user)
                .addOnSuccessListener(documentReference -> {

                    dataManager.getSharedPrefs().setUserId(myId);
                    viewModel.getNavigator().openDashBoard();

                })
                .addOnFailureListener(e -> {
                    viewModel.getNavigator().handleError("Upload failed");
                    //
                });

    }

    /**
     * Checks to see if the user's details have already been saved
     *
     * @return
     */
    private boolean isCurrentUser(WelcomeViewModel viewModel) {

        String phone = viewModel.getPhoneNumber();
        String firstName = viewModel.getFirstName();
        String lastName = viewModel.getLastName();

        String cachedPhone = dataManager.getSharedPrefs().getPhoneNumber();
        String cachedFirstName = dataManager.getSharedPrefs().getFirstName();
        String cachedLastName = dataManager.getSharedPrefs().getLastName();


        if (phone.equals(cachedPhone)
                && firstName.equals(cachedFirstName)
                && lastName.equals(cachedLastName)) {

            return true;

        } else {

            return false;
        }
    }


    public void firebaseAuthenticateByEmail(WelcomeViewModel viewModel,String email,String password){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(viewModel.getNavigator().getParentActivity(), task -> {

                    if (task.isSuccessful()){

                        viewModel.getNavigator().handleError("success");
                        checkAllBusinesses(viewModel,email);

                    } else {

                        viewModel.getNavigator().showBusinessNotFoundAlert();
                    }
                });


        // checkAllBusinesses(viewModel,email);

    }

    public void checkAllBusinesses(WelcomeViewModel viewModel, String email) {

        //query database to look for the email
        //if match found login as the business
        //if multiple matched recylerview to select the business to represent

        Map<String,String> namesMap = new LinkedHashMap<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Logger.i("email:" + document.getData().get("email"));
                            String userId = document.getData().get("id").toString();
                            String name = document.getData().get("name").toString();

                            namesMap.put(userId,name);


                        }

                        viewModel.setBusinessNamesMap(namesMap);

                        if (namesMap.isEmpty()){

                            //todo can this situation exist
                            viewModel.getNavigator().showBusinessNotFoundAlert();

                        } else if (namesMap.size() == 1){

                            viewModel.getNavigator().openDashBoard();

                        } else {

                            viewModel.getNavigator().showMultipleBusinessLogin();
                        }

                    } else {

                        viewModel.getNavigator().handleError("Error getting documents");
                        task.getException().printStackTrace();
                    }
                });

    }



}
