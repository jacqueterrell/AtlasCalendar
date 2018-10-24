package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.UserProfileForContactBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class ContactProfileFragment extends BaseFragment<UserProfileForContactBinding, ContactProfileViewModel>
        implements ContactProfileNavigator {


    @Inject
    ContactProfileViewModel viewModel;

    @Inject
    ContactProfileDataModel dataModel;

    private UserProfileForContactBinding binding;
    private DashBoardActivityNavigator parentNavigator;
    private static UserProfile profile;
    private static final String CELL_PHONE = "cellPhone";
    private static final String OFFICE_PHONE = "officePhone";
    private static final String HOME_PHONE = "homePhone";
    private static final String PERSONAL_PHONE = "personalPhone";
    private static int connectionType;
    private String adjustedPhone;
    private String displayPhoneName;


    public static ContactProfileFragment newInstance(UserProfile userProfile) {

        profile = userProfile;
        return new ContactProfileFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.user_profile_for_contact;
    }

    @Override
    public ContactProfileViewModel getViewModel() {
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
        binding.setProfile(profile);

        if (profile == null){

            getBaseActivity().onBackPressed();
        }

        if (!(String.valueOf(profile.getConnectionType()).matches(RegEx.ALLOW_DIGITS_AND_DECIMALS))){

            profile.setConnectionType(3);
        }

        onConnectionTypeSaved();

        if (!profile.getImageUrl().replace(".","").isEmpty()) {

            Glide.with(getBaseActivity())
                    .load(profile.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(binding.ivUserProfile);

            binding.ivDefault.setVisibility(View.GONE);
        }


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setContactsDefaultValues();
    }

    @Override
    public void contactCellPhoneClicked() {

        viewModel.setProfilePhone(profile.getPhone());
        if (isPhonePermissionsGranted()) {

            showPhoneAlert();
        }
    }

    @Override
    public void contactOnOfficePhoneClicked() {

        viewModel.setProfilePhone(profile.getWorkPhone());
        if (isPhonePermissionsGranted()) {

            showPhoneAlert();
        }
    }

    @Override
    public void contactOnHomePhoneClicked() {

        viewModel.setProfilePhone(profile.getHomePhone());
        if (isPhonePermissionsGranted()) {

            showPhoneAlert();
        }
    }

    @Override
    public void contactOnPersonalPhoneClicked() {

        viewModel.setProfilePhone(profile.getPersonalPhone());
        if (isPhonePermissionsGranted()) {

            showPhoneAlert();
        }
    }


    @Override
    public void contactOnFaxClicked() {

        sendToClipBoard(profile.getFax());
    }

    @Override
    public void contactOnPersonalEmailClicked() {

        sendToClipBoard(profile.getEmail());
    }

    @Override
    public void contactOnWorkEmailClicked() {

        sendToClipBoard(profile.getWorkEmail());
    }

    @Override
    public void contactOnHomeAddressClicked() {

        sendToClipBoard(profile.getCityStateZip());
    }

    @Override
    public void contactOnWorkAddressClicked() {

        sendToClipBoard(profile.getWorkCityStateZip());
    }

    @Override
    public void contactOnWorkHistoryClicked() {

        sendToClipBoard(profile.getWorkHistoryString());
    }

    @Override
    public void contactOnEducationClicked() {

        sendToClipBoard(profile.getEducationString());
    }

    private void showPhoneAlert() {

        String profilePhone = viewModel.getProfilePhone();

        if (profilePhone.contains("x")) {

            int index = profilePhone.indexOf("x");
            displayPhoneName = profilePhone.substring(0, index - 1);
            adjustedPhone = profilePhone.substring(0, index - 1).replaceAll(RegEx.REMOVE_NON_DIGITS, "");

        } else {

            adjustedPhone = profilePhone.replaceAll(RegEx.REMOVE_NON_DIGITS, "");
            displayPhoneName = profilePhone;
        }

        if (adjustedPhone.isEmpty()) {

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());

        builder.setMessage(displayPhoneName)
                .setPositiveButton("Call", (dialog, id) -> {

                    Long phone2 = Long.valueOf(adjustedPhone);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone2));//change the number

                    try {

                        startActivity(callIntent);
                        showToastLong("dialing " + displayPhoneName);

                    } catch (Exception e) {
                        Logger.e(e.getMessage());
                    }

                })

                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder.create();
        alert11.show();

    }


    public void onConnectionTypeSaved() {


        if (!profile.getShareNeeds().replace(".","").isEmpty()) {

            setUpShareNeeds();

        } else {

            connectionType = profile.getConnectionType();
            binding.tvPosition.setVisibility(View.VISIBLE);
            binding.tvEmployer.setVisibility(View.VISIBLE);

            if (connectionType == 0 || connectionType == 1) {//family can see everything

                binding.layoutContactCellPhone.setVisibility(View.VISIBLE);
                binding.layoutContactOfficePhone.setVisibility(View.VISIBLE);
                binding.layoutContactHomePhone.setVisibility(View.VISIBLE);
                binding.layoutContactPersonalPhone.setVisibility(View.VISIBLE);
                binding.layoutContactFax.setVisibility(View.VISIBLE);
                binding.layoutContactPersonalEmail.setVisibility(View.VISIBLE);
                binding.layoutContactWorkEmail.setVisibility(View.VISIBLE);
                binding.layoutContactWorkHistory.setVisibility(View.VISIBLE);
                binding.layoutContactEducation.setVisibility(View.VISIBLE);
                binding.layoutContactHomeAddress.setVisibility(View.VISIBLE);
                binding.layoutContactWorkAddress.setVisibility(View.VISIBLE);

            } else if (connectionType == 2) { //connection type is 2(New Acquaintance)

                binding.layoutContactCellPhone.setVisibility(View.VISIBLE);
                binding.layoutContactOfficePhone.setVisibility(View.VISIBLE);
                binding.layoutContactFax.setVisibility(View.VISIBLE);
                binding.layoutContactWorkEmail.setVisibility(View.VISIBLE);
                binding.layoutContactWorkAddress.setVisibility(View.VISIBLE);

                binding.layoutContactWorkHistory.setVisibility(View.GONE);
                binding.layoutContactEducation.setVisibility(View.GONE);
                binding.layoutContactHomePhone.setVisibility(View.GONE);
                binding.layoutContactHomeAddress.setVisibility(View.GONE);
                binding.layoutContactPersonalPhone.setVisibility(View.GONE);
                binding.layoutContactPersonalEmail.setVisibility(View.GONE);

            } else { //connectionType = Business

                binding.layoutContactCellPhone.setVisibility(View.VISIBLE);
                binding.layoutContactOfficePhone.setVisibility(View.VISIBLE);
                binding.layoutContactFax.setVisibility(View.VISIBLE);
                binding.layoutContactWorkEmail.setVisibility(View.VISIBLE);
                binding.layoutContactWorkAddress.setVisibility(View.VISIBLE);
                binding.layoutContactWorkHistory.setVisibility(View.VISIBLE);
                binding.layoutContactEducation.setVisibility(View.GONE);

                binding.layoutContactHomePhone.setVisibility(View.GONE);
                binding.layoutContactHomeAddress.setVisibility(View.GONE);
                binding.layoutContactPersonalPhone.setVisibility(View.GONE);
            }

        }

        binding.layoutContactProfile.setVisibility(View.VISIBLE);
        setContactsDefaultValues();
    }


    private void setUpShareNeeds() {

        List<String> shareNeedsList = Arrays.asList(profile.getShareNeeds().split(","));

        for (String shareNeeds : shareNeedsList) {

            switch (shareNeeds.trim()) {

                case "2":
                    binding.layoutContactPersonalEmail.setVisibility(View.VISIBLE);
                    break;

                case "3":
                    binding.layoutContactWorkEmail.setVisibility(View.VISIBLE);
                    break;

                case "4":
                    binding.layoutContactCellPhone.setVisibility(View.VISIBLE);
                    break;

                case "5":
                    binding.layoutContactOfficePhone.setVisibility(View.VISIBLE);
                    break;

                case "6":
                    binding.layoutContactHomePhone.setVisibility(View.VISIBLE);
                    break;

                case "7":
                    binding.layoutContactPersonalPhone.setVisibility(View.VISIBLE);
                    break;

                case "8":
                    binding.layoutContactFax.setVisibility(View.VISIBLE);
                    break;

                case "9":
                    viewModel.setStreetAddress(true);
                    break;

                case "10":
                    viewModel.setCityStateZip(true);
                    break;

                case "11":
                    binding.layoutContactEducation.setVisibility(View.VISIBLE);
                    break;

                case "12":
                    binding.layoutContactWorkHistory.setVisibility(View.VISIBLE);
                    break;

                case "13":
                    binding.tvEmployer.setVisibility(View.VISIBLE);
                    break;

                case "14":
                    binding.tvPosition.setVisibility(View.VISIBLE);
                    break;

                case "15":
                    viewModel.setWorkStreet(true);
                    break;

                case "16":
                    profile.setWorkCityStateZip(toString());
                    break;

            }



            String street = profile.getStreet();
            String cityState = profile.getCityStateZip();

            if (viewModel.isCityStateZip() && viewModel.isStreetAddress()){

                String address = street + " " + cityState;
                binding.tvHomeAddress.setText(address);
                binding.layoutContactHomeAddress.setVisibility(View.VISIBLE);

            } else if (viewModel.isCityStateZip()){

                binding.tvHomeAddress.setText(cityState);
                binding.layoutContactHomeAddress.setVisibility(View.VISIBLE);

            } else if (viewModel.isStreetAddress()){

                binding.tvHomeAddress.setText(street);
                binding.layoutContactHomeAddress.setVisibility(View.VISIBLE);
            }
        }


        String workStreet = profile.getWorkStreet();
        String workCityStateZip = profile.getWorkCityStateZip();

        if (viewModel.isWorkStreet() && viewModel.isWorkCityStateZip()){

            String address = workStreet + " " + workCityStateZip;
            binding.tvWorkAddress.setText(address);
            binding.layoutContactWorkAddress.setVisibility(View.VISIBLE);

        } else if (viewModel.isWorkCityStateZip()){

            binding.tvWorkAddress.setText(workCityStateZip);
            binding.layoutContactWorkAddress.setVisibility(View.VISIBLE);

        } else if (viewModel.isWorkStreet()){

            binding.tvHomeAddress.setText(workStreet);
            binding.layoutContactWorkAddress.setVisibility(View.VISIBLE);
        }
    }


    public boolean isPhonePermissionsGranted() {

        if (sdk >= marshMallow) {

            if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                        AppConstants.REQUEST_PHONE_PERMISSIONS);

                return false;

            } else {

                return true;
            }

        } else {

            return true;
        }

    }


    private void setContactsDefaultValues() {

        if (binding.tvPosition.getText().toString().trim().equals(",")) {

            binding.tvPosition.setText("");
        }

        //phone info
        if (binding.tvCellPhone.getText().toString().isEmpty()) {

            binding.tvCellPhone.setText("...");
        }

        if (binding.tvOfficePhone.getText().toString().isEmpty()) {

            binding.tvOfficePhone.setText("...");
        }

        if (binding.tvHomePhone.getText().toString().isEmpty()) {

            binding.tvHomePhone.setText("...");
        }

        if (binding.tvFaxPhone.getText().toString().isEmpty()) {

            binding.tvFaxPhone.setText("...");
        }

        if (binding.tvPersonalPhone.getText().toString().isEmpty()) {

            binding.tvPersonalPhone.setText("...");
        }

        //email info
        if (binding.tvWorkEmail.getText().toString().isEmpty()) {

            binding.tvWorkEmail.setText("...");
        }

        if (binding.tvPersonalEmail.getText().toString().isEmpty()) {

            binding.tvPersonalEmail.setText("...");
        }

        //address info
        if (binding.tvHomeAddress.getText().toString().isEmpty()) {

            binding.tvHomeAddress.setText("...");
        }

        if (binding.tvWorkAddress.getText().toString().isEmpty()) {

            binding.tvWorkAddress.setText("...");
        }

        //work history info
        if (binding.tvWorkHistory.getText().toString().isEmpty()) {

            binding.tvWorkHistory.setText("...");
        }

        //education info
        if (binding.tvEducation.getText().toString().isEmpty()) {

            binding.tvEducation.setText("...");
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == AppConstants.REQUEST_PHONE_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            showPhoneAlert();
        }
    }


    /**
     * Copys the text to the clip board
     *
     * @param text
     */
    private void sendToClipBoard(String text) {

        String label = text + " is copied to your clipboard";

        if (!text.isEmpty() && !text.equals("...")) {

            try {

                ClipboardManager clipboard = (ClipboardManager) getBaseActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, text);
                clipboard.setPrimaryClip(clip);
                showToastShort(label);

            } catch (Exception e) {

                Logger.e(e.getMessage());
            }

            try {

                Vibrator v = (Vibrator) getBaseActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);

            } catch (Exception e) {

                Logger.e(e.getMessage());
            }
        }

    }


}
