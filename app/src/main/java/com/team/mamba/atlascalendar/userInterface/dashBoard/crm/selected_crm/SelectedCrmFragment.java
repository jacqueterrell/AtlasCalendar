package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.selected_crm;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.databinding.SelectedCrmLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageOneFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmViewModel;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import javax.inject.Inject;

public class SelectedCrmFragment extends BaseFragment<SelectedCrmLayoutBinding,SelectedCrmViewModel>
implements SelectedCrmNavigator{


    @Inject
    SelectedCrmViewModel viewModel;

    @Inject
    SelectedCrmDataModel dataModel;

    private SelectedCrmLayoutBinding binding;
    private static CrmNotes notes;
    public static boolean isNoteDeleted = false;

    public static SelectedCrmFragment newInstance(CrmNotes crmNotes){

        notes = crmNotes;
        return new SelectedCrmFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.selected_crm_layout;
    }

    @Override
    public SelectedCrmViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();

         binding.setCrmNote(notes);

         return binding.getRoot();
    }

    @Override
    public void onSuccess() {

        CrmViewModel.crmNotesList.clear();
        hideProgressSpinner();
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onEditClicked() {

        ChangeFragments.replaceFragmentVertically(EditAddNotePageOneFragment.newInstance(notes,false),getBaseActivity().getSupportFragmentManager(),"SelectedCrm",null);

    }

    @Override
    public void onDeleteClicked() {


        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());


        String title = "Delete " + notes.getNoteName() + "?";
        String msg = "This will permanently remove " + notes.getNoteName() + " from your opportunity list";
        dialog.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("No", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                    showProgressSpinner();
                    viewModel.deleteNote(getViewModel(),notes);
                });

        dialog.show();
    }
}
