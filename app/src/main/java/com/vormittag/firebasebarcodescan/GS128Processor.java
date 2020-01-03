package com.vormittag.firebasebarcodescan;

import android.util.Log;

import com.vormittag.firebasebarcodescan.common.GS1Code128Data;


public class GS128Processor {
    private static final String LOG_TAG = "GS128Processor";

    public GS128Processor(String scanContent) {
        processBarcode(scanContent);
    }

    public void processBarcode(String scanContent){

        try {
            GS1Code128Data data = new GS1Code128Data(scanContent, '\f');
            Log.v(LOG_TAG, "Serial Shipping Container Code:" + data.get00());
            Log.v(LOG_TAG, "Global Trade Item Number/UPC:" + data.get01());
            Log.v(LOG_TAG, "Production Date:" + data.get11());
            Log.v(LOG_TAG, "Due Date:" + data.getDueDate());
            Log.v(LOG_TAG, "Packaging Date:" + data.get13());
            Log.v(LOG_TAG, "Best Before Date (YYMMDD)" + data.get15());
            Log.v(LOG_TAG, "Expiration Date: " + data.get17());
            Log.v(LOG_TAG, "Serial Number :" + data.get21());
            Log.v(LOG_TAG, "Product Net Weight, in pounds :" + data.get3202());


        } catch (Exception e) {
            Log.v(LOG_TAG, "exception " + e.getLocalizedMessage());
        }
    }
}
