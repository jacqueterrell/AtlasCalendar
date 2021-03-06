package com.team.mamba.atlascalendar.userInterface.dashBoard.locator.calendarDayView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.CalendarEvents;
import com.team.mamba.atlascalendar.userInterface.dashBoard.locator.calendarDayView.CalendarDayAdapter.CalendarDayViewHolder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.team.mamba.atlascalendar.databinding.CalendarDayListRowBinding;

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayViewHolder> {


    public List<CalendarEvents> calendarEventsList;

    public CalendarDayAdapter(List<CalendarEvents> calendarEvents){

        this.calendarEventsList = calendarEvents;
    }


    public class CalendarDayViewHolder extends RecyclerView.ViewHolder{

        private CalendarDayListRowBinding binding;

        public CalendarDayViewHolder(@NonNull CalendarDayListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public CalendarDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        CalendarDayListRowBinding binding =
                DataBindingUtil.inflate(inflater,R.layout.calendar_day_list_row,parent,false);

        return new CalendarDayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarDayViewHolder holder, int position) {

        CalendarEvents events = calendarEventsList.get(position);
        SimpleDateFormat date_format = new SimpleDateFormat("h:mm a");


        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(events.getStartTime() * 1000);
        String startHour = date_format.format(startCalendar.getTime());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(events.getEndTime() * 1000);
        String endHour = date_format.format(endCalendar.getTime());

        holder.binding.tvTitle.setText(events.getTitle());
        holder.binding.tvDescription.setText(events.getDescription());

        if (!events.isAllDayEvent()){

            String date = startHour + " - " + endHour;
            holder.binding.tvDate.setText(date);

        } else {

            holder.binding.tvDate.setText(startHour);
        }

        if (events.isHoliday()){

            holder.binding.ivEvent.setVisibility(View.GONE);
            holder.binding.ivHoliday.setVisibility(View.VISIBLE);
            holder.binding.tvDescription.setText("U.S. Holiday");
            holder.binding.tvDate.setText("All Day");

        } else {

            holder.binding.ivEvent.setVisibility(View.VISIBLE);
            holder.binding.ivHoliday.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return calendarEventsList.size();
    }

}
