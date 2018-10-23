package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.HowDidYouMeetBinding;
import com.team.mamba.atlascalendar.data.model.api.fireStore.PersonalNotes;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import javax.inject.Inject;

public class HowDidYouMeetFragment extends BaseFragment<HowDidYouMeetBinding,HowDidYouMeetViewModel> {



    @Inject
    HowDidYouMeetViewModel viewModel;

    @Inject
    Context appContext;

    private HowDidYouMeetBinding binding;
    private static PersonalNotes personalNotes;

    public static HowDidYouMeetFragment newInstance(PersonalNotes notes){

        personalNotes = notes;
       return new HowDidYouMeetFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;

    }

    @Override
    public int getLayoutId() {
        return R.layout.how_did_you_meet;
    }

    @Override
    public HowDidYouMeetViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.how_did_you_meet,container,false);


        binding.btnContinue.setOnClickListener(view -> {

            PersonalNotes notes = new PersonalNotes();
            notes.setAuthorID(dataManager.getSharedPrefs().getUserId());
            notes.setHowMet(getHowMet());
            notes.setWhereMet(binding.etWhere.getText().toString());

            if (personalNotes != null){

                notes.setDetails(personalNotes.getDetails());
                notes.setSubjectID(personalNotes.getSubjectID());
            }

            ChangeFragments.addFragmentFadeIn(PersonalNoteRecyclerView
                    .newInstance(notes,dataManager), getActivity().getSupportFragmentManager(), "ContactInfoDialog", null);

        });

        setUpListeners();
        return binding.getRoot();
    }


    private void setUpListeners(){

        //chance encounter
        binding.chkBoxChanceEncounter.setOnClickListener(view -> {

            if (binding.chkBoxChanceEncounter.isChecked()) {

                binding.chkBoxWorkEvent.setChecked(false);
                binding.chkBoxSocialEvent.setChecked(false);
                binding.chkBoxPeerIntroduction.setChecked(false);
                binding.chkBoxPeerColleague.setChecked(false);

            } else {

                binding.chkBoxChanceEncounter.setChecked(true);
            }

        });

        //work event
        binding.chkBoxWorkEvent.setOnClickListener(view -> {

            if (binding.chkBoxWorkEvent.isChecked()) {

                binding.chkBoxChanceEncounter.setChecked(false);
                binding.chkBoxSocialEvent.setChecked(false);
                binding.chkBoxPeerIntroduction.setChecked(false);
                binding.chkBoxPeerColleague.setChecked(false);

            } else {

                binding.chkBoxWorkEvent.setChecked(true);
            }

        });

        //social event
        binding.chkBoxSocialEvent.setOnClickListener(view -> {

            if (binding.chkBoxChanceEncounter.isChecked()) {

                binding.chkBoxWorkEvent.setChecked(false);
                binding.chkBoxChanceEncounter.setChecked(false);
                binding.chkBoxPeerIntroduction.setChecked(false);
                binding.chkBoxPeerColleague.setChecked(false);

            } else {

                binding.chkBoxSocialEvent.setChecked(true);
            }

        });

        //peer introduction
        binding.chkBoxPeerIntroduction.setOnClickListener(view -> {

            if (binding.chkBoxPeerIntroduction.isChecked()) {

                binding.chkBoxWorkEvent.setChecked(false);
                binding.chkBoxSocialEvent.setChecked(false);
                binding.chkBoxChanceEncounter.setChecked(false);
                binding.chkBoxPeerColleague.setChecked(false);

            } else {

                binding.chkBoxPeerIntroduction.setChecked(true);
            }

        });

        //peer colleague
        binding.chkBoxPeerColleague.setOnClickListener(view -> {

            if (binding.chkBoxPeerColleague.isChecked()) {

                binding.chkBoxWorkEvent.setChecked(false);
                binding.chkBoxSocialEvent.setChecked(false);
                binding.chkBoxPeerIntroduction.setChecked(false);
                binding.chkBoxChanceEncounter.setChecked(false);

            } else {

                binding.chkBoxPeerColleague.setChecked(true);
            }

        });
    }

    public String getHowMet(){

        if (binding.chkBoxChanceEncounter.isChecked()){

            return "Change Encounter";

        } else if (binding.chkBoxWorkEvent.isChecked()){

            return "Work Event";

        } else if (binding.chkBoxSocialEvent.isChecked()){

            return "Social Event";

        } else if (binding.chkBoxPeerIntroduction.isChecked()){

            return "Peer Introduction";

        } else {

            return "Peer/Colleague";
        }
    }
}
