package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class LocatorViewModel extends BaseViewModel<LocatorNavigator> {

    private LocatorDataModel dataModel;

    /***************view logic************/

    /***************getters and setters************/

    public void setDataModel(LocatorDataModel dataModel) {
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
