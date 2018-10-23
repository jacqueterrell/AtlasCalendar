package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info;

import java.util.List;

public interface AddEducationNavigator {


    void onSchoolRowClicked(String university);

    void onDegreeRowClicked(String degree);

    void onFieldOfStudyRowClicked(String fieldOfStudy);

    List<String> getPermUniversityList();

    List<String> getPermDegreeList();

    List<String> getPermFieldOfStudyList();

}
