package com.team.mamba.atlascalendar.userInterface.dashBoard.crm;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmViewModel;

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
@RunWith(RobolectricTestRunner.class)
public class CrmViewModelTest {


    @Mock
    private CrmNavigator mockNavigator;

    @Mock
    private CrmDataModel mockDataModel;

    private CrmViewModel viewModel;
    private CrmViewModel spyViewModel;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new CrmViewModel();
        spyViewModel = spy(new CrmViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }


    @Test
    public void getSavedUserId() {

        //set up
        String userId = "123456";

        //action
        viewModel.setSavedUserId(userId);

        //assert
        assertEquals(userId,viewModel.getSavedUserId());
    }


    @Test
    public void getCrmNotesList() {

        //set up
        List<CrmNotes> crmNotesList = new ArrayList<>();

        //action
        viewModel.setCrmNotesList(crmNotesList);

        //assert
        assertEquals(crmNotesList,viewModel.getCrmNotesList());
    }


    @Test
    public void getUsersContactProfiles() {

        //set up
        List<UserProfile> userProfileList = new ArrayList<>();

        //action
        viewModel.setUsersContactProfiles(userProfileList);

        //assert
        assertEquals(userProfileList,viewModel.getUsersContactProfiles());
    }


//    @Test
//    public void getUserProfile() {
//
//        //set up
//        UserProfile  profile = new UserProfile();
//
//        //action
//        viewModel.setUserProfile(profile);
//
//        //assert
//        assertEquals(profile,viewModel.getUserProfile());
//    }



    @Test
    public void getSelectedOpportunity() {

        //set up
        CrmNotes notes = new CrmNotes();

        //action
        viewModel.setSelectedOpportunity(notes);

        //assert
        assertEquals(notes,viewModel.getSelectedOpportunity());
    }


    @Test
    public void getSelectedRowPosition() {

        //set up
        int position = 5;

        //action
        viewModel.setSelectedRowPosition(position);

        //assert
        assertEquals(position,viewModel.getSelectedRowPosition());
    }

    @Test
    public void onInfoClicked() {

        //action
        spyViewModel.onInfoClicked();

        //assert
        verify(mockNavigator).onCrmInfoButtonClicked();
    }

    @Test
    public void onSettingsClicked() {

        //action
        spyViewModel.onSettingsClicked();

        //assert
        verify(mockNavigator).onSettingsClicked();
    }

    @Test
    public void onShareClicked() {

        //action
        spyViewModel.onShareClicked();

        //assert
        verify(mockNavigator).onShareClicked();
    }

    @Test
    public void onCreateNewNoteClicked() {

        //action
        spyViewModel.onCreateNewNoteClicked();

        //assert
        verify(mockNavigator).onCreateNewNoteClicked();
    }

    @Test
    public void onFilterClicked() {

        //action
        spyViewModel.onFilterClicked();

        //assert
        verify(mockNavigator).onFilterClicked();
    }

    @Test
    public void hideCrmInfoDialog() {

        //action
        spyViewModel.hideCrmInfoDialog();

        //assert
        verify(mockNavigator).hideCrmInfoDialog();
    }

    @Test
    public void onExportButtonClicked() {

        //action
        spyViewModel.onExportButtonClicked();

        //assert
        verify(mockNavigator).onExportButtonClicked();
    }

    @Test
    public void requestAllOpportunities() {

        //action
        viewModel.requestAllOpportunities(viewModel);

        //assert
        verify(mockDataModel).getAllOpportunities(viewModel);
    }
}