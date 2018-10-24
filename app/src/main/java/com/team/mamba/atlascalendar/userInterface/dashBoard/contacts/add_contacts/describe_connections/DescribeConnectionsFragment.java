package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.databinding.DescribeConnectionBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;

import javax.inject.Inject;

public class DescribeConnectionsFragment extends BaseFragment<DescribeConnectionBinding,DescribeConnectionsViewModel>
        implements DescribeConnectionsNavigator {

    @Inject
    DescribeConnectionsViewModel viewModel;

    @Inject
    DescribeConnectionsDataModel dataModel;

    private DescribeConnectionBinding binding;
    private static String lastName;
    private static String userCode;
    private DashBoardActivityNavigator parentNavigator;
    private static boolean isApprovingConnection = false;
    private static boolean isUpdatingConnection = false;
    private static UserConnections userConnections;

    public static DescribeConnectionsFragment newInstance(String lastname,String code){

        isApprovingConnection = false;
        isUpdatingConnection = false;
        lastName = lastname;
        userCode = code;
        return new DescribeConnectionsFragment();
    }

    public static DescribeConnectionsFragment newInstance(UserConnections connections,boolean isApproving,boolean isUpdating){

        lastName = connections.getUserProfile().getLastName();
        userCode = connections.getUserProfile().getCode();
        userConnections = connections;
        isApprovingConnection = isApproving;
        isUpdatingConnection = isUpdating;
        return new DescribeConnectionsFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.describe_connection;
    }

    @Override
    public DescribeConnectionsViewModel getViewModel() {
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
        viewModel.setApprovingConnection(isApprovingConnection);
        viewModel.setRequestingConnection(userConnections);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();

        setUpListeners();

        if (isApprovingConnection || isUpdatingConnection){

             setCachedConnectionType();
             binding.tvTitle.setText("Edit Connection");
         }

        return binding.getRoot();
    }

    @Override
    public void onFinishButtonClicked() {

        if (isUpdatingConnection){

            showUpdateConnectionAlert();

        } else {

            viewModel.addUserRequest(getViewModel(),lastName,userCode,getConnectionType());
        }
    }

    @Override
    public void onInfoClicked() {

        showConnectionsInfo();
    }

    private void showUpdateConnectionAlert(){

        String name = "";

        if (userConnections.isOrgBus){

            name = userConnections.getBusinessProfile().getName();

        } else {

            String first = userConnections.getUserProfile().getFirstName();
            String last = userConnections.getUserProfile().getLastName();
            name =  first + " " + last;
        }

        userConnections.setConnectionType(getConnectionType());
        String title = "Edit Connection Type?";
        String body = "Do you want to change your connection with " + name + " to " + userConnections.getConnectionTypeToString();

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setCancelable(false)
                .setMessage(body)
                .setNegativeButton("Cancel",(paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                    userConnections.setConnectionType(getConnectionType());
                    viewModel.updateConnection(getViewModel(),userConnections);
                    showToastShort("Contact details updated");

                });

        dialog.show();
    }

    @Override
    public void onConnectionRequestApproved() {

        parentNavigator.setContactRecentlyAdded(true);
        parentNavigator.refreshInfoFragment();
    }

    @Override
    public void onConnectionUpdated() {

        String title = "Connection Type Edited";
        String body = "The connection type has been successfully changed.";

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setCancelable(false)
                .setMessage(body)
                .setPositiveButton("Ok", (paramDialogInterface, paramInt) -> {

                    parentNavigator.resetToFirstFragment();

                });

        dialog.show();

    }

    @Override
    public void hideConnectionsInfo() {

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> binding.dialogConnectionsInfo.setVisibility(View.GONE))
                .playOn(binding.dialogConnectionsInfo);
    }

    public void showConnectionsInfo(){

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> binding.dialogConnectionsInfo.setVisibility(View.VISIBLE))
                .playOn(binding.dialogConnectionsInfo);
    }


    @Override
    public void onRequestSent() {

        String title = getBaseActivity().getResources().getString(R.string.connection_sent_title);
        String msg = getBaseActivity().getResources().getString(R.string.connection_sent_body);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton("Ok", (paramDialogInterface, paramInt) -> {

                    parentNavigator.resetToFirstFragment();
                });

        dialog.show();

    }

    @Override
    public void showUserNotFoundAlert() {

        String title = "User Not Found";
        String msg = "No user found with a last name of " + lastName + " and a code of " + userCode;
        showAlert(title,msg);
    }


    @Override
    public void showAlreadyAContactAlert() {

        String title = getBaseActivity().getResources().getString(R.string.already_connected_title);
        String body = getBaseActivity().getResources().getString(R.string.already_connected_body);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setCancelable(false)
                .setMessage(body)
                .setPositiveButton("Ok", (paramDialogInterface, paramInt) -> {

                   parentNavigator.resetToFirstFragment();

                });

        dialog.show();
    }

    @Override
    public void handleError(String errorMsg) {

        hideProgressSpinner();
        showAlert("Error",errorMsg);
    }

    private void setUpListeners(){

        //family member
        binding.chkBoxFamilyMember.setOnClickListener(v -> {

            if (binding.chkBoxFamilyMember.isChecked()){

                binding.chkBoxPersonalFriend.setChecked(false);
                binding.chkBoxNewAcquaintance.setChecked(false);
                binding.chkBoxBusinessContact.setChecked(false);
                binding.chkBoxColleague.setChecked(false);
                binding.chkBoxClient.setChecked(false);

            } else {

                binding.chkBoxFamilyMember.setChecked(true);
            }
        });

        //personal friend
        binding.chkBoxPersonalFriend.setOnClickListener(v -> {

            if (binding.chkBoxPersonalFriend.isChecked()){

                binding.chkBoxFamilyMember.setChecked(false);
                binding.chkBoxNewAcquaintance.setChecked(false);
                binding.chkBoxBusinessContact.setChecked(false);
                binding.chkBoxColleague.setChecked(false);
                binding.chkBoxClient.setChecked(false);

            } else {

                binding.chkBoxPersonalFriend.setChecked(true);
            }
        });

        //new acquaintance
        binding.chkBoxNewAcquaintance.setOnClickListener(v -> {

            if (binding.chkBoxNewAcquaintance.isChecked()){

                binding.chkBoxPersonalFriend.setChecked(false);
                binding.chkBoxFamilyMember.setChecked(false);
                binding.chkBoxBusinessContact.setChecked(false);
                binding.chkBoxColleague.setChecked(false);
                binding.chkBoxClient.setChecked(false);

            } else {

                binding.chkBoxNewAcquaintance.setChecked(true);
            }
        });

        //Business contact
        binding.chkBoxBusinessContact.setOnClickListener(v -> {

            if (binding.chkBoxBusinessContact.isChecked()){

                binding.chkBoxPersonalFriend.setChecked(false);
                binding.chkBoxFamilyMember.setChecked(false);
                binding.chkBoxNewAcquaintance.setChecked(false);
                binding.chkBoxColleague.setChecked(false);
                binding.chkBoxClient.setChecked(false);

            } else {

                binding.chkBoxBusinessContact.setChecked(true);
            }
        });

        //Colleague
        binding.chkBoxColleague.setOnClickListener(v -> {

            if (binding.chkBoxColleague.isChecked()){

                binding.chkBoxPersonalFriend.setChecked(false);
                binding.chkBoxFamilyMember.setChecked(false);
                binding.chkBoxBusinessContact.setChecked(false);
                binding.chkBoxNewAcquaintance.setChecked(false);
                binding.chkBoxClient.setChecked(false);

            } else {

                binding.chkBoxColleague.setChecked(true);
            }
        });

        //Client
        binding.chkBoxClient.setOnClickListener(v -> {

            if (binding.chkBoxClient.isChecked()){

                binding.chkBoxPersonalFriend.setChecked(false);
                binding.chkBoxFamilyMember.setChecked(false);
                binding.chkBoxBusinessContact.setChecked(false);
                binding.chkBoxColleague.setChecked(false);
                binding.chkBoxNewAcquaintance.setChecked(false);

            } else {

                binding.chkBoxClient.setChecked(true);
            }
        });
    }


    private int getConnectionType(){

        if (binding.chkBoxFamilyMember.isChecked()){

            return 0;

        } else if (binding.chkBoxPersonalFriend.isChecked()){

            return 1;

        } else if (binding.chkBoxNewAcquaintance.isChecked()){

            return 2;

        } else if (binding.chkBoxBusinessContact.isChecked()){

            return 3;

        } else if (binding.chkBoxColleague.isChecked()){

            return 4;

        } else {

            return 5;
        }
    }

    private void setCachedConnectionType(){

        int type = userConnections.getConnectionType();

        if (type == 0){

            binding.chkBoxFamilyMember.performClick();

        } else if (type == 1){

            binding.chkBoxPersonalFriend.performClick();

        } else if (type == 2){

            binding.chkBoxNewAcquaintance.performClick();

        } else if (type == 3){

            binding.chkBoxBusinessContact.performClick();

        } else if (type == 4){

            binding.chkBoxColleague.performClick();

        } else {

            binding.chkBoxClient.performClick();
        }
    }
}
