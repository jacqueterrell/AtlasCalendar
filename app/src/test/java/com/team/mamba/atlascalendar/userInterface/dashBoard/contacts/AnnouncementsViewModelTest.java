package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.Announcements;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;



@SuppressWarnings("ResultOfMethodCallIgnored")
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class AnnouncementsViewModelTest {


    @Mock
    AnnouncementsNavigator mockNavigator;

    @Mock
    AnnouncementsDataModel mockDataModel;


    private AnnouncementsViewModel viewModel;
    private AnnouncementsViewModel spyViewModel;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new AnnouncementsViewModel();
        spyViewModel = spy(new AnnouncementsViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }



    @Test
    public void getAnnouncementsList() {

        //set up
        List<Announcements> announcementsList = new ArrayList<>();

        //action
        viewModel.setAnnouncementsList(announcementsList);

        //assert
        assertEquals(announcementsList,viewModel.getAnnouncementsList());
    }

    @Test
    public void requestAnnouncements() {

        //action
        viewModel.requestAnnouncements(viewModel);

        //assert
        verify(mockDataModel).requestAnnouncements(viewModel);
    }
}