package com.example.voicetranslator.voice_translator.trans;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceTranslator {
    private static PreferenceTranslator mTrans;
    private SharedPreferences mPref;

    private PreferenceTranslator(Context context) {
        this.mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceTranslator m3943a(Context context) {
        if (mTrans == null) {
            mTrans = new PreferenceTranslator(context);
        }
        return mTrans;
    }

    public void m3948b(int i) {
        this.mPref.edit().putInt("taal_naar", i).apply();
    }

    public boolean m3953e() {
        return this.mPref.getBoolean("translateByEnter", false);
    }

    public int m3954f() {
        return Integer.parseInt(this.mPref.getString("textSize", "20"));
    }

    public int m3955g() {
        return this.mPref.getInt("taal_naar", 0);
    }
}