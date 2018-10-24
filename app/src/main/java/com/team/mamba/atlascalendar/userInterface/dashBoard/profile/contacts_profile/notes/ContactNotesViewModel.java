package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes;

import com.team.mamba.atlascalendar.data.model.api.fireStore.PersonalNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class ContactNotesViewModel extends BaseViewModel<ContactNotesNavigator> {

    private ContactNotesDataModel dataModel;
    PersonalNotes personalNotes;
    private int yourConnectionType = 3;
    private long timestamp;

    /*********************Getters and Setters********************/

    public void setDataModel(ContactNotesDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public PersonalNotes getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(PersonalNotes personalNotes) {
        this.personalNotes = personalNotes;
    }

    public void setYourConnectionType(int yourConnectionType) {
        this.yourConnectionType = yourConnectionType;
    }

    public int getYourConnectionType() {
        return yourConnectionType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /*******************Onclick Listeners***********************/


    public void onEditDetailsClicked(){

        getNavigator().onEditDetailsClicked();
    }

    public void onConnectionInfoClicked(){

        getNavigator().onConnectionInfoClicked();
    }

    public void onNotesInfoClicked(){

        getNavigator().onNotesInfoClicked();
    }


    /*********************DataModel Requests**********************/

    public void getConnectionType(ContactNotesViewModel viewModel,UserProfile profile){

        dataModel.getConnectionType(viewModel,profile);
    }
}
