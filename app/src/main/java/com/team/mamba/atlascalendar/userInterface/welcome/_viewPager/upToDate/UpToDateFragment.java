package com.team.mamba.atlascalendar.userInterface.welcome._viewPager.upToDate;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.databinding.UpToDateLayoutBinding;

public class UpToDateFragment extends Fragment{


    public static UpToDateFragment newInstance(){

        return new UpToDateFragment();
    }

    private UpToDateLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.up_to_date_layout,container,false);

        binding.cvImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        return binding.getRoot();
    }
}
