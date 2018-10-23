package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.FindUsersListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.FindUsersAdapter.FindUsersViewHolder;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.find_users.recycler_view.FindUsersRecyclerNavigator;
import java.util.List;

public class FindUsersAdapter extends RecyclerView.Adapter<FindUsersViewHolder>{


    private List<UserProfile> userProfileList;
    private FindUsersRecyclerNavigator navigator;

    public FindUsersAdapter(List<UserProfile> userProfiles,FindUsersRecyclerNavigator navigator){

        this.userProfileList = userProfiles;
        this.navigator = navigator;
    }


    public class FindUsersViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        private FindUsersListRowBinding binding;

        public FindUsersViewHolder(FindUsersListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            UserProfile profile = userProfileList.get(getAdapterPosition());
            navigator.onUsersRowClicked(profile);
        }
    }

    @NonNull
    @Override
    public FindUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        FindUsersListRowBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.find_users_list_row,parent,false);

        return new FindUsersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FindUsersViewHolder holder, int position) {

        UserProfile profile = userProfileList.get(position);
        String name = profile.getFirstName() + " " + profile.getLastName();

        holder.binding.tvName.setText(name);
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }
}
