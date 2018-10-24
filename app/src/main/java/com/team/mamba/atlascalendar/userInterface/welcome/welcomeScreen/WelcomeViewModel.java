package com.team.mamba.atlascalendar.userInterface.welcome.welcomeScreen;

import com.team.mamba.atlascalendar.userInterface.base.BaseViewModel;

import java.util.Calendar;

public class WelcomeViewModel extends BaseViewModel<WelcomeNavigator> {


    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_A_DAY = 24;
    private WelcomeDataModel dataModel;

    private String userCode;
    private long dateOfBirth;


    /***************view logic************/

    public boolean isAgeValid(int month, int day, int year){

        Calendar calDob = Calendar.getInstance();
        Calendar calTodaysDate = Calendar.getInstance();
        Calendar calThirteen = Calendar.getInstance();

        calDob.set(year,month,day);
        calDob.set(Calendar.HOUR,0);
        calDob.set(Calendar.MINUTE,1);
        calDob.set(Calendar.AM_PM,Calendar.AM);
        calDob.set(Calendar.SECOND,0);

        int thirteenYearsBack = (calThirteen.get(Calendar.YEAR) - 13);
        calThirteen.set(Calendar.YEAR,thirteenYearsBack);

        // Get the represented date in milliseconds
        long millisDob = calDob.getTimeInMillis();
        long millisToday = calTodaysDate.getTimeInMillis();
        long millisThirteenBack = calThirteen.getTimeInMillis();

        long dobTimeStamp = millisDob / 1000;

        setDateOfBirth(dobTimeStamp);

        // Calculate difference in milliseconds
        long diff = millisToday - millisDob;
        long diffThirteen = millisToday - millisThirteenBack;

        // Calculate difference in days
        long ageInDays = diff / (HOURS_IN_A_DAY * MINUTES_IN_HOUR * 60 * 1000);
        long daysInThirteenYears = diffThirteen / (HOURS_IN_A_DAY * MINUTES_IN_HOUR * 60 * 1000);


        if (ageInDays >= daysInThirteenYears){

            return true;

        } else {

            return false;
        }

    }

    /***************getters and setters************/

    public void setDataModel(WelcomeDataModel dataModel) {
        this.dataModel = dataModel;
    }



    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    /***************onclick listeners************/

    public void onStartButtonClicked(){

        getNavigator().onStartButtonClicked();
    }

    public void onBusinessLoginClicked(){

        getNavigator().onBusinessLoginClicked();
    }

    public void onDateVerifyClicked(){

        getNavigator().onDateVerifyClicked();
    }

    public void onDateCancelClicked(){

        getNavigator().onDateCancelClicked();
    }

    //data model requests

}
