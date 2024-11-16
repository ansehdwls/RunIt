package com.ssafy.runit.util;


public class DoubleUtils {
    public static double distanceFormatter(double dis){
        String formattedValue = String.format("%.2f", dis);
        return Double.parseDouble(formattedValue);
    }
}
