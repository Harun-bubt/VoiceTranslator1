package com.example.voicetranslator.voice_translator.trans;

import android.content.ClipData;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.ClipboardManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.voicetranslator.R;

public class TransUtils {
    public static int m3918a(int i) {
        return i == 0 ? 1 : 0;
    }

    public static boolean m3924b(int i) {
        return true;
    }

    public static float m3917a(float f, Context context) {
        return (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f) * f;
    }

    public static void m3921a(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void m3922a(Context context, String str) {
        Toast makeText = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public static boolean m3925b(Context context, String str) {
        String str2 = "clipboard";
        try {
            if (VERSION.SDK_INT < 11) {
                ((ClipboardManager) context.getSystemService(str2)).setText(str);
            } else {
                ((android.content.ClipboardManager) context.getSystemService(str2)).setPrimaryClip(ClipData.newPlainText(context.getResources().getString(R.string.copy), str));
                m3922a(context, context.getResources().getString(R.string.copy));
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}