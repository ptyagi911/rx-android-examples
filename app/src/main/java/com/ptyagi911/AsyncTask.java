package com.ptyagi911;

import android.util.Log;

/**
 * Created by tyagp001 on 4/4/16.
 */
public class AsyncTask extends android.os.AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        return params[0];
    }

    protected void onPostExecute(String string) {
        MainActivityAsync.textView.setText(string);
        Log.d("Priyanka", "onPostExecute: " + string);
    }
}
