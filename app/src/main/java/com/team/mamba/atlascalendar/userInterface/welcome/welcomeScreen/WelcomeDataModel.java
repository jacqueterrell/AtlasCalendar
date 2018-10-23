package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen;

import com.team.mamba.atlascalendar.data.AppDataManager;
import javax.inject.Inject;

public class WelcomeDataModel {

    private AppDataManager dataManager;

    @Inject
    public WelcomeDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }



}
