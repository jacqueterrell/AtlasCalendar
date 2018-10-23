package com.team.mamba.atlascalendar.userInterface.welcome._viewPager.howYouMet;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;

public class RememberHowYouMetFragment extends Fragment {


    public static RememberHowYouMetFragment newInstance(){

        return new RememberHowYouMetFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.how_you_met_layout,container,false);



        return v;
    }
}
