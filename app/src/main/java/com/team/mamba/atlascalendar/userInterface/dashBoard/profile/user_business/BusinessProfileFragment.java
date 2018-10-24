package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_business;

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
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.databinding.BusinessProfileLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;
import javax.inject.Inject;

public class BusinessProfileFragment extends BaseFragment<BusinessProfileLayoutBinding, BusinessProfileViewModel>
implements BusinessProfileNavigator {


    @Inject
    BusinessProfileViewModel viewModel;

    @Inject
    BusinessProfileDataModel dataModel;

    private BusinessProfileLayoutBinding binding;
    private static BusinessProfile profile;


    public static BusinessProfileFragment newInstance(BusinessProfile businessProfile){

        profile = businessProfile;
        return new BusinessProfileFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.business_profile_layout;
    }

    @Override
    public BusinessProfileViewModel getViewModel() {
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

        if (profile.getId().equals(dataManager.getSharedPrefs().getUserId())){//the logged in account is the business

            binding.contactProfile.layoutContactsProfile.setVisibility(View.GONE);
            binding.setProfile(profile);

            if (!profile.getImageUrl().replace(".","").isEmpty()) {

                Glide.with(getBaseActivity())
                        .load(profile.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(binding.ivUserProfile);

                binding.ivDefault.setVisibility(View.GONE);
            }


        } else {

            binding.contactProfile.layoutContactsProfile.setVisibility(View.VISIBLE);
            binding.contactProfile.setProfile(profile);

            if (!profile.getImageUrl().replace(".","").isEmpty()) {

                Glide.with(getBaseActivity())
                        .load(profile.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(binding.contactProfile.ivUserProfile);

            }

        }
         return binding.getRoot();
    }

    @Override
    public void callBusinessContact() {

        if (isPhonePermissionsGranted()) {

            String phone = profile.getPhone().replaceAll(RegEx.REMOVE_NON_DIGITS,"");
            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());

            builder.setMessage(profile.getPhone())
                    .setPositiveButton("Call", (dialog, id) -> {

                        Long phone2 = Long.valueOf(phone);
                        callPhone(phone2);
                    })

                    .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

            AlertDialog alert11 = builder.create();
            alert11.show();

        }

    }

    private void callPhone(Long phone){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));//change the number

            try {

                startActivity(callIntent);
                showToastLong("dialing " + profile.getPhone());

            } catch (Exception e) {
                Logger.e(e.getMessage());
            }

    }

    @Override
    public void contactOnNameClicked() {

        sendToClipBoard(profile.getName());
    }

    @Override
    public void contactOnEmailClicked() {

        sendToClipBoard(profile.getEmail());
    }

    @Override
    public void contactOnFaxClicked() {

        sendToClipBoard(profile.getFax());
    }

    @Override
    public void contactOnAddressClicked() {

        sendToClipBoard(profile.getCityStateZip());
    }

    @Override
    public void contactOnCodeClicked() {

        sendToClipBoard(profile.getCode());
    }

    @Override
    public void contactOnContactNameClicked() {

        sendToClipBoard(profile.getContactName());
    }

    @Override
    public void onProfileImageClicked() {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppConstants.REQUEST_PHONE_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            callBusinessContact();
        }
    }


    /**
     * Copys the text to the clip board
     * @param text
     */
    private void sendToClipBoard(String text){

        String label = text + " is copied to your clipboard";

        if (!text.isEmpty() && !text.equals("...")){

            try {

                ClipboardManager clipboard = (ClipboardManager) getBaseActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(label, text);
                clipboard.setPrimaryClip(clip);
                showToastShort(label);

            } catch (Exception e){

                Logger.e(e.getMessage());
            }

            try{

                Vibrator v = (Vibrator) getBaseActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(400);

            } catch (Exception e){

                Logger.e(e.getMessage());
            }
        }

    }
}
