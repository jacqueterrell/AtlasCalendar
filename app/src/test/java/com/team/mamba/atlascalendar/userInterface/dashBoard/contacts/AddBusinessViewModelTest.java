package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessViewModel;

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
public class AddBusinessViewModelTest {


    @Mock
    private AddBusinessNavigator mockNavigator;

    @Mock
    private AddBusinessDataModel mockDataModel;

    private AddBusinessViewModel viewModel;
    private AddBusinessViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new AddBusinessViewModel();
        spyViewModel = spy(new AddBusinessViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }


    @Test
    public void setRequestingBusinessProfile() {
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
    public void setRequestingUserProfile() {
    }

    @Test
    public void getRequestingUserProfile(){

        //set up
        UserProfile profile = new UserProfile();

        //action
        viewModel.setRequestingUserProfile(profile);

        //assert
        assertEquals(profile,viewModel.getRequestingUserProfile());
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
    public void onFinishButtonClicked() {

        //action
        spyViewModel.onFinishButtonClicked();

        //assert
        verify(mockNavigator).onFinishButtonClicked();
    }

    @Test
    public void addBusinessRequest() {

        //set up
        String name = "Atlas";
        String code = "Code";

        //action
        viewModel.addBusinessRequest(viewModel,name,code);

        //assert
        verify(mockDataModel).addBusinessRequest(viewModel,name,code);
    }
}