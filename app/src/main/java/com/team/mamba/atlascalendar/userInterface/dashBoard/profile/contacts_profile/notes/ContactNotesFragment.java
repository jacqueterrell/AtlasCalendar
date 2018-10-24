package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.PersonalNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.ContactNotesBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note.HowDidYouMeetFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class ContactNotesFragment extends BaseFragment<ContactNotesBinding,ContactNotesViewModel>
implements ContactNotesNavigator{


    @Inject
    ContactNotesViewModel viewModel;

    @Inject
    ContactNotesDataModel dataModel;

    private static UserProfile profile;
    private ContactNotesBinding binding;


    public static ContactNotesFragment newInstance(UserProfile userProfile){

        profile = userProfile;
        return new ContactNotesFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.contact_notes;
    }

    @Override
    public ContactNotesViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = getViewDataBinding();

        viewModel.getConnectionType(getViewModel(),profile);

        if (profile.getImageUrl() != null){

            Glide.with(getBaseActivity())
                    .load(profile.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(binding.ivUserProfile);
        }

        return binding.getRoot();
    }

    @Override
    public void onUserNotesReturned() {

        binding.tvConnectionDate.setText(getConnectionDate());
        binding.tvConnectionType.setText(getConnectionTypeToString());

        if (viewModel.getPersonalNotes() != null){

            binding.setPersonalNotes(viewModel.getPersonalNotes());
        }

        String name = profile.getFirstName() + " " + profile.getLastName();
        binding.tvName.setText(name);
    }

    @Override
    public void onEditDetailsClicked() {

        //open how did you meet fragment
        PersonalNotes notes = viewModel.getPersonalNotes();
        ChangeFragments.addFragmentFadeIn(HowDidYouMeetFragment.newInstance(notes), getBaseActivity().getSupportFragmentManager(), "HowDidYouMeet", null);

    }

    @Override
    public void onConnectionInfoClicked() {

        ChangeFragments.addFragmentFadeIn(new ContactInfoDialog(), getBaseActivity().getSupportFragmentManager(), "ContactInfoDialog", null);

    }

    @Override
    public void onNotesInfoClicked() {

        ChangeFragments.addFragmentFadeIn(new PersonalNoteDialog(), getBaseActivity().getSupportFragmentManager(), "ContactInfoDialog", null);

    }

    public String getConnectionTypeToString(){

        int connectionType = viewModel.getYourConnectionType();
        if (connectionType == 0){

            return "Family Member";

        } else if (connectionType == 1){

            return "Personal Friend";

        } else if (connectionType == 2){

            return "New Acquaintance";

        } else if (connectionType == 3){

            return "Business Contact";

        } else if (connectionType == 4){

            return "Colleague";

        } else {

            return "Client";
        }

    }


    private String getConnectionDate(){

        long timeStamp = viewModel.getTimestamp();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);

        int month = calendar.get(Calendar.MONTH);
        String monthName = getMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);


        if (String.valueOf(timeStamp).matches(RegEx.ALLOW_DIGITS_AND_DECIMALS)){

            return monthName + " " + day + " " + year;

        } else {

            return "";
        }

    }

    private static String getMonth(int index) {

        List<String> monthsList = new ArrayList<>();

        monthsList.add("Jan");
        monthsList.add("Feb");
        monthsList.add("Mar");
        monthsList.add("Apr");
        monthsList.add("May");
        monthsList.add("Jun");
        monthsList.add("Jul");
        monthsList.add("Aug");
        monthsList.add("Sep");
        monthsList.add("Oct");
        monthsList.add("Nov");
        monthsList.add("Dec");

        return monthsList.get(index);
    }
}
