package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual;

import android.net.Uri;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

public class UserProfileViewModel extends BaseViewModel<UserProfileNavigator> {


    private static BusinessProfile businessProfile;
    private UserProfileDataModel dataModel;
    private Uri imageUri = null;
    private String selfiePath = null;
    private static UserProfile userProfile = new UserProfile();


    /*******Getters and Setters********/
    public void setDataModel(UserProfileDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void onProfileImageClicked() {

        getNavigator().onUserProfileClicked();
    }

    public void setUserProfile(UserProfile userProfile) {
        UserProfileViewModel.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getSelfiePath() {
        return selfiePath;
    }

    public void setSelfiePath(String s) {
        this.selfiePath = s;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri galleryImageUri) {
        imageUri = galleryImageUri;
    }

    /*******Onclick Listeners********/

    public void editEmployer(){

        getNavigator().editEmployer();;
    }

    public void onSettingsClicked() {

        getNavigator().onSettingsClicked();
    }

    public void editPhoneInfo() {

        getNavigator().editPhoneInfo();
    }


    public void editEmailInfo() {

        getNavigator().editEmailInfo();
    }


    public void editAddressInfo() {

        getNavigator().editAddressInfo();
    }


    public void editWorkHistoryInfo() {

        getNavigator().editWorkHistoryInfo();
    }


    public void editEductionInfo() {

        getNavigator().editEductionInfo();
    }

    public boolean isStringValid(String phone) {

        try {
            String adjustedPhone = phone.replaceAll(RegEx.REMOVE_NON_DIGITS, "");
        } catch (Exception e) {

            return false;
        }

        return true;
    }


    /****************DataModel Requests*****************/

    public void uploadImage(UserProfileViewModel viewModel, UserProfile profile) {

        dataModel.uploadImage(viewModel, profile);
    }

    public void updateUserDetails(UserProfileViewModel viewModel) {

        dataModel.updateUserDetails(viewModel);
    }

    }
