package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.DirectoryListRowBinding;

import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;

import java.util.List;

public class DirectoryListAdapter extends RecyclerView.Adapter<DirectoryListAdapter.DirectoryViewHolder> {

    private List<UserConnections> directoryConnections;
    private DirectoryNavigator directoryNavigator;

    public DirectoryListAdapter(List<UserConnections> directoryConnections,DirectoryNavigator navigator){

        this.directoryConnections = directoryConnections;
        this.directoryNavigator = navigator;
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private DirectoryListRowBinding binding;

        public DirectoryViewHolder(DirectoryListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            UserConnections connections = directoryConnections.get(getAdapterPosition());
            directoryNavigator.onRowClicked(connections);
        }
    }

    @NonNull
    @Override
    public DirectoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DirectoryListRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.directory_list_row,parent,false);

        return new DirectoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectoryViewHolder holder, int position) {

        UserConnections connections = directoryConnections.get(position);
        String name = connections.getConsentingUserName();
        holder.binding.tvDirectory.setText(name);
    }

    @Override
    public int getItemCount() {
        return directoryConnections.size();
    }

}
