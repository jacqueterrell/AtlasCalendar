package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecyclerDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecyclerNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecyclerViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(RobolectricTestRunner.class)
public class FindUsersRecyclerViewModelTest {


    @Mock
    private FindUsersRecyclerNavigator mockNavigator;

    @Mock
    private FindUsersRecyclerDataModel mockDataModel;

    private FindUsersRecyclerViewModel viewModel;
    private FindUsersRecyclerViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new FindUsersRecyclerViewModel();
        spyViewModel = spy(new FindUsersRecyclerViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }



    @Test
    public void getRequestingUserProfile() {

        //set up
        UserProfile profile = new UserProfile();

        //action
        viewModel.setRequestingUserProfile(profile);

        //assert
        assertEquals(profile,viewModel.getRequestingUserProfile());
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
    public void initiateNewUserRequest() {

        //set up
        UserProfile profile = new UserProfile();

        //action
        viewModel.initiateNewUserRequest(viewModel,profile);

        //assert
        verify(mockDataModel).initiateNewUserRequest(viewModel,profile);
    }
}