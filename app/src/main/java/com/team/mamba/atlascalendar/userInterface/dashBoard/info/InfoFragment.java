package com.team.mamba.atlascalendar.userInterface.dashBoard.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.InfoLayoutBinding;
import com.team.mamba.atlascalendar.service.MyFirebaseMessagingService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.describe_connections.DescribeConnectionsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivity;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import com.team.mamba.atlascalendar.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InfoFragment extends BaseFragment<InfoLayoutBinding, InfoViewModel>
        implements InfoNavigator {

    @Inject
    InfoViewModel viewModel;

    @Inject
    InfoDataModel dataModel;

    @Inject
    Context appContext;

    private InfoLayoutBinding binding;
    float barWidth = 0.65f; // x2 dataset
    private static List<String> userStatsList = new ArrayList<>();
    private UserStatsAdapter userStatsAdapter;
    private RecentActivitiesAdapter recentActivitiesAdapter;
    private static List<UserConnections> recentActivityConnections = new ArrayList<>();
    private DashBoardActivityNavigator parentNavigator;
    private DashBoardActivity parentActivity;
    private UserProfile selectedContactProfile;
    private CompositeDisposable compositeDisposable;


    public static InfoFragment newInstance() {

        return new InfoFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.info_layout;
    }

    @Override
    public InfoViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (DashBoardActivityNavigator) context;
        parentActivity = (DashBoardActivity) context;
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.barChartNetwork.clearAnimation();
        binding.barChartNetwork.clearAllViewportJobs();
        binding.barChartOpportunities.clearAnimation();
        binding.barChartOpportunities.clearAllViewportJobs();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getViewDataBinding();

        setUpRecyclerViews();
        showHideAddContactButton();
        binding.swipeContainerRecentActiviy.setOnRefreshListener(() -> viewModel.getAllUsers(getViewModel()));
        binding.swipeContainerUserStats.setOnRefreshListener(() -> viewModel.getAllUsers(getViewModel()));


        //retrieves the cached list, also checks to see if the user logged out
        //and back in under a different account type
        if (!viewModel.getUserStatsList().isEmpty()) {

            if (!viewModel.getSavedUserId().equals(dataManager.getSharedPrefs().getUserId())) {//the user logged out as a user and back in as a business

                binding.layoutSplashScreen.setVisibility(View.VISIBLE);
                viewModel.getAllUsers(getViewModel());

            } else if (parentNavigator.wasContactRecentlyDeleted()) {//a contact was just deleted

                viewModel.getAllUsers(getViewModel());
                parentNavigator.setContactRecentlyDeleted(false);

            } else if (parentNavigator.wasContactRecentlyAdded() || DashBoardActivity.newRequestCount > 0){

                viewModel.getAllUsers(getViewModel());
                parentNavigator.setContactRecentlyAdded(false);

            } else {

                setUserStatsAdapter(viewModel.getUserStatsList(), viewModel.getRecentActivityConnections());
                setBarChartData();

                if (viewModel.isNetworkChartSelected()) {

                    showSelectedNetworkButton();

                } else {

                    showSelectedOpportunitiesButton();
                }
            }


        } else {

            binding.layoutSplashScreen.setVisibility(View.VISIBLE);
            viewModel.getAllUsers(getViewModel());
        }

        return binding.getRoot();
    }

    private void setUpRecyclerViews(){

        userStatsAdapter = new UserStatsAdapter(userStatsList);
        binding.recyclerUserStats.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerUserStats.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerUserStats.setAdapter(userStatsAdapter);

        recentActivitiesAdapter = new RecentActivitiesAdapter(getViewModel(), recentActivityConnections);
        binding.recyclerRecentActivity.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerRecentActivity.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerRecentActivity.setAdapter(recentActivitiesAdapter);

    }

    /**
     * Hides the add contact button if the user is logged in as
     * a business.
     */
    private void showHideAddContactButton(){

        if (dataManager.getSharedPrefs().isBusinessAccount()){

            binding.ivAdd.setVisibility(View.INVISIBLE);

        } else {

            binding.ivAdd.setVisibility(View.VISIBLE);
        }
    }

        @Override
    public void onUserStatsInfoClicked() {

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> binding.dialogUserStatisticsInfo.setVisibility(View.VISIBLE))
                .playOn(binding.dialogUserStatisticsInfo);
    }

    @Override
    public void hideUserStatusInfoDialog() {

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> binding.dialogUserStatisticsInfo.setVisibility(View.GONE))
                .playOn(binding.dialogUserStatisticsInfo);
    }

    @Override
    public void onRecentActivityInfoClicked() {

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> binding.dialogRecentActivityInfo.setVisibility(View.VISIBLE))
                .playOn(binding.dialogRecentActivityInfo);
    }

    @Override
    public void hideRecentActivityInfoDialog() {

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> binding.dialogRecentActivityInfo.setVisibility(View.GONE))
                .playOn(binding.dialogRecentActivityInfo);
    }

    @Override
    public void onNetworkButtonClicked() {

        showSelectedNetworkButton();
        viewModel.setNetworkChartSelected(true);

        if (viewModel.getNetworksMap().isEmpty()) {

            viewModel.getAllUsers(getViewModel());

        } else {

            setNetworksBarChart();
        }

    }

    @Override
    public void onOpportunitiesButtonClicked() {

        showSelectedOpportunitiesButton();
        viewModel.setNetworkChartSelected(false);

        if (viewModel.getOpportunitiesMap().isEmpty()) {

            viewModel.getAllUsers(getViewModel());

        } else {

            setOpportunitiesBarChart();
        }

    }

    @Override
    public void onAddContactClicked() {

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            parentNavigator.openAddContactDialog();
        }
    }

    @Override
    public void onUserProfileClicked() {

        if (dataManager.getSharedPrefs().isBusinessAccount()) {

            for (BusinessProfile profile : viewModel.getBusinessProfileList()) {

                if (profile.getId().equals(dataManager.getSharedPrefs().getUserId())) {

                    parentNavigator.openBusinessProfile(profile);

                }
            }


        } else {

            for (UserProfile profile : viewModel.getAllUsersList()) {

                if (profile.getId().equals(dataManager.getSharedPrefs().getUserId())) {

                    parentNavigator.openUserProfile(profile);

                }
            }
        }
    }

    @Override
    public void onSettingsClicked() {

        parentNavigator.openSettingsScreen();
    }


    @Override
    public void onRecentActivitiesRowClicked(UserConnections userConnections) {

        if (userConnections.isOrgBus && !userConnections.isOverrideBusinessProfile()) {

            for (BusinessProfile profile : viewModel.getBusinessProfileList()) {

                if (profile.getId().equals(userConnections.getConsentingUserID())) {

                    parentNavigator.openBusinessProfile(profile);

                }
            }

        } else {


            if (userConnections.isNeedsApproval()) {

                resetNewConnectionRequestBadge();

                for (UserProfile profile : viewModel.getAllUsersList()) {

                    if (profile.getId().equals(userConnections.getRequestingUserID())) {//another user is requesting to connect

                        userConnections.setUserProfile(profile);
                        String first = profile.getFirstName();
                        String last = profile.getLastName();
                        String code = profile.getCode();

                        String title = "Approve Connection?";
                        String msg = first + " " + last + " " + "is requesting to connect with you as a " + userConnections.getConnectionTypeToString()
                                + "\n\n" + "Tap approve to share connect with " + first + " " + last + " and proceed to define your connection type";

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

                        dialog.setTitle(title)
                                .setMessage(msg)
                                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> {

                                })
                                .setPositiveButton("Approve", (paramDialogInterface, paramInt) -> {

                                    viewModel.getUserStatsList().clear();
                                    ChangeFragments.replaceFragmentVertically(DescribeConnectionsFragment.newInstance(userConnections, true, false), getBaseActivity()
                                            .getSupportFragmentManager(), "AddUserLayout", null);
                                });

                        dialog.show();

                    }

                }

            } else {


                if (userConnections.isOverrideBusinessProfile()){

                    openUserProfileLoggedInAsOrg(userConnections);

                } else {
                    openUserProfile(userConnections);
                }
            }
        }
    }

    /**
     * Selects the correct profile for If the user is logged in as a business
     * and clicks on an individual profile
     * @param userConnections
     */
    private void openUserProfileLoggedInAsOrg(UserConnections userConnections){

        for (UserProfile profile : viewModel.getAllUsersList()) {//find the contact's profile

            if (profile.getId().equals(userConnections.getRequestingUserID())) {

                selectedContactProfile = profile;
            }
        }

        for (UserConnections con : viewModel.getUserConnections()) {// get the contact's connection type

            String userId = dataManager.getSharedPrefs().getUserId();

            if (con.getConsentingUserID().equals(userId) && con.getRequestingUserID().equals(userConnections.getRequestingUserID())) {


                selectedContactProfile.setConnectionType(con.getConnectionType());

            }
        }

        parentNavigator.openUserProfile(selectedContactProfile);
    }


    /**
     * Selects the correct profile for If the user is logged in as an individual
     * and clicks on an individual profile
     * @param userConnections
     */
    private void openUserProfile(UserConnections userConnections){

        for (UserProfile profile : viewModel.getAllUsersList()) {//find the contact's profile

            if (profile.getId().equals(userConnections.getConsentingUserID())) {

                selectedContactProfile = profile;
            }
        }

        for (UserConnections con : viewModel.getUserConnections()) {// get the contact's connection type

            String userId = dataManager.getSharedPrefs().getUserId();

            if (con.getConsentingUserID().equals(userId) && con.getRequestingUserID().equals(userConnections.getConsentingUserID())) {


                selectedContactProfile.setConnectionType(con.getConnectionType());

            }
        }

        parentNavigator.openUserProfile(selectedContactProfile);
    }

    @Override
    public void setUserStatsAdapter(List<String> userStats, List<UserConnections> connectionRecords) {

        if (dataManager.getSharedPrefs().isBusinessAccount()) {//logged in as a business profile

            binding.tvUserId.setText(viewModel.getBusinessProfile().getCode());

        } else {

            try {

                binding.tvUserId.setText(viewModel.getUserProfile().getCode());

            } catch (Exception e) {

                dataManager.getSharedPrefs().setUserLoggedIn(false);
                getBaseActivity().finishAffinity();
            }
        }

        Collections.sort(connectionRecords, (o1, o2) -> Double.compare(o2.getTimestamp(), o1.getTimestamp()));

        userStatsList.clear();
        userStatsList.addAll(userStats);
        recentActivityConnections.clear();
        recentActivityConnections.addAll(connectionRecords);

        userStatsAdapter.notifyDataSetChanged();
        recentActivitiesAdapter.notifyDataSetChanged();
        hideSplashScreen();
        binding.swipeContainerUserStats.setRefreshing(false);
        binding.swipeContainerRecentActiviy.setRefreshing(false);

        UserProfile profile = viewModel.getUserProfile();
        parentNavigator.setUserProfile(profile);
    }


    @Override
    public void handleError(String msg) {

        showSnackbar(msg);
        hideSplashScreen();
        binding.swipeContainerUserStats.setRefreshing(false);
        binding.swipeContainerRecentActiviy.setRefreshing(false);
    }

    @Override
    public void restartApplication() {

        dataManager.getSharedPrefs().setUserLoggedIn(false);
        getBaseActivity().finishAffinity();
        startActivity(WelcomeActivity.newIntent(getBaseActivity()));
    }

    @Override
    public void onCrmClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(new CrmFragment(), manager, "CrmFragment", null);
    }

    @Override
    public void onNotificationsClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(AnnouncementsFragment.newInstance(), manager, "Announcements", null);
    }

    @Override
    public void onContactsClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(new ContactsFragment(), manager, "ContactsFragment", null);
    }

    @Override
    public void setBarChartData() {

        if (viewModel.isNetworkChartSelected()) {

            setNetworksBarChart();

        } else {

            setOpportunitiesBarChart();

        }
    }


    /**
     * Sets the labels for our network chart
     *
     * @return list of label names
     */
    private List<String> setNetworkLabels() {

        List<String> labels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        for (int i = 0; i < 6; i++) {

            calendar.set(Calendar.MONTH, currentMonth - i);
            labels.add(getMonthsList().get(currentMonth));
            currentMonth -= 1;
        }

        return labels;
    }

    /**
     * Sets the data values for our Network Chart
     *
     * @return network chart data
     */
    private List<BarEntry> setNetworkBarValues() {

        List<BarEntry> entries = new ArrayList<>();

        int count = 0;

        for (Map.Entry<Integer, Integer> entry : viewModel.getNetworksMap().entrySet()) {

            entries.add(new BarEntry(count, entry.getValue()));
            count += 1;
        }

        return entries;
    }

    /**
     * Sets the labels for the Opportunity Bar Chart
     *
     * @return String of label names
     */
    private List<String> setOpportunitiesLabels() {

        List<String> labels = new ArrayList<>();

        labels.add("New");
        labels.add("Qualified");
        labels.add("Proposal");
        labels.add("Negotiation");
        labels.add("Closed");

        return labels;
    }

    /**
     * Sets the data values for our Opportunities Bar Chart
     *
     * @return data values
     */
    private List<BarEntry> setOpportunitiesBarValues() {

        List<BarEntry> entries = new ArrayList<>();

        int count = 0;

        for (Map.Entry<Integer, Integer> entry : viewModel.getOpportunitiesMap().entrySet()) {

            entries.add(new BarEntry(count, entry.getValue()));
            count += 1;
        }

        return entries;
    }


    /**
     * Sets all data and labels for the Network Bar Chart
     */
    private void setNetworksBarChart() {

        BarDataSet dataSet = new BarDataSet(setNetworkBarValues(), "New Connections");

        dataSet.setColor(ContextCompat.getColor(appContext, R.color.chart_blue));
        dataSet.setValueTextColor(R.color.black);
        dataSet.setValueTextSize(14f);
        dataSet.setValueFormatter(new LargeValueFormatter());

        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);

        //X-axis
        XAxis xAxis = binding.barChartNetwork.getXAxis();
        xAxis.setTextColor(R.color.black);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisLineColor(R.color.black);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter((value, axis) -> {
            if (setNetworkLabels().size() > (int) value) {
                return setNetworkLabels().get((int) value);
            } else return null;
        });
        //Y-axis
        YAxis rightAxis = binding.barChartNetwork.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = binding.barChartNetwork.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        leftAxis.setDrawLabels(false);

        binding.barChartNetwork.setFitBars(true);
        binding.barChartNetwork.setData(data);

        binding.barChartNetwork.getLegend().setTextColor(ContextCompat.getColor(appContext, R.color.black));
        binding.barChartNetwork.invalidate();
        binding.barChartNetwork.animateXY(1000, 1000);
        binding.barChartNetwork.getDescription().setText("");
        binding.barChartNetwork.setFocusable(false);

        binding.barChartNetwork.setVisibility(View.INVISIBLE);
        Handler handler = new Handler();
        binding.barChartNetwork.animateXY(1000, 1000);
        handler.postDelayed(() -> binding.barChartNetwork.setVisibility(View.VISIBLE), 400);

    }

    /**
     * Sets all data and labels for the Opportunities Bar Chart
     */
    private void setOpportunitiesBarChart() {

        BarDataSet dataSet = new BarDataSet(setOpportunitiesBarValues(), "Opportunities");

        dataSet.setColor(ContextCompat.getColor(appContext, R.color.chart_red));
        dataSet.setValueTextColor(R.color.black);
        dataSet.setValueTextSize(14f);
        dataSet.setValueFormatter(new LargeValueFormatter());

        BarData data = new BarData(dataSet);
        data.setBarWidth(barWidth);

        //X-axis
        XAxis xAxis = binding.barChartNetwork.getXAxis();
        xAxis.setTextColor(R.color.black);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisLineColor(R.color.black);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter((value, axis) -> {
            if (setOpportunitiesLabels().size() > (int) value) {
                return setOpportunitiesLabels().get((int) value);
            } else return null;
        });
        //Y-axis
        YAxis rightAxis = binding.barChartNetwork.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = binding.barChartNetwork.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);
        leftAxis.setDrawLabels(false);

        binding.barChartNetwork.setData(data);
        binding.barChartNetwork.setFitBars(true);
        binding.barChartNetwork.getLegend().setTextColor(ContextCompat.getColor(appContext, R.color.black));
        binding.barChartNetwork.getDescription().setText("");
        binding.barChartNetwork.setClickable(false);

        binding.barChartNetwork.setVisibility(View.INVISIBLE);
        Handler handler = new Handler();
        binding.barChartNetwork.animateXY(1000, 1000);
        handler.postDelayed(() -> binding.barChartNetwork.setVisibility(View.VISIBLE), 400);

    }

    /**
     * @return String names for months
     */
    private List<String> getMonthsList() {

        List<String> monthsList = new ArrayList<>();

        monthsList.add("Jan");
        monthsList.add("Feb");
        monthsList.add("Mar");
        monthsList.add("Apr");
        monthsList.add("May");
        monthsList.add("Jun");
        monthsList.add("Jul");
        monthsList.add("Aug");
        monthsList.add("Sep");
        monthsList.add("Oct");
        monthsList.add("Nov");
        monthsList.add("Dec");

        return monthsList;
    }


    private void hideSplashScreen() {

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> binding.layoutSplashScreen.setVisibility(View.GONE))
                .playOn(binding.layoutSplashScreen);
    }


    private void showSelectedNetworkButton() {

        binding.layoutNetworkNotSelected.setVisibility(View.GONE);
        binding.layoutNetworkSelected.setVisibility(View.VISIBLE);

        binding.layoutOpportunityNotSelected.setVisibility(View.VISIBLE);
        binding.layoutOpportunitySelected.setVisibility(View.GONE);

    }

    private void showSelectedOpportunitiesButton() {

        binding.layoutNetworkNotSelected.setVisibility(View.VISIBLE);
        binding.layoutNetworkSelected.setVisibility(View.GONE);

        binding.layoutOpportunityNotSelected.setVisibility(View.GONE);
        binding.layoutOpportunitySelected.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable = new CompositeDisposable();
        setUpNewAnnouncementBadge();
        setUpNewConnectionRequestBadge();
        setNotificationObservable();

    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
        resetNewConnectionRequestBadge();
    }

    /*Set up Notification Badges*/

    private void resetNewConnectionRequestBadge(){

        DashBoardActivity.newRequestCount = 0;
        binding.cardRequestBadge.setVisibility(View.GONE);
    }

    private void setUpNewConnectionRequestBadge(){

        if (DashBoardActivity.newRequestCount > 0){//show badge

            binding.cardRequestBadge.setVisibility(View.VISIBLE);
            binding.tvRequestBadgeCount.setText(String.valueOf(DashBoardActivity.newRequestCount));

        } else {//hide badge

            binding.cardRequestBadge.setVisibility(View.GONE);
        }
    }


    private void setUpNewAnnouncementBadge(){

        if (DashBoardActivity.newAnnouncementCount > 0){//show badge

            binding.cardNotificationBadge.setVisibility(View.VISIBLE);
            binding.tvNotificationBadgeCount.setText(String.valueOf(DashBoardActivity.newAnnouncementCount));

        } else {//hide badge

            binding.cardNotificationBadge.setVisibility(View.GONE);
        }
    }

    /**
     * Subscribes to the Observable in {@link MyFirebaseMessagingService}
     *
     */
    private void setNotificationObservable(){

        Observable<String> observable = MyFirebaseMessagingService.getObservable();
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }
            @Override
            public void onNext(String s) {

                Logger.i(s);

                if (s.equals(AppConstants.NOTIFICATION_NEW_CONNECTION)) {

                  showNewConnectionRequestBadge();

                } else if (s.equals(AppConstants.NOTIFICATION_NEW_ANNOUNCEMENT)) {

                    showNewAnnouncementBadge();
                }
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() { }
        };

        observable.subscribe(observer);
    }

    @SuppressLint("CheckResult")
    private void showNewConnectionRequestBadge(){

        Completable.fromCallable(() -> {

            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    viewModel.getAllUsers(getViewModel());
                    binding.cardRequestBadge.setVisibility(View.VISIBLE);
                    binding.tvRequestBadgeCount.setText(String.valueOf(DashBoardActivity.newRequestCount));
                });
    }

    @SuppressLint("CheckResult")
    private void showNewAnnouncementBadge(){

        Completable.fromCallable(()->{

            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    binding.cardNotificationBadge.setVisibility(View.VISIBLE);
                    binding.tvNotificationBadgeCount.setText(String.valueOf(DashBoardActivity.newAnnouncementCount));
                });
    }
}
