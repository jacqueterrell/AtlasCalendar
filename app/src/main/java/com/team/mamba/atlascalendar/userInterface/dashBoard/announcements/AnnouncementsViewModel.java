package com.team.mamba.atlascalendar.userInterface.dashBoard.announcements;

import com.team.mamba.atlascalendar.data.model.api.fireStore.Announcements;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementsViewModel extends BaseViewModel<AnnouncementsNavigator> {

    private AnnouncementsDataModel dataModel;
    private static List<Announcements> announcementsList = new ArrayList<>();


    /**************Getters and Setters*******************/

    public void setDataModel(AnnouncementsDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setAnnouncementsList(List<Announcements> announcementsList) {
        AnnouncementsViewModel.announcementsList = announcementsList;
    }

    public List<Announcements> getAnnouncementsList() {
        return announcementsList;
    }

    public void requestAnnouncements(AnnouncementsViewModel viewModel){

        dataModel.requestAnnouncements(viewModel);
    }

    /*****************OnClick Listeners*****************/

    public void onContactsClicked(){

        getNavigator().onContactsClicked();
    }

    public void onCrmClicked(){

        getNavigator().onCrmClicked();
    }

    public void onInfoClicked(){

        getNavigator().onInfoClicked();
    }

    public void onAddAnnouncementClicked(){

        getNavigator().onAddAnnouncementClicked();
    }
}
