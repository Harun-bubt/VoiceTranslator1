package com.example.voicetranslator.helper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Helper {
    public static void show(Context con, String s) {
        Toast.makeText(con, s, Toast.LENGTH_SHORT).show();
    }

    public static void showLog(String s) {
        System.out.println("AAA : " + s);
    }

    public static void showLog(String tag, String s) {
        System.out.println(tag + " : " + s);
    }

    public static void shareText(Context con, String text) {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType("text/plain");
        share.putExtra("android.intent.extra.TEXT", text);
        con.startActivity(Intent.createChooser(share, "Share Result"));
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (conMan.getActiveNetworkInfo() == null || !conMan.getActiveNetworkInfo().isConnected()) {
            return false;
        }
        return true;
    }
}