package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.databinding.RecentActivitiesListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.info.RecentActivitiesAdapter.RecentActivitiesHolder;

import java.util.List;


/**
 * This class acts as the adapter for our recycler view defined in
 * {@link InfoFragment}
 */
public class RecentActivitiesAdapter extends RecyclerView.Adapter<RecentActivitiesHolder> {

    private List<UserConnections> connectionRecords;
    private InfoViewModel viewModel;

    public RecentActivitiesAdapter(InfoViewModel viewModel, List<UserConnections> connectionRecords){

        this.viewModel = viewModel;
        this.connectionRecords = connectionRecords;
}

    @NonNull
    @Override
    public RecentActivitiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        RecentActivitiesListRowBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.recent_activities_list_row, parent, false);

        return new RecentActivitiesHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentActivitiesHolder holder, int position) {

        UserConnections record = connectionRecords.get(position);

        if (record.isNeedsApproval()
                || record.isOverrideBusinessProfile()){

            holder.binding.tvUserStat.setText(record.getRequestingUserName());

        } else {

            holder.binding.tvUserStat.setText(record.getConsentingUserName());

        }

        if (record.isIsOrgBus() && !record.isOverrideBusinessProfile()){ //confirmed business record

            holder.binding.ivConfirmedConnectionBusiness.setVisibility(View.VISIBLE);
            holder.binding.ivConfirmedConnectionIndividual.setVisibility(View.GONE);
            holder.binding.ivAddNewConnection.setVisibility(View.GONE);
            holder.binding.ivRecentUpdatedAccount.setVisibility(View.GONE);

        } else if (!record.isIsOrgBus() && !record.isNeedsApproval() && !record.isRecentActivity()
                || record.isIsOrgBus() && record.isOverrideBusinessProfile()){ //confirmed individual record

            holder.binding.ivConfirmedConnectionBusiness.setVisibility(View.GONE);
            holder.binding.ivConfirmedConnectionIndividual.setVisibility(View.VISIBLE);
            holder.binding.ivAddNewConnection.setVisibility(View.GONE);
            holder.binding.ivRecentUpdatedAccount.setVisibility(View.GONE);

        } else if (record.isNeedsApproval()){ //new connection to be approved

            holder.binding.ivConfirmedConnectionBusiness.setVisibility(View.GONE);
            holder.binding.ivConfirmedConnectionIndividual.setVisibility(View.GONE);
            holder.binding.ivAddNewConnection.setVisibility(View.VISIBLE);
            holder.binding.ivRecentUpdatedAccount.setVisibility(View.GONE);

        } else if (record.isRecentActivity()){ //new connection to be approved

            holder.binding.ivConfirmedConnectionBusiness.setVisibility(View.GONE);
            holder.binding.ivConfirmedConnectionIndividual.setVisibility(View.GONE);
            holder.binding.ivAddNewConnection.setVisibility(View.GONE);
            holder.binding.ivRecentUpdatedAccount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return connectionRecords.size();
    }

    public class RecentActivitiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecentActivitiesListRowBinding binding;

        public RecentActivitiesHolder(RecentActivitiesListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            UserConnections record = connectionRecords.get(getAdapterPosition());
            viewModel.getNavigator().onRecentActivitiesRowClicked(record);

        }
    }
}
