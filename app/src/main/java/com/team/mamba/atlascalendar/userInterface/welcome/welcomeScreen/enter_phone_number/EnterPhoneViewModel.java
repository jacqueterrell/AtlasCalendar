package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen.enter_phone_number;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

public class EnterPhoneViewModel extends BaseViewModel<EnterPhoneNavigator> {

    private EnterPhoneDataModel dataModel;
    private String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private String verificationId;
    private PhoneAuthCredential phoneAuthCredential;

    private String firstName;
    private String lastName;
    private String userCode;
    private long dateOfBirth;




    /***************Getters and Setters****************/


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDataModel(
            EnterPhoneDataModel dataModel) {
        this.dataModel = dataModel;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setForceResendingToken(PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        this.forceResendingToken = forceResendingToken;
    }

    public PhoneAuthProvider.ForceResendingToken getForceResendingToken() {
        return forceResendingToken;
    }

    public void setPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        this.phoneAuthCredential = phoneAuthCredential;
    }

    public PhoneAuthCredential getPhoneAuthCredential() {
        return phoneAuthCredential;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /***************OnClick Listeners********************/

    public void onPhoneSubmitPreviousClicked(){

        getNavigator().onPhoneSubmitPreviousClicked();
    }

    public void onEnterSmsCancelClicked(){

        getNavigator().onEnterSmsCancelClicked();
    }

    public void onPhoneSubmitClicked(){

        getNavigator().onPhoneSubmitClicked();
    }

    public void onEnterSmsContinueClicked(){

        getNavigator().onEnterSmsContinueClicked();
    }

    /***************DataModel Requests***************/

    public void fireBaseVerifyPhoneNumber(EnterPhoneViewModel viewModel,String phoneNumber){

        dataModel.fireBaseVerifyPhoneNumber(viewModel,phoneNumber);
    }

    public void signInWithPhoneAuthCredential(EnterPhoneViewModel viewModel){

        dataModel.signInWithPhoneAuthCredential(viewModel);
    }
    }
