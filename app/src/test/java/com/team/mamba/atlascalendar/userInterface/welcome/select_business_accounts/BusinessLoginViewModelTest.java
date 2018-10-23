package com.team.mamba.atlascalendar.userInterface.welcome.select_business_accounts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivity;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login.BusinessLoginDataModel;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login.BusinessLoginNavigator;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login.BusinessLoginViewModel;
import com.team.mamba.atlascalendar.utils.CommonUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;



@SuppressWarnings("ResultOfMethodCallIgnored")
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class BusinessLoginViewModelTest {


    @Mock
    private BusinessLoginNavigator mockNavigator;

    @Mock
    private BusinessLoginDataModel mockDataModel;

    private BusinessLoginViewModel viewModel;
    private BusinessLoginViewModel spyViewModel;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        WelcomeActivity activity = Robolectric.buildActivity(WelcomeActivity.class)
                .create()
                .resume()
                .get();

        viewModel = new BusinessLoginViewModel();
        spyViewModel = spy(new BusinessLoginViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }



    @Test
    public void getBusinessProfileList() {

        //set up
        List<BusinessProfile> businessProfiles = new ArrayList<>();

        //action
        viewModel.setBusinessProfileList(businessProfiles);

        //assert
       assertEquals(businessProfiles,viewModel.getBusinessProfileList());
    }

    @Test
    public void isEmailValid_returns_true() {

        //set up
        String email = "atlas@email.com";

        //assert
        assertTrue(CommonUtils.isEmailValid(email));
    }

    @Test
    public void isEmailValid_returns_false() {

        //set up
        String email = "atlas@email";

        //assert
        assertFalse(CommonUtils.isEmailValid(email));
    }

    @Test
    public void isPasswordValid_returns_true() {

        //set up
        String password = "passWord";

        //assert
        assertTrue(viewModel.isPasswordValid(password));
    }


    @Test
    public void isPasswordValid_returns_false() {

        //set up
        String password = "";

        //assert
        assertFalse(viewModel.isPasswordValid(password));
    }

    @Test
    public void onBusinessScreenLoginClicked() {

        //action
        spyViewModel.onBusinessScreenLoginClicked();

        //assert
        verify(mockNavigator).onBusinessScreenLoginClicked();
    }

    @Test
    public void onBusinessScreenLearnMoreClicked() {

        //action
        spyViewModel.onBusinessScreenLearnMoreClicked();

        //assert
        verify(mockNavigator).onBusinessScreenLearnMoreClicked();
    }

    @Test
    public void firebaseAuthenticateByEmail() {

        //set up
        String password = "password";
        String email = "atlas@email.com";

        //action
        viewModel.firebaseAuthenticateByEmail(viewModel,email,password);

        //assert
        verify(mockDataModel).firebaseAuthenticateByEmail(viewModel,email,password);
    }
}