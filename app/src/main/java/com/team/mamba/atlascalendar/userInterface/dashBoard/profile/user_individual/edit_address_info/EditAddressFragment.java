package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_address_info;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.EditAddressLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.EditEducationFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import javax.inject.Inject;

public class EditAddressFragment extends BaseFragment<EditAddressLayoutBinding,EditAddressViewModel> implements EditAddressNavigator {

    @Inject
    EditAddressViewModel viewModel;

    @Inject
    EditAddressDataModel dataModel;

    private EditAddressLayoutBinding binding;
    private static UserProfile profile;
    private DashBoardActivityNavigator parentNavigator;

    public static EditAddressFragment newInstance(UserProfile userProfile){

        profile = userProfile;
        return new EditAddressFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_address_layout;
    }

    @Override
    public EditAddressViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (DashBoardActivityNavigator) context;
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

        if (!profile.getCityStateZip().trim().equals("...")){binding.etCityStateZip.setText(profile.getCityStateZip());}
        if (!profile.getStreet().trim().equals("...")){binding.etStreetAddress.setText(profile.getStreet());}
        if (!profile.getWorkCityStateZip().trim().equals("...")){binding.etWorkCityStateZip.setText(profile.getWorkCityStateZip());}
        if (!profile.getWorkStreet().trim().equals("...")){binding.etWorkStreetAddress.setText(profile.getWorkStreet());}

         return binding.getRoot();
    }

    @Override
    public void onSaveAndCloseClicked() {

        String street = binding.etStreetAddress.getText().toString();
        String cityStateZip = binding.etCityStateZip.getText().toString();
        String workStreet = binding.etWorkStreetAddress.getText().toString();
        String workCityStateZip = binding.etWorkCityStateZip.getText().toString();
        Long timeStamp = System.currentTimeMillis() / 1000;


        profile.setStreet(street);
        profile.setCityStateZip(cityStateZip);
        profile.setWorkStreet(workStreet);
        profile.setWorkCityStateZip(workCityStateZip);
        profile.setTimestamp(timeStamp);

        viewModel.updateUser(getViewModel(),profile);
    }

    @Override
    public void onEducationClicked() {

        ChangeFragments.addFragmentFadeIn(EditEducationFragment.newInstance(profile),getBaseActivity()
                .getSupportFragmentManager(),"EducationFragment",null);
    }

    @Override
    public void onProfileUpdated() {

        showToastShort("Profile Updated");
        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        manager.popBackStack("UserProfile",0);
    }
}
