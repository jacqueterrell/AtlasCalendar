package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.WorkHistory;
import com.team.mamba.atlascalendar.databinding.AddWorkHistoryLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work.EditWorkNavigator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AddWorkFragment extends BaseFragment<AddWorkHistoryLayoutBinding,AddWorkViewModel>
implements AddWorkNavigator{


    @Inject
    AddWorkViewModel viewModel;

    @Inject
    AddWorkDataModel dataModel;


    private AddWorkHistoryLayoutBinding binding;
    private static EditWorkNavigator navigator;
    private DashBoardActivity parentActivity;
    private AddWorkAdapter addWorkAdapter;
    private List<String> descriptionList = new ArrayList<>();



    public static AddWorkFragment newInstance(EditWorkNavigator editWorkNavigator){

        navigator = editWorkNavigator;
        return new AddWorkFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_work_history_layout;
    }

    @Override
    public AddWorkViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (DashBoardActivity) context;
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
       super.onCreateView(inflater,container,savedInstanceState);
        binding = getViewDataBinding();

        addWorkAdapter = new AddWorkAdapter(descriptionList,getViewModel());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(addWorkAdapter);

        setUpListeners();

        binding.btnSave.setOnClickListener(v -> {

            onSavedClicked();

        });

        if (navigator.isEditing()){

            WorkHistory workHistory = navigator.getEditingWork();
            String title = workHistory.getTitle();
            String industry = workHistory.getIndustry();
            String companyName = workHistory.getCompanyName();

            binding.etTitle.setText(title);
            binding.etIndustry.setText(industry);
            binding.etCompanyName.setText(companyName);
        }


        return binding.getRoot();
    }


    private void setUpListeners(){

        binding.etCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                viewModel.getGooglePlaceHints(getViewModel(),editable.toString());
            }
        });
    }

    @Override
    public void onRowClicked(String text) {

        binding.etCompanyName.setText(text);
    }

    @Override
    public void onAutoCompleteReturned(List<String> descriptionArrayList) {

        descriptionList.clear();
        descriptionList.addAll(descriptionArrayList);
        addWorkAdapter.notifyDataSetChanged();
    }

    @Override
    public void handleError(String msg) {

        showAlert(msg,"");
    }


    private void onSavedClicked(){

        String title = binding.etTitle.getText().toString();
        String industry = binding.etIndustry.getText().toString();
        String companyName = binding.etCompanyName.getText().toString();

        if (isAllValid(title,industry,companyName)){

            WorkHistory workHistory = new WorkHistory();
            workHistory.setCompanyName(companyName);
            workHistory.setTitle(title);
            workHistory.setIndustry(industry);

            if (navigator.isEditing()){

                navigator.onEditRowSaved(workHistory);

            } else {

                navigator.onSaveNewWorkHistory(workHistory);
            }

            parentActivity.onBackPressed();

        } else {

            Snackbar.make(binding.getRoot(),"Please enter all values",Snackbar.LENGTH_LONG)
                    .show();
        }
    }


    private boolean isAllValid(String title,String industry,String companyName){

        if (title.isEmpty() || industry.isEmpty() || companyName.isEmpty()){

            return false;

        }

        return true;
    }
}
