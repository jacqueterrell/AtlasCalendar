package com.team.mamba.atlas.userInterface.dashBoard._container_activity.describe_connections;

import com.team.mamba.atlas.data.AppDataManager;

import javax.inject.Inject;

public class DescribeConnectionsDataModel {

    private AppDataManager dataManager;

    @Inject
    public DescribeConnectionsDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void addUser(DescribeConnectionsViewModel viewModel,String lastName, String userCode){



    }
}
