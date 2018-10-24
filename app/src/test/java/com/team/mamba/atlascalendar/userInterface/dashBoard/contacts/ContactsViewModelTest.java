package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class ContactsViewModelTest {


    @Mock
    private ContactsNavigator mockNavigator;

    @Mock
    private ContactsDataModel mockDataModel;

    private ContactsViewModel viewModel;
    private ContactsViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        viewModel = new ContactsViewModel();
        spyViewModel = spy(new ContactsViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }



    @Test
    public void getUserProfileList() {

        //set up
        List<UserProfile> userProfileList = new ArrayList<>();

        //action
        viewModel.setUserProfileList(userProfileList);

        //assert
        assertEquals(userProfileList,viewModel.getUserProfileList());
    }


    @Test
    public void getBusinessProfileList() {

        //set up
        List<BusinessProfile> businessProfileList = new ArrayList<>();

        //action
        viewModel.setBusinessProfileList(businessProfileList);

        //assert
        assertEquals(businessProfileList,viewModel.getBusinessProfileList());
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


    @Test
    public void getBusinessProfile() {

        //set up
        BusinessProfile profile = new BusinessProfile();

        //action
        viewModel.setBusinessProfile(profile);

        //assert
        assertEquals(profile,viewModel.getBusinessProfile());
    }


    @Test
    public void getUserConnectionsList() {

        //set up
        List<UserConnections> userConnections = new ArrayList<>();

        //action
        viewModel.setUserConnectionsList(userConnections);

        //assert
        assertEquals(userConnections,viewModel.getUserConnectionsList());
    }


    @Test
    public void getSavedUserId() {

        //set up
        String userId = "7777";

        //action
        viewModel.setSavedUserId(userId);

        //assert
        assertEquals(userId,viewModel.getSavedUserId());
    }


    @Test
    public void getSelectedBusinessProfile() {

        //set up
        BusinessProfile profile = new BusinessProfile();

        //action
        viewModel.setSelectedBusinessProfile(profile);

        //assert
        assertEquals(profile,viewModel.getSelectedBusinessProfile());
    }

    @Test
    public void onAddNewContactClicked() {

        //action
        spyViewModel.onAddNewContactClicked();

        //assert
        verify(mockNavigator).onAddNewContactClicked();
    }

    @Test
    public void onSycnContactsClicked() {

        //action
        spyViewModel.onSycnContactsClicked();

        //assert
        verify(mockNavigator).onSyncContactsClicked();
    }

    @Test
    public void onSettingsClicked() {

        //action
        spyViewModel.onSettingsClicked();

        //assert
        verify(mockNavigator).onSettingsClicked();
    }

    @Test
    public void onProfileImageClicked() {

        //action
        spyViewModel.onProfileImageClicked();

        //assert
        verify(mockNavigator).onProfileImageClicked();
    }

    @Test
    public void onGroupFilterClicked() {

        //action
        spyViewModel.onGroupFilterClicked();

        //assert
        verify(mockNavigator).onBusinessFilterClicked();
    }

    @Test
    public void onIndividualFilterClicked() {

        //action
        spyViewModel.onIndividualFilterClicked();

        //assert
        verify(mockNavigator).onIndividualFilterClicked();
    }

    @Test
    public void setBusinessContactProfiles_validates_user_is_a_business() {

        //set up
        Map<String,String> contactsMap = new LinkedHashMap<>();
        contactsMap.put("123456789","123456789");

        BusinessProfile profile = new BusinessProfile();
        profile.setContacts(contactsMap);

        List<UserConnections> userConnectionsList = new ArrayList<>();
        UserConnections connections = new UserConnections();
        connections.setOrgBus(true);
        connections.setBusinessProfile(profile);
        userConnectionsList.add(connections);


        //action
        spyViewModel.setUserConnectionsList(userConnectionsList);
        spyViewModel.setBusinessContactProfiles();

        //assert
        verify(spyViewModel).setSelectedBusinessProfile(connections.getBusinessProfile());
        verify(mockNavigator).onBusinessContactsSet(anyList());
    }


    @Test
    public void setBusinessContactProfiles_validates_user_is_an_individual() {

        //set up
        Map<String,String> contactsMap = new LinkedHashMap<>();
        contactsMap.put("123456789","123456789");

        UserProfile profile = new UserProfile();
        profile.setConnections(contactsMap);

        List<UserConnections> userConnectionsList = new ArrayList<>();
        UserConnections connections = new UserConnections();
        connections.setOrgBus(false);
        connections.setUserProfile(profile);
        userConnectionsList.add(connections);


        //action
        spyViewModel.setUserConnectionsList(userConnectionsList);
        spyViewModel.setBusinessContactProfiles();

        //assert
        verify(spyViewModel,never()).setSelectedBusinessProfile(connections.getBusinessProfile());
        verify(mockNavigator).onBusinessContactsSet(anyList());
    }


    @Test
    public void requestContactsInfo() {

        //action
        viewModel.requestContactsInfo(viewModel);

        //assert
        verify(mockDataModel).requestContactsInfo(viewModel);
    }
}