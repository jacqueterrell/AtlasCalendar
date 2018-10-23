package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.filter_list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.CrmFilter;
import com.team.mamba.atlascalendar.databinding.FilterCrmBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import javax.inject.Inject;

public class CrmFilterSettingsFragment extends BaseFragment<FilterCrmBinding, CrmFilterViewModel>
        implements CrmFilterNavigator {


    private DashBoardActivity parentActivity;
    private FilterCrmBinding binding;
    private CrmFilter savedFilter;
    private CrmFilter newFilter = new CrmFilter();


    @Inject
    CrmFilterViewModel viewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.filter_crm;
    }

    @Override
    public CrmFilterViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getViewDataBinding();


        return binding.getRoot();
    }

    @Override
    public void onCancelButtonClicked() {

        CrmFilter filter = new CrmFilter();
        filter.setNextStep(-1);
        filter.setOpportunity(-1);
        filter.setStatus(-1);

        parentActivity.setCrmFilter(filter);
        getBaseActivity().getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onSubmitButtonClicked() {

        if (savedFilter != null) {

            parentActivity.setCrmFilter(savedFilter);

        } else {

            CrmFilter filter = new CrmFilter();
            filter.setNextStep(-1);
            filter.setOpportunity(-1);
            filter.setStatus(-1);

            parentActivity.setCrmFilter(filter);
        }

        getBaseActivity().getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onNextStepEmailClicked() {

        newFilter.setNextStep(0);
        savedFilter = newFilter;

        binding.ivEmailEnabled.setVisibility(View.VISIBLE);
        binding.ivEmailDisabled.setVisibility(View.GONE);

        binding.ivPhoneEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivTeleconferenceEnabled.setVisibility(View.GONE);
        binding.ivTeleconferenceDisabled.setVisibility(View.VISIBLE);

        binding.ivMeetingEnabled.setVisibility(View.GONE);
        binding.ivMeetingDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNextStepPhoneClicked() {

        newFilter.setNextStep(1);
        savedFilter = newFilter;

        binding.ivEmailEnabled.setVisibility(View.GONE);
        binding.ivEmailDisabled.setVisibility(View.VISIBLE);

        binding.ivPhoneEnabled.setVisibility(View.VISIBLE);
        binding.ivPhoneDisabled.setVisibility(View.GONE);

        binding.ivTeleconferenceEnabled.setVisibility(View.GONE);
        binding.ivTeleconferenceDisabled.setVisibility(View.VISIBLE);

        binding.ivMeetingEnabled.setVisibility(View.GONE);
        binding.ivMeetingDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNextStepTeleconferenceClicked() {

        newFilter.setNextStep(2);
        savedFilter = newFilter;

        binding.ivEmailEnabled.setVisibility(View.GONE);
        binding.ivEmailDisabled.setVisibility(View.VISIBLE);

        binding.ivPhoneEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivTeleconferenceEnabled.setVisibility(View.VISIBLE);
        binding.ivTeleconferenceDisabled.setVisibility(View.GONE);

        binding.ivMeetingEnabled.setVisibility(View.GONE);
        binding.ivMeetingDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNextMeetingClicked() {

        newFilter.setNextStep(3);
        savedFilter = newFilter;

        binding.ivEmailEnabled.setVisibility(View.GONE);
        binding.ivEmailDisabled.setVisibility(View.VISIBLE);

        binding.ivPhoneEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivTeleconferenceEnabled.setVisibility(View.GONE);
        binding.ivTeleconferenceDisabled.setVisibility(View.VISIBLE);

        binding.ivMeetingEnabled.setVisibility(View.VISIBLE);
        binding.ivMeetingDisabled.setVisibility(View.GONE);
    }

    @Override
    public void onSolicitationClicked() {

        newFilter.setOpportunity(0);
        savedFilter = newFilter;

        binding.ivSolicitationEnabled.setVisibility(View.VISIBLE);
        binding.ivSolicitationDisabled.setVisibility(View.GONE);

        binding.ivTeamingEnabled.setVisibility(View.GONE);
        binding.ivTeamingDisabled.setVisibility(View.VISIBLE);

        binding.ivDirectSellEnabled.setVisibility(View.GONE);
        binding.ivDirectSellDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTeamingClicked() {

        newFilter.setOpportunity(1);
        savedFilter = newFilter;

        binding.ivSolicitationEnabled.setVisibility(View.GONE);
        binding.ivSolicitationDisabled.setVisibility(View.VISIBLE);

        binding.ivTeamingEnabled.setVisibility(View.VISIBLE);
        binding.ivTeamingDisabled.setVisibility(View.GONE);

        binding.ivDirectSellEnabled.setVisibility(View.GONE);
        binding.ivDirectSellDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDirectSellClicked() {

        newFilter.setOpportunity(2);
        savedFilter = newFilter;

        binding.ivSolicitationEnabled.setVisibility(View.GONE);
        binding.ivSolicitationDisabled.setVisibility(View.VISIBLE);

        binding.ivTeamingEnabled.setVisibility(View.GONE);
        binding.ivTeamingDisabled.setVisibility(View.VISIBLE);

        binding.ivDirectSellEnabled.setVisibility(View.VISIBLE);
        binding.ivDirectSellDisabled.setVisibility(View.GONE);
    }

    @Override
    public void onNewClicked() {

        newFilter.setStatus(0);
        savedFilter = newFilter;

        binding.ivNewEnabled.setVisibility(View.VISIBLE);
        binding.ivNewDisabled.setVisibility(View.GONE);

        binding.ivQualifiedEnabled.setVisibility(View.GONE);
        binding.ivQualifiedDisabled.setVisibility(View.VISIBLE);

        binding.ivProposalEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivNegotiationEnabled.setVisibility(View.GONE);
        binding.ivNegotiationDisabled.setVisibility(View.VISIBLE);

        binding.ivClosedEnabled.setVisibility(View.GONE);
        binding.ivClosedDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQualifiedClicked() {

        newFilter.setStatus(1);
        savedFilter = newFilter;

        binding.ivNewEnabled.setVisibility(View.GONE);
        binding.ivNewDisabled.setVisibility(View.VISIBLE);

        binding.ivQualifiedEnabled.setVisibility(View.VISIBLE);
        binding.ivQualifiedDisabled.setVisibility(View.GONE);

        binding.ivProposalEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivNegotiationEnabled.setVisibility(View.GONE);
        binding.ivNegotiationDisabled.setVisibility(View.VISIBLE);

        binding.ivClosedEnabled.setVisibility(View.GONE);
        binding.ivClosedDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProposalClicked() {

        newFilter.setStatus(2);
        savedFilter = newFilter;

        binding.ivNewEnabled.setVisibility(View.GONE);
        binding.ivNewDisabled.setVisibility(View.VISIBLE);

        binding.ivQualifiedEnabled.setVisibility(View.GONE);
        binding.ivQualifiedDisabled.setVisibility(View.VISIBLE);

        binding.ivProposalEnabled.setVisibility(View.VISIBLE);
        binding.ivProposalDisabled.setVisibility(View.GONE);

        binding.ivNegotiationEnabled.setVisibility(View.GONE);
        binding.ivNegotiationDisabled.setVisibility(View.VISIBLE);

        binding.ivClosedEnabled.setVisibility(View.GONE);
        binding.ivClosedDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNegotiationClicked() {

        newFilter.setStatus(3);
        savedFilter = newFilter;

        binding.ivNewEnabled.setVisibility(View.GONE);
        binding.ivNewDisabled.setVisibility(View.VISIBLE);

        binding.ivQualifiedEnabled.setVisibility(View.GONE);
        binding.ivQualifiedDisabled.setVisibility(View.VISIBLE);

        binding.ivProposalEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivNegotiationEnabled.setVisibility(View.VISIBLE);
        binding.ivNegotiationDisabled.setVisibility(View.GONE);

        binding.ivClosedEnabled.setVisibility(View.GONE);
        binding.ivClosedDisabled.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClosedClicked() {

        newFilter.setStatus(4);
        savedFilter = newFilter;

        binding.ivNewEnabled.setVisibility(View.GONE);
        binding.ivNewDisabled.setVisibility(View.VISIBLE);

        binding.ivQualifiedEnabled.setVisibility(View.GONE);
        binding.ivQualifiedDisabled.setVisibility(View.VISIBLE);

        binding.ivProposalEnabled.setVisibility(View.GONE);
        binding.ivPhoneDisabled.setVisibility(View.VISIBLE);

        binding.ivNegotiationEnabled.setVisibility(View.GONE);
        binding.ivNegotiationDisabled.setVisibility(View.VISIBLE);

        binding.ivClosedEnabled.setVisibility(View.VISIBLE);
        binding.ivClosedDisabled.setVisibility(View.GONE);
    }

}
