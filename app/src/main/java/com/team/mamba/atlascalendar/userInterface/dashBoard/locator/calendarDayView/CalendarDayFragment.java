package com.team.mamba.atlascalendar.userInterface.dashBoard.locator.calendarDayView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.CalendarEvents;
import com.team.mamba.atlascalendar.databinding.CalendarDayRecyclerBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarDayFragment extends Fragment {


    private static List<CalendarEvents> allCalendarEventsList;
    private static List<CalendarEvents> filteredEventsList = new ArrayList<>();

    private CalendarDayAdapter calendarDayAdapter;
    private CalendarDayRecyclerBinding binding;
    private static CalendarDay selectedCalDay;


    public static CalendarDayFragment newInstance(List<CalendarEvents> calendarEvents,CalendarDay calendarDay) {

        allCalendarEventsList = calendarEvents;
        selectedCalDay = calendarDay;
        return new CalendarDayFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.calendar_day_recycler, container, false);

        calendarDayAdapter = new CalendarDayAdapter(filteredEventsList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(calendarDayAdapter);

        setDate();
        sortCalendarEvents();

        return binding.getRoot();
    }

    private void sortCalendarEvents(){

        filteredEventsList.clear();

        for (CalendarEvents events : allCalendarEventsList){

            if (events.getCalendarDay().equals(selectedCalDay)){

                filteredEventsList.add(events);
            }
        }

        Collections.sort(filteredEventsList,(o1,o2) -> Double.compare(o1.getStartTime(), o2.getStartTime()));
        calendarDayAdapter.notifyDataSetChanged();

    }

    private void setDate(){

        int year = selectedCalDay.getYear();
        int month = selectedCalDay.getMonth();
        int day = selectedCalDay.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month - 1,day);
        Date date = calendar.getTime();

        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
        String monthString  = (String) DateFormat.format("MMM",  date); // Jun
        String dayOfMonth          = (String) DateFormat.format("dd",   date); // 20
        String selectedYear         = (String) DateFormat.format("yyyy", date); // 2013

        String newDate = dayOfTheWeek + " " + monthString + " " + dayOfMonth + ", " + selectedYear;
        binding.tvDate.setText(newDate);
    }
}
