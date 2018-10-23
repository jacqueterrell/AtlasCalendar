package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info.SelectSchoolAdapter.SelectSchoolViewHolder;
import java.util.List;
import com.team.mamba.atlascalendar.databinding.SingleTextListRowBinding;

public class SelectSchoolAdapter extends RecyclerView.Adapter<SelectSchoolViewHolder> {

    private List<String> universityList;
    private AddEducationNavigator navigator;


    public SelectSchoolAdapter(List<String> universityList,AddEducationNavigator navigator){

        this.universityList = universityList;
        this.navigator = navigator;
    }

    public class SelectSchoolViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        private SingleTextListRowBinding binding;

        public SelectSchoolViewHolder(SingleTextListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            String university = universityList.get(getAdapterPosition());
            navigator.onSchoolRowClicked(university);
        }
    }

    @NonNull
    @Override
    public SelectSchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SingleTextListRowBinding binding = DataBindingUtil.inflate(inflater,R.layout.single_text_list_row,parent,false);

        return new SelectSchoolViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectSchoolViewHolder holder, int position) {

        String university = universityList.get(position);

        holder.binding.tvSelection.setText(university.replaceAll("\"",""));
    }

    @Override
    public int getItemCount() {
        return universityList.size();
    }


    /**
     * Used by a QueryTextListener defined in {@link AddEducationFragment}
     *
     * @param text
     */
    public void filter(String text){

        universityList.clear();

        if (text.equals("")){

            universityList.addAll(navigator.getPermUniversityList());

        } else {

            text = text.toLowerCase();
            for (String university : navigator.getPermUniversityList()){

                if (university.toLowerCase().contains(text)){

                    universityList.add(university);
                }
            }
        }

        notifyDataSetChanged();
    }
}
