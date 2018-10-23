package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.EditAddPageOneBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmViewModel;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

public class EditAddNotePageOneFragment extends Fragment implements EditPageOneNavigator {



    private static CrmNotes notes;
    private EditAddPageOneBinding binding;
    private static boolean isNewContact;

    public static EditAddNotePageOneFragment newInstance(CrmNotes crmNotes,boolean newContact){

        notes = crmNotes;
        isNewContact = newContact;
        return new EditAddNotePageOneFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_add_page_one,container,false);

        if (isNewContact && !CrmViewModel.usersContactProfiles.isEmpty()){

            Handler handler = new Handler();
            handler.postDelayed(this::showContactList,800);

        }


        SelectContactAdapter selectContactAdapter = new SelectContactAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(selectContactAdapter);

        binding.btnContinue.setOnClickListener(v -> {

            if (allValuesCompleted()){

                notes.setNoteName(binding.etName.getText().toString());
                notes.setPoc(binding.etPointOfContact.getText().toString());
                notes.setWhereMetCitySt(binding.etWhere.getText().toString());

                ChangeFragments.addFragmentFadeIn(EditAddNotePageTwoFragment.newInstance(notes),getActivity().getSupportFragmentManager(),"PageTwo",null);
            }

        });

        showSoftKeyboard(binding.etName);
        binding.setCrmNote(notes);
        return binding.getRoot();
    }

    @Override
    public void onRowClicked(UserProfile userProfile) {

        hideContactsList();

        String name = userProfile.getFirstName() + " " + userProfile.getLastName();
        binding.etPointOfContact.setText(name);

    }

    @Override
    public boolean isContactsScreenShown() {
        return binding.layoutContactList.getVisibility() == View.VISIBLE;
    }

    @Override
    public void closeContactsScreen() {

        hideContactsList();
    }

    private boolean allValuesCompleted(){

        String opportunityName = binding.etName.getText().toString();
        String primaryContact = binding.etPointOfContact.getText().toString();
        String location = binding.etWhere.getText().toString();

        if (opportunityName.isEmpty()
                || primaryContact.isEmpty()
                || location.isEmpty()){

            Snackbar.make(binding.getRoot(), "Please enter all values to continue", Snackbar.LENGTH_LONG)
                    .show();

            return false;

        } else {

            return true;
        }
    }

    private void showSoftKeyboard(View view) {

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    private void showContactList(){

        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart(animator -> binding.layoutContactList.setVisibility(View.VISIBLE))
                .playOn(binding.layoutContactList);

    }

    private void hideContactsList(){

        YoYo.with(Techniques.SlideOutDown)
                .duration(500)
                .onEnd(animator -> binding.layoutContactList.setVisibility(View.GONE))
                .playOn(binding.layoutContactList);
    }

}
