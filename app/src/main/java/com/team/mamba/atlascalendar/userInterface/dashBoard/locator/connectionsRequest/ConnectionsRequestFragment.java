package com.team.mamba.atlascalendar.userInterface.dashBoard.locator.connectionsRequest;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.databinding.ConnectionRequestsRecyclerBinding;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ConnectionsRequestFragment extends Fragment implements ConnectionsNavigator {


    private ConnectionsRequestAdapter connectionsRequestAdapter;
    private static List<UserConnections> userConnections = new ArrayList<>();
    private ConnectionRequestsRecyclerBinding binding;


    public static ConnectionsRequestFragment newInstance(List<UserConnections> connections) {

        userConnections = connections;
        return new ConnectionsRequestFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.connection_requests_recycler, container, false);

        connectionsRequestAdapter = new ConnectionsRequestAdapter(this, userConnections);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(connectionsRequestAdapter);

        binding.layoutBackground.setOnClickListener(v -> {

            getActivity().onBackPressed();
        });

        return binding.getRoot();
    }


    @Override
    public void onRowClicked(UserConnections userConnections) {

        UserProfile profile = userConnections.getUserProfile();
        String first = profile.getFirstName();
        String last = profile.getLastName();

        String title = "Approve Connection?";
        String msg = first + " " + last + " " + "is requesting to connect with you as a " + userConnections.getConnectionTypeToString()
                + "\n\n" + "Tap approve to share connect with " + first + " " + last + " and proceed to define your connection type";

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("Approve", (paramDialogInterface, paramInt) -> {

                    getActivity().onBackPressed();
                    ChangeFragments.replaceFragmentVertically(DescribeConnectionsFragment.newInstance(userConnections, true, false), getActivity()
                            .getSupportFragmentManager(), "AddUserLayout", null);
                });

        dialog.show();

    }
}
