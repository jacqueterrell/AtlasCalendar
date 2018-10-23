package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.PersonalNoteListRowBinding;

public class PersonalNoteAdapter extends RecyclerView.Adapter<PersonalNoteAdapter.PersonalNoteViewHolder> {


    private Map<Integer,String> detailsMap = new LinkedHashMap<>();
    private List<String> detailsList;
    private PersonalNoteNavigator navigator;


    public PersonalNoteAdapter(List<String> detailsList,PersonalNoteNavigator navigator){

        this.navigator = navigator;
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public PersonalNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        PersonalNoteListRowBinding binding = DataBindingUtil.inflate(inflater,R.layout.personal_note_list_row,parent,false);

        return new PersonalNoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalNoteViewHolder holder, int position) {

        String detail = detailsList.get(position);
        detailsMap.put(position,detail);

        holder.binding.etDetails.setText(detail);

        holder.binding.ivAdd.setOnClickListener(view -> {

            navigator.onAddButtonClicked();
        });

        holder.binding.etDetails.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                String text = editable.toString();
                detailsMap.put(position,text);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }


    public class PersonalNoteViewHolder extends RecyclerView.ViewHolder{

        private PersonalNoteListRowBinding binding;

        public PersonalNoteViewHolder(PersonalNoteListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }

    public Map<Integer,String> getDetailsMap(){

        return detailsMap;
    }

    public void deleteMap(int position){

        detailsMap.remove(position);
    }
}
