package com.team.mamba.atlascalendar.userInterface.dashBoard.announcements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.Announcements;
import com.team.mamba.atlascalendar.databinding.AnnouncementsLayoutBinding;
import com.team.mamba.atlascalendar.service.MyFirebaseMessagingService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.create_announcement.CreateAnnouncementFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.locator.LocatorFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AnnouncementsFragment extends BaseFragment<AnnouncementsLayoutBinding, AnnouncementsViewModel>
        implements AnnouncementsNavigator {


    @Inject
    AnnouncementsViewModel viewModel;

    @Inject
    AnnouncementsDataModel dataModel;

    private AnnouncementsLayoutBinding binding;
    private List<Announcements> announcementsList = new ArrayList<>();
    private AnnouncementsAdapter announcementsAdapter;
    private DashBoardActivity parentActivity;
    private DashBoardActivityNavigator parentNavigator;
    private CompositeDisposable compositeDisposable;

    public static AnnouncementsFragment newInstance() {

        return new AnnouncementsFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.announcements_layout;
    }

    @Override
    public AnnouncementsViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (DashBoardActivityNavigator) context;
        parentActivity = (DashBoardActivity) context;
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

        announcementsAdapter = new AnnouncementsAdapter(announcementsList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(announcementsAdapter);

        binding.swipeContainer.setOnRefreshListener(() -> viewModel.requestAnnouncements(getViewModel()));

        if (!viewModel.getAnnouncementsList().isEmpty()){

            onAnnouncementsReturned();
        }

        if (dataManager.getSharedPrefs().isBusinessAccount()){

            binding.ivAddAnnouncement.setVisibility(View.VISIBLE);

        } else {

            binding.ivAddAnnouncement.setVisibility(View.INVISIBLE);
        }

        showProgressSpinner();
        viewModel.requestAnnouncements(getViewModel());

        return binding.getRoot();
    }

    @Override
    public void onAnnouncementsReturned() {

        hideProgressSpinner();
        announcementsList.clear();
        announcementsList.addAll(viewModel.getAnnouncementsList());
        Collections.sort(announcementsList,(o1,o2) -> Double.compare(o2.getAdjustedTimeStamp(), o1.getAdjustedTimeStamp()));

        announcementsAdapter.notifyDataSetChanged();
        binding.swipeContainer.setRefreshing(false);
    }

    @Override
    public void handleError(String errorMsg) {

        hideProgressSpinner();
        showAlert("Error",errorMsg);
        binding.swipeContainer.setRefreshing(false);
    }

    @Override
    public void onCrmClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(new CrmFragment(), manager, "CrmFragment", null);
    }

    @Override
    public void onInfoClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(LocatorFragment.newInstance(), manager, "Info", null);
    }

    @Override
    public void onContactsClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(new ContactsFragment(), manager, "ContactsFragment", null);
    }

    @Override
    public void onAddAnnouncementClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFragmentVertically(new CreateAnnouncementFragment(), manager, "CreateAnnouncement", null);
    }

    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable = new CompositeDisposable();
        setUpNewAnnouncementBadge();
        setUpNewConnectionRequestBadge();
        setNotificationObservable();
        resetNewAnnouncementBadge();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
        resetNewAnnouncementBadge();
    }

    /*Set up Notification Badges*/

    private void resetNewAnnouncementBadge(){

        DashBoardActivity.newAnnouncementCount = 0;
        binding.cardNotificationBadge.setVisibility(View.GONE);
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
            @SuppressLint("CheckResult")
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
                    viewModel.requestAnnouncements(getViewModel());
                    binding.cardNotificationBadge.setVisibility(View.VISIBLE);
                    binding.tvNotificationBadgeCount.setText(String.valueOf(DashBoardActivity.newAnnouncementCount));
                });
    }
}
