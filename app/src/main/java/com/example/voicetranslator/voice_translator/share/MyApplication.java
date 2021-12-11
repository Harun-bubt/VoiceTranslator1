package com.example.voicetranslator.voice_translator.share;

import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.appizona.yehiahd.fastsave.FastSave;

public class MyApplication extends MultiDexApplication {
    public static String FIRST_LANG_CODE = "FIRST_LANG_CODE";
    public static String SEC_LANG_CODE = "SEC_LANG_CODE";
    private static MyApplication appInstance;
    public static int selected_lang_pos;

    public void onCreate() {
        super.onCreate();
        appInstance = this;
        MultiDex.install(this);
        FastSave.init(getApplicationContext());
        StrictMode.setVmPolicy(new Builder().build());
    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            myApplication = appInstance;
        }
        return myApplication;
    }
}