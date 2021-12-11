package com.example.voicetranslator.voice_translator.speak;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CustomClass {
    private static CustomClass mCustom;
    private Context mContext;
    private MediaPlayer mPlayer;

    public static CustomClass m3415a(Context context) {
        if (mCustom == null) {
            mCustom = new CustomClass(context);
        }
        return mCustom;
    }

    private CustomClass(Context context) {
        this.mContext = context;
    }

    public void stop() {
        MediaPlayer mediaPlayer = this.mPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mPlayer = null;
        }
    }

    public void m3417a(byte[] bArr) {
        try {
            File createTempFile = File.createTempFile("kurchina", "mp3", this.mContext.getCacheDir());
            createTempFile.deleteOnExit();
            FileOutputStream fileOutputStream = new FileOutputStream(createTempFile);
            fileOutputStream.write(bArr);
            fileOutputStream.close();
            stop();
            this.mPlayer = new MediaPlayer();
            this.mPlayer.setDataSource(new FileInputStream(createTempFile).getFD());
            this.mPlayer.prepare();
            this.mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}