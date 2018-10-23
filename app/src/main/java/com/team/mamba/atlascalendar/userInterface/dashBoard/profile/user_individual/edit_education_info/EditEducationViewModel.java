package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.Education;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class EditEducationViewModel extends BaseViewModel<EditEducationNavigator> {

    private EditEducationDataModel dataModel;
    private boolean isEditing = false;
    private Education editingEducation;
    private int editingIndex;

    public void setDataModel(EditEducationDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public Education getEditingEducation() {
        return editingEducation;
    }

    public void setEditingEducation(Education editingEducation) {
        this.editingEducation = editingEducation;
    }

    public int getEditingIndex() {
        return editingIndex;
    }

    public void setEditingIndex(int editingIndex) {
        this.editingIndex = editingIndex;
    }




    public void onSaveAndCloseClicked(){

        getNavigator().onSaveAndCloseClicked();
    }

    public void onContinueClicked(){

        getNavigator().onContinueClicked();
    }

    public void onAddNewEducation(){

        getNavigator().onAddNewEducation();
    }

    public void updateUser(EditEducationViewModel viewModel,UserProfile profile){

        dataModel.updateUser(viewModel,profile);
    }
}
