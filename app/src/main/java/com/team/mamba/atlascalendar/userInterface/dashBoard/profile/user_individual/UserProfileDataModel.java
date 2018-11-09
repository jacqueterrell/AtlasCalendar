package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.List;

import javax.inject.Inject;

public class UserProfileDataModel {


    private AppDataManager dataManager;

    @Inject
    public UserProfileDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }



    public void getUserDetails(UserProfileViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dataManager.getSharedPrefs().getUserId();

        db.collection(AppConstants.USERS_COLLECTION)
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        if (!userProfiles.isEmpty()) {

                            viewModel.setUserProfile(userProfiles.get(0));
                            viewModel.getNavigator().onProfileUpdated();
                        }

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getLocalizedMessage());
                    }

                });

    }


    /**
     * Uploads our user's image to our Firebase storage
     *
     * @param viewModel
     * @param profile
     */
    public void uploadImage(UserProfileViewModel viewModel,UserProfile profile){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference galleryRef = storageRef.child("images/"+ dataManager.getSharedPrefs().getUserId());

        galleryRef.putFile(viewModel.getImageUri())
                .addOnSuccessListener(taskSnapshot -> galleryRef.getDownloadUrl().addOnCompleteListener(task -> {

                    Logger.i("Successful " + task.getResult());
                    profile.setImageUrl(task.getResult().toString());
                    updateUser(profile);
                }))
                .addOnFailureListener(e -> Logger.e("failed"));

    }


    /**
     * Updates the user's image to the new link
     *
     * @param profile
     */
    public void updateUser(UserProfile profile) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.USERS_COLLECTION)
                .document(profile.getId())
                .set(profile)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Logger.i("Image uploaded successfully");

                    } else {

                        Logger.e("Could not update address: " + task.getException());
                    }

                });
    }

}
