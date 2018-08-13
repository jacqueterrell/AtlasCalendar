package com.team.mamba.atlas.userInterface.dashBoard.profile.individual;

import android.net.Uri;
import com.team.mamba.atlas.data.model.api.BusinessProfile;
import com.team.mamba.atlas.userInterface.base.BaseViewModel;

public class UserProfileViewModel extends BaseViewModel<UserProfileNavigator> {


    private static BusinessProfile businessProfile;
    private UserProfileDataModel dataModel;
    private String selectedPhone;
    private Uri imageUri = null;
    private String selfiePath = null;
    private String imageUrl = "";

    /*******Getters and Setters********/
    public void setDataModel(UserProfileDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setBusinessProfile(BusinessProfile businessProfile) {
        UserProfileViewModel.businessProfile = businessProfile;
    }

    public BusinessProfile getBusinessProfile() {
        return businessProfile;
    }


    public void onProfileImageClicked(){

        getNavigator().onUserProfileClicked();
    }

    public void getUserDetails(UserProfileViewModel viewModel,String userId){

        dataModel.getUserDetails(viewModel,userId);
    }

    public String getSelectedPhone() {
        return selectedPhone;
    }

    public void setSelectedPhone(String selectedPhone) {
        this.selectedPhone = selectedPhone;
    }

    public void setSavedImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    public void onSettingsClicked(){

        getNavigator().onSettingsClicked();
    }

    public void contactCellPhoneClicked(){

        getNavigator().contactCellPhoneClicked();
    }


    public void contactOnOfficePhoneClicked(){

        getNavigator().contactOnOfficePhoneClicked();
    }


    public void contactOnHomePhoneClicked(){

        getNavigator().contactOnHomePhoneClicked();
    }


    public void contactOnPersonalPhoneClicked(){

        getNavigator().contactOnPersonalPhoneClicked();
    }


    public void contactOnFaxClicked(){

        getNavigator().contactOnFaxClicked();
    }


    public void contactOnPersonalEmailClicked(){

        getNavigator().contactOnPersonalEmailClicked();
    }


    public void contactOnWorkEmailClicked(){

        getNavigator().contactOnWorkEmailClicked();
    }


    public void contactOnHomeAddressClicked(){

        getNavigator().contactOnHomeAddressClicked();
    }


    public void contactOnWorkAddressClicked(){

        getNavigator().contactOnWorkAddressClicked();
    }


    public void contactOnWorkHistoryClicked(){

        getNavigator().contactOnWorkHistoryClicked();
    }


    public void contactOnEducationClicked(){

        getNavigator().contactOnEducationClicked();
    }


    public void editPhoneInfo(){

        getNavigator().editPhoneInfo();
    }


    public void editEmailInfo(){

        getNavigator().editEmailInfo();
    }


    public void editAddressInfo(){

        getNavigator().editAddressInfo();
    }


    public void editWorkHistoryInfo(){

        getNavigator().editWorkHistoryInfo();
    }


    public void editEductionInfo(){

        getNavigator().editEductionInfo();
    }


    public void uploadImage(UserProfileViewModel viewModel,boolean isGallery){

        dataModel.uploadImage(viewModel,isGallery);
    }


}
