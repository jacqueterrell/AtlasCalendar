package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.ContactViewPagerBinding;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.ContactNotesFragment;

public class ContactProfilePager extends Fragment {


    private ContactViewPagerBinding binding;
    private static UserProfile profile;

    public static ContactProfilePager newInstance(UserProfile userProfile){

        profile = userProfile;
        return new ContactProfilePager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.contact_view_pager,container,false);

        ContactPager contactPager = new ContactPager(getActivity().getSupportFragmentManager());
        binding.viewPager.setAdapter(contactPager);

        return binding.getRoot();
    }


    public class ContactPager extends FragmentStatePagerAdapter{


        public ContactPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0){

                return ContactProfileFragment.newInstance(profile);

            } else  {
                return ContactNotesFragment.newInstance(profile);
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
