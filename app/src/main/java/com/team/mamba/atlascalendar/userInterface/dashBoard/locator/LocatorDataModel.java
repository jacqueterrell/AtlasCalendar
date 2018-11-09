package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.text.TextUtils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;

import com.team.mamba.atlascalendar.data.local_database.favoriteUsers.FavoriteUsersEntity;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocatorDataModel {


    private AppDataManager dataManager;
    private static List<UserProfile> allUserProfiles = new ArrayList<>();

    @Inject
    public LocatorDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }


    public void requestContactsInfo(LocatorViewModel viewModel){

        requestUserProfiles(viewModel);
    }


    /**
     * Gets all User profiles from the database
     */
    private void requestUserProfiles(LocatorViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserProfile> userProfileList = task.getResult().toObjects(UserProfile.class);
                        setAllUserProfiles(viewModel,userProfileList);

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }
                });
    }


    /**
     * Saves the signed in user's profile and removes all profiles
     * that do not have a userId
     *
     * @param userProfileList list of all profiles using Atlas
     */
    private void setAllUserProfiles(LocatorViewModel viewModel,List<UserProfile> userProfileList){

        List<UserProfile> adjustedProfileList = new ArrayList<>();
        String savedUserId = dataManager.getSharedPrefs().getUserId();

        for (UserProfile profile : userProfileList){

            if (profile.getId() != null) {

                if (profile.getId().equals(savedUserId)) {

                    viewModel.setSelectedUserProfile(profile);
                    viewModel.setCalendarCompanyId(profile.getCalendarConnection());
                    viewModel.getNavigator().onSelectedUserProfileSaved();
                }

                adjustedProfileList.add(profile);
            }
        }

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            if (TextUtils.isEmpty(viewModel.getCalendarCompanyId())){

                viewModel.getNavigator().showAddCalendarMessage();

            } else {
                requestBusinessProfiles(viewModel,adjustedProfileList);
            }

        }

        viewModel.setAllUserProfiles(adjustedProfileList);
    }


    /**
     * Retrieves all business profiles from the database
     *
     * @param userProfileList list of all user profiles
     */
    private void requestBusinessProfiles(LocatorViewModel viewModel,List<UserProfile> userProfileList){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String calendarCompanyId = viewModel.getCalendarCompanyId();
        List<UserProfile> selectedProfilesList = new ArrayList<>();

        List<String> connectionsIdList = new ArrayList<>();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects(BusinessProfile.class);

                        for (BusinessProfile profile : businessProfiles){

                            if (profile.getId().equals(calendarCompanyId)){//retrieve the employers contacts

                                viewModel.setCalendarContactProfile(profile);
                                for (Map.Entry<String, String> entry : profile.getContacts().entrySet()){

                                    String contactId = entry.getKey();
                                    connectionsIdList.add(contactId);
                                }
                            }
                        }

                        for (UserProfile profile : userProfileList){//assigns the necessary profile for the contact id

                            for (String contactId : connectionsIdList){

                                //todo check proper location code before adding contact
                                if (profile.getId().equals(contactId)) {

                                    profile.setSearchable(true);
                                    profile.setFavorite(false);
                                    selectedProfilesList.add(profile);
                                }
                            }
                        }

                        viewModel.setEmployeeProfilesList(selectedProfilesList);
                        getFavoriteUsers(viewModel);

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }
                });
    }



    /**
     * Retrieves all lists of user profiles. This second query of user profiles
     * is necessary to provide two separate lists of user profiles
     */
    private void getFavoriteUsers(LocatorViewModel viewModel){


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> adjustedProfileList = new ArrayList<>();

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserProfile> userProfileList = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfileList){

                            if (profile.getId() != null) {

                                adjustedProfileList.add(profile);
                            }

                        }

                        allUserProfiles.addAll(adjustedProfileList);
                        requestFavoriteUsers(viewModel,adjustedProfileList);

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }
                });


    }

    /**
     * Retrieves the list of favorite users from the local Favorites Db
     */
    private void requestFavoriteUsers(LocatorViewModel viewModel, List<UserProfile> userProfiles){


        viewModel.getCompositeDisposable().add(dataManager.getFavoriteUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteUsersEntities -> {

                    List<String> profileIdList = new ArrayList<>();
                    List<UserProfile> favoritesProfiles = new ArrayList<>();

                    viewModel.setFavoriteUsersEntityList(favoriteUsersEntities);

                    for (FavoriteUsersEntity entities : favoriteUsersEntities){//get all fa

                        profileIdList.add(entities.getUserId());
                    }

                    for (UserProfile profile : userProfiles){

                        if (profileIdList.contains(profile.getId())){

                            profile.setSearchable(false);
                            profile.setFavorite(true);
                            favoritesProfiles.add(profile);
                        }
                    }

                    viewModel.setFavoritesProfileList(favoritesProfiles);
                    getConnectionRequests(viewModel);

                },throwable -> {

                    viewModel.getNavigator().handleError(throwable.getLocalizedMessage());
                    Logger.e(throwable.getLocalizedMessage());
                }));
    }


    /**
     * Removes the user from the local Favorites Db
     * @param profileId the User Profile Id to be deleted
     */
    public void removeFavoriteUser(LocatorViewModel viewModel, String profileId){


        for (FavoriteUsersEntity entity : viewModel.getFavoriteUsersEntityList()){

            if (entity.getUserId().equals(profileId)){

                dataManager.deleteFavoriteUser(entity);
                break;
            }
        }

        for (Iterator<UserProfile> iterator = viewModel.getFavoritesProfileList().iterator(); iterator.hasNext();){

            UserProfile profile = iterator.next();

            if (profile.getId().equals(profileId)){

                iterator.remove();
            }
        }

        viewModel.getNavigator().updateFavoritesList();
    }


    /**
     * Adds a user to our local Favorites Db
     * @param profileId the User Profile Id to be deleted
     */
    public void addFavoriteUser(LocatorViewModel viewModel,String profileId){

        FavoriteUsersEntity favoriteUsersEntity = new FavoriteUsersEntity(profileId);
        dataManager.insertFavoriteUser(favoriteUsersEntity);

        List<UserProfile> favoriteProfiles = new ArrayList<>();
        favoriteProfiles.addAll(viewModel.getFavoritesProfileList());

        for (UserProfile profile : allUserProfiles){

            if (profile.getId().equals(profileId)){

                profile.setFavorite(true);
                profile.setSearchable(false);
                favoriteProfiles.add(profile);
                viewModel.setFavoritesProfileList(favoriteProfiles);
                break;
            }
        }

        viewModel.getNavigator().updateFavoritesList();
    }


    /**
     * Updates the isPrivacy field in the Users database
     * @param privacyMode the user's privacy setting
     */
    public void setPrivacyMode(LocatorViewModel viewModel,boolean privacyMode){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dataManager.getSharedPrefs().getUserId();

        db.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .update("isPrivacyMode",privacyMode)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        UserProfile profile = viewModel.getSelectedUserProfile();
                        profile.setIsPrivacyMode(privacyMode);
                        viewModel.getNavigator().startLocationService();

                    } else {

                        if (task.getException() != null) {

                            Logger.e(task.getException().getMessage());
                            viewModel.getNavigator().handleError(task.getException().getMessage());

                        }
                    }
                });
    }


    /**
     * Updates the isVacation field in the Users database
     * @param vacationMode the user's vacation setting
     */
    public void setVacationMode(LocatorViewModel viewModel,boolean vacationMode){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dataManager.getSharedPrefs().getUserId();

        db.collection(AppConstants.USERS_COLLECTION)
                .document(userId)
                .update("isVacationMode",vacationMode)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        UserProfile profile = viewModel.getSelectedUserProfile();
                        profile.setIsVacationMode(vacationMode);
                        viewModel.getNavigator().startLocationService();

                    } else {

                        if (task.getException() != null) {

                            viewModel.getNavigator().handleError(task.getException().getMessage());
                            Logger.e(task.getException().getMessage());
                        }
                    }
                });
    }


    /**
     * Retrieves all Connections, loops through them to see which ones have not been
     * approved, and then finds the correlating user profile
     *
     */
    public void getConnectionRequests(LocatorViewModel viewModel){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dataManager.getSharedPrefs().getUserId();
        List<UserConnections> approvalConnections = new ArrayList<>();


        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .whereEqualTo("consentingUserID", userId)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()){

                        List<UserConnections> connectionsList = task.getResult().toObjects(UserConnections.class);

                        for (UserConnections connections : connectionsList){

                            if (!connections.isConfirmed){//needs approval

                                for (UserProfile profile : viewModel.getAllUserProfiles()){

                                    if (profile.getId().equals(connections.getRequestingUserID())){

                                        connections.setUserProfile(profile);
                                        approvalConnections.add(connections);

                                    }
                                }

                            }
                        }

                        viewModel.setNeedsApprovalConnections(approvalConnections);
                        viewModel.getNavigator().onEmployeeContactsReturned();

                    } else {

                        viewModel.getNavigator().handleError(task.getException().getMessage());
                    }
                });
    }
}
