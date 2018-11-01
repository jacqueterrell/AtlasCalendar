package com.team.mamba.atlascalendar.userInterface.dashBoard.locator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.team.mamba.atlascalendar.R;
import com.team.mamba.atlascalendar.data.model.local.CalendarEvents;
import com.team.mamba.atlascalendar.databinding.CalendarMonthLayoutBinding;
import com.team.mamba.atlascalendar.utils.AppConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarMonthFragment extends Fragment {

    private CalendarMonthLayoutBinding binding;
    private List<CalendarDay> calendarDayList = new ArrayList<>();
    private List<CalendarEvents> returnedCalEvents = new ArrayList<>();
    private static String fullName = "";
    private int sdk = Build.VERSION.SDK_INT;
    private int marshMallow = Build.VERSION_CODES.M;
    private List<CalendarEvents> calendarEventsArrayList = new ArrayList<>();
    private boolean allDayEvent = false;

    public static CalendarMonthFragment newInstance(String name) {

        fullName = name;
        return new CalendarMonthFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.calendar_month_layout, container, false);



        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String date = getMonth(month) + " " + year;
        binding.tvDate.setText(date);
        binding.tvUser.setText(fullName);

        binding.calendarView.setDateSelected(CalendarDay.today(), true);
        binding.calendarView.setTopbarVisible(false);
        binding.calendarView.setSelectionColor(getActivity().getResources().getColor(R.color.dark_blue));

        setDecorator();
        setOnClickListeners();

        getCurrentCalendarInfo();
        return binding.getRoot();
    }


    private void setDecorator() {

        binding.calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay calendarDay) {

                return calendarDayList.contains(calendarDay);
            }

            @Override
            public void decorate(DayViewFacade dayViewFacade) {

                dayViewFacade.addSpan(new ForegroundColorSpan(Color.WHITE));
                dayViewFacade.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.calendar_decorator));
            }
        });
    }


    /**
     * Sets onClickListeners for the Calendar
     */
    private void setOnClickListeners() {

        binding.calendarView.setOnMonthChangedListener((materialCalendarView, calendarDay) -> {

            int month = calendarDay.getMonth();
            String year = String.valueOf(calendarDay.getYear());
            String date = getMonth(month) + " " + year;
            binding.tvDate.setText(date);
        });

        binding.calendarView.setOnDateChangedListener((materialCalendarView, calendarDay, b) -> {


            binding.calendarView.setDateSelected(calendarDay, false);
            binding.calendarView.setDateSelected(CalendarDay.today(), true);


            if (calendarDayList.contains(calendarDay)) {

                showSnackBar("Schedule already set");
            }
        });
    }

    private void showSnackBar(String txt) {

        Snackbar.make(binding.getRoot(), txt, Snackbar.LENGTH_LONG)
                .show();
    }




    private void getCurrentCalendarInfo() {

        if (isCalendarPermissionsGranted()) {

            getUserCalendarInfo();

        }
    }


    /**
     * Uses a content provider to get all calendar events
     * from the user's device
     */
    @SuppressLint({"MissingPermission", "CheckResult"})
    private void getUserCalendarInfo(){

        returnedCalEvents.clear();

        Observable.fromCallable(() -> {

            Cursor cursor;
            ContentResolver cr = getActivity().getContentResolver();

            String[] mProjection =
                    {
                            "_id",
                            Events.TITLE,
                            Events.EVENT_LOCATION,
                            Events.DTSTART,
                            Events.DTEND,
                    };

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
            long startDay = calendar.getTimeInMillis();

            Uri uri = Events.CONTENT_URI;

            String selection = Events.DTSTART + " >= ? ";
            String[] selectionArgs = new String[]{Long.toString(startDay)};// equals 'where startTime >= Jan[current year]'

            cursor = cr.query(uri, mProjection, selection, selectionArgs, Events.DTSTART + " ASC");

            while (cursor.moveToNext()) {

                if (cursor.getString(cursor.getColumnIndex(Events.DTSTART)) != null){//invalid start date
                    queryCursor(cursor);
                }
            }

            return returnedCalEvents;

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendarEvents -> {

                    Logger.i("response successful");
                    calendarEventsArrayList.clear();
                    calendarEventsArrayList.addAll(calendarEvents);
                    binding.calendarView.invalidateDecorators();

                }, throwable -> {

                    Logger.e(throwable.getLocalizedMessage());
                });
    }


    private void queryCursor(Cursor cursor){

        String title = cursor.getString(cursor.getColumnIndex(Events.TITLE));
        String location = cursor.getString(cursor.getColumnIndex(Events.EVENT_LOCATION));
        String startTime = cursor.getString(cursor.getColumnIndex(Events.DTSTART)).substring(0,10);
        String endTime = cursor.getString(cursor.getColumnIndex(Events.DTEND));

        if (endTime == null){//one day event
            endTime = startTime;
            allDayEvent = true;
        } else {
            allDayEvent = false;
            endTime = cursor.getString(cursor.getColumnIndex(Events.DTEND)).substring(0,10);
        }

        long start = Long.parseLong(startTime);
        Date startDate = new Date();
        startDate.setTime(start * 1000);

        long end = Long.parseLong(endTime);
        Date endDate = new Date();
        endDate.setTime(end * 1000);

        String day          = (String) DateFormat.format("dd",   startDate); // 20
        String monthString  = (String) DateFormat.format("MMM",  startDate); // Jun
        String monthNumber  = (String) DateFormat.format("MM",   startDate); // 06
        String year         = (String) DateFormat.format("yyyy", startDate); // 2013
        CalendarDay calendarDay =  CalendarDay.from(Integer.parseInt(year),Integer.parseInt(monthNumber),Integer.parseInt(day));

        int diff = (int) (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

        if (diff == 0){//one day event

            CalendarEvents calendarEvents = new CalendarEvents.Builder()
                    .setTitle(title)
                    .setStartTime(start)
                    .setEndTime(end)
                    .setLocation(location)
                    .setAllDayEvent(allDayEvent)
                    .setCalendarDay(calendarDay)
                    .build();

            calendarDayList.add(calendarDay);
            returnedCalEvents.add(calendarEvents);

        } else {//multiple day event

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(start * 1000);
            allDayEvent = true;

            for (int i = 0; i <= diff; i++){

                long dateTimestamp = calendar.getTimeInMillis() / 1000;

                CalendarEvents calendarEvents = new CalendarEvents.Builder()
                        .setTitle(title)
                        .setStartTime(dateTimestamp)
                        .setEndTime(dateTimestamp)
                        .setLocation(location)
                        .setAllDayEvent(allDayEvent)
                        .setCalendarDay(calendarDay)
                        .build();

                calendarDayList.add(calendarDay);
                returnedCalEvents.add(calendarEvents);

                calendar.add(Calendar.DAY_OF_MONTH,1);
            }

        }

        Logger.i(title);
    }


    private boolean isCalendarPermissionsGranted() {

        if (sdk >= marshMallow) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_CALENDAR},
                        AppConstants.REQUEST_READ_CALENDAR_PERMISSIONS);

                return false;

            } else {

                return true;
            }

        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AppConstants.REQUEST_READ_CALENDAR_PERMISSIONS
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            getUserCalendarInfo();
        }
    }



    private String getMonth(int index) {

        List<String> monthsList = new ArrayList<>();

        monthsList.add("");
        monthsList.add("January");
        monthsList.add("February");
        monthsList.add("March");
        monthsList.add("April");
        monthsList.add("May");
        monthsList.add("June");
        monthsList.add("July");
        monthsList.add("August");
        monthsList.add("September");
        monthsList.add("October");
        monthsList.add("November");
        monthsList.add("December");

        return monthsList.get(index);
    }
}
