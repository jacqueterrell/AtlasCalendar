package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import com.team.mamba.atlascalendar.data.AppDataManager;

import javax.inject.Inject;

public class LocatorDataModel {


    private AppDataManager dataManager;

    @Inject
    public LocatorDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


}
