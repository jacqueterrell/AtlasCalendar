package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class FindUsersRecyclerViewModel extends BaseViewModel<FindUsersRecyclerNavigator> {

    private FindUsersRecyclerDataModel dataModel;
    private UserProfile requestingUserProfile;
    private BusinessProfile requestingBusinessProfile;

    public void setDataModel(FindUsersRecyclerDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setRequestingUserProfile(UserProfile requestingUserProfile) {
        this.requestingUserProfile = requestingUserProfile;
    }

    public UserProfile getRequestingUserProfile() {
        return requestingUserProfile;
    }

    public void setRequestingBusinessProfile(BusinessProfile requestingBusinessProfile) {
        this.requestingBusinessProfile = requestingBusinessProfile;
    }

    public BusinessProfile getRequestingBusinessProfile() {
        return requestingBusinessProfile;
    }

    public void initiateNewUserRequest(FindUsersRecyclerViewModel viewModel, UserProfile profile) {

        dataModel.initiateNewUserRequest(viewModel, profile);
    }
}
