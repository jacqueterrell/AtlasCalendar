package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user.AddUserNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_user.AddUserViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
@Config(constants = BuildConfig.class)
public class AddUserViewModelTest {


    @Mock
    private AddUserNavigator mockNavigator;

    private AddUserViewModel viewModel;
    private AddUserViewModel spyViewModel;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new AddUserViewModel();
        spyViewModel = spy(new AddUserViewModel());
        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }

    @Test
    public void isLastNameValid_returns_true() {

        //assert
        assertTrue(viewModel.isLastNameValid("lastName"));
    }

    @Test
    public void isLastNameValid_returns_false() {

        //assert
        assertFalse(viewModel.isLastNameValid(""));
    }

    @Test
    public void isUserCodeValid_returns_true() {

        //assert
        assertTrue(viewModel.isUserCodeValid("code"));
    }

    @Test
    public void isUserCodeValid_returns_false() {

        //assert
        assertFalse(viewModel.isUserCodeValid(""));
    }

    @Test
    public void onNextButtonClicked() {

        //action
        spyViewModel.onNextButtonClicked();

        //assert
        verify(mockNavigator).onNextButtonClicked();
    }
}