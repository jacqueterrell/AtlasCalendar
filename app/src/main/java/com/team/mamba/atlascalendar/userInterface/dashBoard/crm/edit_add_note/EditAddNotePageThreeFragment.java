package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.databinding.EditAddPageThreeBinding;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

public class EditAddNotePageThreeFragment extends Fragment{


    private EditAddPageThreeBinding binding;
    private static CrmNotes notes;

    public static EditAddNotePageThreeFragment newInstance(CrmNotes crmNotes){

        notes = crmNotes;
        return new EditAddNotePageThreeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_add_page_three,container,false);

        setListeners();

        binding.btnContinue.setOnClickListener(v -> {

            notes.setStage(getStage());
            openPageFour(notes);

        });

        binding.setCrmNote(notes);
        setCachedData();

        return binding.getRoot();
    }


    private void setListeners(){

        binding.chkBxNew.setOnClickListener(v -> {

            if (binding.chkBxNew.isChecked()){

                binding.chkBxQualified.setChecked(false);
                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxNegotiation.setChecked(false);
                binding.chkBoxClosedLost.setChecked(false);
                binding.chkBoxClosedWon.setChecked(false);

            } else {

                binding.chkBxNew.setChecked(true);
            }

        });

        binding.chkBxQualified.setOnClickListener(v -> {

            if (binding.chkBxQualified.isChecked()){

                binding.chkBxNew.setChecked(false);
                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxNegotiation.setChecked(false);
                binding.chkBoxClosedLost.setChecked(false);
                binding.chkBoxClosedWon.setChecked(false);

            } else {

                binding.chkBxQualified.setChecked(true);
            }
        });

        binding.chkBoxProposal.setOnClickListener(v -> {

            if (binding.chkBoxProposal.isChecked()){

                binding.chkBxNew.setChecked(false);
                binding.chkBxQualified.setChecked(false);
                binding.chkBoxNegotiation.setChecked(false);
                binding.chkBoxClosedLost.setChecked(false);
                binding.chkBoxClosedWon.setChecked(false);

            } else {

                binding.chkBoxProposal.setChecked(true);
            }

        });

        binding.chkBoxNegotiation.setOnClickListener(v -> {

            if (binding.chkBoxNegotiation.isChecked()){

                binding.chkBxNew.setChecked(false);
                binding.chkBxQualified.setChecked(false);
                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxClosedLost.setChecked(false);
                binding.chkBoxClosedWon.setChecked(false);

            } else {

                binding.chkBoxNegotiation.setChecked(true);
            }

        });

        binding.chkBoxClosedWon.setOnClickListener(v -> {

            if (binding.chkBoxClosedWon.isChecked()){

                binding.chkBxNew.setChecked(false);
                binding.chkBxQualified.setChecked(false);
                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxClosedLost.setChecked(false);
                binding.chkBoxNegotiation.setChecked(false);

            } else {

                binding.chkBoxClosedWon.setChecked(true);
            }

        });

        binding.chkBoxClosedLost.setOnClickListener(v -> {

            if (binding.chkBoxClosedLost.isChecked()){

                binding.chkBxNew.setChecked(false);
                binding.chkBxQualified.setChecked(false);
                binding.chkBoxProposal.setChecked(false);
                binding.chkBoxClosedWon.setChecked(false);
                binding.chkBoxNegotiation.setChecked(false);

            } else {

                binding.chkBoxClosedLost.setChecked(true);
            }

        });
    }


    private int getStage(){

        if (binding.chkBxNew.isChecked()){

            return 0;

        } else if (binding.chkBxQualified.isChecked()){

            return 1;

        } else if (binding.chkBoxProposal.isChecked()){

            return 2;

        } else if (binding.chkBoxNegotiation.isChecked()){

            return 3;

        } else if (binding.chkBoxClosedWon.isChecked()){

            return 4;

        } else {

            return 5;
        }
    }

    private void openPageFour(CrmNotes crmNotes){

        ChangeFragments.addFragmentFadeIn(EditAddNotePageFourFragment.newInstance(crmNotes),getActivity().getSupportFragmentManager(),"PageFour",null);

    }


    private void setCachedData(){

        if (notes.getStage() == 0){

            binding.chkBxNew.setChecked(true);
            binding.chkBxQualified.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxClosedWon.setChecked(false);
            binding.chkBoxClosedLost.setChecked(false);
            binding.chkBoxNegotiation.setChecked(false);

        } else if (notes.getStage() == 1){

            binding.chkBxNew.setChecked(false);
            binding.chkBxQualified.setChecked(true);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxClosedWon.setChecked(false);
            binding.chkBoxClosedLost.setChecked(false);
            binding.chkBoxNegotiation.setChecked(false);

        } else if (notes.getStage() == 2){

            binding.chkBxNew.setChecked(false);
            binding.chkBxQualified.setChecked(false);
            binding.chkBoxProposal.setChecked(true);
            binding.chkBoxClosedWon.setChecked(false);
            binding.chkBoxClosedLost.setChecked(false);
            binding.chkBoxNegotiation.setChecked(false);

        } else if (notes.getStage() == 3){

            binding.chkBxNew.setChecked(false);
            binding.chkBxQualified.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxClosedWon.setChecked(false);
            binding.chkBoxClosedLost.setChecked(false);
            binding.chkBoxNegotiation.setChecked(true);

        } else if (notes.getStage() == 4){

            binding.chkBxNew.setChecked(false);
            binding.chkBxQualified.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxClosedWon.setChecked(true);
            binding.chkBoxClosedLost.setChecked(false);
            binding.chkBoxNegotiation.setChecked(false);

        } else {

            binding.chkBxNew.setChecked(false);
            binding.chkBxQualified.setChecked(false);
            binding.chkBoxProposal.setChecked(false);
            binding.chkBoxClosedWon.setChecked(false);
            binding.chkBoxClosedLost.setChecked(true);
            binding.chkBoxNegotiation.setChecked(false);
        }
    }
}
