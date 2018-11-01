package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.AddBusinessLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;

import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import javax.inject.Inject;

public class AddBusinessFragment extends BaseFragment<AddBusinessLayoutBinding, AddBusinessViewModel>
implements AddBusinessNavigator {


    @Inject
    AddBusinessViewModel viewModel;

    @Inject
    AddBusinessDataModel dataModel;


    private AddBusinessLayoutBinding binding;
    private DashBoardActivityNavigator parentNavigator;
    private static boolean calendarConnection = false;


    public static AddBusinessFragment newInstance(boolean isCalendarConnection){

        calendarConnection = isCalendarConnection;
        return new AddBusinessFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_business_layout;
    }

    @Override
    public AddBusinessViewModel getViewModel() {
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
        AddBusinessViewModel.setCalendarConnection(calendarConnection);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         binding = getViewDataBinding();



         return binding.getRoot();
    }

    @Override
    public void onFinishButtonClicked() {

        showProgressSpinner();
        String businessName = binding.etBusinessName.getText().toString();
        String code = binding.etIdCode.getText().toString();

        if (businessName.isEmpty() || code.isEmpty()){

            String title = "Check Fields";
            String msg = "Please make sure that the name and code fields are complete";
            showAlert(title,msg);

        } else {

            viewModel.addBusinessRequest(getViewModel(),businessName,code);

        }
    }

    @Override
    public void showUserNotFoundAlert() {

        String businessName = binding.etBusinessName.getText().toString();
        String code = binding.etIdCode.getText().toString();

        String title = "User Not Found";
        String msg = "No Business found with a name of " + businessName + " and a code of " + code;
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
    public void onRequestSent() {

        hideProgressSpinner();
        String title = "Contact Added";
        parentNavigator.setContactRecentlyAdded(true);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Ok", (paramDialogInterface, paramInt) -> {

                    parentNavigator.refreshInfoFragment();
                });

        dialog.show();
    }

    @Override
    public void onCalendarConnectionSaved() {


        hideProgressSpinner();
        String title = "Calendar Contact Saved";
        parentNavigator.setContactRecentlyAdded(true);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle(title)
                .setCancelable(false)
                .setPositiveButton("Ok", (paramDialogInterface, paramInt) -> {

                    parentNavigator.refreshInfoFragment();
                });

        dialog.show();
    }
}
