package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen;

import com.team.mamba.atlascalendar.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class WelcomeViewModelTest {


    @Mock
    private WelcomeNavigator mockNavigator;

    @Mock
    private WelcomeDataModel mockDataModel;

    private WelcomeViewModel viewModel;
    private WelcomeViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new WelcomeViewModel();
        spyViewModel = spy(new WelcomeViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }

    @Test
    public void isAgeValid_returns_true() {

        //set up
        int month = 11;
        int day = 21;
        int year = 1989;

        //assert
        assertTrue(viewModel.isAgeValid(month,day,year));
    }

    @Test
    public void isAgeValid_returns_false() {

        //set up
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR,thisYear - 12);

        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        //assert
        assertFalse(viewModel.isAgeValid(month,day,year));
    }



    @Test
    public void getDateOfBirth() {

        //setup
        viewModel.setDateOfBirth(123456L);

        //assert
        assertEquals(123456L,viewModel.getDateOfBirth());
    }

    @Test
    public void onStartButtonClicked() {

        //action
        spyViewModel.onStartButtonClicked();

        //assert
        verify(mockNavigator).onStartButtonClicked();
    }

    @Test
    public void onBusinessLoginClicked() {

        //action
        spyViewModel.onBusinessLoginClicked();

        //assert
        verify(mockNavigator).onBusinessLoginClicked();
    }

    @Test
    public void onDateVerifyClicked() {

        //action
        spyViewModel.onDateVerifyClicked();

        //assert
        verify(mockNavigator).onDateVerifyClicked();
    }

    @Test
    public void onDateCancelClicked() {

        //action
        spyViewModel.onDateCancelClicked();

        //assert
        verify(mockNavigator).onDateCancelClicked();
    }
}