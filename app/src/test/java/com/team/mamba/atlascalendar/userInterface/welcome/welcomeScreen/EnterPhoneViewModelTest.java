package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number.EnterPhoneDataModel;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number.EnterPhoneNavigator;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number.EnterPhoneViewModel;

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
public class EnterPhoneViewModelTest {


    @Mock
    EnterPhoneNavigator mockNavigator;

    @Mock
    EnterPhoneDataModel mockDataModel;

    private EnterPhoneViewModel viewModel;
    private EnterPhoneViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new EnterPhoneViewModel();
        spyViewModel = spy(new EnterPhoneViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }

    @Test
    public void getPhoneNumber() {

        //set up
        String phone = "770-599-0000";

        //action
        viewModel.setPhoneNumber(phone);

        //assert
        assertEquals(phone,viewModel.getPhoneNumber());
    }


    @Test
    public void getVerificationId() {

        //set up
        String verifyId = "123456";

        //action
        viewModel.setVerificationId(verifyId);

        //assert
        assertEquals(verifyId,viewModel.getVerificationId());
    }



    @Test
    public void getFirstName() {

        //set up
        String firstName = "Atlas";

        //action
        viewModel.setFirstName(firstName);

        //assert
        assertEquals(firstName,viewModel.getFirstName());
    }

    @Test
    public void getLastName() {

        //set up
        String lastName = "Atlas";

        //action
        viewModel.setLastName(lastName);

        //assert
        assertEquals(lastName,viewModel.getLastName());
    }


    @Test
    public void getDateOfBirth() {

        //set up
        long dob = 627631200L;

        //action
        viewModel.setDateOfBirth(dob);

        //assert
        assertEquals(dob,viewModel.getDateOfBirth());
    }


    @Test
    public void onPhoneSubmitPreviousClicked() {

        //action
        spyViewModel.onPhoneSubmitPreviousClicked();

        //assert
        verify(mockNavigator).onPhoneSubmitPreviousClicked();
    }

    @Test
    public void onEnterSmsCancelClicked() {

        //action
        spyViewModel.onEnterSmsCancelClicked();

        //assert
        verify(mockNavigator).onEnterSmsCancelClicked();
    }

    @Test
    public void onPhoneSubmitClicked() {

        //action
        spyViewModel.onPhoneSubmitClicked();

        //assert
        verify(mockNavigator).onPhoneSubmitClicked();
    }

    @Test
    public void onEnterSmsContinueClicked() {

        //action
        spyViewModel.onEnterSmsContinueClicked();

        //assert
        verify(mockNavigator).onEnterSmsContinueClicked();
    }

    @Test
    public void fireBaseVerifyPhoneNumber() {

        //set up
        String phoneNumber = "777-777-7777";

        //action
        viewModel.fireBaseVerifyPhoneNumber(viewModel,phoneNumber);

        //assert
        verify(mockDataModel).fireBaseVerifyPhoneNumber(viewModel,phoneNumber);
    }

    @Test
    public void signInWithPhoneAuthCredential() {

        //action
        viewModel.signInWithPhoneAuthCredential(viewModel);

        //assert
        verify(mockDataModel).signInWithPhoneAuthCredential(viewModel);
    }
}