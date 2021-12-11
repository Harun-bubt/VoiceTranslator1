package com.example.voicetranslator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.voicetranslator.helper.Constant;
import com.example.voicetranslator.helper.Helper;
import com.example.voicetranslator.helper.ListViewAdapter;
import com.example.voicetranslator.helper.RelativePopupWindow;
import com.example.voicetranslator.helper.TranslateTask;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Image_to_text_act extends AppCompatActivity {

    public static TextToSpeech tts;
    public String detected_text_valuenew;
    private static final int MY_REQUEST_CODE = 5;
    private static final int REQUEST_GALLERY = 0;
    private static final int REQUEST_CAMERA = 1;
    private int RECORD_REQUEST_Hindi = 2;
    EditText etsourcenew;
    int fpos = 8;
    int countad = 0;
    ImageView iva1new, iva2new, ivcopynew, ivresetnew, ivsharenew, ivswapnew;
    LinearLayout ivtransnew, linnew;
    String[] lang_name = new String[0];
    Dialog loadingDialognew;
    LinearLayout loutnew, lspinnew;
    int stat = -1;
    int tpos = 0;
    TextView tvinnew, tvtnew, tvoutnew;
    Vibrator vbnew;
    RelativePopupWindow windownew;
    public String PhotoItemnew;
    AdView adView;
    private Uri imageUrinew;
    ImageView gallery_img, camera_img, more_img, Full_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_to_text_activity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        permissionnew();

        tvtnew = (TextView) findViewById(R.id.tvtrans);
        lspinnew = (LinearLayout) findViewById(R.id.linearspinner);
        linnew = (LinearLayout) findViewById(R.id.linlangin);
        loutnew = (LinearLayout) findViewById(R.id.linlangout);
        tvinnew = (TextView) findViewById(R.id.tvlangin);
        tvoutnew = (TextView) findViewById(R.id.tvlangout);
        iva1new = (ImageView) findViewById(R.id.iva1);
        iva2new = (ImageView) findViewById(R.id.iva2);
        ivswapnew = (ImageView) findViewById(R.id.ivswap);
        etsourcenew = (EditText) findViewById(R.id.etsource);
        ivresetnew = (ImageView) findViewById(R.id.ivreset);
        ivsharenew = (ImageView) findViewById(R.id.ivsharenew);
        ivcopynew = (ImageView) findViewById(R.id.ivcopy);
        ivtransnew = (LinearLayout) findViewById(R.id.ivtrans);

        gallery_img = findViewById(R.id.gallery_img);
        camera_img = findViewById(R.id.camera_img);
        more_img = findViewById(R.id.more_img);
        Full_screen = findViewById(R.id.full_screen);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView = new AdView(Image_to_text_act.this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(adRequest);

        camera_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = System.currentTimeMillis() + ".jpg";
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                imageUrinew = Image_to_text_act.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrinew);
                startActivityForResult(intent, REQUEST_CAMERA);

            }
        });

        gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_GALLERY);

            }
        });

        ivswapnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etsourcenew.setCursorVisible(false);
                hideKeyboard();
                RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
                rotate.setRepeatCount(0);
                rotate.setDuration(500);
                rotate.setInterpolator(new LinearInterpolator());
                ivswapnew.startAnimation(rotate);
                int temp = fpos;
                fpos = tpos;
                tpos = temp;
                tvoutnew.setText(lang_name[tpos]);
                tvinnew.setText(lang_name[fpos]);

            }
        });

        ivtransnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countad++;
                if (countad % 4 == 0) {

                    if (Main_activity.interstitial != null && Main_activity.interstitial.isLoaded()) {
                        Main_activity.interstitial.show();
                    }
                }

                String text = etsourcenew.getText().toString();
                if (TextUtils.isEmpty(text)) {

                    final Animation animShake = AnimationUtils.loadAnimation(Image_to_text_act.this, R.anim.shake);
                    etsourcenew.startAnimation(animShake);
                    vbnew.vibrate(100);
                    return;
                }
                loadingDialognew.show();
                Translate(text, Constant.lang_code[fpos], Constant.lang_code[tpos]);
            }
        });

        loutnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etsourcenew.setCursorVisible(false);
                hideKeyboard();
                if (windownew.isShowing()) {
                    windownew.dismiss();
                    return;
                }
                View v = ((LayoutInflater) Image_to_text_act.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_dropdown, null, false);
                windownew.setContentView(v);
                windownew.setWidth((getResources().getDisplayMetrics().widthPixels * 480) / 1080);
                windownew.setHeight((getResources().getDisplayMetrics().heightPixels * 1573) / 1920);
                windownew.setFocusable(true);
                windownew.setOutsideTouchable(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    windownew.setElevation(20.0f);
                }
                ListView lv = (ListView) v.findViewById(R.id.spinlist);
                lv.setAdapter(new ListViewAdapter(Image_to_text_act.this, lang_name));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tpos = i;
                        tvoutnew.setText(lang_name[i]);
                        windownew.dismiss();
                    }
                });

                iva2new.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                windownew.showOnAnchor(tvoutnew, 2, 2);
                windownew.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    public void onDismiss() {
                        iva2new.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    }
                });

            }
        });

        linnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etsourcenew.setCursorVisible(false);
                hideKeyboard();
                if (windownew.isShowing()) {
                    windownew.dismiss();
                    return;
                }
                View v = ((LayoutInflater) Image_to_text_act.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_dropdown, null, false);
                windownew.setContentView(v);
                windownew.setWidth((getResources().getDisplayMetrics().widthPixels * 480) / 1080);
                windownew.setHeight((getResources().getDisplayMetrics().heightPixels * 1573) / 1920);
                windownew.setFocusable(true);
                windownew.setOutsideTouchable(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    windownew.setElevation(20.0f);
                }
                ListView lv = (ListView) v.findViewById(R.id.spinlist);
                lv.setAdapter(new ListViewAdapter(Image_to_text_act.this, lang_name));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        fpos = i;
                        tvinnew.setText(lang_name[i]);
                        windownew.dismiss();
                    }
                });
                iva1new.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                windownew.showOnAnchor(tvinnew, 2, 1);
                windownew.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    public void onDismiss() {
                        iva1new.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    }
                });
            }
        });

        ivsharenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivsharenew.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        etsourcenew.setCursorVisible(false);
                        String result = tvtnew.getText().toString();
                        if (TextUtils.isEmpty(result)) {
                            Helper.show(Image_to_text_act.this, "Nothing to Share");
                        } else {
                            Helper.shareText(Image_to_text_act.this, result);
                        }
                        ivsharenew.setVisibility(View.VISIBLE);
                    }
                }, 0);
            }
        });

        Full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = tvtnew.getText().toString();
                if (TextUtils.isEmpty(result)) {

                    final Animation animShake = AnimationUtils.loadAnimation(Image_to_text_act.this, R.anim.shake);
                    tvtnew.startAnimation(animShake);
                    vbnew.vibrate(100);

                } else {
                    Intent myIntent = new Intent(Image_to_text_act.this, Full_screen_act.class);
                    myIntent.putExtra("mytext", result);
                    startActivity(myIntent);
                }
            }
        });

        ivcopynew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivcopynew.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        etsourcenew.setCursorVisible(false);
                        hideKeyboard();
                        copyToClipboard(tvtnew.getText().toString());
                        ivcopynew.setVisibility(View.VISIBLE);
                    }
                }, 0);
            }
        });

        ivresetnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivresetnew.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvtnew.setText("");
                        tvtnew.setHint(R.string.transtext);
                        etsourcenew.setText("");
                        etsourcenew.setCursorVisible(false);
                        hideKeyboard();
                        ivresetnew.setVisibility(View.VISIBLE);
                    }
                }, 0);


            }
        });

        Bundle bundle = Image_to_text_act.this.getIntent().getExtras();
        if (bundle != null) {
            PhotoItemnew = bundle.getString("photo_value");
        }

        init();

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

    private void init() {
        this.lang_name = getResources().getStringArray(R.array.language_in);
        this.tvoutnew.setText(this.lang_name[this.tpos]);
        this.tvinnew.setText(this.lang_name[this.fpos]);
        this.etsourcenew.setCursorVisible(false);

        etsourcenew.setText(PhotoItemnew);

        this.etsourcenew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etsourcenew.setCursorVisible(true);
            }
        });
        this.vbnew = (Vibrator) Image_to_text_act.this.getSystemService(VIBRATOR_SERVICE);
        this.windownew = new RelativePopupWindow(Image_to_text_act.this);
        initdialog();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1 || data == null) {
        } else if (requestCode == this.RECORD_REQUEST_Hindi) {
            ArrayList<String> result = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            this.etsourcenew.setText((CharSequence) result.get(0));
            this.loadingDialognew.show();
            Translate((String) result.get(0), Constant.lang_code[this.fpos], Constant.lang_code[this.tpos]);
        }

        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    inspect(data.getData());
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (imageUrinew != null) {
                        inspect(imageUrinew);
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void Translate(String text, String fromLang, String toLang) {
        if (Helper.isNetworkAvailable(Image_to_text_act.this)) {
            TranslateTask task = new TranslateTask(Image_to_text_act.this, text, fromLang, toLang);
            task.json_response = new TranslateTask.AsyncResponse() {
                public void processFinish(String response) {
                    loadingDialognew.dismiss();
                    vbnew.vibrate(100);
                    if (response != null) {
                        tvtnew.setText(response);
                    } else {
                        tvtnew.setText(R.string.somethinwrong);
                    }
                }
            };
            task.execute(new String[0]);
            return;
        }
        this.loadingDialognew.dismiss();
        this.tvtnew.setText(R.string.interneterr);
    }

    public void copyToClipboard(String t) {
        ClipboardManager manager = (ClipboardManager) Image_to_text_act.this.getSystemService(CLIPBOARD_SERVICE);
        if (manager == null) {
            Helper.show(Image_to_text_act.this, "Error while text copy");
        } else if (t.equals("")) {
            Helper.show(Image_to_text_act.this, "No text to copy");
        } else {
            manager.setPrimaryClip(ClipData.newPlainText("label", t));
            Helper.showLog("Text Copied");
            Helper.show(Image_to_text_act.this, "Text Copied");
        }
    }

    public void initdialog() {
        View vj = ((LayoutInflater) Image_to_text_act.this.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_dialog, null);
        this.loadingDialognew = new Dialog(Image_to_text_act.this, R.style.Theme_Transparent);
        this.loadingDialognew.setContentView(vj);
        ((TextView) vj.findViewById(R.id.tvloading)).setText("Translating...");
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void hideKeyboard() {
        try {
            View view = Image_to_text_act.this.getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) Image_to_text_act.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public void permissionnew() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

            } else if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.CAMERA,
                        },
                        MY_REQUEST_CODE);
            }
        } else {
        }
    }

    private void inspect(Uri uri) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = Image_to_text_act.this.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 2;
            options.inScreenDensity = DisplayMetrics.DENSITY_LOW;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            inspectFromBitmap(bitmap);
        } catch (FileNotFoundException e) {
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void inspectFromBitmap(Bitmap bitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(Image_to_text_act.this).build();
        try {
            if (!textRecognizer.isOperational()) {
                new AlertDialog.
                        Builder(Image_to_text_act.this).
                        setMessage("Text recognizer could not be set up on your device").show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);
            List<TextBlock> textBlocks = new ArrayList<TextBlock>();
            for (int i = 0; i < origTextBlocks.size(); i++) {
                TextBlock textBlock = origTextBlocks.valueAt(i);
                textBlocks.add(textBlock);
            }

            Collections.sort(textBlocks, new Comparator<TextBlock>() {
                @Override
                public int compare(TextBlock o1, TextBlock o2) {
                    int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
                    int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
                    if (diffOfTops != 0) {
                        return diffOfTops;
                    }
                    return diffOfLefts;
                }
            });

            StringBuilder detectedText = new StringBuilder();
            for (TextBlock textBlock : textBlocks) {
                if (textBlock != null && textBlock.getValue() != null) {
                    detectedText.append(textBlock.getValue());
                    detectedText.append("\n");
                }
            }

            detected_text_valuenew = String.valueOf(detectedText);
            Intent i = new Intent(Image_to_text_act.this, Image_to_text_act.class);
            i.putExtra("photo_value", detected_text_valuenew);
            startActivity(i);


        } finally {
            textRecognizer.release();
        }
    }
}