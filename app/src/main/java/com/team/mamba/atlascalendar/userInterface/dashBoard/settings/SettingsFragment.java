package com.team.mamba.atlascalendar.userInterface.dashBoard.settings;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.SettingsLayoutBinding;
import com.team.mamba.atlascalendar.service.CurrentLocationService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.settings.network_management.NetworkManagementFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.ViewPagerFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.io.InputStream;
import javax.inject.Inject;

public class SettingsFragment extends BaseFragment<SettingsLayoutBinding,SettingsViewModel> implements SettingsNavigator {


    @Inject
    SettingsViewModel viewModel;

    @Inject
    SettingsDataModel dataModel;

    private SettingsLayoutBinding binding;

    public static SettingsFragment newInstance(){

        return new SettingsFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;

    }

    @Override
    public int getLayoutId() {
        return R.layout.settings_layout;
    }

    @Override
    public SettingsViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
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
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();

        String version = "Version " + BuildConfig.VERSION_NAME;
        binding.tvVersionName.setText(version);

        return binding.getRoot();
    }

    @Override
    public void onSiteLinkClicked() {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.ATLAS_SITE_URL));
        startActivity(i);
    }


    @Override
    public void onCorporateDirectoryClicked() {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.ATLAS_BUSINESS_URL));
        startActivity(i);
    }

    @Override
    public void onOrganizationalOutreachClicked() {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.ATLAS_BUSINESS_URL));
        startActivity(i);
    }

    @Override
    public void onAlumniNetworkingClicked() {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.ATLAS_BUSINESS_URL));
        startActivity(i);
    }


    @Override
    public void onPrivacyPolicyClicked() {

        showTermsOfService();

        try {

            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.privacy);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            binding.tvTerms.setText(new String(b));

        } catch (Exception e) {
            binding.tvTerms.setText("Error: can't show terms.");
        }
    }

    @Override
    public void onTermsOfServiceClicked() {

        showTermsOfService();

        try {

            Resources res = getResources();
            InputStream in_s = res.openRawResource(R.raw.terms);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            binding.tvTerms.setText(new String(b));

        } catch (Exception e) {
            binding.tvTerms.setText("Error: can't show terms.");
        }
    }

    @Override
    public void onNetworkManagementClicked() {

        ChangeFragments.addFragmentVertically(NetworkManagementFragment.newInstance(), getBaseActivity().getSupportFragmentManager(), "NetworkManagement", null);

    }

    @Override
    public void onLogOutClicked() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle("Log Out")
                .setMessage("Do you want to log out of this account?")
                .setNegativeButton("no", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("yes", (paramDialogInterface, paramInt) -> {

                    dataManager.getSharedPrefs().setUserLoggedIn(false);
                    showToastShort("Logging out");
                    resetApplication();

                    Intent intent = new Intent(getBaseActivity(),CurrentLocationService.class);
                    getBaseActivity().stopService(intent);
                })
                .show();

    }


    @Override
    public void onDeleteMyAccountClicked() {

        String title = "Delete Your Atlas Account?";
        String msg = "If you would like to delete your Atlas account, please confirm, and login to complete the process.";

        final AlertDialog.Builder dialog = new Builder(getBaseActivity());

        dialog.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("Cancel",(paramDialogInterface, paramInt) ->{

                })
                .setPositiveButton("Confirm",(paramDialogInterface, paramInt) ->{

                    viewModel.deleteUser();
                    showToastShort("Account deleted");
                    dataManager.getSharedPrefs().setUserLoggedIn(false);
                    resetApplication();
                })
                .show();
    }


    private void showTermsOfService() {

        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart(animator -> binding.layoutTermsOfService.setVisibility(View.VISIBLE))
                .playOn(binding.layoutTermsOfService);

    }


    /**
     * Removes all Activities from the back stack and opens up
     * {@link ViewPagerFragment}
     */
    private void resetApplication(){

        getBaseActivity().finishAffinity();
        startActivity(WelcomeActivity.newIntent(getBaseActivity()));
    }
}
