package com.team.mamba.atlascalendar.userInterface.dashBoard.profile.contacts_profile.notes.edit_note;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.team.mamba.atlascalendar.BR;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.AppDataManager;

import com.team.mamba.atlascalendar.data.model.api.fireStore.PersonalNotes;
import com.team.mamba.atlascalendar.databinding.PersonalNoteRecyclerViewBinding;
import com.team.mamba.atlascalendar.userInterface.base.BaseFragment;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivity;
import com.team.mamba.atlascalendar.userInterface.dashBoard._container_activity.DashBoardActivityNavigator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class PersonalNoteRecyclerView extends BaseFragment<PersonalNoteRecyclerViewBinding,PersonalNotesViewModel> implements PersonalNoteNavigator {


    @Inject
    PersonalNotesViewModel viewModel;

    @Inject
    PersonalNoteDataModel dataModel;

    @Inject
    Context appContext;


    private PersonalNoteRecyclerViewBinding binding;
    private static PersonalNotes personalNotes;
    private PersonalNoteAdapter personalNoteAdapter;
    private List<String> detailsList = new ArrayList<>();
    private String deletedDetail = "";
    private DashBoardActivityNavigator parentNavigator;




    public static PersonalNoteRecyclerView newInstance(PersonalNotes notes,AppDataManager appDataManager){

        personalNotes = notes;
        return new PersonalNoteRecyclerView();
    }


    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.personal_note_recycler_view;
    }

    @Override
    public PersonalNotesViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View getProgressSpinner() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentNavigator = (DashBoardActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setNavigator(this);
        viewModel.setDataModel(dataModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.personal_note_recycler_view,container,false);

        detailsList.addAll(personalNotes.getDetails());
        personalNoteAdapter = new PersonalNoteAdapter(detailsList,this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(personalNoteAdapter);


        setUpItemTouchHelper();
        return binding.getRoot();
    }

    @Override
    public void onAddButtonClicked() {

        detailsList.add("");
        personalNoteAdapter.notifyDataSetChanged();

    }

    @Override
    public void onFinishButtonClicked() {

        List<String> descriptionList = new ArrayList<>();

        for (Map.Entry<Integer,String> entry : personalNoteAdapter.getDetailsMap().entrySet()){

            String description = entry.getValue();
            descriptionList.add(description);
        }

        personalNotes.setDetails(descriptionList);
        Toast.makeText(getActivity(),"Saving your info",Toast.LENGTH_SHORT).show();

        viewModel.sendUserNote(getViewModel(),personalNotes);

    }


    /**
     * Sets the swipe to delete on our adapter
     */
    private void setUpItemTouchHelper(){

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    deletedDetail = detailsList.get(position);
                    detailsList.remove(position);
                    personalNoteAdapter.deleteMap(position);

                    personalNoteAdapter.notifyDataSetChanged();

                    Snackbar snackbar = Snackbar.make(binding.getRoot(), "item deleted", 4000)

                            .setAction("Undo", v -> {

                                detailsList.add(position,deletedDetail);
                                personalNoteAdapter.notifyDataSetChanged();

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

    @Override
    public void onNoteSentSuccessfully() {

        hideProgressSpinner();
        parentNavigator.resetToFirstFragment();
    }
}
