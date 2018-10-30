package com.team.mamba.atlascalendar.userInterface.dashBoard;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.CrmFilter;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityViewModel;

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
public class DashBoardActivityViewModelTest {

    @Mock
    private DashBoardActivityNavigator mockNavigator;

    private DashBoardActivityViewModel viewModel;
    private DashBoardActivityViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new DashBoardActivityViewModel();
        spyViewModel = spy(new DashBoardActivityViewModel());

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }


    @Test
    public void getCrmFilter() {

        //set up
        CrmFilter filter = new CrmFilter();

        //action
        viewModel.setCrmFilter(filter);

        //assert
        assertEquals(filter,viewModel.getCrmFilter());
    }

    @Test
    public void getUserProfile() {

        //set up
        UserProfile profile = new UserProfile();

        //action
        viewModel.setUserProfile(profile);

        //assert
        assertEquals(profile,viewModel.getUserProfile());
    }

}