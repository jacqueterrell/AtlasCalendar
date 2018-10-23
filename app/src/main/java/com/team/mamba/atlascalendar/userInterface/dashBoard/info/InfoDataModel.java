package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.data.AppDataManager;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class InfoDataModel {


    private static final String AUTHOR_ID = "authorID";

    private AppDataManager dataManager;
    private int companyTotal = 0;
    private int totalOpportunities = 0;
    private String userRanking = "";

    private List<String> userStatsList = new ArrayList<>();
    private List<UserConnections> recentActivityConnections = new ArrayList<>();
    private List<UserProfile> individualConnections = new ArrayList<>();
    private List<String> companyUserIds = new ArrayList<>();
    private int confirmedConnections = 0;

    private List<Integer> allUserScores = new ArrayList<>();
    private int totalNumberOfUsers;

    private static final int ALLOWED_TOTAL_RECENT_ACTIVITIES = 51;


    @Inject
    public InfoDataModel(AppDataManager dataManager) {

        this.dataManager = dataManager;
    }

    /*******************Revised DataModel***********************/


    /**
     * Gets all users from our database
     */
    public void getAllUsers(InfoViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<UserProfile> adjustedProfileList = new ArrayList<>();

        viewModel.setSavedUserId(dataManager.getSharedPrefs().getUserId());

        db.collection(AppConstants.USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserProfile> userProfiles = task.getResult().toObjects(UserProfile.class);

                        for (UserProfile profile : userProfiles) {

                            if (profile.getId() != null) {

                                adjustedProfileList.add(profile);
                            }
                        }

                        viewModel.setAllUsersList(adjustedProfileList);
                        requestBusinessProfile(viewModel);

                    } else {

                        Logger.e(task.getException().getMessage());
                        viewModel.getNavigator().restartApplication();
                    }

                });
    }

    /**
     * If the user is signed in as a business, loop through all
     * business profile and find the correct match.
     */
    private void requestBusinessProfile(InfoViewModel viewModel) {

        //clear the list before use
        userStatsList.clear();
        totalOpportunities = 0;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String savedUserId = dataManager.getSharedPrefs().getUserId();

        db.collection(AppConstants.BUSINESSES_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<BusinessProfile> businessProfiles = task.getResult().toObjects(BusinessProfile.class);

                        viewModel.setBusinessProfileList(businessProfiles);

                        for (BusinessProfile profile : businessProfiles) {

                            if (profile.getId().equals(savedUserId)) {

                                viewModel.setBusinessProfile(profile);

                            }

                        }

                        checkAllConnections(viewModel);
                    }
                });

    }


    /**
     * Gets the total number of connections based on the profile type (user/business) and also
     * gets the correlating date from the time stamp
     */
    public void checkAllConnections(InfoViewModel viewModel) {

        List<Integer> connectionMonths = new ArrayList<>();
        confirmedConnections = 0;
        companyUserIds.clear();
        companyTotal = 0;

        String userId = dataManager.getSharedPrefs().getUserId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(AppConstants.CONNECTIONS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<UserConnections> userConnections = task.getResult().toObjects(UserConnections.class);

                        viewModel.setUserConnections(userConnections);

                        if (dataManager.getSharedPrefs().isBusinessAccount()) {

                            for (UserConnections connection : viewModel.getUserConnections()) {

                                if (connection.isIsConfirmed() && connection.getConsentingUserID().equals(userId)) {

                                    confirmedConnections += 1;

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(connection.getTimestamp()
                                            * 1000);//this timestamp allows us to see when the connection was made
                                    int month = calendar.get(Calendar.MONTH);
                                    connectionMonths.add(month);

                                    for (BusinessProfile profile : viewModel.getBusinessProfileList()) {

                                        if (profile.getId().equals(connection.getRequestingUserID())) {

                                            FirebaseMessaging.getInstance().subscribeToTopic(connection.getRequestingUserID());
                                            companyTotal += 1;
                                            companyUserIds.add(connection.getId());
                                            break;
                                        }
                                    }
                                }
                            }

                        } else {

                            for (UserConnections connection : viewModel.getUserConnections()) {

                                if (connection.isIsConfirmed() && connection.getRequestingUserID().equals(userId)) {

                                    confirmedConnections += 1;

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(connection.getTimestamp()
                                            * 1000);//this timestamp allows us to see when the connection was made
                                    int month = calendar.get(Calendar.MONTH);
                                    connectionMonths.add(month);

                                    for (BusinessProfile profile : viewModel.getBusinessProfileList()) {

                                        if (profile.getId().equals(connection.getConsentingUserID())) {

                                            FirebaseMessaging.getInstance().subscribeToTopic(connection.getConsentingUserID());
                                            companyTotal += 1;
                                            companyUserIds.add(connection.getId());
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        setConnectionsMap(viewModel, connectionMonths);

                    } else {

                        viewModel.getNavigator().handleError("");
                        Logger.e(task.getException().getMessage());
                        task.getException().printStackTrace();
                    }
                });


    }

    /**
     * Creates a default map of six sets of zeroes, then
     * looks through our list of connection months and appends
     * the number of connections for that month
     *
     * @param connectionMonths months the connections were added
     */
    private void setConnectionsMap(InfoViewModel viewModel, List<Integer> connectionMonths) {

        Map<Integer, Integer> connectionsMap = new LinkedHashMap<>();

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        for (int i = 0; i < 6; i++) {

            calendar.set(Calendar.MONTH, currentMonth - i);

            connectionsMap.put(currentMonth, 0); //7,6,5,4,3,2
            currentMonth -= 1;

        }

        for (Integer months : connectionMonths) {

            for (Map.Entry<Integer, Integer> entry : connectionsMap.entrySet()) {

                int monthKey = entry.getKey();
                int count = entry.getValue();

                if (monthKey == months) {

                    entry.setValue(count + 1);
                }
            }

        }

        viewModel.setNetworksMap(connectionsMap);
        setUserStatusOpportunities(viewModel);
    }


    /**
     * Looks through Bus Notes to find Opportunities total
     */
    private void setUserStatusOpportunities(InfoViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = dataManager.getSharedPrefs().getUserId();
        List<Integer> opportunityStageNumbers = new ArrayList<>();

        db.collection(AppConstants.BUS_NOTES_COLLECTION)
                .whereEqualTo(AUTHOR_ID, userId)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        List<CrmNotes> crmNotesList = task.getResult().toObjects(CrmNotes.class);

                        for (CrmNotes notes : crmNotesList) {

                            int stage = notes.getStage();

                            opportunityStageNumbers.add(stage);
                            totalOpportunities += 1;
                        }

                        setOpportunitiesMap(viewModel, opportunityStageNumbers);
                        getUserDetails(viewModel);
                        setRecentActivity(viewModel);

                    } else {

                        viewModel.getNavigator().handleError("Error setUserStatusOpportunities");
                        Logger.e(task.getException().getMessage());
                    }
                });

    }

    /**
     * @param viewModel
     */
    private void setRecentActivity(InfoViewModel viewModel) {

        recentActivityConnections.clear();

        List<UserConnections> completedConnections = new ArrayList<>();
        List<String> confirmedUsers = new ArrayList<>();

        //gets recent info for a business
        if (dataManager.getSharedPrefs().isBusinessAccount()) {

            getAllBusinessRecentInfo(viewModel, completedConnections, confirmedUsers);

        } else {

            getAllIndividualsRecentInfo(viewModel, completedConnections, confirmedUsers);
        }

    }


    /**
     * Retrieves all recent info if the user is logged in a a Business
     */
    private void getAllBusinessRecentInfo(InfoViewModel viewModel,
            List<UserConnections> completedConnections,
            List<String> confirmedUsers) {

        String userId = dataManager.getSharedPrefs().getUserId();

        for (UserConnections record : viewModel.getUserConnections()) {

            if (record.isIsConfirmed()
                    && record.isOrgBus
                    && record.getConsentingUserID().equals(userId)) { //The connection has been confirmed

                    if (completedConnections.size() < ALLOWED_TOTAL_RECENT_ACTIVITIES
                            && !confirmedUsers.contains(record.getRequestingUserName())) {

                        record.setOverrideBusinessProfile(true);
                        confirmedUsers.add(record.getRequestingUserName());
                        completedConnections.add(record);
                    }

            }
        }

        recentActivityConnections.addAll(completedConnections);
        setUserStatsTotal();

        viewModel.setUserStatsList(userStatsList);
        viewModel.setRecentActivityConnections(recentActivityConnections);
        viewModel.getNavigator().setUserStatsAdapter(userStatsList, recentActivityConnections);
    }


    /**
     * Retrieves all recent info if the user is logged in a an individual
     */
    private void getAllIndividualsRecentInfo(InfoViewModel viewModel,
            List<UserConnections> completedConnections
            , List<String> confirmedUsers) {

        String userId = dataManager.getSharedPrefs().getUserId();
        List<UserConnections> newConnections = new ArrayList<>();

        for (UserConnections record : viewModel.getUserConnections()) {

            if (record.isIsConfirmed() && record.getRequestingUserID()
                    .equals(userId)) { //The connection has been confirmed

                Calendar calendar = Calendar.getInstance();
                int today = calendar.get(Calendar.DAY_OF_YEAR);

                if (record.isIsOrgBus()) {

                    //the users is the requester
                    if (completedConnections.size() < ALLOWED_TOTAL_RECENT_ACTIVITIES
                            && !confirmedUsers.contains(record.getConsentingUserName())) {

                        confirmedUsers.add(record.getConsentingUserName());
                        completedConnections.add(record);
                    }

                } else {

                    //Look through all users and find the id for the user.
                    //This allows us to know the timestamp associated with the
                    //connection.
                    for (UserProfile profile : viewModel.getAllUsersList()) {

                        try {

                            if (profile.getId().equals(record.getConsentingUserID())) {

                                //todo Right now we are taking the user's timestamp from their
                                //profile and not from the record (which is how it should be done)
                                //but the IOS app takes it from the record
                                calendar.setTimeInMillis(profile.getAdjustedTimeStamp() * 1000);
                                int lastTimeUpdating = calendar.get(Calendar.DAY_OF_YEAR);

                                if (today - lastTimeUpdating <= 2) {

                                    record.setRecentActivity(true);

                                } else {

                                    record.setRecentActivity(false);
                                }

                                if (completedConnections.size() < ALLOWED_TOTAL_RECENT_ACTIVITIES
                                        && !confirmedUsers.contains(record.getConsentingUserName())) {

                                    confirmedUsers.add(record.getConsentingUserName());
                                    completedConnections.add(record);

                                }

                            }

                        } catch (Exception e) {

                        }

                    }

                }

            } else if (!record.isIsConfirmed()
                    && record.getConsentingUserID().equals(userId)) { //The connection is new and needs to be approved

                record.setNeedsApproval(true);

                if (newConnections.size() < ALLOWED_TOTAL_RECENT_ACTIVITIES) {

                    newConnections.add(record);
                }
            }
        }

        recentActivityConnections.addAll(completedConnections);
        recentActivityConnections.addAll(newConnections);

        setUserStatsTotal();

        viewModel.setUserStatsList(userStatsList);
        viewModel.setRecentActivityConnections(recentActivityConnections);
        viewModel.getNavigator().setUserStatsAdapter(userStatsList, recentActivityConnections);
    }


    /**
     * This creates a list of all totals for our User Stats
     * Recyclerview
     */
    private void setUserStatsTotal() {

        String connectionsTotal = confirmedConnections + " Connections";

        userStatsList.clear();
        userStatsList.add(connectionsTotal);

        if (companyTotal == 1) {

            String company = companyTotal + " Company";
            userStatsList.add(company);

        } else {

            String company = companyTotal + " Companies";
            userStatsList.add(company);
        }

        if (totalOpportunities == 1) {

            String opportunities = totalOpportunities + " Opportunity";
            userStatsList.add(opportunities);

        } else {

            String opportunities = totalOpportunities + " Opportunities";
            userStatsList.add(opportunities);
        }

        if (!dataManager.getSharedPrefs().isBusinessAccount()) {

            userStatsList.add(userRanking);
        }

    }


    /**
     * @param viewModel
     * @param opportunityStages
     */
    private void setOpportunitiesMap(InfoViewModel viewModel, List<Integer> opportunityStages) {

        Map<Integer, Integer> connectionsMap = new LinkedHashMap<>();

        for (int i = 0; i < 5; i++) {

            connectionsMap.put(i, 0); //7,6,5,4,3,2
        }

        for (Integer stage : opportunityStages) {

            for (Map.Entry<Integer, Integer> entry : connectionsMap.entrySet()) {

                int stageKey = entry.getKey();
                int count = entry.getValue();

                if (stageKey == stage) {

                    entry.setValue(count + 1);
                }
            }

        }

        viewModel.setOpportunitiesMap(connectionsMap);
        viewModel.getNavigator().setBarChartData();

    }


    /**
     * Used to calculate the user's score
     */
    public void getUserDetails(InfoViewModel viewModel) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        individualConnections.clear();
        allUserScores.clear();
        String savedUserId = dataManager.getSharedPrefs().getUserId();
        List<String> connectionIds = new ArrayList<>();

        totalNumberOfUsers = viewModel.getAllUsersList().size();

        //save the selected user profile
        for (UserProfile profile : viewModel.getAllUsersList()) {

            allUserScores.add(profile.getScore());

            try {
                if (profile.getId().equals(savedUserId)) {

                    viewModel.setUserProfile(profile);

                    //get all of the user's connections by id
                    for (Map.Entry<String, String> entry : profile.getConnections().entrySet()) {

                        String userId = entry.getKey();

                        if (!companyUserIds.contains(userId)) {

                            connectionIds.add(userId);
                        }
                    }

                }
            } catch (Exception e) {

                Logger.e("Invalid profile setup " + e.getLocalizedMessage());
            }
        }

        //This loops through all of the user's individual(non business) contacts and pulls their
        //contacts and adds them to a list.
        for (UserProfile profile : viewModel.getAllUsersList()) {

            for (String connectionId : connectionIds) {

                try {
                    if (profile.getId().equals(connectionId)) {

                        for (Map.Entry<String, String> entry : profile.getConnections().entrySet()) {

                            String userId = entry.getKey();

                            individualConnections.add(profile);
                        }
                    }
                } catch (Exception e) {

                    Logger.e("Invalid profile setup " + e.getLocalizedMessage());
                }
            }
        }

        calculateAllUsersScore(viewModel);


    }


    private void calculateAllUsersScore(InfoViewModel viewModel) {

        int totalScoresLowerThanUsers = 0;
        DecimalFormat df2 = new DecimalFormat(".##");

        /*****Get total individual users****/

        int connections = 2 * confirmedConnections;
        int compTotal = 3 * companyTotal;
        int oppTotal = 5 * totalOpportunities;
        int algorithmTotal = connections + compTotal + oppTotal + individualConnections.size();

        for (Integer score : allUserScores) {

            if (score < algorithmTotal) {

                totalScoresLowerThanUsers += 1;
            }
        }

        double finalPercentile = (double) totalScoresLowerThanUsers / totalNumberOfUsers * 100;

        if (finalPercentile > 80) {

            userRanking = "All-Star";

        } else if (finalPercentile > 60) {

            userRanking = "Expert";

        } else if (finalPercentile > 40) {

            userRanking = "Advanced";

        } else if (finalPercentile > 20) {

            userRanking = "Intermediate";

        } else {

            userRanking = "Beginner";
        }

        Logger.i("total score" + algorithmTotal);
        Logger.i("final score" + finalPercentile);

        setUserStatsTotal();

        viewModel.setUserStatsList(userStatsList);
        viewModel.setRecentActivityConnections(recentActivityConnections);
        viewModel.getNavigator().setUserStatsAdapter(userStatsList, recentActivityConnections);
    }

}
