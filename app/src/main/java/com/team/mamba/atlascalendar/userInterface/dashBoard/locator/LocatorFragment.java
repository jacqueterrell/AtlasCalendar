package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.LocatorLayoutBinding;
import com.team.mamba.atlascalendar.service.MyFirebaseMessagingService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import com.team.mamba.atlascalendar.R;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocatorFragment extends BaseFragment<LocatorLayoutBinding, LocatorViewModel>
        implements LocatorNavigator, SearchView.OnQueryTextListener {

    @Inject
    LocatorViewModel viewModel;

    @Inject
    LocatorDataModel dataModel;

    @Inject
    Context appContext;

    private LocatorLayoutBinding binding;
     private DashBoardActivityNavigator parentNavigator;
    private CompositeDisposable compositeDisposable;
    private LocatorAdapter locatorAdapter;


    public static LocatorFragment newInstance() {

        return new LocatorFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.locator_layout;
    }

    @Override
    public LocatorViewModel getViewModel() {
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

        locatorAdapter = new LocatorAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(locatorAdapter);
        locatorAdapter.setProfileList();

        setUpSearchView();
        viewModel.requestContactsInfo(getViewModel());
        return binding.getRoot();
    }


    @Override
    public void onSettingsClicked() {

        parentNavigator.openSettingsScreen();
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
    public void handleError(String msg) {

        showSnackbar(msg);
        hideSplashScreen();
    }

    @Override
    public List<UserProfile> getPermProfileList() {
        return viewModel.getEmployeeProfilesList();
    }

    @Override
    public List<UserProfile> getFavoriteProfileList() {
        return viewModel.getFavoritesProfileList();
    }

    @Override
    public void onEmployeeContactsReturned() {

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            binding.tvEmployersName.setText(viewModel.getSelectedUserProfile().getCurrentEmployer());
        }

        locatorAdapter.setProfileList();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
         locatorAdapter.filter(newText);
         return true;
    }

    private void setUpSearchView() {

        binding.searchView.setOnQueryTextListener(this);
        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setFocusable(false);

        //set the color for our search view edit text and text hint
        EditText searchEditText = binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(R.color.material_icons_light));
        searchEditText.setHint("Search...");

        //set the color for our search view icon
        ImageView searchMagIcon = binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchMagIcon.setColorFilter(ContextCompat.getColor(appContext, R.color.white));
        searchMagIcon.setVisibility(View.GONE);

        //set the line color
        View v = binding.searchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
    }

    private void hideSplashScreen() {

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> binding.layoutSplashScreen.setVisibility(View.GONE))
                .playOn(binding.layoutSplashScreen);
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
                    //todo look into this implementation
                    //viewModel.getAllUsers(getViewModel());
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
