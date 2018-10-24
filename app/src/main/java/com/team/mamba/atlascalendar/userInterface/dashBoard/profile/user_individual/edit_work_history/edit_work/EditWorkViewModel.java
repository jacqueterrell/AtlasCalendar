package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.WorkHistory;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class EditWorkViewModel extends BaseViewModel<EditWorkNavigator> {

    private EditWorkDataModel dataModel;
    private boolean isEditing = false;
    private WorkHistory editingWorkHistory;
    private int editingIndex;

    public void setDataModel(EditWorkDataModel dataModel) {
        this.dataModel = dataModel;
    }


    public EditWorkDataModel getDataModel() {
        return dataModel;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public WorkHistory getEditingWorkHistory() {
        return editingWorkHistory;
    }

    public void setEditingWorkHistory(WorkHistory editingWorkHistory) {
        this.editingWorkHistory = editingWorkHistory;
    }

    public int getEditingIndex() {
        return editingIndex;
    }

    public void setEditingIndex(int editingIndex) {
        this.editingIndex = editingIndex;
    }

    /*********OnClick Listeners*************/

    public void onSaveAndCloseClicked(){

        getNavigator().onSaveAndCloseClicked();
    }

    public void onFinishedClicked(){

        getNavigator().onFinishedClicked();
    }

    public void onAddNewWork(){

        getNavigator().onAddNewWork();
    }

    public void updateUser(EditWorkViewModel viewModel,UserProfile profile){

        dataModel.updateUser(viewModel,profile);
    }
}
