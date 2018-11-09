package com.team.mamba.atlascalendar.userInterface.dashBoard.locator.connectionsRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.BusinessAccountsListRowBinding;
import com.team.mamba.atlascalendar.databinding.ConnectionRequestListRowBinding;
import com.team.mamba.atlascalendar.databinding.SingleTextListRowBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectionsRequestAdapter extends RecyclerView.Adapter<ConnectionsRequestAdapter.ConnectionsViewHolder> {


    private List<UserConnections> userConnections;
    private ConnectionsNavigator navigator;


    public ConnectionsRequestAdapter(ConnectionsNavigator navigator,List<UserConnections> userConnections){

        this.userConnections = userConnections;
        this.navigator = navigator;
    }


    public class ConnectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConnectionRequestListRowBinding binding;

        public ConnectionsViewHolder(ConnectionRequestListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            UserConnections connections = userConnections.get(getAdapterPosition());
            navigator.onRowClicked(connections);
        }
    }

    @NonNull
    @Override
    public ConnectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ConnectionRequestListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.connection_request_list_row,parent,false);

        return new ConnectionsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionsViewHolder holder, int position) {

        UserProfile userProfile = userConnections.get(position).getUserProfile();
        String name = userProfile.getFirstName() + " " + userProfile.getLastName();

        holder.binding.tvSelection.setText(name);
    }

    @Override
    public int getItemCount() {
        return userConnections.size();
    }

}
