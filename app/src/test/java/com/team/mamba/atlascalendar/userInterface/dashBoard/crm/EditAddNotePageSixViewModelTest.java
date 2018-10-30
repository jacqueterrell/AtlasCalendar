package com.team.mamba.atlascalendar.userInterface.dashBoard.crm;

import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageSixDataModel;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageSixNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageSixViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(RobolectricTestRunner.class)
public class EditAddNotePageSixViewModelTest {


    @Mock
    private EditAddNotePageSixNavigator mockNavigator;

    @Mock
    private EditAddNotePageSixDataModel mockDataModel;

    private EditAddNotePageSixViewModel viewModel;
    private EditAddNotePageSixViewModel spyViewModel;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        viewModel = new EditAddNotePageSixViewModel();
        spyViewModel = spy(new EditAddNotePageSixViewModel());
        viewModel.setDataModel(mockDataModel);

        doReturn(mockNavigator).when(spyViewModel).getNavigator();
    }

    @Test
    public void onFinishButtonClicked() {

        //action
        spyViewModel.onFinishButtonClicked();

        //assert
        verify(mockNavigator).onFinishButtonClicked();
    }

    @Test
    public void submitNoteRequest() {

        //set up
        CrmNotes crmNotes = new CrmNotes();

        //action
        viewModel.submitNoteRequest(viewModel,crmNotes);

        //assert
        verify(mockDataModel).setNoteRequest(viewModel,crmNotes);

    }
}