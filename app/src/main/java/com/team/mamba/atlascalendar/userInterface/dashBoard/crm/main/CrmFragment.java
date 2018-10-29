package com.team.mamba.atlascalendar.userInterface.dashBoard.crm.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.BuildConfig;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.CrmNotes;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.CrmFilter;
import com.team.mamba.atlascalendar.databinding.CrmLayoutBinding;
import com.team.mamba.atlascalendar.service.MyFirebaseMessagingService;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;

import com.team.mamba.atlascalendar.userInterface.dashBoard.announcements.AnnouncementsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.contacts.ContactsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.edit_add_note.EditAddNotePageOneFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.filter_list.CrmFilterSettingsFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard.crm.selected_crm.SelectedCrmFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.locator.LocatorFragment;
import com.team.mamba.atlascalendar.utils.AppConstants;
import com.team.mamba.atlascalendar.utils.ChangeFragments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class CrmFragment extends BaseFragment<CrmLayoutBinding, CrmViewModel>
        implements CrmNavigator, SearchView.OnQueryTextListener {


    @Inject
    CrmViewModel viewModel;

    @Inject
    CrmDataModel dataModel;

    @Inject
    Context appContext;

    private CrmLayoutBinding binding;
    private static List<CrmNotes> crmNotesList = new ArrayList<>();
    private CrmAdapter crmAdapter;
    private DashBoardActivityNavigator parentNavigator;
    private DashBoardActivity parentActivity;
    private static final int SEND_CSV = 0;
    private static List<CrmNotes> permCrmNotesList = new ArrayList<>();
    private CompositeDisposable compositeDisposable;


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.crm_layout;
    }

    @Override
    public CrmViewModel getViewModel() {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = getViewDataBinding();

        crmAdapter = new CrmAdapter(getViewModel(), crmNotesList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        setUpSearchView();

        binding.swipeContainer.setOnRefreshListener(() -> {

            viewModel.requestAllOpportunities(getViewModel());

        });


        if (viewModel.getCrmNotesList().isEmpty()) {

            viewModel.requestAllOpportunities(getViewModel());

        } else {

            if (!viewModel.getSavedUserId().equals(dataManager.getSharedPrefs().getUserId())) {//the user logged in as business but was previous a user

                viewModel.requestAllOpportunities(getViewModel());

            } else {

                crmAdapter.clearMonths();
                binding.recyclerView.setAdapter(crmAdapter);
                viewModel.requestAllOpportunities(getViewModel());
            }

        }

        return binding.getRoot();
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


    @Override
    public void handleError(String error) {

        binding.swipeContainer.setRefreshing(false);
    }

    @Override
    public void onCrmDataReturned() {

        crmNotesList.clear();
        permCrmNotesList.clear();
        crmAdapter.clearMonths();

        crmNotesList.addAll(getFilteredNotes());
        permCrmNotesList.addAll(getFilteredNotes());

        binding.recyclerView.setAdapter(crmAdapter);

        crmAdapter.notifyDataSetChanged();
        binding.swipeContainer.setRefreshing(false);

        if (getFilteredNotes().isEmpty()){
            binding.tvEmptyCrmPrompt.setVisibility(View.VISIBLE);
        } else {
        binding.tvEmptyCrmPrompt.setVisibility(View.GONE);
        }
    }

    @Override
    public List<CrmNotes> getPerCrmNotesList() {

        List<CrmNotes> notesList = new ArrayList<>(permCrmNotesList);

        return notesList;
    }

    @Override
    public void onRowClicked(CrmNotes notes) {

        ChangeFragments.replaceFragmentVertically(SelectedCrmFragment.newInstance(notes),
                getBaseActivity().getSupportFragmentManager(), "SelectedCrm", null);

    }


    @Override
    public void onCrmInfoButtonClicked() {

        showInfoDialog();
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
    public void onContactsClicked() {

        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        ChangeFragments.replaceFromBackStack(new ContactsFragment(), manager, "ContactsFragment", null);
    }

    @Override
    public void onSettingsClicked() {

        parentNavigator.openSettingsScreen();
    }

    @Override
    public void onShareClicked() {

        showExportDialog();

    }

    @Override
    public void onCreateNewNoteClicked() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newUserRef = db.collection(AppConstants.USERS_COLLECTION).document();
        String noteId = newUserRef.getId();
        Long timeStamp = System.currentTimeMillis() / 1000;

        CrmNotes crmNotes = new CrmNotes();
        crmNotes.setId(noteId);
        crmNotes.setAuthorID(dataManager.getSharedPrefs().getUserId());
        crmNotes.setTimestamp(timeStamp);

        ChangeFragments.replaceFragmentVertically(EditAddNotePageOneFragment.newInstance(crmNotes, true),
                getBaseActivity().getSupportFragmentManager(), "PageOne", null);

    }

    @Override
    public void onFilterClicked() {

        ChangeFragments.replaceFragmentVertically(new CrmFilterSettingsFragment(),
                getBaseActivity().getSupportFragmentManager(), "FilterCrm", null);

    }

    @Override
    public boolean isInfoDialogShown() {
        return binding.dialogCrmInfo.getVisibility() == View.VISIBLE;
    }

    @Override
    public void hideCrmInfoDialog() {

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .onEnd(animator -> binding.dialogCrmInfo.setVisibility(View.GONE))
                .playOn(binding.dialogCrmInfo);
    }


    private void showInfoDialog() {

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .onStart(animator -> binding.dialogCrmInfo.setVisibility(View.VISIBLE))
                .playOn(binding.dialogCrmInfo);
    }

    @Override
    public boolean isExportScreenShown() {
        return binding.dialogExport.layoutExport.getVisibility() == View.VISIBLE;
    }

    @Override
    public void hideExportScreen() {

        YoYo.with(Techniques.SlideOutDown)
                .duration(500)
                .onEnd(animator -> binding.dialogExport.layoutExport.setVisibility(View.GONE))
                .playOn(binding.dialogExport.layoutExport);
    }

    private void showExportDialog() {

        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .onStart(animator -> binding.dialogExport.layoutExport.setVisibility(View.VISIBLE))
                .playOn(binding.dialogExport.layoutExport);

    }


    @Override
    public void onExportButtonClicked() {

        if (isExternalStoragePermissionsGranted()) {

            hideExportScreen();

            if (binding.dialogExport.chkBoxContacts.isChecked()) {

                if (viewModel.getUsersContactProfiles().isEmpty()){

                    String body = "Your Contact list is empty";
                    showAlert("No Contacts Found",body);

                } else {

                    exportContacts();
                }

            } else {

                if (viewModel.getCrmNotesList().isEmpty()){

                    String title = "Your Opportunities list is empty";
                    showAlert(title,"");

                } else {

                    exportOpportunities();
                }
            }
        }
    }

    @Override
    public void onExportContactsCheckboxClicked() {

        if (binding.dialogExport.chkBoxContacts.isChecked()) {

            binding.dialogExport.chkBoxOpportunities.setChecked(false);

        } else {

            binding.dialogExport.chkBoxOpportunities.setChecked(false);
            binding.dialogExport.chkBoxContacts.setChecked(true);
        }
    }

    @Override
    public void onExportOpportunitiesCheckboxClicked() {

        if (binding.dialogExport.chkBoxOpportunities.isChecked()) {

            binding.dialogExport.chkBoxContacts.setChecked(false);

        } else {

            binding.dialogExport.chkBoxOpportunities.setChecked(true);
            binding.dialogExport.chkBoxContacts.setChecked(false);
        }
    }


    private void writeCsvHeader(FileWriter writer, String h1) throws IOException {

        writer.write(h1);
    }

    private void writeCsvData(FileWriter writer, String data) throws IOException {

        writer.write(data);
    }


    /**
     * Takes all of the User's Contacts and exports them as a CSV file
     */
    private void exportContacts() {

        File root = Environment.getExternalStorageDirectory();
        File csvFile = new File(root, "CRM_from_Atlas.csv");
        Uri uri;

        FileWriter writer;

        try {

            writer = new FileWriter(csvFile);
            writeCsvHeader(writer, "First Name,");
            writeCsvHeader(writer, "Last Name,");
            writeCsvHeader(writer, "Email,");
            writeCsvHeader(writer, "Work Email,");
            writeCsvHeader(writer, "Cell Phone,");
            writeCsvHeader(writer, "Work Phone,");
            writeCsvHeader(writer, "Home Phone,");
            writeCsvHeader(writer, "Fax,");
            writeCsvHeader(writer, "Home Address,");
            writeCsvHeader(writer, "Work Address,");
            writeCsvHeader(writer, "Current Employer,");
            writeCsvHeader(writer, "Current Position\n");



            for (UserProfile profile : viewModel.getUsersContactProfiles()) {

                String workAddress = profile.getWorkStreet() + " " + profile.getWorkCityStateZip();
                String homeAddress = profile.getStreet() + " " + profile.getCityStateZip();


                if (profile.getConnectionType() == 0 || profile.getConnectionType() == 1){//family can see everything

                    writeCsvData(writer, profile.getFirstName().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getLastName().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getEmail().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getWorkEmail().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getPersonalPhone().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getWorkPhone().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getHomePhone().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getFax().replaceAll(",", " ") + ",");
                    writeCsvData(writer, homeAddress.replaceAll(",", " ") + ",");
                    writeCsvData(writer, workAddress.replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getCurrentEmployer().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getCurrentPosition().replaceAll(",", " ") + "\n");

                } else { //connectionType = Business

                    writeCsvData(writer, profile.getFirstName().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getLastName().replaceAll(",", " ") + ",");
                    writeCsvData(writer, "..." + ",");//personal email
                    writeCsvData(writer, profile.getWorkEmail().replaceAll(",", " ") + ",");
                    writeCsvData(writer, "..." + ",");//cell phone
                    writeCsvData(writer, profile.getWorkPhone().replaceAll(",", " ") + ",");
                    writeCsvData(writer, "..." + ",");//home phone
                    writeCsvData(writer, profile.getFax().replaceAll(",", " ") + ",");
                    writeCsvData(writer, "..." + ",");//home address
                    writeCsvData(writer, workAddress.replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getCurrentEmployer().replaceAll(",", " ") + ",");
                    writeCsvData(writer, profile.getCurrentPosition().replaceAll(",", " ") + "\n");
                }

            }

            writer.flush();
            writer.close();

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/html");

            if (sdk >= Build.VERSION_CODES.N) {

                uri = FileProvider.getUriForFile(getBaseActivity(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        csvFile);
            } else {
                uri = Uri.fromFile(csvFile);
            }

            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(Intent.createChooser(emailIntent, "Pick an Email provider"), SEND_CSV);


        } catch (Exception e) {

            Logger.e(e.getMessage());
        }

    }

    /**
     * Takes all of the User's opportunities and exports them as a CSV file
     */
    private void exportOpportunities() {

        File root = Environment.getExternalStorageDirectory();
        File csvFile = new File(root, "CRM_from_Atlas.csv");
        Uri uri;

        FileWriter writer;

        try {

            writer = new FileWriter(csvFile);
            writeCsvHeader(writer, "Point of Contact,");
            writeCsvHeader(writer, "Opportunity Name,");
            writeCsvHeader(writer, "Where Met City/State,");
            writeCsvHeader(writer, "Where Met Event Name,");
            writeCsvHeader(writer, "How Met,");
            writeCsvHeader(writer, "Stage,");
            writeCsvHeader(writer, "Type,");

            writeCsvHeader(writer, "Opportunity Goal,");
            writeCsvHeader(writer, "Description,");
            writeCsvHeader(writer, "Next Step,");
            writeCsvHeader(writer, "Date Created,");
            writeCsvHeader(writer, "Date Closed\n");


            for (CrmNotes notes : viewModel.getCrmNotesList()) {

                writeCsvData(writer, notes.getPoc().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getNoteName().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getWhereMetCitySt().replaceAll(",","") + ",");
                writeCsvData(writer, notes.getWhereMetEventName().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getHowMetToString().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getStageToString().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getTypeToString().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getOpportunityGoalToString() + ",");
                writeCsvData(writer, notes.getDesc().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getNextStepToString().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getDateCreatedToString().replaceAll(","," ") + ",");
                writeCsvData(writer, notes.getDateClosedToString().replaceAll(","," ") + "\n");

            }

            writer.flush();
            writer.close();

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/html");

            if (sdk >= Build.VERSION_CODES.N) {

                uri = FileProvider.getUriForFile(getBaseActivity(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        csvFile);
            } else {
                uri = Uri.fromFile(csvFile);
            }

            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(Intent.createChooser(emailIntent, "Pick an Email provider"), SEND_CSV);


        } catch (Exception e) {

            Logger.e(e.getMessage());
        }

    }


    public boolean isExternalStoragePermissionsGranted() {

        if (sdk >= marshMallow) {

            if (ActivityCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

              requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        AppConstants.REQUEST_EXTERNAL_STORAGE_PERMISSIONS);

                return false;

            } else {

                return true;
            }

        } else {

            return true;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppConstants.REQUEST_EXTERNAL_STORAGE_PERMISSIONS
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            onExportButtonClicked();

        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        crmAdapter.filter(newText);
        return true;
    }


    /**
     * This takes the list and filters it by the params
     * defined in {@link CrmFilterSettingsFragment}
     *
     * @return returns the filtered list
     */
    private List<CrmNotes> getFilteredNotes() {

        List<CrmNotes> filteredList = new ArrayList<>();

        if (parentNavigator.getCrmFilter() == null) {

            return viewModel.getCrmNotesList();

        } else {

            //filter by status
            for (CrmNotes notes : viewModel.getCrmNotesList()) {

                CrmFilter crmFilter = new CrmFilter(parentNavigator.getCrmFilter());
                // first give a valid value to each field
                if (crmFilter.getNextStep() < 0) {

                    crmFilter.setNextStep(notes.getNextStep());
                }

                if (crmFilter.getStatus() < 0) {

                    crmFilter.setStatus(notes.getStage());
                }

                if (crmFilter.getOpportunity() < 0) {

                    crmFilter.setOpportunity(notes.getOppGoal());
                }

                //second filter the list by all set values
                if (crmFilter.getOpportunity() == notes.getOppGoal()
                        && crmFilter.getStatus() == notes.getStage()
                        && crmFilter.getNextStep() == notes.getNextStep()) {

                    filteredList.add(notes);

                }

            }

            //third return our list
            return filteredList;

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
                    binding.cardNotificationBadge.setVisibility(View.VISIBLE);
                    binding.tvNotificationBadgeCount.setText(String.valueOf(DashBoardActivity.newAnnouncementCount));
                });
    }

}
