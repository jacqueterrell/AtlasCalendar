package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoViewModel extends BaseViewModel<InfoNavigator> {

    private InfoDataModel dataModel;

    /***************view logic************/

    /***************getters and setters************/

    public void setDataModel(InfoDataModel dataModel) {
        this.dataModel = dataModel;
    }

    /***************onclick listeners************/

    public void onContactsClicked(){

        getNavigator().onContactsClicked();
    }

    public void onCrmClicked(){

        getNavigator().onCrmClicked();
    }

    public void onNotificationsClicked(){

        getNavigator().onNotificationsClicked();
    }


    /********* Datamodel Calls********/


}
