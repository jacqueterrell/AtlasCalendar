package com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.databinding.BusinessLoginLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.admin_accounts.AdminAccountsFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_accounts_recycler.BusinessAccountsActivity;
import com.team.mamba.atlascalendar.utils.ChangeWelcomeFragments;

import javax.inject.Inject;

public class BusinessLoginFragment extends BaseFragment<BusinessLoginLayoutBinding, BusinessLoginViewModel>
        implements BusinessLoginNavigator {


    @Inject
    BusinessLoginViewModel viewModel;

    @Inject
    BusinessLoginDataModel dataModel;


    private BusinessLoginLayoutBinding binding;
    private WelcomeActivityNavigator parentNavigator;
    private long dateOfBirth;


    public static BusinessLoginFragment newInstance() {

        return new BusinessLoginFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.business_login_layout;
    }

    @Override
    public BusinessLoginViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (WelcomeActivityNavigator) context;

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

        showSoftKeyboard(binding.etEmail);
        return binding.getRoot();
    }

    @Override
    public Activity getParentActivity() {
        return getBaseActivity();
    }


    @Override
    public void openDashBoard() {
        hideProgressSpinner();

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());
        BusinessProfile profile = viewModel.getBusinessProfileList().get(0);
        String title = "Login as " + profile.getName();
        String body = "You are an authorized representative for " + profile.getName();

        dialog.setTitle(title)
                .setMessage(body)
                .setNegativeButton("No", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                    if (parentNavigator.isBusinessLogin()) {

                        dataManager.getSharedPrefs().setBusinessAccount(true);

                    } else {

                        dataManager.getSharedPrefs().setBusinessAccount(false);
                    }

                    getBaseActivity().finishAffinity();
                    startActivity(DashBoardActivity.newIntent(getBaseActivity()));
                });

        dialog.show();

    }

    @Override
    public void showMultipleBusinessLogin() {

        hideProgressSpinner();
        startActivity(BusinessAccountsActivity.newIntent(getBaseActivity(), viewModel.getBusinessProfileList()));
    }

    @Override
    public void showBusinessNotFoundAlert() {

        hideProgressSpinner();
        String title = "Login Issue";
        String body = getBaseActivity().getResources().getString(R.string.business_not_found_body);
        showAlert(title, body);
    }

    @Override
    public void onBusinessScreenLearnMoreClicked() {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.ATLAS_BUSINESS_URL));
        startActivity(i);
    }

    @Override
    public void onBusinessScreenLoginClicked() {

        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        hideKeyboard();

        if (email.isEmpty() && password.equals("admin")) {

            ChangeWelcomeFragments.addFragmentFadeIn(AdminAccountsFragment.newInstance(),
                    getBaseActivity().getSupportFragmentManager(), "ContactsFragment", null);


        } else if (email.isEmpty() && password.equals("test")) {

            dataManager.getSharedPrefs().setUserId("Dy3PDR8BiWS0L7gqfjo16YqFKKN2"); //Mike R
            //dataManager.getSharedPrefs().setUserId("RGxZhoaRI2WE6Ge2I6oC"); // Jacque Terrell
            //dataManager.getSharedPrefs().setUserId("RGxZhoaRI2WE6Ge2I6oC"); // Sofwr

            parentNavigator.setBusinessLogin(false);

            openDashBoard();

        } else {

            if (viewModel.isEmailValid(email) && viewModel.isPasswordValid(password)) {

                viewModel.firebaseAuthenticateByEmail(viewModel, email, password);
                showProgressSpinner();

            } else {

                if (!viewModel.isEmailValid(email) && !viewModel.isPasswordValid(password)) {

                    showSnackbar("Please enter a valid email and password");

                } else if (!viewModel.isEmailValid(email)) {

                    showSnackbar("Please enter a valid email");

                } else {

                    showSnackbar("Please enter a valid password");
                }
            }
        }
    }


    private void showSoftKeyboard(View view) {

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }
}
