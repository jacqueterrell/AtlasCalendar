package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_phone_info;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.EditPhoneLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_email_info.EditEmailFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import javax.inject.Inject;

public class EditPhoneFragment extends BaseFragment<EditPhoneLayoutBinding, EditPhoneViewModel>
        implements EditPhoneNavigator {

    @Inject
    EditPhoneDataModel dataModel;

    @Inject
    EditPhoneViewModel viewModel;

    private EditPhoneLayoutBinding binding;
    private static UserProfile profile;

    public static EditPhoneFragment newInstance(UserProfile userProfile) {

        profile = userProfile;
        return new EditPhoneFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_phone_layout;
    }

    @Override
    public EditPhoneViewModel getViewModel() {
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getViewDataBinding();

        setDataValues();
        setListeners();

        return binding.getRoot();
    }

    @Override
    public void onContinueClicked() {

        setPhoneData();

        ChangeFragments.addFragmentFadeIn(EditEmailFragment.newInstance(profile), getBaseActivity()
                .getSupportFragmentManager(), "EmailFragment", null);
    }

    @Override
    public void onSaveAndCloseClicked() {

        setPhoneData();
        showProgressSpinner();
        viewModel.updateUser(getViewModel(), profile);

    }

    private void setPhoneData() {

        String homePhone = getParsedNumber(
                binding.etHomeNumber.getText().toString().replaceAll(RegEx.REMOVE_NON_DIGITS, ""));
        String homeExt = binding.etHomeExt.getText().toString();

        String officePhone = getParsedNumber(
                binding.etOfficeNumber.getText().toString().replaceAll(RegEx.REMOVE_NON_DIGITS, ""));
        String officeExt = binding.etOfficeExt.getText().toString();

        String personalPhone = getParsedNumber(
                binding.etPersonalNumber.getText().toString().replaceAll(RegEx.REMOVE_NON_DIGITS, ""));
        String personalExt = binding.etPersonalExt.getText().toString();

        String faxPhone = getParsedNumber(
                binding.etFaxNumber.getText().toString().replaceAll(RegEx.REMOVE_NON_DIGITS, ""));
        String faxExt = binding.etFaxExt.getText().toString();

        if (homeExt.isEmpty()) {

            profile.setHomePhone(homePhone);

        } else {

            profile.setHomePhone(homePhone + " x" + homeExt);
        }

        if (officeExt.isEmpty()) {

            profile.setWorkPhone(officePhone);

        } else {

            profile.setWorkPhone(officePhone + " x" + officeExt);
        }

        if (personalExt.isEmpty()) {

            profile.setPersonalPhone(personalPhone);

        } else {

            profile.setPersonalPhone(personalPhone + " x" + personalExt);
        }

        if (faxExt.isEmpty()) {

            profile.setFax(faxPhone);

        } else {

            profile.setFax(faxPhone + " x" + faxExt);
        }

        Long timeStamp = System.currentTimeMillis() / 1000;

        profile.setTimestamp(timeStamp);

    }

    @Override
    public void onProfileUpdated() {

        showToastShort("Profile Updated");
        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        manager.popBackStack("UserProfile",0);
    }

    private String getParsedNumber(String phone) {

        String adjustedPhone = "";
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        try {

            Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(phone, "US");
            String national = phoneNumberUtil.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

            adjustedPhone = national;

        } catch (Exception e) {

            Logger.e(e.getMessage());
        }

        return adjustedPhone;
    }

    private void setListeners() {

        binding.etPersonalNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.etOfficeNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.etHomeNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.etFaxNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

    }

    private void setDataValues() {

        try {

            int length = profile.getWorkPhone().length();

            int index = profile.getWorkPhone().indexOf("x");

            String officeNumber = profile.getWorkPhone().substring(0, index - 1);
            binding.etOfficeNumber.setText(officeNumber);
            binding.etOfficeExt.setText(profile.getHomePhone().substring(index + 1, length));

        } catch (Exception e) {

            binding.etOfficeNumber.setText(profile.getWorkPhone());
        }

        try {

            int length = profile.getHomePhone().length();

            int index = profile.getHomePhone().indexOf("x");

            String homeNumber = profile.getHomePhone().substring(0, index - 1);
            binding.etHomeNumber.setText(homeNumber);
            binding.etHomeExt.setText(profile.getHomePhone().substring(index + 1, length));

        } catch (Exception e) {

            binding.etHomeNumber.setText(profile.getHomePhone());
        }

        try {

            int length = profile.getPersonalPhone().length();

            int index = profile.getPersonalPhone().indexOf("x");

            String personalNumber = profile.getPersonalPhone().substring(0, index - 1);
            binding.etPersonalNumber.setText(personalNumber);
            binding.etPersonalExt.setText(profile.getPersonalPhone().substring(index + 1, length));

        } catch (Exception e) {

            binding.etPersonalNumber.setText(profile.getPersonalPhone());
        }

        try {

            int length = profile.getFax().length();
            int index = profile.getFax().indexOf("x");

            String faxNumber = profile.getFax().substring(0, index - 1);
            binding.etFaxNumber.setText(faxNumber);
            binding.etFaxExt.setText(profile.getFax().substring(index + 1, length));

        } catch (Exception e) {

            binding.etFaxNumber.setText(profile.getFax());
        }

    }
}
