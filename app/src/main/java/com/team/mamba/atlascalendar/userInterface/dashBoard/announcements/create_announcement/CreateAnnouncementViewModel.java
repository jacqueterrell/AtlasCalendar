package com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.create_announcement;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class CreateAnnouncementViewModel extends BaseViewModel<CreateAnnouncementNavigator> {

    private CreateAnnouncementDataModel dataModel;
    private boolean event = true;
    private String announcementMessage;

    /*************Getters and Setters***************/

    public void setDataModel(
            CreateAnnouncementDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public boolean isEvent() {
        return event;
    }

    public void setAnnouncementMessage(String announcementMessage) {
        this.announcementMessage = announcementMessage;
    }

    public String getAnnouncementMessage() {
        return announcementMessage;
    }

    /***********Onclick Listeners**************/

    public void onSendButtonClicked() {

        getNavigator().onSendButtonClicked();
    }

    /*********DataModel Requests*************/

    public void sendAnnouncement(CreateAnnouncementViewModel viewModel) {

        dataModel.sendAnnouncement(viewModel);
    }
}
