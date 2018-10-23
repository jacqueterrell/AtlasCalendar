package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.selected_crm;

import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class SelectedCrmViewModel extends BaseViewModel<SelectedCrmNavigator> {


    private SelectedCrmDataModel dataModel;


    public void setDataModel(SelectedCrmDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void onEditClicked(){

        getNavigator().onEditClicked();

    }

    public void onDeleteClicked (){

        getNavigator().onDeleteClicked();
    }

    public void deleteNote(SelectedCrmViewModel viewModel, CrmNotes notes){

        dataModel.deleteCrmNote(viewModel,notes);
    }
}
