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
import com.team.mamba.atlascalendar.databinding.EditAddPageFourBinding;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

public class EditAddNotePageFourFragment extends Fragment {

    private EditAddPageFourBinding binding;
    private static CrmNotes notes;


    public static EditAddNotePageFourFragment newInstance(CrmNotes crmNotes){

        notes = crmNotes;
        return new EditAddNotePageFourFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_add_page_four,container,false);

        setListeners();

        binding.btnContinue.setOnClickListener(v -> {

            notes.setType(getType());
            openPageFive(notes);
        });


        return binding.getRoot();
    }


    private void setListeners(){

        binding.chkBoxCommercial.setOnClickListener(v -> {

            if (binding.chkBoxCommercial.isChecked()){

                binding.chkBxNonProfit.setChecked(false);
                binding.chkBoxFederal.setChecked(false);
                binding.chkBoxLocal.setChecked(false);
                binding.chkBoxState.setChecked(false);

            } else {

                binding.chkBoxCommercial.setChecked(true);
            }
        });

        binding.chkBxNonProfit.setOnClickListener(v -> {

            if (binding.chkBxNonProfit.isChecked()){

                binding.chkBoxCommercial.setChecked(false);
                binding.chkBoxFederal.setChecked(false);
                binding.chkBoxLocal.setChecked(false);
                binding.chkBoxState.setChecked(false);

            } else {

                binding.chkBxNonProfit.setChecked(true);
            }
        });

        binding.chkBoxFederal.setOnClickListener(v -> {

            if (binding.chkBoxFederal.isChecked()){

                binding.chkBoxCommercial.setChecked(false);
                binding.chkBxNonProfit.setChecked(false);
                binding.chkBoxLocal.setChecked(false);
                binding.chkBoxState.setChecked(false);

            } else {

                binding.chkBoxFederal.setChecked(true);
            }
        });

        binding.chkBoxLocal.setOnClickListener(v -> {

            if (binding.chkBoxLocal.isChecked()){

                binding.chkBoxCommercial.setChecked(false);
                binding.chkBxNonProfit.setChecked(false);
                binding.chkBoxFederal.setChecked(false);
                binding.chkBoxState.setChecked(false);

            } else {

                binding.chkBoxLocal.setChecked(true);
            }

        });

        binding.chkBoxState.setOnClickListener(v -> {

            if (binding.chkBoxState.isChecked()){

                binding.chkBoxCommercial.setChecked(false);
                binding.chkBxNonProfit.setChecked(false);
                binding.chkBoxFederal.setChecked(false);
                binding.chkBoxLocal.setChecked(false);

            } else {

                binding.chkBoxState.setChecked(true);
            }
        });

    }

    private void openPageFive(CrmNotes crmNotes){

        ChangeFragments.addFragmentFadeIn(EditAddNotePageFiveFragment.newInstance(crmNotes),getActivity().getSupportFragmentManager(),"PageFive",null);

    }

    private int getType(){

        if (binding.chkBoxCommercial.isChecked()){

            return 0;

        } else if (binding.chkBxNonProfit.isChecked()){

            return 1;

        } else if (binding.chkBoxFederal.isChecked()){

            return 2;

        } else if (binding.chkBoxLocal.isChecked()){

            return 3;

        } else {

            return 4;

        }
    }
}
