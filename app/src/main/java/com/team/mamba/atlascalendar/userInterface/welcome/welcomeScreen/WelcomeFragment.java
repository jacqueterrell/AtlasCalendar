package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.WelcomeScreenLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.welcome.select_business_account.business_login.BusinessLoginFragment;
import com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_first_name.EnterFirstNameFragment;
import com.team.mamba.atlascalendar.utils.ChangeWelcomeFragments;
import javax.inject.Inject;


public class WelcomeFragment extends BaseFragment<WelcomeScreenLayoutBinding, WelcomeViewModel> implements WelcomeNavigator {

    @Inject
    WelcomeViewModel viewModel;

    @Inject
    WelcomeDataModel dataModel;

    private WelcomeScreenLayoutBinding binding;
    private static final int DEFAULT_TIME_IN_MILLI_SECS = 500;
    private WelcomeActivityNavigator parentNavigator;

    public static WelcomeFragment newInstance() {

        return new WelcomeFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.welcome_screen_layout;
    }

    @Override
    public WelcomeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onStartButtonClicked() {

        parentNavigator.setBusinessLogin(false);

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> binding.dialogVerifyAge.layoutVerifyAge.setVisibility(View.VISIBLE))
                .playOn(binding.dialogVerifyAge.layoutVerifyAge);

    }

    @Override
    public void onBusinessLoginClicked() {

        parentNavigator.setBusinessLogin(true);

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> binding.dialogVerifyAge.layoutVerifyAge.setVisibility(View.VISIBLE))
                .playOn(binding.dialogVerifyAge.layoutVerifyAge);
    }

    @Override
    public void onDateVerifyClicked() {

        validateAge();
    }

    @Override
    public void onDateCancelClicked() {

        YoYo.with(Techniques.FadeOut)
                .duration(DEFAULT_TIME_IN_MILLI_SECS)
                .onEnd(animator -> binding.dialogVerifyAge.layoutVerifyAge.setVisibility(View.GONE))
                .playOn(binding.dialogVerifyAge.layoutVerifyAge);
    }


    @Override
    public void onBackPressed() {

         if (binding.dialogVerifyAge.layoutVerifyAge.getVisibility() == View.VISIBLE) {

            onDateCancelClicked();

        } else {

            getBaseActivity().onBackPressed();
        }
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

        Glide.with(getBaseActivity())
                .load(R.drawable.welcome_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.imgViewBackground);


        //Todo reserved for when the adding a business feature is enabled
//        String email = "jacqueterrell@yahoo.com";
//        String password = "123456";
//
//
//                mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(viewModel.getNavigator().getParentActivity(), task -> {
//
//                    if (task.isSuccessful()){
//
//                        Logger.e("Email Auth successfull");
//
//                    } else {
//
//                        Logger.e("Error creating authentication");
//                    }
//                });

        return binding.getRoot();
    }



    private void validateAge() {

        int month = (binding.dialogVerifyAge.datePicker.getMonth());
        int day = binding.dialogVerifyAge.datePicker.getDayOfMonth();
        int year = binding.dialogVerifyAge.datePicker.getYear();


        if (viewModel.isAgeValid(month, day, year)) {

            onDateCancelClicked();

            if (parentNavigator.isBusinessLogin()){

                ChangeWelcomeFragments.addFragmentFadeIn(BusinessLoginFragment.newInstance(), getBaseActivity().getSupportFragmentManager(), "ContactsFragment", null);

            } else {

                ChangeWelcomeFragments.addFragmentFadeIn(EnterFirstNameFragment.newInstance(viewModel.getDateOfBirth()), getBaseActivity().getSupportFragmentManager(), "ContactsFragment", null);
            }

        } else {

            showSnackbar("You must be 13 years or older");
        }

    }

}
