package com.team.mamba.atlascalendar.utils.formatData;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AppFormatter {

    private AppFormatter(){

    }


    public static NumberFormat timeStampFormatter = new DecimalFormat("#");

    public static DecimalFormat numberWithoutDecimals = new DecimalFormat("#,###,###");

}
