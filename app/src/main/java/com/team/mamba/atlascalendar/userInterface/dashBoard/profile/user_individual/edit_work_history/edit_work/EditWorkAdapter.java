package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.WorkHistory;
import com.team.mamba.atlascalendar.databinding.EditWorkHistoryListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work.EditWorkAdapter.EditWorkViewHolder;
import java.util.List;

public class EditWorkAdapter extends RecyclerView.Adapter<EditWorkViewHolder> {


    private List<WorkHistory> workHistoryList;
    private EditWorkViewModel viewModel;


    public EditWorkAdapter(EditWorkViewModel viewModel,List<WorkHistory> workHistories){

        this.workHistoryList = workHistories;
        this.viewModel = viewModel;
    }


    public class EditWorkViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        private EditWorkHistoryListRowBinding binding;

        public EditWorkViewHolder(EditWorkHistoryListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            WorkHistory workHistory = workHistoryList.get(getAdapterPosition());
            viewModel.getNavigator().onRowClicked(workHistory,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public EditWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        EditWorkHistoryListRowBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.edit_work_history_list_row,parent,false);

        return new EditWorkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EditWorkViewHolder holder, int position) {

        WorkHistory workHistory = workHistoryList.get(position);

        int index = position + 1;
        String entry = "Entry " + index;

        holder.binding.tvIndex.setText(entry);
        holder.binding.tvCompanyName.setText(workHistory.getCompanyName());
        holder.binding.tvTitle.setText(workHistory.getTitle());
        holder.binding.tvIndustry.setText(workHistory.getIndustry());
    }

    @Override
    public int getItemCount() {
        return workHistoryList.size();
    }
}
