package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_education_info;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;

import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.Education;
import com.team.mamba.atlascalendar.databinding.AddEducationLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class AddEducationFragment extends BaseFragment<AddEducationLayoutBinding,AddEducationViewModel> implements AddEducationNavigator,SearchView.OnQueryTextListener {


    @Inject
    AddEducationViewModel viewModel;

    @Inject
    Context appContext;

    private AddEducationLayoutBinding binding;
    private static EditEducationNavigator navigator;
    private DashBoardActivity parentActivity;
    private SelectSchoolAdapter selectSchoolAdapter;
    private SelectDegreeAdapter selectDegreeAdapter;
    private SelectFieldOfStudyAdapter fieldOfStudyAdapter;

    private List<String> universityList = new ArrayList<>();
    private List<String> permUniversityList = new ArrayList<>();

    private List<String> degreeList = new ArrayList<>();
    private List<String> permDegreeList = new ArrayList<>();

    private List<String> fieldOfStudyList = new ArrayList<>();
    private List<String> permFieldOfStudyList = new ArrayList<>();

    public static AddEducationFragment newInstance(EditEducationNavigator editEducationNavigator){

        navigator = editEducationNavigator;
        return new AddEducationFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.add_education_layout;
    }

    @Override
    public AddEducationViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (DashBoardActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = getViewDataBinding();

        getBaseActivity().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN);

        permUniversityList.addAll(Arrays.asList(CollegesArray.colleges));
        permDegreeList.addAll(Arrays.asList(appContext.getResources().getStringArray(R.array.degree_array)));
        permFieldOfStudyList.addAll(Arrays.asList(FieldsOfStudyArray.collegeMajors));

        selectSchoolAdapter = new SelectSchoolAdapter(universityList,this);
        selectDegreeAdapter = new SelectDegreeAdapter(degreeList,this);
        fieldOfStudyAdapter = new SelectFieldOfStudyAdapter(fieldOfStudyList,this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.btnSave.setOnClickListener(v -> onSavedClicked());

        if (navigator.isEditing()){

            Education education = navigator.getEditingEducation();
            String degree = education.getDegree();
            String school = education.getSchool();
            String fieldOfStudy = education.getFieldOfStudy();

            binding.searchViewDegree.setQuery(degree,true);
            binding.searchViewSchool.setQuery(school,true);
            binding.searchViewFieldOfStudy.setQuery(fieldOfStudy,true);
        }

        setUpListeners();
        setUpUniversitySearchView();
        setUpDegreeSearchView();
        setUpFieldOfStudySearchView();

        return binding.getRoot();
    }

    @Override
    public List<String> getPermUniversityList() {
        return permUniversityList;
    }

    @Override
    public List<String> getPermDegreeList() {
        return permDegreeList;
    }

    @Override
    public List<String> getPermFieldOfStudyList() {
        return permFieldOfStudyList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (binding.searchViewSchool.hasFocus()){

            selectSchoolAdapter.filter(newText);

        } else if (binding.searchViewDegree.hasFocus()){

            selectDegreeAdapter.filter(newText);

        } else if (binding.searchViewFieldOfStudy.hasFocus()){

            fieldOfStudyAdapter.filter(newText);
        }

        return true;
    }

    private void setUpListeners(){


        binding.searchViewSchool.setOnQueryTextFocusChangeListener((v, hasFocus) -> {

            if (hasFocus){

                universityList.clear();
                universityList.addAll(Arrays.asList(CollegesArray.colleges));
                binding.recyclerView.setAdapter(selectSchoolAdapter);
            }
        });

        binding.searchViewDegree.setOnQueryTextFocusChangeListener((v, hasFocus) -> {

            if (hasFocus){

                degreeList.clear();
                degreeList.addAll(permDegreeList);
                binding.recyclerView.setAdapter(selectDegreeAdapter);

            }
        });

        binding.searchViewFieldOfStudy.setOnQueryTextFocusChangeListener((v, hasFocus) -> {

            if (hasFocus){

                fieldOfStudyList.clear();
                fieldOfStudyList.addAll(permFieldOfStudyList);
                binding.recyclerView.setAdapter(fieldOfStudyAdapter);

            }
        });
    }

    @Override
    public void onSchoolRowClicked(String university) {

        binding.searchViewSchool.setQuery(university,true);
        binding.searchViewSchool.clearFocus();
        EditText searchEditText = binding.searchViewSchool.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setSelection(0);

        universityList.clear();
        universityList.addAll(permUniversityList);
        selectSchoolAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDegreeRowClicked(String degree) {

        binding.searchViewDegree.setQuery(degree,true);
        binding.searchViewDegree.clearFocus();
        EditText searchEditText = binding.searchViewDegree.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setSelection(0);

        degreeList.clear();
        degreeList.addAll(permDegreeList);
        selectDegreeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFieldOfStudyRowClicked(String fieldOfStudy) {

        binding.searchViewFieldOfStudy.setQuery(fieldOfStudy,true);
        binding.searchViewFieldOfStudy.clearFocus();
        EditText searchEditText = binding.searchViewFieldOfStudy.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setSelection(0);

        fieldOfStudyList.clear();
        fieldOfStudyList.addAll(permFieldOfStudyList);
        fieldOfStudyAdapter.notifyDataSetChanged();
    }

    private void onSavedClicked() {

        String degree = binding.searchViewDegree.getQuery().toString();
        String field = binding.searchViewFieldOfStudy.getQuery().toString();
        String school = binding.searchViewSchool.getQuery().toString();

        if (isAllValuesValid()) {

            Education education = new Education();
            education.setDegree(degree);
            education.setFieldOfStudy(field);
            education.setSchool(school);

           //add to list and close
            if (navigator.isEditing()){

                navigator.onEditRowSaved(education);

            } else {

                navigator.onSaveNewEducation(education);
            }

            parentActivity.onBackPressed();

        } else {

            Snackbar.make(binding.getRoot(), "Please enter all values", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Check that the values are not empty
     *
     * * @return
     */
    private boolean isAllValuesValid(){

        String degree = binding.searchViewDegree.getQuery().toString();
        String field = binding.searchViewFieldOfStudy.getQuery().toString();
        String school = binding.searchViewSchool.getQuery().toString();

        if (degree.isEmpty() || field.isEmpty() || school.isEmpty()){

            return false;
        }

        return true;
    }


    /**
     * Sets default values for the Select School search view
     */
    private void setUpUniversitySearchView(){

        binding.searchViewSchool.setOnQueryTextListener(this);
        binding.searchViewSchool.setIconifiedByDefault(false);
        binding.searchViewSchool.setFocusable(false);

        //set the color for our search view edit text and text hint
        EditText searchEditText = binding.searchViewSchool.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(R.color.material_icons_light));
        searchEditText.setHint("School");
        searchEditText.setGravity(Gravity.CENTER);

        //set the color for our search view icon
        ImageView searchMagIcon = binding.searchViewSchool.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchMagIcon.setColorFilter(ContextCompat.getColor(appContext, R.color.white));
        searchMagIcon.setVisibility(View.GONE);

        //set the line color
        View v = binding.searchViewSchool.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * Sets default values for the Select Degree search view
     */
    private void setUpDegreeSearchView(){

        binding.searchViewDegree.setOnQueryTextListener(this);
        binding.searchViewDegree.setIconifiedByDefault(false);
        binding.searchViewDegree.setFocusable(false);

        //set the color for our search view edit text and text hint
        EditText searchEditText = binding.searchViewDegree.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(R.color.material_icons_light));
        searchEditText.setHint("Degree");
        searchEditText.setGravity(Gravity.CENTER);

        //set the color for our search view icon
        ImageView searchMagIcon = binding.searchViewDegree.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchMagIcon.setColorFilter(ContextCompat.getColor(appContext, R.color.white));
        searchMagIcon.setVisibility(View.GONE);

        //set the line color
        View v = binding.searchViewDegree.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
    }


    /**
     * Sets default values for the Select Field of Study search view
     */
    private void setUpFieldOfStudySearchView(){

        binding.searchViewFieldOfStudy.setOnQueryTextListener(this);
        binding.searchViewFieldOfStudy.setIconifiedByDefault(false);
        binding.searchViewFieldOfStudy.setFocusable(false);

        //set the color for our search view edit text and text hint
        EditText searchEditText = binding.searchViewFieldOfStudy.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHintTextColor(getResources().getColor(R.color.material_icons_light));
        searchEditText.setHint("Field of Study");
        searchEditText.setGravity(Gravity.CENTER);

        //set the color for our search view icon
        ImageView searchMagIcon = binding.searchViewFieldOfStudy.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchMagIcon.setColorFilter(ContextCompat.getColor(appContext, R.color.white));
        searchMagIcon.setVisibility(View.GONE);

        //set the line color
        View v = binding.searchViewFieldOfStudy.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);
    }
}
