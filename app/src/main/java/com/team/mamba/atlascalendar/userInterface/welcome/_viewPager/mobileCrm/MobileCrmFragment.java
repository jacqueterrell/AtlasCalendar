package com.team.mamba.atlascalendar.userInterface.welcome._viewPager.mobileCrm;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;

public class MobileCrmFragment extends Fragment {


    public static MobileCrmFragment newInstance(){

        return new MobileCrmFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mobile_crm_layout,container,false);

        return v;
    }
}
