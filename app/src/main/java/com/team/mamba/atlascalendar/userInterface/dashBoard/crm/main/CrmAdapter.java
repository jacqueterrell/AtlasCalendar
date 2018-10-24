package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.databinding.CrmListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmAdapter.CrmViewHolder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CrmAdapter extends RecyclerView.Adapter<CrmViewHolder> {

    private List<CrmNotes> crmNotesList;
    private CrmViewModel viewModel;
    private List<String> monthsList = new ArrayList<>();
    private List<Integer> monthPositions = new ArrayList<>();

    public CrmAdapter(CrmViewModel viewModel,List<CrmNotes> crmNotes){


        this.crmNotesList = crmNotes;
        this.viewModel = viewModel;

    }

    public class CrmViewHolder extends RecyclerView.ViewHolder {

        private CrmListRowBinding binding;

        public CrmViewHolder(CrmListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            Collections.sort(crmNotesList, (o1, o2) -> Double.compare(o2.getAdjustedTimeStamp(), o1.getAdjustedTimeStamp()));

        }



        private String getMonth(int index) {

            List<String> monthsList = new ArrayList<>();

            monthsList.add("January");
            monthsList.add("February");
            monthsList.add("March");
            monthsList.add("April");
            monthsList.add("May");
            monthsList.add("June");
            monthsList.add("July");
            monthsList.add("August");
            monthsList.add("September");
            monthsList.add("October");
            monthsList.add("November");
            monthsList.add("December");

            return monthsList.get(index);
        }
    }


    @NonNull
    @Override
    public CrmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        CrmListRowBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.crm_list_row,parent,false);

        return new CrmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CrmViewHolder holder, int position) {

        CrmNotes notes = crmNotesList.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(notes.getAdjustedTimeStamp() * 1000);
        int month = calendar.get(Calendar.MONTH);
        String monthName = holder.getMonth(month);
        holder.binding.setViewModel(viewModel);

        if (!monthsList.contains(monthName)){

            holder.binding.tvMonth.setText(monthName);
            holder.binding.layoutMonth.setVisibility(View.VISIBLE);

            monthsList.add(monthName);
            monthPositions.add(position);

        } else if (monthPositions.contains(position)){

            holder.binding.layoutMonth.setVisibility(View.VISIBLE);
            holder.binding.tvMonth.setText(monthName);

        } else {

            holder.binding.layoutMonth.setVisibility(View.GONE);
            holder.binding.tvMonth.setText(monthName);

        }

        if (position == 0){

            holder.binding.ivInfo.setVisibility(View.VISIBLE);

        } else {

            holder.binding.ivInfo.setVisibility(View.GONE);
        }

        holder.binding.tvNoteName.setText(notes.getNoteName());

        if (notes.getStage() == 0){

            Glide.with(holder.binding.tvMonth.getContext())
                    .load(R.drawable.pie0)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding.ivStage);

        } else if (notes.getStage() == 1){

            Glide.with(holder.binding.tvMonth.getContext())
                    .load(R.drawable.pie1)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding.ivStage);

        } else if (notes.getStage() == 2){

            Glide.with(holder.binding.tvMonth.getContext())
                    .load(R.drawable.pie2)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding.ivStage);

        } else if (notes.getStage() == 3){

            Glide.with(holder.binding.tvMonth.getContext())
                    .load(R.drawable.pie3)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding.ivStage);

        } else if (notes.getStage() == 4){

            Glide.with(holder.binding.tvMonth.getContext())
                    .load(R.drawable.pie4)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding.ivStage);

        } else if (notes.getStage() == 5){

            Glide.with(holder.binding.tvMonth.getContext())
                    .load(R.drawable.pie5)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.binding.ivStage);
        }

        holder.binding.layoutRow.setOnClickListener(v -> {

            viewModel.getNavigator().onRowClicked(notes);
            viewModel.setSelectedOpportunity(notes);
            viewModel.setSelectedRowPosition(position);

        });

    }

    @Override
    public int getItemCount() {
        return crmNotesList.size();
    }

    public void filter(String text){
        crmNotesList.clear();

        if (text.equals("")){

            crmNotesList.addAll(viewModel.getNavigator().getPerCrmNotesList());

        } else {

            text = text.toLowerCase();
            for (CrmNotes notes : viewModel.getNavigator().getPerCrmNotesList()){

                if (notes.getNoteName().toLowerCase().contains(text)
                        || notes.getPoc().toLowerCase().contains(text)
                        || notes.getWhereMetCitySt().toLowerCase().contains(text)
                        || notes.getWhereMetEventName().toLowerCase().contains(text)){

                    crmNotesList.add(notes);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void clearMonths(){

        monthsList.clear();
        monthPositions.clear();
    }
}
