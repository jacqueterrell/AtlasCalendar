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
    private static String fullName = "";
    private int sdk = Build.VERSION.SDK_INT;
    private int marshMallow = Build.VERSION_CODES.M;
    private List<CalendarEvents> calendarEventsArrayList = new ArrayList<>();


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


        int dayTest = Integer.parseInt("25");
        int monthTest = Integer.parseInt("10");
        int yearTest = Integer.parseInt("2018");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String date = getMonth(month) + " " + year;
        binding.tvDate.setText(date);
        binding.tvUser.setText(fullName);

        CalendarDay calendarDay = CalendarDay.from(yearTest, monthTest, dayTest);
        calendarDayList.add(calendarDay);

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


    @SuppressLint({"MissingPermission", "CheckResult"})
    private void getUserCalendarInfo(){

        Observable.fromCallable(() -> {

            List<CalendarEvents> calendarEventsList = new ArrayList<>();
            Cursor cursor;
            ContentResolver cr = getActivity().getContentResolver();

            String[] mProjection =
                    {
                            "_id",
                            CalendarContract.Events.TITLE,
                            CalendarContract.Events.EVENT_LOCATION,
                            CalendarContract.Events.DTSTART,
                            CalendarContract.Events.DTEND,
                    };

            Uri uri = CalendarContract.Events.CONTENT_URI;

            String selection = CalendarContract.Events.EVENT_LOCATION + " = ? ";
            String[] selectionArgs = new String[]{"London"};// equals 'where location == London'

            cursor = cr.query(uri, mProjection, null, null, CalendarContract.Events.DTSTART + " ASC");

            while (cursor.moveToNext()) {
                int size = cursor.getCount();
                int index = cursor.getPosition();
                String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                String location = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                String startTime = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
                String endTime = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND));

                CalendarEvents calendarEvents = new CalendarEvents(title,location,Long.parseLong(startTime),Long.parseLong(endTime));
                calendarEventsList.add(calendarEvents);

               Logger.i(title);
            }

            return calendarEventsList;

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendarEvents -> {

                    Logger.i("response successful");
                    calendarEventsArrayList.clear();
                    calendarEventsArrayList.addAll(calendarEvents);

                }, throwable -> {

                    Logger.e(throwable.getLocalizedMessage());
                });
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
