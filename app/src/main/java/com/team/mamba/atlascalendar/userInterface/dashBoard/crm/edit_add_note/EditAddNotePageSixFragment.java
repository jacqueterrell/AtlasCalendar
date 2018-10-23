package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.BR;

import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.databinding.EditAddPageSixBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmViewModel;

import javax.inject.Inject;

public class EditAddNotePageSixFragment extends BaseFragment<EditAddPageSixBinding, EditAddNotePageSixViewModel>
implements EditAddNotePageSixNavigator{


    @Inject
    EditAddNotePageSixViewModel viewModel;

    @Inject
    EditAddNotePageSixDataModel dataModel;


    private EditAddPageSixBinding binding;
    private static CrmNotes notes;

    public static EditAddNotePageSixFragment newInstance(CrmNotes crmNotes){

        notes = crmNotes;
        return new EditAddNotePageSixFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_add_page_six;
    }

    @Override
    public EditAddNotePageSixViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onFinishButtonClicked() {

        String description = binding.etDescription.getText().toString();

        if (description.isEmpty()){

            showSnackbar("Please type the event");

        } else {

            showProgressSpinner();
            Long timeStamp = System.currentTimeMillis() / 1000;

            notes.setDesc(description);
            notes.setNextStep(getNextStep());
            notes.setTimestamp(timeStamp);
            viewModel.submitNoteRequest(getViewModel(),notes);

        }

    }

    @Override
    public void onNoteSubmitted() {

        hideProgressSpinner();

        CrmViewModel.crmNotesList.clear();

        getBaseActivity().getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = getViewDataBinding();

        setUpListeners();
        setCachedData();

        return binding.getRoot();
    }



    private int getNextStep(){

        if (binding.chkBoxEmail.isChecked()){

            return 0;

        } else if (binding.chkBoxPhoneCall.isChecked()){

            return 1;

        }  else if (binding.chkBoxTeleconference.isChecked()){

            return 2;

        }  else if (binding.chkBoxMeeting.isChecked()){

            return 3;

        } else {

            return 4;

        }
    }


    private void setUpListeners(){

        binding.chkBoxEmail.setOnClickListener(v -> {

            if (binding.chkBoxEmail.isChecked()){

                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxTeleconference.setChecked(false);
                binding.chkBoxMeeting.setChecked(false);
                binding.chkBoxPhoneCall.setChecked(false);

            } else {

                binding.chkBoxEmail.setChecked(true);
            }

        });

        binding.chkBoxPhoneCall.setOnClickListener(v -> {

            if (binding.chkBoxPhoneCall.isChecked()){

                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxTeleconference.setChecked(false);
                binding.chkBoxMeeting.setChecked(false);
                binding.chkBoxEmail.setChecked(false);

            } else {

                binding.chkBoxPhoneCall.setChecked(true);
            }
        });

        binding.chkBoxMeeting.setOnClickListener(v -> {

            if (binding.chkBoxMeeting.isChecked()){

                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxTeleconference.setChecked(false);
                binding.chkBoxPhoneCall.setChecked(false);
                binding.chkBoxEmail.setChecked(false);

            } else {

                binding.chkBoxMeeting.setChecked(true);
            }
        });

        binding.chkBoxTeleconference.setOnClickListener(v -> {

            if (binding.chkBoxTeleconference.isChecked()){

                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxMeeting.setChecked(false);
                binding.chkBoxPhoneCall.setChecked(false);
                binding.chkBoxEmail.setChecked(false);

            } else {

                binding.chkBoxTeleconference.setChecked(true);
            }

        });

        binding.chkBoxProposal.setOnClickListener(v -> {

            if (binding.chkBoxProposal.isChecked()){

                binding.chkBoxTeleconference.setChecked(false);
                binding.chkBoxMeeting.setChecked(false);
                binding.chkBoxPhoneCall.setChecked(false);
                binding.chkBoxEmail.setChecked(false);

            } else {

                binding.chkBoxProposal.setChecked(true);
            }

        });

    }

    private void setCachedData(){

       // binding.setCrmNote(notes);


        if (notes.getNextStep() == 0){

            binding.chkBoxEmail.setChecked(true);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxTeleconference.setChecked(false);
            binding.chkBoxMeeting.setChecked(false);
            binding.chkBoxPhoneCall.setChecked(false);

        } else if (notes.getNextStep() == 1){

            binding.chkBoxEmail.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxTeleconference.setChecked(false);
            binding.chkBoxMeeting.setChecked(false);
            binding.chkBoxPhoneCall.setChecked(true);

        } else if (notes.getNextStep() == 2){

            binding.chkBoxEmail.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxTeleconference.setChecked(true);
            binding.chkBoxMeeting.setChecked(false);
            binding.chkBoxPhoneCall.setChecked(false);

        } else if (notes.getNextStep() == 3){

            binding.chkBoxEmail.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxTeleconference.setChecked(false);
            binding.chkBoxMeeting.setChecked(true);
            binding.chkBoxPhoneCall.setChecked(false);

        } else {

            binding.chkBoxEmail.setChecked(false);
            binding.chkBoxProposal.setChecked(true);
            binding.chkBoxTeleconference.setChecked(false);
            binding.chkBoxMeeting.setChecked(false);
            binding.chkBoxPhoneCall.setChecked(false);
        }
    }

}
