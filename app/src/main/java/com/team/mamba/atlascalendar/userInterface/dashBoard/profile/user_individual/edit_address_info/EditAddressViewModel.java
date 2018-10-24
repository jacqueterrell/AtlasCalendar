package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_address_info;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class EditAddressViewModel extends BaseViewModel<EditAddressNavigator> {

    private EditAddressDataModel dataModel;


    public void setDataModel(EditAddressDataModel dataModel) {
        this.dataModel = dataModel;
    }


    public void updateUser(EditAddressViewModel viewModel,UserProfile profile){

        dataModel.updateUser(viewModel,profile);
    }

    public void onSaveAndCloseClicked(){

        getNavigator().onSaveAndCloseClicked();
    }

    public void onEducationClicked(){

        getNavigator().onEducationClicked();
    }
}
