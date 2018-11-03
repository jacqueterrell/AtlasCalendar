package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.LocatorLayoutBinding;
import com.team.mamba.atlascalendar.service.CurrentLocationService;
import com.team.mamba.atlascalendar.service.MyFirebaseMessagingService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.add_contacts.add_business.AddBusinessFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmFragment;
import com.team.mamba.atlascalendar.userInterface.welcome._container_activity.WelcomeActivity;
import com.team.mamba.atlascalendar.userInterface.welcome._viewPager.ViewPagerFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
import com.team.mamba.atlascalendar.utils.CommonUtils;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import okhttp3.HttpUrl;
import org.json.JSONException;
import org.json.JSONObject;

public class LocatorFragment extends BaseFragment<LocatorLayoutBinding, LocatorViewModel>
        implements LocatorNavigator, SearchView.OnQueryTextListener, DatePickerDialog.OnDateSetListener {

    @Inject
    LocatorViewModel viewModel;

    @Inject
    LocatorDataModel dataModel;

    @Inject
    Context appContext;

    private static final String GOOGLE_CALENDAR_PACKAGE_NAME = "com.google.android.calendar";
    private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
    private LocatorLayoutBinding binding;
    private DashBoardActivityNavigator parentNavigator;
    private CompositeDisposable compositeDisposable;
    private LocatorAdapter locatorAdapter;
    private static DatePickerDialog datePickerDialog;
    private static final String DATE_PICKER = "datePickerDialog";


    /**
     * You client id, you have it from the google console when you register your project
     * https://console.developers.google.com/a
     */
    private static final String CLIENT_ID = "450285275359-7iqk4stuc54gbbkcbsqrdh78jhoaq8jq.apps.googleusercontent.com";
    /**
     * The redirect uri you have define in your google console for your project
     */
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    /**
     * The redirect root uri you have define in your google console for your project
     * It is also the scheme your Main Activity will react
     */
    public static final String REDIRECT_URI_ROOT = "urn:ietf:wg:oauth:2.0:oob";

    private static final String CLIENT_SECRET = "jQKi8GXvmYF-jLPol7aeDqOy";
    /**
     * You are asking to use a code when autorizing
     */
    private static final String CODE = "code";
    /**
     * The scope: what do we want to use
     * Here we want to be able to do anything on the user's GDrive
     */
    public static final String API_SCOPE = "https://www.googleapis.com/auth/calendar.readonly";


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
        return binding.progressSpinner;
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

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final Calendar minimumYear = Calendar.getInstance();
        minimumYear.add(Calendar.YEAR, -3);

        datePickerDialog = new DatePickerDialog(getBaseActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(minimumYear.getTimeInMillis());

        setUpSearchView();
        showProgressSpinner();
        setUpSwitchListeners();
        viewModel.requestContactsInfo(getViewModel());
        return binding.getRoot();
    }


    @Override
    public void onSettingsClicked() {

        parentNavigator.openSettingsScreen();
    }

    @Override
    public void onCrmClicked() {

        setUpOAuth();
//        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
//        ChangeFragments.replaceFromBackStack(new CrmFragment(), manager, "CrmFragment", null);
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
    public void onHamburgerClicked() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {

            binding.drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onAddConnectionClicked() {

        ChangeFragments.replaceFragmentVertically(AddBusinessFragment.newInstance(true),
                getBaseActivity().getSupportFragmentManager(), "AddBusiness", null);
    }

    @Override
    public void onAccountManagementClicked() {

    }

    @Override
    public void onCalendarRowClicked(UserProfile userProfile) {

        Intent launchIntent = getBaseActivity().getPackageManager()
                .getLaunchIntentForPackage(GOOGLE_CALENDAR_PACKAGE_NAME);

        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            showInstallGoogleCalendarAlert();
        }
    }

    @Override
    public void onUsersCalendarClicked() {

        binding.drawerLayout.closeDrawer(GravityCompat.START);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            FragmentManager manager = getBaseActivity().getSupportFragmentManager();
            UserProfile profile = viewModel.getSelectedUserProfile();
            String fullName = profile.getFirstName() + " " + profile.getLastName();
            //ChangeFragments.addFragmentVertically(CalendarMonthActivity.newInstance(fullName), manager, "CalendarMonth", null);
            startActivity(CalendarMonthActivity.newIntent(getBaseActivity(),fullName));

        }, 200);

    }

    @Override
    public void onLogOutClicked() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle("Log Out")
                .setMessage("Do you want to log out of this account?")
                .setNegativeButton("no", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("yes", (paramDialogInterface, paramInt) -> {

                    dataManager.getSharedPrefs().setUserLoggedIn(false);
                    showToastShort("Logging out");
                    resetApplication();
                })
                .show();
    }

    @Override
    public void handleError(String msg) {

        hideProgressSpinner();
        showSnackbar(msg);
    }

    @Override
    public void showAddCalendarMessage() {

        binding.tvEmployersName.setText(viewModel.getSelectedUserProfile().getCurrentEmployer());
        binding.tvDrawerCompanyName.setText(viewModel.getSelectedUserProfile().getCurrentEmployer());
        binding.layoutEmptyScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public List<UserProfile> getPermProfileList() {
        return viewModel.getEmployeeProfilesList();
    }


    @Override
    public void onEmployeeContactsReturned() {

        hideProgressSpinner();
        if (!dataManager.getSharedPrefs().isBusinessAccount()) {

            binding.tvEmployersName.setText(viewModel.getSelectedUserProfile().getCurrentEmployer());
            binding.tvDrawerCompanyName.setText(viewModel.getSelectedUserProfile().getCurrentEmployer());
        }

        List<UserProfile> favUserProfiles = new ArrayList<>(viewModel.getFavoritesProfileList());
        locatorAdapter.setFavoriteProfiles(favUserProfiles);
    }

    @Override
    public void addFavoriteUser(UserProfile userProfile) {

        String profileId = userProfile.getId();
        String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();

        if (viewModel.getFavoriteUsersEntityList().size() >= 10) {

            String title = "Max Reached";
            String body = "You can only hve up to ten favorites.";
            showAlert(title, body);

        } else {

            for (UserProfile profile : viewModel.getFavoritesProfileList()) {

                if (profile.getId().equals(profileId)) {

                    String title = "Already a Favorite";
                    String body = "You already added this user as a favorite.";
                    showAlert(title, body);
                    return;
                }
            }

            final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

            dialog.setTitle("Add to Favorites?")
                    .setMessage("Do you want to add " + fullName + " as a favorite?")
                    .setNegativeButton("No", (paramDialogInterface, paramInt) -> {
                    })
                    .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                        viewModel.addFavoriteUser(getViewModel(), profileId);
                        showSnackbar(fullName + " added to favorites");
                    })
            ;

            dialog.show();
        }

    }

    @Override
    public void removeFavoriteUser(UserProfile userProfile) {

        String profileId = userProfile.getId();
        String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle("Remove From Favorites?")
                .setMessage("Do you want to remove " + fullName + " from your favorites list?")
                .setNegativeButton("No", (paramDialogInterface, paramInt) -> {
                })
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                    viewModel.removeFavorteUser(getViewModel(), profileId);
                    showSnackbar(fullName + " removed from favorites");
                })
        ;

        dialog.show();
    }


    @Override
    public void updateFavoritesList() {

        List<UserProfile> favUserProfiles = new ArrayList<>(viewModel.getFavoritesProfileList());
        locatorAdapter.setFavoriteProfiles(favUserProfiles);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        //todo query the Firebase DB for results
        locatorAdapter.filter(newText);
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

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

    private void setUpSwitchListeners() {

        binding.switchPrivacyMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {

                binding.switchPrivacyMode.getTrackDrawable()
                        .setColorFilter(getResources().getColor(R.color.dessert_green), PorterDuff.Mode.MULTIPLY);

            } else {

                binding.switchPrivacyMode.getTrackDrawable()
                        .setColorFilter(getResources().getColor(R.color.material_icons_light),
                                PorterDuff.Mode.MULTIPLY);
            }
        });

        binding.switchVacationMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            if (isChecked) {

                binding.switchVacationMode.getTrackDrawable()
                        .setColorFilter(getResources().getColor(R.color.dessert_green), PorterDuff.Mode.MULTIPLY);

            } else {

                binding.switchVacationMode.getTrackDrawable()
                        .setColorFilter(getResources().getColor(R.color.material_icons_light),
                                PorterDuff.Mode.MULTIPLY);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable = new CompositeDisposable();
        setUpNewAnnouncementBadge();
        setUpNewConnectionRequestBadge();
        setNotificationObservable();

        if (!LocatorViewModel.getCalendarCompanyId().isEmpty()) {

            binding.layoutEmptyScreen.setVisibility(View.GONE);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
        resetNewConnectionRequestBadge();
    }

    /**
     * Removes all Activities from the back stack and opens up
     * {@link ViewPagerFragment}
     */
    private void resetApplication() {

        getBaseActivity().finishAffinity();
        startActivity(WelcomeActivity.newIntent(getBaseActivity()));
    }

    private void showInstallGoogleCalendarAlert() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());

        dialog.setTitle("Google Calendar Not Found")
                .setMessage("You need the Google Calendar app to use this feature")
                .setNegativeButton("cancel", (paramDialogInterface, paramInt) -> {

                })
                .setPositiveButton("install", (paramDialogInterface, paramInt) -> {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + GOOGLE_CALENDAR_PACKAGE_NAME)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(GOOGLE_PLAY_URL + GOOGLE_CALENDAR_PACKAGE_NAME));
//                    intent.setPackage("com.android.vending");
                        startActivity(intent);
                    }
                })
                .show();
    }


    private boolean isGoogleCalendarAppInstalled() {

        String packageName = GOOGLE_CALENDAR_PACKAGE_NAME;
        PackageManager pm = getBaseActivity().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return pm.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Set up Notification Badges
     */

    private void resetNewConnectionRequestBadge() {

        DashBoardActivity.newRequestCount = 0;
        binding.cardRequestBadge.setVisibility(View.GONE);
    }

    private void setUpNewConnectionRequestBadge() {

        if (DashBoardActivity.newRequestCount > 0) {//show badge

            binding.cardRequestBadge.setVisibility(View.VISIBLE);
            binding.tvRequestBadgeCount.setText(String.valueOf(DashBoardActivity.newRequestCount));

        } else {//hide badge

            binding.cardRequestBadge.setVisibility(View.GONE);
        }
    }


    private void setUpNewAnnouncementBadge() {

        if (DashBoardActivity.newAnnouncementCount > 0) {//show badge

            binding.cardNotificationBadge.setVisibility(View.VISIBLE);
            binding.tvNotificationBadgeCount.setText(String.valueOf(DashBoardActivity.newAnnouncementCount));

        } else {//hide badge

            binding.cardNotificationBadge.setVisibility(View.GONE);
        }
    }

    /**
     * Subscribes to the Observable in {@link MyFirebaseMessagingService}
     */
    private void setNotificationObservable() {

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
            public void onComplete() {
            }
        };

        observable.subscribe(observer);
    }

    @SuppressLint("CheckResult")
    private void showNewConnectionRequestBadge() {

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
    private void showNewAnnouncementBadge() {

        Completable.fromCallable(() -> {

            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    binding.cardNotificationBadge.setVisibility(View.VISIBLE);
                    binding.tvNotificationBadgeCount.setText(String.valueOf(DashBoardActivity.newAnnouncementCount));
                });
    }


    private void setUpOAuth(){


        HttpUrl authorizeUrl =
                HttpUrl.parse("https://accounts.google.com/o/oauth2/v2/auth")
                        .newBuilder()
                        .addQueryParameter("client_id", CLIENT_ID)
                        .addQueryParameter("redirect_uri", REDIRECT_URI)
                        .addQueryParameter("scope", API_SCOPE)
                        .addQueryParameter("response_type", CODE)
                        .build();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(String.valueOf(authorizeUrl.url())));
        startActivity(i);

//        try{
//
//            JSONObject jsonObject = new JSONObject(
//                    CommonUtils.loadJSONFromAsset(getBaseActivity(), "json/credentials.json"));
//
//            String CLIENT_ID = jsonObject.getString("client_id");
//            String REDIRECT_URI = jsonObject.getString("redirect_uris");
//            String CLIENT_SECRET = jsonObject.getString("client_secret");
//
//
//        } catch (JSONException | IOException e){
//
//            showAlert("Error",e.getLocalizedMessage());
//        }

    }


    private void startLocationsService(){

        Intent intent = new Intent(getBaseActivity(),CurrentLocationService.class);
        getBaseActivity().startService(intent);
    }

}
