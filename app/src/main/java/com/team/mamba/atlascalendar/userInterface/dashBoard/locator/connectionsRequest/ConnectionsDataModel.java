package com.team.mamba.atlascalendar.userInterface.dashBoard.locator.connectionsRequest;

import com.team.mamba.atlascalendar.data.AppDataManager;

import javax.inject.Inject;

public class ConnectionsDataModel {


    private AppDataManager dataManager;


    @Inject
    public ConnectionsDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }
}
