package com.team.mamba.atlascalendar.userInterface.dashBoard.announcements;

import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.Announcements;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class AnnouncementsDataModel {

    private AppDataManager dataManager;

    @Inject
    public AnnouncementsDataModel(AppDataManager dataManager){

        this.dataManager = dataManager;
    }


    public void requestAnnouncements(AnnouncementsViewModel viewModel){

        getAllBusinesses(viewModel);
    }


    /**
     * Retrieves all Business profiles, loops through each one to see if
     * the logged in user is a contact of the business.
     * @param viewModel
     */
    private void getAllBusinesses(AnnouncementsViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dataManager.getSharedPrefs().getUserId();
        List<String> businessIdList = new ArrayList<>();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects(BusinessProfile.class);

                        if (dataManager.getSharedPrefs().isBusinessAccount()){

                            businessIdList.add(userId);

                        } else {

                            for (BusinessProfile profile : businessProfiles){

                                if (profile.getContacts().containsKey(userId)
                                        && !businessIdList.contains(profile.getId())){

                                    businessIdList.add(profile.getId());
                                }

                            }
                        }

                        getAllAnnouncements(viewModel,businessIdList);

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }

                });
    }

    /**
     * Retrieves all Business Announcements and finds the ones that belong to
     * any business the signed in user is a contact of.
     * @param viewModel
     * @param businessIdList
     */
    private void getAllAnnouncements(AnnouncementsViewModel viewModel,List<String> businessIdList){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Announcements> userAnnouncements = new ArrayList<>();

        db.collection(AppConstants.ANNOUNCEMENTS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<Announcements> announcementsList = task.getResult().toObjects(Announcements.class);

                        for (Announcements entry : announcementsList){

                            for (String id : businessIdList){

                                if (id.equals(entry.getOrgBusID())){

                                    userAnnouncements.add(entry);
                                }
                            }
                        }

                        viewModel.setAnnouncementsList(userAnnouncements);
                        viewModel.getNavigator().onAnnouncementsReturned();

                    } else {

                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                        viewModel.getNavigator().handleError(task.getException().getMessage());

                    }

                });
    }
}
