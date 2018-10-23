package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info;

import com.team.mamba.atlascalendar.data.model.local.Education;

public interface EditEducationNavigator {


        void onContinueClicked();
        void onSaveAndCloseClicked();
        void onEditRowSaved(Education education);
        void onProfileUpdated();
        void onSaveNewEducation(Education education);
        void onAddNewEducation();
        void onRowClicked(Education education, int pos);
        boolean isEditing();
        Education getEditingEducation();
}
