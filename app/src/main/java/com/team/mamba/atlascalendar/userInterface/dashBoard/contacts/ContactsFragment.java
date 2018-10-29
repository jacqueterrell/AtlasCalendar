package com.team.mamba.atlascalendar.userInterface.dashBoard.contacts;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.BusinessProfile;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserConnections;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.databinding.ContactsLayoutBinding;
import com.team.mamba.atlascalendar.service.MyFirebaseMessagingService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;

import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main.CrmFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.locator.LocatorFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactsFragment extends BaseFragment<ContactsLayoutBinding, ContactsViewModel>
        implements ContactsNavigator, SearchView.OnQueryTextListener {


    @Inject
    ContactsViewModel viewModel;

    @Inject
    ContactsDataModel dataModel;

    @Inject
    Context appContext;

    private ContactsLayoutBinding binding;
    private DashBoardActivityNavigator parentNavigator;
    private DashBoardActivity parentActivity;
    private static List<UserConnections> userConnectionsList = new ArrayList<>();
    private static List<UserConnections> permUserConnectionsList = new ArrayList<>();
    private static List<UserConnections> filteredUserConnectionsList = new ArrayList<>();
    private static String userName = "";
    private static String userCode = "";
    private CompositeDisposable compositeDisposable;

    private ContactListAdapter contactListAdapter;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.contacts_layout;
    }

    @Override
    public ContactsViewModel getViewModel() {
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

        showHideAddContactButton();
        contactListAdapter = new ContactListAdapter(userConnectionsList, getViewModel());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(contactListAdapter);

        binding.swipeContainer.setOnRefreshListener(() -> {

            if (viewModel.isBusinessContactsShown()) {

                viewModel.setBusinessContactProfiles();

            } else {

                viewModel.requestContactsInfo(getViewModel());
            }

        });

        checkForEmptyContactList();

        setUpSearchView();
        return binding.getRoot();
    }

    /**
     * Hides the add contact button if the user is logged in as
     * a business.
     */
    private void showHideAddContactButton(){

        if (dataManager.getSharedPrefs().isBusinessAccount()){

            binding.ivAddContact.setVisibility(View.INVISIBLE);
            binding.layoutGroupFilter.setVisibility(View.INVISIBLE);

        } else {

            binding.ivAddContact.setVisibility(View.VISIBLE);
            binding.layoutGroupFilter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks to see if our contact list is empty
     */
    private void checkForEmptyContactList() {

        if (viewModel.getUserConnectionsList().isEmpty()) {

            showProgressSpinner();
            viewModel.requestContactsInfo(getViewModel());

        } else {

            checkIfDifferentUserHasLoggedIn();
        }
    }


    /**
     * Checks to see if a different user has logged in
     */
    private void checkIfDifferentUserHasLoggedIn() {

        if (!viewModel.getSavedUserId().equals(dataManager.getSharedPrefs().getUserId())) {

            showProgressSpinner();
            viewModel.requestContactsInfo(getViewModel());

        } else {

            checkShownContactType();
        }
    }


    /**
     * Resets list back to individual contacts
     */
    private void checkShownContactType() {

        binding.tvUserName.setText(userName);
        binding.tvUserCode.setText(userCode);

        if (!dataManager.getSharedPrefs().isBusinessAccount()){
            onIndividualFilterClicked();
        }
        viewModel.requestContactsInfo(getViewModel());
    }

    @Override
    public void onAddNewContactClicked() {

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            parentActivity.openAddContactDialog();
        }
    }

    @Override
    public void onSyncContactsClicked() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseActivity());
        String title = getBaseActivity().getResources().getString(R.string.sync_contacts_title);
        String body = getBaseActivity().getResources().getString(R.string.sync_contacts_body);

        dialog.setTitle(title)
                .setMessage(body)
                .setNegativeButton("Not Now", (paramDialogInterface, paremInt) -> {

                })
                .setPositiveButton("Yes", (paramDialogInterface, paramInt) -> {

                    syncAllContactRequest();

                });

        dialog.show();
    }

    @Override
    public void onSettingsClicked() {

        parentActivity.openSettingsScreen();
    }

    @Override
    public void onProfileImageClicked() {

        if (dataManager.getSharedPrefs().isBusinessAccount()){

            parentActivity.openBusinessProfile(viewModel.getBusinessProfile());

        } else {

            if (binding.layoutIndividualProfile.getVisibility() == View.VISIBLE) {//showing all contacts

                parentActivity.openUserProfile(viewModel.getUserProfile());

            } else { //showing business directory

                if (viewModel.getSelectedBusinessProfile() != null) {

                    parentActivity.openBusinessProfile(viewModel.getSelectedBusinessProfile());

                }
            }
        }
    }

    @Override
    public void onBusinessFilterClicked() {

        if (!dataManager.getSharedPrefs().isBusinessAccount()){

            binding.layoutGroupFilter.setVisibility(View.GONE);
            binding.ivIndividualFilter.setVisibility(View.VISIBLE);
            binding.layoutIndividualProfile.setVisibility(View.GONE);
            binding.layoutBusinessProfile.setVisibility(View.VISIBLE);
            viewModel.setBusinessContactsShown(true);
            viewModel.setBusinessContactProfiles();
        }
    }

    @Override
    public void onIndividualFilterClicked() {

        for (UserConnections connections : permUserConnectionsList) {

            connections.setOverrideBusinessProfile(false);

            if (connections.getUserProfile() != null) {

                connections.getUserProfile().setShareNeeds("");
            }
        }

        binding.layoutGroupFilter.setVisibility(View.VISIBLE);
        binding.ivIndividualFilter.setVisibility(View.GONE);

        binding.layoutBusinessProfile.setVisibility(View.GONE);
        binding.layoutIndividualProfile.setVisibility(View.VISIBLE);
        viewModel.setBusinessContactsShown(false);

        userConnectionsList.clear();
        filteredUserConnectionsList.clear();
        filteredUserConnectionsList.addAll(permUserConnectionsList);
        userConnectionsList.addAll(permUserConnectionsList);
        contactListAdapter.clearTitleList();

        binding.recyclerView.getLayoutManager().scrollToPosition(0);
        contactListAdapter.notifyDataSetChanged();
        createCharList();
    }

    @Override
    public void onRowClicked(UserConnections userConnections) {

        if (userConnections.isOrgBus && !userConnections.isOverrideBusinessProfile()) {

            parentNavigator.openBusinessProfile(userConnections.getBusinessProfile());

        } else {

            parentNavigator.openUserProfile(userConnections.getUserProfile());
        }

        hideProgressSpinner();

    }

    @Override
    public void onDirectoryCountClicked() {

        Fragment fragment = DirectoryListRecycler.newInstance(this,viewModel.getDirectoryConnections());
        ChangeFragments.addFragmentFadeIn(fragment,getBaseActivity().getSupportFragmentManager(),"DirectoryRecycler",null);
    }


    @Override
    public void onDirectoryRowClicked(UserConnections userConnections) {

        viewModel.setSelectedBusinessProfile(userConnections.getBusinessProfile());
        viewModel.getAllBusinessConnections(userConnections);
    }

    @Override
    public void onDataValuesReturned(List<UserConnections> connectionsList) {

        binding.swipeContainer.setRefreshing(false);

        if (dataManager.getSharedPrefs().isBusinessAccount()) {

            BusinessProfile profile = viewModel.getBusinessProfile();
            String name = profile.getName();
            userCode = profile.getCode();
            userName = name;
            binding.tvUserCode.setText(profile.getCode());
            binding.tvUserName.setText(name);

            if (!profile.getImageUrl().replace(".", "").isEmpty()) {

                Glide.with(getBaseActivity())
                        .load(profile.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(binding.ivUserProfileImage);

                binding.ivDefault.setVisibility(View.GONE);
            }

        } else {

            UserProfile profile = viewModel.getUserProfile();
            String name = profile.getFirstName() + " " + profile.getLastName();
            userCode = profile.getCode();
            userName = name;
            binding.tvUserCode.setText(profile.getCode());
            binding.tvUserName.setText(name);

            if (!profile.getImageUrl().replace(".", "").isEmpty()) {

                Glide.with(getBaseActivity())
                        .load(profile.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(binding.ivUserProfileImage);

                binding.ivDefault.setVisibility(View.GONE);
            }

            setUpDirectoryBadge();

        }

        userConnectionsList.clear();
        permUserConnectionsList.clear();
        filteredUserConnectionsList.clear();

        permUserConnectionsList.addAll(connectionsList);
        userConnectionsList.addAll(connectionsList);
        filteredUserConnectionsList.addAll(connectionsList);
        contactListAdapter.clearTitleList();

        contactListAdapter.notifyDataSetChanged();

        createCharList();
        hideProgressSpinner();
    }

    /**
     * Sets the count for the directory badge
     */
    private void setUpDirectoryBadge(){

        if (viewModel.getTotalDirectories() > 0){

            binding.cardDirectoryBadge.setVisibility(View.VISIBLE);
            binding.tvDirectoryBadgeCount.setText(String.valueOf(viewModel.getTotalDirectories()));

        } else {
            binding.cardDirectoryBadge.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBusinessContactsSet(List<UserConnections> businessAssociatesList) {

        userConnectionsList.clear();
        filteredUserConnectionsList.clear();

        userConnectionsList.addAll(businessAssociatesList);
        filteredUserConnectionsList.addAll(businessAssociatesList);
        contactListAdapter.clearTitleList();

        binding.recyclerView.getLayoutManager().scrollToPosition(0);
        contactListAdapter.notifyDataSetChanged();

        BusinessProfile businessProfile = viewModel.getSelectedBusinessProfile();

        if (businessProfile != null) {

            binding.tvBusinessName.setText(businessProfile.getName());

            if (!businessProfile.getImageUrl().replace(".", "").isEmpty()) {

                Glide.with(getBaseActivity())
                        .load(businessProfile.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(binding.ivBusinessProfile);

                binding.ivBusinessDefault.setVisibility(View.GONE);

            } else {

                binding.ivBusinessProfile.setImageResource(android.R.color.transparent);
                binding.ivBusinessDefault.setVisibility(View.VISIBLE);
            }
        }

        if (viewModel.getDirectoryConnections().size() > 1){

            int otherDirectoriesCount = viewModel.getDirectoryConnections().size() - 1;
            String total = "+" + otherDirectoriesCount;
            binding.tvDirectoryCount.setText(total);
            binding.tvDirectoryCount.setVisibility(View.VISIBLE);

        } else {
            binding.tvDirectoryCount.setVisibility(View.INVISIBLE);
            binding.cardDirectoryBadge.setVisibility(View.INVISIBLE);
        }

        hideProgressSpinner();
        createCharList();
        binding.swipeContainer.setRefreshing(false);
    }

    /**
     * Populates our floating textView
     */
    private void createCharList() {

        StringBuilder stringBuilder = new StringBuilder();
        List<String> charList = new ArrayList<>();
        List<UserConnections> businessConnections = new ArrayList<>();

        for (UserConnections connections : userConnectionsList) {

            if (connections.isOrgBus && !connections.isOverrideBusinessProfile()) {

                businessConnections.add(connections);

            } else {

                String firstChar = connections.getUserProfile().getLastName().substring(0, 1);

                if (!charList.contains(firstChar)) {

                    stringBuilder.append(firstChar + "\n");
                    charList.add(firstChar);
                }
            }
        }

        if (!businessConnections.isEmpty()) {

            stringBuilder.append("+");
        }

        binding.tvCharList.setText(stringBuilder.toString());
    }

    @Override
    public void handleError(String errorMsg) {

        binding.swipeContainer.setRefreshing(false);
        hideProgressSpinner();
    }

    @Override
    public boolean isActivityVisible() {
        return getBaseActivity() != null;
    }

    @Override
    public List<UserConnections> getPermConnectionList() {
        return filteredUserConnectionsList;
    }

    @Override
    public void onInfoClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(LocatorFragment.newInstance(), manager, "Info", null);
    }

    @Override
    public void onNotificationsClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(AnnouncementsFragment.newInstance(), manager, "Announcements", null);
    }

    @Override
    public void onCrmClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(new CrmFragment(), manager, "CrmFragment", null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        contactListAdapter.filter(newText);
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

    private void syncAllContactRequest() {

        for (UserConnections connection : filteredUserConnectionsList) {

            String displayName = "";
            String workNumber = "";
            String emailID = "email@nomail.com";

            if (connection.isOrgBus && !connection.isOverrideBusinessProfile()) {

                BusinessProfile businessProfile = connection.getBusinessProfile();

                displayName = businessProfile.getName();
                workNumber = businessProfile.getPhone();
                emailID = businessProfile.getEmail();

            } else {

                UserProfile userProfile = connection.getUserProfile();
                String name = userProfile.getFirstName() + " " + userProfile.getLastName();

                displayName = name;
                workNumber = userProfile.getWorkPhone();
                emailID = userProfile.getEmail();

            }

            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());

            //------------------------------------------------------ Names
            if (displayName != null) {
                ops.add(ContentProviderOperation.newInsert(
                        ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                displayName).build());
            }

            //------------------------------------------------------ Mobile Number
//            if (MobileNumber != null) {
//                ops.add(ContentProviderOperation.
//                        newInsert(ContactsContract.Data.CONTENT_URI)
//                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                        .withValue(ContactsContract.Data.MIMETYPE,
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
//                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
//                        .build());
//            }

            //------------------------------------------------------ Home Numbers
//            if (HomeNumber != null) {
//                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                        .withValue(ContactsContract.Data.MIMETYPE,
//                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
//                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
//                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
//                        .build());
//            }

            //------------------------------------------------------ Work Numbers
            if (workNumber != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .build());
            }

            //------------------------------------------------------ Email
            if (emailID != null) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
            }

            //------------------------------------------------------ Organization
//            if (!company.equals("") && !jobTitle.equals("")) {
//                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                        .withValue(ContactsContract.Data.MIMETYPE,
//                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
//                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
//                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
//                        .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
//                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
//                        .build());
//            }

            // Asking the Contact provider to create a new contact

            if (isWriteContactsPermissionsGranted()) {

                try {
                    getBaseActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }

        }

        showToastShort("Syncing contacts...");
    }


    public boolean isWriteContactsPermissionsGranted() {

        if (sdk >= marshMallow) {

            if (ActivityCompat.checkSelfPermission(getBaseActivity(), permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS},   //request specific permission from user
                        AppConstants.REQUEST_WRITE_CONTACTS_PERMISSIONS);

                return false;

            } else {

                return true;
            }

        } else {

            return true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        compositeDisposable = new CompositeDisposable();
        setNotificationObservable();
        setUpNewAnnouncementBadge();
        setUpNewConnectionRequestBadge();
    }

    @Override
    public void onPause() {
        super.onPause();
        compositeDisposable.dispose();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppConstants.REQUEST_WRITE_CONTACTS_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            onSyncContactsClicked();
        }
    }


}
