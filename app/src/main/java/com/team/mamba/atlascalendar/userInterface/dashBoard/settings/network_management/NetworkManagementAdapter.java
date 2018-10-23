package com.team.mamba.atlascalendar.userInterface.dashBoard.settings.network_management;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.NetworkManagementListRowBinding;

import java.util.List;

public class NetworkManagementAdapter extends RecyclerView.Adapter<NetworkManagementAdapter.NetworkViewHolder> {

    private List<UserConnections> userProfileList;
    private NetworkManagementViewModel viewModel;


    public NetworkManagementAdapter(List<UserConnections>connections,NetworkManagementViewModel viewModel){

        this.userProfileList = connections;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NetworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        NetworkManagementListRowBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.network_management_list_row,parent,false);

        return new NetworkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkViewHolder holder, int position) {

        UserConnections connections = userProfileList.get(position);

        if (connections.isOrgBus && !connections.isOverrideBusinessProfile()){

            BusinessProfile profile = connections.getBusinessProfile();
            holder.binding.tvName.setText(profile.getName());
            holder.binding.tvDate.setText(connections.getDateToString());


        } else {

            UserProfile profile = connections.getUserProfile();
            String name = profile.getFirstName() + " " + profile.getLastName();
            holder.binding.tvDate.setText(connections.getDateToString());
            holder.binding.tvName.setText(name);

        }

    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public class NetworkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private NetworkManagementListRowBinding binding;

        public NetworkViewHolder(NetworkManagementListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            UserConnections connection = userProfileList.get(getAdapterPosition());
            viewModel.getNavigator().onRowClicked(connection);
        }
    }
}
