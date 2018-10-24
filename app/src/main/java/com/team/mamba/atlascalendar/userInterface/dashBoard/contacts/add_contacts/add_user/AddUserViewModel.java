package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class AddUserViewModel extends BaseViewModel<AddUserNavigator> {



    /*****Our App Logic*****/

    public boolean isLastNameValid(String lastName){

        if (lastName.isEmpty()){

            return false;

        } else {

            return true;
        }
    }

    public boolean isUserCodeValid(String userCode){

        if (userCode.isEmpty()){

            return false;

        } else {

            return true;
        }
    }

    /*****On Click Listeners*****/
    public void onNextButtonClicked(){

        getNavigator().onNextButtonClicked();
    }
}
