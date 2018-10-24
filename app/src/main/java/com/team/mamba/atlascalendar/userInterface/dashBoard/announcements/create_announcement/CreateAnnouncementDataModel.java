package com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.create_announcement;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.Announcements;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.Map;
import javax.inject.Inject;

public class CreateAnnouncementDataModel {

    private AppDataManager dataManager;

    @Inject
    public CreateAnnouncementDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void sendAnnouncement(CreateAnnouncementViewModel viewModel){

        getBusinessProfile(viewModel);
    }


    /**
     * Retrieves tbe Business profile for the logged in business
     */
    private void getBusinessProfile(CreateAnnouncementViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .whereEqualTo("id", dataManager.getSharedPrefs().getUserId())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        BusinessProfile profile = task.getResult().toObjects((BusinessProfile.class)).get(0);
                        addNewAnnouncement(viewModel,profile);

                    } else {

                        if (task.getException() != null){

                            viewModel.getNavigator().handleError( task.getException().getLocalizedMessage());
                        }
                    }

                });
    }

    /**
     * Adds the new announcment to the Announcements collection
     * @param profile the business profile
     */
    private void addNewAnnouncement(CreateAnnouncementViewModel viewModel,BusinessProfile profile){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newAnnouncementRef = db.collection(AppConstants.ANNOUNCEMENTS_COLLECTION).document();

        Long timeStamp = System.currentTimeMillis() / 1000;

        Announcements announcements = new Announcements();
        announcements.setId(newAnnouncementRef.getId());
        announcements.setEvent(viewModel.isEvent());
        announcements.setOrgBusID(profile.getId());
        announcements.setOrgBusName(profile.getName());
        announcements.setText(viewModel.getAnnouncementMessage());
        announcements.setTimestamp(timeStamp);

        newAnnouncementRef.set(announcements)
                .addOnSuccessListener(documentReference ->{

                    addAnnouncementToBusinessProfile(viewModel,profile,newAnnouncementRef.getId());
                })
                .addOnFailureListener(e -> {

                    viewModel.getNavigator().handleError(e.getMessage());
                    Logger.e(e.getMessage());
                });
    }


    /**
     * Adds the announcement to the business' contacts list
     * @param profile the business profile
     * @param announcementId the announcement's Id
     */
    private void addAnnouncementToBusinessProfile(CreateAnnouncementViewModel viewModel,BusinessProfile profile,String announcementId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> announcementsMap = profile.getAnnouncements();
        announcementsMap.put(announcementId,announcementId);

        //add the announcement to the businesses announcement list
        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .document(profile.getId())
                .set(profile)
                .addOnCompleteListener(task ->{

                    if (task.isSuccessful()){

                        Logger.i("Announcement updated successfully");

                    } else {
                        handleError(task,viewModel);
                    }
                });
    }

    private void handleError(Task task,CreateAnnouncementViewModel viewModel){

        if (task.getException() != null){

            viewModel.getNavigator().handleError(task.getException().getMessage());
        }
    }
}
