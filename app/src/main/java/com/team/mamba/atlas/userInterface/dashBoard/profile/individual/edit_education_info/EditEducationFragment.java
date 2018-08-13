package com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_education_info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlas.BR;
import com.team.mamba.atlas.R;
import com.team.mamba.atlas.data.model.api.UserProfile;
import com.team.mamba.atlas.data.model.local.Education;
import com.team.mamba.atlas.databinding.EditEducationLayoutBinding;
import com.team.mamba.atlas.userInterface.base.BaseFragment;

import com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_address_info.EditAddressFragment;
import com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_email_info.EditEmailFragment;
import com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_work_history.EditWorkFragment;
import com.team.mamba.atlas.utils.ChangeFragments;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class EditEducationFragment extends BaseFragment<EditEducationLayoutBinding, EditEducationViewModel>
        implements EditEducationNavigator {


    @Inject
    EditEducationViewModel viewModel;

    @Inject
    public EditEducationDataModel dataModel;

    private EditEducationLayoutBinding binding;
    private static UserProfile profile;
    private List<Education> educationList = new ArrayList<>();
    private EditEducationAdapter editEducationAdapter;
    private Education deletedEducation;


    public static EditEducationFragment newInstance(UserProfile userProfile){

        profile = userProfile;
        return new EditEducationFragment();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_education_layout;
    }

    @Override
    public EditEducationViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return binding.progressSpinner;
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

        setRecyclerData();
        setCachedData();
        setUpItemTouchHelper();

        return binding.getRoot();
    }

    @Override
    public void onContinueClicked() {

        ChangeFragments.addFragmentFadeIn(EditWorkFragment.newInstance(profile),getBaseActivity()
                .getSupportFragmentManager(),"EditWork",null);
    }

    @Override
    public void onSaveAndCloseClicked() {

        setEducationData();
        showProgressSpinner();
    }

    @Override
    public void onProfileUpdated() {

        hideProgressSpinner();
        getBaseActivity().getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void onAddNewEducation() {

        viewModel.setEditing(false);

        ChangeFragments.addFragmentVertically(AddEducationFragment.newInstance(this), getBaseActivity()
                .getSupportFragmentManager(), "AddEducation", null);
    }

    @Override
    public boolean isEditing() {
        return viewModel.isEditing();
    }


    @Override
    public Education getEditingEducation() {
        return viewModel.getEditingEducation();
    }

    @Override
    public void onRowClicked(Education education, int pos) {

        viewModel.setEditing(true);
        viewModel.setEditingEducation(education);
        viewModel.setEditingIndex(pos);

        ChangeFragments.addFragmentVertically(AddEducationFragment.newInstance(this), getBaseActivity()
                .getSupportFragmentManager(), "AddEducation", null);
    }

    @Override
    public void onSaveNewEducation(Education education) {

        educationList.add(education);
        int index = educationList.size() - 1;
        editEducationAdapter.notifyItemInserted(index);
    }

    @Override
    public void onEditRowSaved(Education education) {

        int position = viewModel.getEditingIndex();

        educationList.remove(position);
        educationList.add(position,education);
        editEducationAdapter.notifyDataSetChanged();
    }

    private void setRecyclerData(){

        editEducationAdapter = new EditEducationAdapter(getViewModel(), educationList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(editEducationAdapter);
    }

    /**
     * Creates a map of all educations
     */
    private void setEducationData() {

        List<Map<String, String>> educationMaps = new ArrayList<>();

        for (Education education : educationList) {

            Map<String, String> map = new LinkedHashMap<>();

            map.put("School", education.getSchool());
            map.put("Field of Study", education.getFieldOfStudy());
            map.put("Degree", education.getDegree());

            educationMaps.add(map);
        }

        profile.setEducation(educationMaps);
        viewModel.updateUser(getViewModel(),profile);
    }

    private void setCachedData(){

        for (Map<String, String> map : profile.getEducation()){

            Education education = new Education();

            for (Map.Entry<String,String> entry : map.entrySet()){

                if (entry.getKey().toLowerCase().equals("degree")){

                    education.setDegree(entry.getValue());

                } else if (entry.getKey().toLowerCase().equals("school")){

                    education.setSchool(entry.getValue());

                } else {

                    education.setFieldOfStudy(entry.getValue());
                }
            }

            educationList.add(education);
            editEducationAdapter.notifyDataSetChanged();

        }
    }


    /**
     * Sets up our recyclerview's itemTouchHelper (for swiping)
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                    RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    deletedEducation = educationList.get(position);
                    educationList.remove(position);

                    editEducationAdapter.notifyDataSetChanged();

                    Snackbar snackbar = Snackbar.make(binding.getRoot(), "item deleted", 4000)

                            .setAction("Undo", v -> {

                                educationList.add(position,deletedEducation);
                                editEducationAdapter.notifyDataSetChanged();

                                showRestoredSnackbar();
                            });

                    View view = snackbar.getView();
                    view.setBackgroundResource(R.drawable.snackbar_background);
                    snackbar.setActionTextColor(getResources().getColor(R.color.white));

                    snackbar.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                    float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                try {

                    Bitmap icon;
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;
                        Paint paint = new Paint();
                        paint.setColor(Color.parseColor("#D32F2F"));

                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getBaseActivity().getResources(), R.drawable.ic_action_delete);
                        RectF destination = new RectF((float) itemView.getRight() - 2 * width,
                                (float) itemView.getTop() + width, (float) itemView.getRight() - width,
                                (float) itemView.getBottom() - width);

                        c.drawBitmap(icon, null, destination, paint);

                    } else {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);

    }

    /**
     * Used to show an 'items restored' snackbar
     */
    private void showRestoredSnackbar() {

        Snackbar snackbar = Snackbar.make(binding.getRoot(), "item restored", 2000);
        View v = snackbar.getView();
        v.setBackgroundColor(ContextCompat.getColor(getBaseActivity(), R.color.dessert_green));

        snackbar.show();
    }

}
