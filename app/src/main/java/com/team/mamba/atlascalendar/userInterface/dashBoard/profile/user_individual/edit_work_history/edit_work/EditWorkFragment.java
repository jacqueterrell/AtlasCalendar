package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.edit_work;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.api.fireStore.UserProfile;
import com.team.mamba.atlascalendar.data.model.local.WorkHistory;
import com.team.mamba.atlascalendar.databinding.EditWorkHistoryLayoutBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;

import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;
import com.team.mamba.atlascalendar.userInterface.dashBoard.profile.user_individual.edit_work_history.add_new.AddWorkFragment;
import com.team.mamba.atlascalendar.utils.ChangeFragments;
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

    @Inject
    Context appContext;

    private EditWorkHistoryLayoutBinding binding;
    private static UserProfile profile;
    private List<WorkHistory> workHistoryList = new ArrayList<>();
    private EditWorkAdapter workAdapter;
    private WorkHistory deletedWorkHistory;
    private DashBoardActivityNavigator parentNavigator;

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


        setRecyclerData();
        setCachedData();
        setUpItemTouchHelper();

         return binding.getRoot();
    }

    @Override
    public void onFinishedClicked() {

        showProgressSpinner();
        setWorkHistoryData();
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

        showToastShort("Profile Updated");
        FragmentManager manager = getBaseActivity().getSupportFragmentManager();
        manager.popBackStack("UserProfile",0);;
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

        Long timeStamp = System.currentTimeMillis() / 1000;

        profile.setTimestamp(timeStamp);
        profile.setWorkHistory(workMaps);
        viewModel.updateUser(getViewModel(),profile);
    }

    private void setCachedData(){

        for (Map<String, String> map : profile.getWorkHistory()){

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

                    deletedWorkHistory = workHistoryList.get(position);
                    workHistoryList.remove(position);
                    workAdapter.notifyItemRemoved(position);

                    Snackbar snackbar = Snackbar.make(binding.getRoot(), "item deleted", 3000)

                            .setAction("Undo", v -> {

                                workHistoryList.add(position,deletedWorkHistory);
                                workAdapter.notifyItemInserted(position);

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
                        icon = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_action_delete);
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
        v.setBackgroundColor(ContextCompat.getColor(appContext, R.color.dessert_green));
        snackbar.show();
    }

}
