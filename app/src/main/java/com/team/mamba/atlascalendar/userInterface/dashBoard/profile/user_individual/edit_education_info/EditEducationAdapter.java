package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.Education;
import com.team.mamba.atlascalendar.databinding.EditEducationListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.EditEducationAdapter.EducationViewHolder;
import java.util.List;

public class EditEducationAdapter extends RecyclerView.Adapter<EducationViewHolder> {


    private List<Education> educationList;
    private EditEducationViewModel viewModel;

    public EditEducationAdapter(EditEducationViewModel viewModel, List<Education> educations){

        this.educationList = educations;
        this.viewModel = viewModel;
    }

    public class EducationViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        private EditEducationListRowBinding binding;

        public EducationViewHolder(EditEducationListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Education education = educationList.get(getAdapterPosition());
            viewModel.getNavigator().onRowClicked(education,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public EducationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        EditEducationListRowBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.edit_education_list_row,parent,false);

        return new EducationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationViewHolder holder, int position) {

        Education education = educationList.get(position);

        int index = position + 1;
        String entry = "Entry " + index;

        holder.binding.tvIndex.setText(entry);
        holder.binding.tvSchool.setText(education.getSchool());
        holder.binding.tvDegree.setText(education.getDegree());
        holder.binding.tvFieldOfStudy.setText(education.getFieldOfStudy());

    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }

}
