package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class EditEmailViewModel extends BaseViewModel<EditEmailNavigator> {

    private EditEmailDataModel dataModel;



    /***********Getters and Setters**************/
    public void setDataModel(EditEmailDataModel dataModel) {
        this.dataModel = dataModel;
    }

    /***********OnClick Listeners***************/
    public void onContinueClicked(){

        getNavigator().onContinueClicked();
    }

    public void onSaveAndCloseClicked(){
        getNavigator().onSaveAndCloseClicked();
    }

    /***********DataModel Requests***************/
    public void updateUser(EditEmailViewModel viewModel, UserProfile profile){

        dataModel.updateUser(viewModel,profile);
    }
}
