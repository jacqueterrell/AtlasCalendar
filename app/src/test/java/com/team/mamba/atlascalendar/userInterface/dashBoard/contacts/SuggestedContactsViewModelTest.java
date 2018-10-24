package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts.SuggestedContactsDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts.SuggestedContactsNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts.SuggestedContactsViewModel;

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
public class SuggestedContactsViewModelTest {

    @Mock
    private SuggestedContactsNavigator mockNavigator;

    @Mock
    private SuggestedContactsDataModel mockDataModel;

    private SuggestedContactsViewModel viewModel;
    private SuggestedContactsViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new SuggestedContactsViewModel();
        spyViewModel = spy(new SuggestedContactsViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }



    @Test
    public void getSuggestedProfileList() {

        //set up
        List<UserProfile> userProfiles = new ArrayList<>();

        //action
        viewModel.setSuggestedProfileList(userProfiles);

        //assert
        assertEquals(userProfiles,viewModel.getSuggestedProfileList());
    }


    @Test
    public void getRequestingUserProfile() {

        //set up
        UserProfile userProfile = new UserProfile();

        //action
        viewModel.setRequestingUserProfile(userProfile);

        //assert
        assertEquals(userProfile,viewModel.getRequestingUserProfile());
    }


    @Test
    public void getRequestingBusinessProfile() {

        //set up
        BusinessProfile profile = new BusinessProfile();

        //action
        viewModel.setRequestingBusinessProfile(profile);

        //assert
        assertEquals(profile,viewModel.getRequestingBusinessProfile());
    }


    @Test
    public void getConnectionIdList() {

        //set up
        List<String> connectionIdList = new ArrayList<>();

        //action
        viewModel.setConnectionIdList(connectionIdList);

        //assert
        assertEquals(connectionIdList,viewModel.getConnectionIdList());
    }


    @Test
    public void getAllUsers() {

        //set up
        List<UserProfile> userProfiles = new ArrayList<>();

        //action
        viewModel.setAllUsers(userProfiles);

        //assert
        assertEquals(userProfiles,viewModel.getAllUsers());
    }

    @Test
    public void onSearchButtonClicked() {

        //action
        spyViewModel.onSearchButtonClicked();

        //assert
        verify(mockNavigator).onSearchButtonClicked();
    }

    @Test
    public void requestSuggestedContacts() {

        //action
        viewModel.requestSuggestedContacts(viewModel);

        //assert
        verify(mockDataModel).requestSuggestedContacts(viewModel);
    }

    @Test
    public void initiateNewUserRequest() {

        //set up
        UserProfile profile = new UserProfile();

        //action
        viewModel.initiateNewUserRequest(viewModel,profile);

        //assert
        verify(mockDataModel).initiateNewUserRequest(viewModel,profile);
    }
}