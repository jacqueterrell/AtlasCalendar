package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.SingleTextListRowBinding;

import java.util.List;

public class AddWorkAdapter extends RecyclerView.Adapter<AddWorkAdapter.AddWorkViewHolder> {

    private List<String> descriptionList;
    private AddWorkViewModel viewModel;


    public AddWorkAdapter(List<String> descriptionList,AddWorkViewModel viewModel){

        this.descriptionList = descriptionList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public AddWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        SingleTextListRowBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.single_text_list_row,parent,false);

        return new AddWorkViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddWorkViewHolder holder, int position) {

        String description = descriptionList.get(position);

        holder.binding.tvSelection.setText(description);
    }

    @Override
    public int getItemCount() {
        return descriptionList.size();
    }


    public class AddWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SingleTextListRowBinding binding;

        public AddWorkViewHolder(SingleTextListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            String text = descriptionList.get(getAdapterPosition());
            viewModel.getNavigator().onRowClicked(text);
        }
    }
}
