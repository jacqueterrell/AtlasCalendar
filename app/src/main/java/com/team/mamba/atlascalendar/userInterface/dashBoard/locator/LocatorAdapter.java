package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.LocatorListRowBinding;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.locator.LocatorAdapter.LocatorViewHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocatorAdapter extends RecyclerView.Adapter<LocatorViewHolder>{

    private List<UserProfile> userProfileList = new ArrayList<>();
    private List<UserProfile> searchHistoryProfiles = new ArrayList<>();
    private List<UserProfile> favoriteProfiles = new ArrayList<>();

    private LocatorNavigator navigator;
    private List<String> favoriteTitleList = new ArrayList<>();
    private List<Integer> favoritePositions = new ArrayList<>();

    public LocatorAdapter(LocatorNavigator navigator){

        this.navigator = navigator;
    }

    public class LocatorViewHolder extends RecyclerView.ViewHolder{

        private LocatorListRowBinding binding;

        public LocatorViewHolder(@NonNull LocatorListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setUpOnClickListeners();
        }

        private void setUpOnClickListeners(){


            binding.ibFavoriteNotSelected.setOnClickListener(v -> {

                UserProfile profile = userProfileList.get(getAdapterPosition());
                String profileId = profile.getId();
                navigator.addFavoriteUser(profileId);

            });

            binding.ibFavoriteSelected.setOnClickListener(v -> {

                UserProfile profile = userProfileList.get(getAdapterPosition());
                String profileId = profile.getId();
                navigator.removeFavoriteUser(profileId);
            });
        }
    }


    @NonNull
    @Override
    public LocatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        LocatorListRowBinding binding =
                DataBindingUtil.inflate(inflater,R.layout.locator_list_row,parent,false);

        return new LocatorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocatorViewHolder holder, int position) {

        UserProfile profile = userProfileList.get(position);
        String fullName = profile.getFirstName() + " " + profile.getLastName();
        holder.binding.tvFullName.setText(fullName);

        if (profile.isSearchable() && !profile.isFavorite()){

            holder.binding.ibFavoriteSelected.setVisibility(View.GONE);
            holder.binding.ibFavoriteNotSelected.setVisibility(View.VISIBLE);

            if (position == 0){

                holder.binding.tvCategory.setText("Search Results");
                holder.binding.tvCategory.setVisibility(View.VISIBLE);

                if (profile.getFirstName().isEmpty()){

                    holder.binding.layoutRow.setVisibility(View.GONE);
                    holder.binding.viewEmptyRow.setVisibility(View.VISIBLE);

                } else {

                    holder.binding.layoutRow.setVisibility(View.VISIBLE);
                    holder.binding.viewEmptyRow.setVisibility(View.GONE);
                }

            } else {

                holder.binding.tvCategory.setVisibility(View.GONE);
                holder.binding.layoutRow.setVisibility(View.VISIBLE);
                holder.binding.viewEmptyRow.setVisibility(View.GONE);

            }

        } else if (profile.isFavorite() && !profile.isSearchable()){

            holder.binding.ibFavoriteSelected.setVisibility(View.VISIBLE);
            holder.binding.ibFavoriteNotSelected.setVisibility(View.GONE);

            if (profile.getId().isEmpty()){

                holder.binding.layoutRow.setVisibility(View.GONE);
                holder.binding.viewEmptyRow.setVisibility(View.VISIBLE);
                holder.binding.tvCategory.setVisibility(View.VISIBLE);
                holder.binding.tvCategory.setText("Favorites");

            } else {

                if (favoriteTitleList.isEmpty()){

                    holder.binding.layoutRow.setVisibility(View.VISIBLE);
                    holder.binding.viewEmptyRow.setVisibility(View.GONE);

                        holder.binding.tvCategory.setVisibility(View.VISIBLE);
                        holder.binding.tvCategory.setText("Favorites");

                        favoriteTitleList.add(profile.getId());
                        favoritePositions.add(position);

                } else if (favoritePositions.contains(position)){

                    holder.binding.tvCategory.setVisibility(View.VISIBLE);
                    holder.binding.tvCategory.setText("Favorites");

                } else {
                    holder.binding.tvCategory.setVisibility(View.GONE);
                }
            }

        } else {

            holder.binding.ibFavoriteSelected.setVisibility(View.GONE);
            holder.binding.ibFavoriteNotSelected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }


    /**
     * Used to filter our recyclerview by the entered text
     * @param text the entered text
     */
    public void filter(String text){

        searchHistoryProfiles.clear();

        if (text.equals("")){

            //searchHistoryProfiles.clear();

        } else {

            text = text.toLowerCase();

            for (UserProfile profile : navigator.getPermProfileList()){

                if (profile.getFirstName().toLowerCase().contains(text) || profile.getLastName().toLowerCase().contains(text)){

                    profile.setSearchable(true);
                    profile.setFavorite(false);
                    searchHistoryProfiles.add(profile);
                }
            }

        }

        setUpProfileList();
    }

    public void setUpProfileList(){

        if (searchHistoryProfiles.isEmpty()){

            UserProfile profile = new UserProfile();
            profile.setSearchable(true);
            searchHistoryProfiles.add(profile);
        }

        if (favoriteProfiles.isEmpty()){

            UserProfile profile = new UserProfile();
            profile.setSearchable(false);
            profile.setFavorite(true);
            favoriteProfiles.add(profile);

        }

        Collections.sort(searchHistoryProfiles, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));
        Collections.sort(favoriteProfiles, (o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));

        favoriteTitleList.clear();
        favoritePositions.clear();

        userProfileList.clear();
        userProfileList.addAll(searchHistoryProfiles);
        userProfileList.addAll(favoriteProfiles);
        notifyDataSetChanged();
    }

    public void setFavoriteProfiles(List<UserProfile> profiles){

        favoriteProfiles.clear();

        for (UserProfile profile: profiles){

            profile.setFavorite(true);
            profile.setSearchable(false);
            favoriteProfiles.add(profile);
        }

        setUpProfileList();
    }

    private List<UserProfile> getFavoriteProfiles(){

        for (UserProfile profile : favoriteProfiles){

            profile.setSearchable(false);
            profile.setFavorite(true);
        }

        return favoriteProfiles;
    }
}
