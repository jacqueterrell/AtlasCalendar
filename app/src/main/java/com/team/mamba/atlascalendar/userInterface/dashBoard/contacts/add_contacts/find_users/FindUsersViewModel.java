package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;
import java.util.ArrayList;
import java.util.List;

public class FindUsersViewModel extends BaseViewModel<FindUsersNavigator> {

    private FindUsersDataModel dataModel;
    List<UserProfile> queriedProfiles = new ArrayList<>();


    /*****Getters and Setters*****/

    public void setDataModel(FindUsersDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setQueriedProfiles(List<UserProfile> queriedProfiles) {
        this.queriedProfiles = queriedProfiles;
    }

    public List<UserProfile> getQueriedProfiles() {
        return queriedProfiles;
    }

    /*****Onclick Listeners********/

    public void onSearchButtonClicked(){

        getNavigator().onSearchButtonClicked();
    }

    /*****DataModel calls*****/
    public void queryUsers(FindUsersViewModel viewModel,String first,String last){

        dataModel.queryUsers(viewModel,first,last);
    }
}
