package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.ContactListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.SelectContactAdapter.ContactViewHolder;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmViewModel;
import java.util.ArrayList;
import java.util.List;

public class SelectContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<UserProfile> userContacts = new ArrayList<>();
    private EditPageOneNavigator navigator;

    public SelectContactAdapter(EditPageOneNavigator navigator){

        this.navigator = navigator;
        userContacts.addAll(CrmViewModel.usersContactProfiles);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());

        ContactListRowBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.contact_list_row,parent,false);


        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        UserProfile profile = userContacts.get(position);

        String title = profile.getCurrentPosition() + ", " + profile.getCurrentEmployer();
        String name = profile.getFirstName() + " " + profile.getLastName();

        holder.binding.tvName.setText(name);
        holder.binding.tvPositionAndCompany.setText(title);
    }

    @Override
    public int getItemCount() {
        return userContacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        private ContactListRowBinding binding;

        public ContactViewHolder(ContactListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            UserProfile userProfile = userContacts.get(getAdapterPosition());
            navigator.onRowClicked(userProfile);
        }
    }
}
