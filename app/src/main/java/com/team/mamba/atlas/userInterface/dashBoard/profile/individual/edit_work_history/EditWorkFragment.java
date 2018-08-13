package com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_work_history;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlas.BR;
import com.team.mamba.atlas.R;
import com.team.mamba.atlas.data.model.api.UserProfile;
import com.team.mamba.atlas.data.model.local.Education;
import com.team.mamba.atlas.data.model.local.WorkHistory;
import com.team.mamba.atlas.databinding.EditWorkHistoryLayoutBinding;
import com.team.mamba.atlas.userInterface.base.BaseFragment;

import com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_education_info.AddEducationFragment;
import com.team.mamba.atlas.userInterface.dashBoard.profile.individual.edit_education_info.EditEducationAdapter;
import com.team.mamba.atlas.utils.ChangeFragments;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class EditWorkFragment extends BaseFragment<EditWorkHistoryLayoutBinding,EditWorkViewModel> implements EditWorkNavigator {

    @Inject
    EditWorkDataModel dataModel;

    @Inject
    EditWorkViewModel viewModel;

    private EditWorkHistoryLayoutBinding binding;
    private static UserProfile profile;
    private List<WorkHistory> workHistoryList = new ArrayList<>();
    private EditWorkAdapter workAdapter;
    private int deletedPosition;
    private WorkHistory deletedWorkHistory;

    public static EditWorkFragment newInstance(UserProfile userProfile){

        profile = userProfile;
        return new EditWorkFragment();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.edit_work_history_layout;
    }

    @Override
    public EditWorkViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
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
    public void onFinishedClicked() {

        showProgressSpinner();
    }

    @Override
    public void onSaveAndCloseClicked() {

        showProgressSpinner();
        setWorkHistoryData();

    }

    @Override
    public void onEditRowSaved(WorkHistory workHistory) {

        int position = viewModel.getEditingIndex();

        workHistoryList.remove(position);
        workHistoryList.add(position,workHistory);
        workAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProfileUpdated() {

        hideProgressSpinner();
        getBaseActivity().getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onSaveNewWorkHistory(WorkHistory workHistory) {

        workHistoryList.add(workHistory);
        int index = workHistoryList.size() - 1;
        workAdapter.notifyItemInserted(index);
    }

    @Override
    public void onAddNewWork() {

        viewModel.setEditing(false);

        ChangeFragments.addFragmentVertically(AddWorkFragment.newInstance(this), getBaseActivity()
                .getSupportFragmentManager(), "AddEducation", null);
    }

    @Override
    public void onRowClicked(WorkHistory workHistory, int pos) {

        viewModel.setEditing(true);
        viewModel.setEditingWorkHistory(workHistory);
        viewModel.setEditingIndex(pos);

        ChangeFragments.addFragmentVertically(AddWorkFragment.newInstance(this), getBaseActivity()
                .getSupportFragmentManager(), "AddEducation", null);
    }

    @Override
    public boolean isEditing() {
        return viewModel.isEditing();
    }

    @Override
    public WorkHistory getEditingWork() {
        return viewModel.getEditingWorkHistory();
    }

    private void setRecyclerData(){

        workAdapter = new EditWorkAdapter(getViewModel(), workHistoryList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(workAdapter);
    }


    /**
     * Creates a map of all educations
     */
    private void setWorkHistoryData() {

        List<Map<String, String>> workMaps = new ArrayList<>();

        for (WorkHistory workHistory : workHistoryList) {

            Map<String, String> map = new LinkedHashMap<>();

            map.put("Industry", workHistory.getIndustry());
            map.put("What was your title?", workHistory.getTitle());
            map.put("Where did you work?", workHistory.getCompanyName());

            workMaps.add(map);
        }

        profile.setWorkHistory(workMaps);
        viewModel.updateUser(getViewModel(),profile);
    }

    private void setCachedData(){

        for (Map<String, String> map : profile.getEducation()){

            WorkHistory workHistory = new WorkHistory();

            for (Map.Entry<String,String> entry : map.entrySet()){

                if (entry.getKey().toLowerCase().contains("industry")){

                    workHistory.setIndustry(entry.getValue());

                } else if (entry.getKey().toLowerCase().contains("title")){

                    workHistory.setTitle(entry.getValue());

                } else {

                    workHistory.setCompanyName(entry.getValue());
                }
            }

            workHistoryList.add(workHistory);
            workAdapter.notifyDataSetChanged();
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

                    deletedPosition = position;
                    deletedWorkHistory = workHistoryList.get(position);
                    workHistoryList.remove(position);

                    workAdapter.notifyDataSetChanged();

                    Snackbar snackbar = Snackbar.make(binding.getRoot(), "item deleted", 4000)

                            .setAction("Undo", v -> {

                                workHistoryList.add(position,deletedWorkHistory);
                                workAdapter.notifyDataSetChanged();

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
