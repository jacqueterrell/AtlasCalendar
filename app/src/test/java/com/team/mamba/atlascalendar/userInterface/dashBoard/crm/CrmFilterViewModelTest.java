package com.team.mamba.atlascalendar.userInterface.dashBoard.crm;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.filter_list.CrmFilterNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.filter_list.CrmFilterViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;



@SuppressWarnings("ResultOfMethodCallIgnored")
@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class CrmFilterViewModelTest {


    @Mock
    private CrmFilterNavigator mockNavigator;

    private CrmFilterViewModel viewModel;
    private CrmFilterViewModel spyViewModel;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new CrmFilterViewModel();
        spyViewModel = spy(new CrmFilterViewModel());

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }

    @Test
    public void onCancelButtonClicked() {

        //action
        spyViewModel.onCancelButtonClicked();

        //assert
        verify(mockNavigator).onCancelButtonClicked();
    }

    @Test
    public void onSubmitButtonClicked() {

        //action
        spyViewModel.onSubmitButtonClicked();

        //assert
        verify(mockNavigator).onSubmitButtonClicked();
    }

    @Test
    public void onNextStepEmailClicked() {

        //action
        spyViewModel.onNextStepEmailClicked();

        //assert
        verify(mockNavigator).onNextStepEmailClicked();
    }

    @Test
    public void onNextStepPhoneClicked() {

        //action
        spyViewModel.onNextStepPhoneClicked();

        //assert
        verify(mockNavigator).onNextStepPhoneClicked();
    }

    @Test
    public void onNextStepTeleconferenceClicked() {

        //action
        spyViewModel.onNextStepTeleconferenceClicked();

        //assert
        verify(mockNavigator).onNextStepTeleconferenceClicked();
    }

    @Test
    public void onNextMeetingClicked() {

        //action
        spyViewModel.onNextMeetingClicked();

        //assert
        verify(mockNavigator).onNextMeetingClicked();
    }

    @Test
    public void onSolicitationClicked() {

        //action
        spyViewModel.onSolicitationClicked();

        //assert
        verify(mockNavigator).onSolicitationClicked();
    }

    @Test
    public void onTeamingClicked() {

        //action
        spyViewModel.onTeamingClicked();

        //assert
        verify(mockNavigator).onTeamingClicked();
    }

    @Test
    public void onDirectSellClicked() {

        //action
        spyViewModel.onDirectSellClicked();

        //assert
        verify(mockNavigator).onDirectSellClicked();
    }

    @Test
    public void onNewClicked() {

        //action
        spyViewModel.onNewClicked();

        //assert
        verify(mockNavigator).onNewClicked();
    }

    @Test
    public void onQualifiedClicked() {

        //action
        spyViewModel.onQualifiedClicked();

        //assert
        verify(mockNavigator).onQualifiedClicked();
    }

    @Test
    public void onProposalClicked() {

        //action
        spyViewModel.onProposalClicked();

        //assert
        verify(mockNavigator).onProposalClicked();
    }

    @Test
    public void onNegotiationClicked() {

        //action
        spyViewModel.onNegotiationClicked();

        //assert
        verify(mockNavigator).onNegotiationClicked();
    }

    @Test
    public void onClosedClicked() {

        //action
        spyViewModel.onClosedClicked();

        //assert
        verify(mockNavigator).onClosedClicked();
    }
}