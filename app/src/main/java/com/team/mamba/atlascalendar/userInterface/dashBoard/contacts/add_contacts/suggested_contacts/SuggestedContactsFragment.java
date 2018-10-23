package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.suggested_contacts;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.SuggestedContactsLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;

import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class SuggestedContactsFragment extends BaseFragment<SuggestedContactsLayoutBinding,SuggestedContactsViewModel>
implements SuggestedContactsNavigator {


    @Inject
    SuggestedContactsViewModel viewModel;

    @Inject
    SuggestedContactsDataModel dataModel;


    private SuggestedContactsLayoutBinding binding;
    private List<UserProfile> suggestedProfileList = new ArrayList<>();
    private SuggestedContactsAdapter suggestedContactsAdapter;
    private DashBoardActivityNavigator parentNavigator;


    public static SuggestedContactsFragment newInstance(){

        return new SuggestedContactsFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.suggested_contacts_layout;
    }

    @Override
    public SuggestedContactsViewModel getViewModel() {
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

         suggestedContactsAdapter = new SuggestedContactsAdapter(viewModel,suggestedProfileList);
         binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
         binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(suggestedContactsAdapter);

        viewModel.requestSuggestedContacts(viewModel);

         return binding.getRoot();
    }

    @Override
    public void onSearchButtonClicked() {

        viewModel.requestSuggestedContacts(viewModel);
    }

    @Override
    public void onSuggestedContactsFound() {

        suggestedProfileList.clear();
        suggestedProfileList.addAll(viewModel.getSuggestedProfileList());
        Collections.sort(suggestedProfileList, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));

        suggestedContactsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onUsersRowClicked(UserProfile profile) {

        String last = profile.getLastName();
        String first = profile.getFirstName();

        String title = "Invite " + first + " " + last + " to connect?";
        String msg = "Please confirm that you know " + first + " " + last + " to send the invitation";

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("No",(paramDialogInterface, paramInt) ->{

                })
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                    // showInvitationSentAlert(first,last);
                    viewModel.initiateNewUserRequest(viewModel,profile);
                });

        dialog.show();
    }

    @Override
    public void showInvitationSentAlert(String first, String last) {


        String title = "Invite sent to " + first + " " + last;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(title)
                .setPositiveButton("OK", (paramDialogInterface, paramInt) -> {

                    parentNavigator.resetToFirstFragment();

                });

        dialog.show();
    }

    @Override
    public void handleError(String msg) {

    }
}
