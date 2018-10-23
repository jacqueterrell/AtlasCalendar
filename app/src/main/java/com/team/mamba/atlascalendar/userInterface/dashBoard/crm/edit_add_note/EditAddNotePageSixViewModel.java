package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class EditAddNotePageSixViewModel extends BaseViewModel<EditAddNotePageSixNavigator> {


    private EditAddNotePageSixDataModel dataModel;


    public void setDataModel(EditAddNotePageSixDataModel dataModel) {
        this.dataModel = dataModel;
    }


    public void onFinishButtonClicked(){

        getNavigator().onFinishButtonClicked();
    }

    public void submitNoteRequest(EditAddNotePageSixViewModel viewModel, CrmNotes crmNotes){

        dataModel.setNoteRequest(viewModel,crmNotes);
    }
}
