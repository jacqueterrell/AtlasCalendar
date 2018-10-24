package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.EnterPhoneNumberDialogBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivityNavigator;
import com.team.mamba.atlascalendar.utils.CommonUtils;
import com.team.mamba.atlascalendar.utils.formatData.RegEx;
import javax.inject.Inject;

public class EnterPhoneNumberFragment extends BaseFragment<EnterPhoneNumberDialogBinding,EnterPhoneViewModel>
implements EnterPhoneNavigator{


    @Inject
    EnterPhoneViewModel viewModel;

    @Inject
    EnterPhoneDataModel dataModel;

    private EnterPhoneNumberDialogBinding binding;
    private static String firstName;
    private static String lastName;
    private static long dateOfBirth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private WelcomeActivityNavigator parenNavigator;


    public static EnterPhoneNumberFragment newInstance(long dob,String first, String last){

        dateOfBirth = dob;
        firstName = first;
        lastName = last;
        return new EnterPhoneNumberFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.enter_phone_number_dialog;
    }

    @Override
    public EnterPhoneViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parenNavigator = (WelcomeActivityNavigator) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
        viewModel.setFirstName(firstName);
        viewModel.setLastName(lastName);
        viewModel.setDateOfBirth(dateOfBirth);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = getViewDataBinding();


        Glide.with(getActivity())
                .load(R.drawable.welcome_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(binding.imgViewBackground);

        binding.etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        binding.etPhoneNumber.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });

        showSoftKeyboard(binding.etPhoneNumber);

        setUpCallBacks();

        return binding.getRoot();
    }

    @Override
    public void onPhoneSubmitClicked() {

        submitPhoneNumber();
    }

    @Override
    public OnVerificationStateChangedCallbacks getPhoneCallBacks() {
        return mCallbacks;
    }

    @Override
    public Activity getParentActivity() {
        return getBaseActivity();
    }

    @Override
    public void handleError(String msg) {

        showSnackbar(msg);
        hideProgressSpinner();
    }


    @Override
    public void onEnterSmsCancelClicked() {

        hideEnterSMSCode();
        hideProgressSpinner();
    }

    @Override
    public void onEnterSmsContinueClicked() {

        String code = binding.etSmsCode.getText().toString();

        if (code.isEmpty()){

            showSnackbar("Your sms code is empty");

        } else {

            String verificationId = viewModel.getVerificationId();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            viewModel.setPhoneAuthCredential(credential);
            viewModel.signInWithPhoneAuthCredential(getViewModel());

            showProgressSpinner();
        }

    }


    private void showEnterSMSCode(){

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .repeat(0)
                .onStart(animator -> binding.layoutEnterSmsCode.setVisibility(View.VISIBLE))
                .playOn(binding.layoutEnterSmsCode);

        binding.etSmsCode.setText("");

    }

    private void hideEnterSMSCode(){

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .repeat(0)
                .onEnd(animator -> binding.layoutEnterSmsCode.setVisibility(View.GONE))
                .playOn(binding.layoutEnterSmsCode);
    }


    /**
     * Opens the soft keyboard with a focus on the passed in view
     * @param view
     */
    private void showSoftKeyboard(View view) {

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }


    private void submitPhoneNumber(){

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String phoneNumber = binding.etPhoneNumber.getText().toString().replaceAll(RegEx.REMOVE_NON_DIGITS, "");

        if (CommonUtils.isPhoneValid(phoneNumber)) {

            try {

                Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(phoneNumber, "US");
                String pnE164 = phoneNumberUtil.format(pn,PhoneNumberUtil.PhoneNumberFormat.E164);
                String international = phoneNumberUtil.format(pn, PhoneNumberFormat.INTERNATIONAL);
                String national = phoneNumberUtil.format(pn, PhoneNumberFormat.NATIONAL);

                viewModel.setPhoneNumber(national);
                showPhoneNumberAlert(pnE164);

            } catch (NumberParseException e) {
                e.printStackTrace();
            }

        }
    }




    /**
     * Shows an alert to validate the phone number
     *
     * @param phoneNumber the entered phone number
     */
    private void showPhoneNumberAlert(String phoneNumber) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(R.string.phone_number_warning_title)
                .setMessage(R.string.phone_number_warning_body)
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("Continue", (paramDialogInterface, paramInt) -> {

                    viewModel.fireBaseVerifyPhoneNumber(getViewModel(),phoneNumber);
                    showProgressSpinner();

                });

        dialog.show();
    }

    private void setUpCallBacks() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Logger.d("onVerificationCompleted: " + phoneAuthCredential);
                hideProgressSpinner();
                viewModel.setPhoneAuthCredential(phoneAuthCredential);
                viewModel.signInWithPhoneAuthCredential(getViewModel());
                showToastShort("Auto-verified");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Logger.e(e.getLocalizedMessage());
                hideProgressSpinner();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // the phone number format is not valid
                    // ...
                    showSnackbar("invalid credentials");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    showSnackbar("sms quota filled");

                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                viewModel.setVerificationId(verificationId);
                viewModel.setForceResendingToken(token);

                showEnterSMSCode();
                hideProgressSpinner();

                Logger.i("onCodeSent: " + verificationId);
            }
        };
    }


    @Override
    public void openDashBoard() {

        hideProgressSpinner();

        if (parenNavigator.isBusinessLogin()){

            dataManager.getSharedPrefs().setBusinessAccount(true);
            getBaseActivity().finishAffinity();
            startActivity(DashBoardActivity.newIntent(getBaseActivity()));

        } else {

            dataManager.getSharedPrefs().setBusinessAccount(false);
            getBaseActivity().finishAffinity();
            startActivity(DashBoardActivity.newIntent(getBaseActivity()));

        }

    }

    @Override
    public void onPhoneSubmitPreviousClicked() {

        getBaseActivity().onBackPressed();
    }


}
