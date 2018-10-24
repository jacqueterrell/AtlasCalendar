package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersViewModel;

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
public class FindUsersViewModelTest {

    @Mock
    private FindUsersNavigator mockNavogator;

    @Mock
    private FindUsersDataModel mockDataModel;

    private FindUsersViewModel viewModel;
    private FindUsersViewModel spyViewModel;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        viewModel = new FindUsersViewModel();
        spyViewModel = spy(new FindUsersViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavogator).when(spyViewModel).getNavigator();

    }

    @Test
    public void setQueriedProfiles() {
    }

    @Test
    public void getQueriedProfiles() {

        //set up
        List<UserProfile>  profiles = new ArrayList<>();

        //action
        viewModel.setQueriedProfiles(profiles);

        //assert
        assertEquals(profiles,viewModel.getQueriedProfiles());
    }

    @Test
    public void onSearchButtonClicked() {

        //action
        spyViewModel.onSearchButtonClicked();

        //assert
        verify(mockNavogator).onSearchButtonClicked();
    }

    @Test
    public void queryUsers() {

        //set up
        String first = "first";
        String last = "last";

        //action
        viewModel.queryUsers(viewModel,first,last);

        //assert
        verify(mockDataModel).queryUsers(viewModel,first,last);
    }
}