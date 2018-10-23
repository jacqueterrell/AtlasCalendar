package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsViewModel;

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
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class DescribeConnectionsViewModelTest {

    @Mock
    private DescribeConnectionsNavigator mockNavigator;

    @Mock
    private DescribeConnectionsDataModel mockDateModel;

    private DescribeConnectionsViewModel viewModel;
    private DescribeConnectionsViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new DescribeConnectionsViewModel();
        spyViewModel = spy(new DescribeConnectionsViewModel());
        viewModel.setDataModel(mockDateModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }


    @Test
    public void getLoggedInProfileIndividual() {

        //set up
        UserProfile profile = new UserProfile();

        //action
        viewModel.setLoggedInProfileIndividual(profile);

        //assert
        assertEquals(profile,viewModel.getLoggedInProfileIndividual());

    }


    @Test
    public void getLoggedInProfileBusiness() {

        //set up
        BusinessProfile profile = new BusinessProfile();

        //action
        viewModel.setLoggedInProfileBusiness(profile);

        //assert
        assertEquals(profile,viewModel.getLoggedInProfileBusiness());
    }



    @Test
    public void isApprovingConnection() {

        //action
        viewModel.setApprovingConnection(false);

        //assert
        assertFalse(viewModel.isApprovingConnection());
    }

    @Test
    public void getRequestingConnection() {

        //set up
        UserConnections connection = new UserConnections();

        //action
        viewModel.setRequestingConnection(connection);

        //assert
        assertEquals(connection,viewModel.getRequestingConnection());
    }

    @Test
    public void onFinishButtonClicked() {

        //action
        spyViewModel.onFinishButtonClicked();

        //assert
        verify(mockNavigator).onFinishButtonClicked();
    }

    @Test
    public void onInfoClicked() {

        //action
        spyViewModel.onInfoClicked();

        //assert
        verify(mockNavigator).onInfoClicked();
    }

    @Test
    public void hideConnectionsInfo() {

        //action
        spyViewModel.hideConnectionsInfo();

        //assert
        verify(mockNavigator).hideConnectionsInfo();
    }

    @Test
    public void addUserRequest() {

        //set up
        String lastName = "last";
        String userCode = "code";
        int type = 5;

        //action
        viewModel.addUserRequest(viewModel,lastName,userCode,type);

        //assert
        verify(mockDateModel).initiateNewUserRequest(viewModel,lastName,userCode,type);
    }

    @Test
    public void updateConnection() {

        //set up
        UserConnections connections = new UserConnections();

        //action
        viewModel.updateConnection(viewModel,connections);

        //assert
        verify(mockDateModel).updateContactsConnection(viewModel,connections);
    }
}