package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile;

import com.google.firebase.firestore.FirebaseFirestore;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.List;
import javax.inject.Inject;

public class ContactProfileDataModel {

    private AppDataManager dataManager;


    @Inject
    public ContactProfileDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void getConnectionType(ContactProfileViewModel viewModel,UserProfile profile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .whereEqualTo("requestingUserID",profile.getId())
                .get()
                .addOnCompleteListener(task -> {

                    List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                    for (UserConnections connection : connectionsList){

                        if (connection.getConsentingUserID().equals(dataManager.getSharedPrefs().getUserId())){

                            profile.setConnectionType(connection.getConnectionType());

                        }
                    }

                });
    }
}
