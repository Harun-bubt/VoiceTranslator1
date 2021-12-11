package com.example.voicetranslator;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voicetranslator.helper.Helper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Full_screen_act extends AppCompatActivity {

    public static TextToSpeech tts;
    TextView settext_txt, Save_image_txt;
    AdView adView2;
    ImageView share_full, copy_full;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_act);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView2 = new AdView(Full_screen_act.this);
        adView2.setAdSize(AdSize.SMART_BANNER);
        adView2 = (AdView) findViewById(R.id.adView2);
        adView2.loadAd(adRequest);

        pd = new ProgressDialog(Full_screen_act.this);

        Save_image_txt = findViewById(R.id.Save_image_txt);
        Save_image_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveClick(v);

            }
        });

        settext_txt = findViewById(R.id.settext_txt);
        settext_txt.setText(getIntent().getStringExtra("mytext"));

        share_full = findViewById(R.id.share_full);
        copy_full = findViewById(R.id.copy_full);

        share_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String result = settext_txt.getText().toString();
                        if (TextUtils.isEmpty(result)) {
                            Helper.show(Full_screen_act.this, "Nothing to Share");
                        } else {
                            Helper.shareText(Full_screen_act.this, result);
                        }
                    }
                }, 0);

            }
        });

        copy_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settext_txt.setCursorVisible(false);
                        hideKeyboard();
                        copyToClipboard(settext_txt.getText().toString());
                    }
                }, 0);

            }
        });

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
                    } else {
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void copyToClipboard(String t) {
        ClipboardManager manager = (ClipboardManager) Full_screen_act.this.getSystemService(CLIPBOARD_SERVICE);
        if (manager == null) {
            Helper.show(Full_screen_act.this, "Error while text copy");
        } else if (t.equals("")) {
            Helper.show(Full_screen_act.this, "No text to copy");
        } else {
            manager.setPrimaryClip(ClipData.newPlainText("label", t));
            Helper.showLog("Text Copied");
            Helper.show(Full_screen_act.this, "Text Copied");
        }
    }

    public void hideKeyboard() {
        try {
            View view = Full_screen_act.this.getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) Full_screen_act.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public void SaveClick(View view) {
        pd.setMessage("saving your image");
        pd.show();
        File file = saveBitMap(Full_screen_act.this, settext_txt);
        if (file != null) {
            pd.cancel();
            Toast.makeText(this, "Image Saved To The Gallery!", Toast.LENGTH_LONG).show();
        } else {
            pd.cancel();
            Toast.makeText(this, "Oops! Image Could Not Be Saved. Try Again!", Toast.LENGTH_LONG).show();
        }
    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Voice_Translator");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}