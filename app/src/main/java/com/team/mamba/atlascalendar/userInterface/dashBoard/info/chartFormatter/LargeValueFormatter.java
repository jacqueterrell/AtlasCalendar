package com.team.mamba.atlascalendar.userInterface.dashBoard.info.chartFormatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by jterrell on 8/22/2017.
 */

public class LargeValueFormatter implements IAxisValueFormatter{
    private static String[] SUFFIX = new String[] {
            "", "k", "m", "b", "t"
    };

    private static int MAX_LENGTH = 4;

    private DecimalFormat mFormat;

    public LargeValueFormatter() {

        mFormat = new DecimalFormat("###E0");
    }


    private String makePretty(double number) {

        String r = mFormat.format(number);

        r = r.replaceAll("E[0-9]", SUFFIX[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);

        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }

        return r;
    }



    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return makePretty(value);
    }


}