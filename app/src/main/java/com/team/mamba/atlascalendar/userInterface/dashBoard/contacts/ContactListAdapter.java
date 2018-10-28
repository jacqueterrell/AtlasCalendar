package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.ContactsMainListRowBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactListAdapter.ContactListViewHolder;
import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListViewHolder> {

    private List<UserConnections> userConnectionsList;
    private ContactsViewModel viewModel;
    private List<String> letterTitleList = new ArrayList<>();
    private List<Integer> letterPositions = new ArrayList<>();
    private int businessIndex = -50;


    public ContactListAdapter(List<UserConnections> userConnectionsList,ContactsViewModel viewModel){

        this.userConnectionsList = userConnectionsList;
        this.viewModel = viewModel;
    }


    public class ContactListViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

        private ContactsMainListRowBinding binding;

        public ContactListViewHolder(ContactsMainListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {

            UserConnections connections = userConnectionsList.get(getAdapterPosition());
            viewModel.getNavigator().onRowClicked(connections);
        }
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ContactsMainListRowBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.contacts_main_list_row,parent,false);

        return new ContactListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {

        UserConnections connection = userConnectionsList.get(position);

        if (!connection.isOrgBus || connection.isOverrideBusinessProfile()){

            UserProfile profile = connection.getUserProfile();

            String firstName = profile.getFirstName();
            String lastName = profile.getLastName().trim();
            String firstChar = profile.getLastName().substring(0,1);

            holder.binding.layoutBusinessHeader.setVisibility(View.GONE);
            holder.binding.tvFirstName.setText(firstName);
            holder.binding.tvLastName.setText(lastName);

            if (!letterTitleList.contains(firstChar)){

                holder.binding.layoutLetterTitle.setVisibility(View.VISIBLE);
                holder.binding.tvLetterTitle.setText(firstChar);

                letterTitleList.add(firstChar);
                letterPositions.add(position);

            } else if (letterPositions.contains(position)){

                holder.binding.layoutLetterTitle.setVisibility(View.VISIBLE);
                holder.binding.tvLetterTitle.setText(firstChar);

            } else {

                holder.binding.layoutLetterTitle.setVisibility(View.GONE);
            }

        } else {

            BusinessProfile profile = connection.getBusinessProfile();
            String companyName = profile.getName();

            holder.binding.tvFirstName.setText(companyName);
            holder.binding.tvLastName.setText("");
            holder.binding.layoutLetterTitle.setVisibility(View.GONE);


            if (businessIndex == -50){ //first instance of a business

                businessIndex = position;
            }

            if (position == businessIndex){

                holder.binding.layoutBusinessHeader.setVisibility(View.VISIBLE);

            } else {

                holder.binding.layoutBusinessHeader.setVisibility(View.GONE);
            }
        }

        holder.binding.layoutRow.setOnClickListener(v -> {

            viewModel.getNavigator().onRowClicked(connection);
        });
    }

    @Override
    public int getItemCount() {
        return userConnectionsList.size();
    }



    public void filter(String text){

        userConnectionsList.clear();

        if (text.equals("")){

            userConnectionsList.addAll(viewModel.getNavigator().getPermConnectionList());

        } else {

            text = text.toLowerCase();
            for (UserConnections connections : viewModel.getNavigator().getPermConnectionList()){

                if (connections.isOrgBus && !connections.isOverrideBusinessProfile()){

                    if (connections.getBusinessProfile().getName().toLowerCase().contains(text)) {

                        userConnectionsList.add(connections);
                    }

                } else if (!connections.isOrgBus || connections.isOverrideBusinessProfile()){

                    if (connections.getUserProfile().getFirstName().toLowerCase().contains(text)
                            || connections.getUserProfile().getLastName().toLowerCase().contains(text)){

                        userConnectionsList.add(connections);
                    }

                }
            }
        }

        notifyDataSetChanged();
    }


    public void clearTitleList(){

        letterPositions.clear();
        letterTitleList.clear();
    }
}
