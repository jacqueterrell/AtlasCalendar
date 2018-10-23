package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.UserStatsListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.info.UserStatsAdapter.UserStatsViewHolder;
import java.util.List;

public class UserStatsAdapter extends RecyclerView.Adapter<UserStatsViewHolder> {

    private List<String> userStatsList;

    public UserStatsAdapter(List<String> userStatsList){

        this.userStatsList = userStatsList;
    }

    @NonNull
    @Override
    public UserStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        UserStatsListRowBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.user_stats_list_row, parent, false);


        return new UserStatsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserStatsViewHolder holder, int position) {

        String userStat = userStatsList.get(position);
        holder.binding.tvUserStat.setText(userStat);
    }

    @Override
    public int getItemCount() {
        return userStatsList.size();
    }

    public class UserStatsViewHolder extends RecyclerView.ViewHolder{

        private UserStatsListRowBinding binding;

        public UserStatsViewHolder(UserStatsListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
