package com.example.voicetranslator;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appizona.yehiahd.fastsave.FastSave;
import com.example.voicetranslator.helper.TranslateLanguage;
import com.example.voicetranslator.voice_translator.activity.HistoryActivity;
import com.example.voicetranslator.voice_translator.activity.LanguageActivity;
import com.example.voicetranslator.voice_translator.activity.MyBaseAdapter;
import com.example.voicetranslator.voice_translator.model.SearchData;
import com.example.voicetranslator.voice_translator.share.MyApplication;
import com.example.voicetranslator.voice_translator.speak.CustomClass;
import com.example.voicetranslator.voice_translator.trans.LangJsonParser;
import com.example.voicetranslator.voice_translator.trans.Language;
import com.example.voicetranslator.voice_translator.trans.ListData;
import com.example.voicetranslator.voice_translator.trans.PreferenceTranslator;
import com.example.voicetranslator.voice_translator.trans.TransUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class Main_activity extends AppCompatActivity {

    public static InterstitialAd interstitial;
    int countad = 0;
    public static EditText et_input;
    public static TextView et_output;
    public static Context context;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences mSharedPreferences;
    int REQUEST_OK = 1;
    int count = 0;
    ScrollView scrollView;
    int int1 = 2;
    TranslatorExecution translatorExecution;
    int int2;
    ImageView more_img;
    ImageView history;
    LinearLayout copy;
    LinearLayout remove_paste;
    AdView adView;
    LinearLayout share;
    LinearLayout speak1;
    LinearLayout speak2;
    LinearLayout voice;
    ArrayList<SearchData> history_list = new ArrayList<>();
    ImageView img_swap;
    InputMethodManager input_manager;
    LinearLayout iv_convert;
    public String[] langCodeList;
    public String[] langList;
    public int mCurrRotation = 0;
    RecognitionListener mRecognitionListener;
    Intent mRecognizerIntent;
    SpeechRecognizer mSpeechRecognizer;
    ProgressBar progressbar;
    LinearLayout rel_first_lang;
    LinearLayout rel_second_lang;
    RecyclerView rvOutputWords;
    public String selected_lang;
    String speech_lag1;
    String speech_lag2;
    ImageView full_screen;
    ArrayList<String> thingsYouSaid;
    TextView txt_first_lang, txt_second_lang, txtrlang, txtslang;
    ImageView gallery_img;

    class C0771n extends Thread {
        final String data;
        final String data2;
        final Main_activity trans_activity;

        C0771n(Main_activity translatorActivity, String str, String str2) {
            this.trans_activity = translatorActivity;
            this.data = str;
            this.data2 = str2;
        }

        public void run() {
            CustomClass.m3415a(this.trans_activity).m3417a(LangJsonParser.m3442a(this.data, this.data2));
        }
    }

    public class TranslatorExecution extends AsyncTask<Void, String, Boolean> {
        private String str1;
        private String str2;
        private String str3;
        private WeakReference<Main_activity> weakReference;

        TranslatorExecution(Main_activity translatorActivity) {
            this.weakReference = new WeakReference<>(translatorActivity);
        }

        public Boolean m3979a(Void[]... voidArr) {
            Iterator it = Language.mLangList(this.str1, HttpStatus.SC_MULTIPLE_CHOICES).iterator();
            boolean z = true;
            boolean z2 = false;
            while (it.hasNext()) {
                if (LangJsonParser.m3911a((String) it.next(), this.str3, this.str2).length() > 0) {
                    if (isCancelled()) {
                        break;
                    }
                    publishProgress(new String[]{LangJsonParser.m3911a(this.str1, this.str3, this.str2)});
                    z2 = true;
                }
            }
            if (isCancelled() || !z2) {
                z = false;
            }
            return Boolean.valueOf(z);
        }

        public void m3980a(Boolean bool) {
            Main_activity translatorActivity = (Main_activity) this.weakReference.get();
            if (translatorActivity != null) {
                String stringfrom = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
                String stringto = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, "ja");
                TranslateLanguage translate=new TranslateLanguage();
                translate.setOnTranslationCompleteListener(new TranslateLanguage.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {
                        // here you can perform initial work before translated the text like displaying progress bar
                    }

                    @Override
                    public void onCompleted(String text) {
                        // "text" variable will give you the translated text
                        et_output.setText(text);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
                translate.execute(et_input.getText().toString(),stringfrom,stringto);
                translatorActivity.progressbar.setVisibility(View.GONE);
                try {
                    if (bool.booleanValue()) {
                        translatorActivity.share.setVisibility(View.VISIBLE);
                        translatorActivity.copy.setVisibility(View.VISIBLE);
                        remove_paste.setVisibility(View.VISIBLE);
                        if (TransUtils.m3924b(translatorActivity.int2)) {
                            translatorActivity.speak1.setVisibility(View.VISIBLE);
                        }
                        if (TransUtils.m3924b(TransUtils.m3918a(translatorActivity.int2))) {
                            translatorActivity.speak2.setVisibility(View.VISIBLE);
                        }
                        Main_activity.this.m3996q();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void m3981a(String... strArr) {
            if (((Main_activity) this.weakReference.get()) != null) {
                Main_activity.this.m3985a(strArr[0]);
            }
        }

        public Boolean doInBackground(Void... voidArr) {
            return m3979a(voidArr);
        }

        public void onPostExecute(Boolean bool) {
            m3980a(bool);
        }

        public void onPreExecute() {
            Context context = (Context) this.weakReference.get();
            if (context != null) {
                this.str1 = Main_activity.et_input.getText().toString();
                TransUtils.m3921a(context, Main_activity.et_input);
                Main_activity.this.progressbar.setVisibility(View.VISIBLE);
                Main_activity.this.share.setVisibility(View.INVISIBLE);
                Main_activity.this.copy.setVisibility(View.INVISIBLE);
                remove_paste.setVisibility(View.INVISIBLE);
                Main_activity.this.speak1.setVisibility(View.GONE);
                Main_activity.this.speak2.setVisibility(View.GONE);
                Main_activity.et_output.setText("");
                Main_activity.et_output.setTag(null);
                Main_activity.this.rvOutputWords.setVisibility(View.GONE);
                Main_activity.this.int1++;
                if (Main_activity.this.int1 >= 10) {
                    Main_activity.this.int1 = 0;
                }
                if (Main_activity.this.int2 == 1) {
                    this.str2 = Main_activity.this.speech_lag1;
                    this.str3 = Main_activity.this.speech_lag2;
                    return;
                }
                this.str2 = Main_activity.this.speech_lag2;
                this.str3 = Main_activity.this.speech_lag1;
            }
        }

        public void onProgressUpdate(String[] strArr) {
            m3981a(strArr);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.main_activity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        context = this;

        showGoogleFull();

        AdRequest adRequest = new AdRequest.Builder().build();
        adView = new AdView(Main_activity.this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(adRequest);

        full_screen=findViewById(R.id.full_screen);
        full_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = et_output.getText().toString();
                if (TextUtils.isEmpty(result)) {

                    final Animation animShake = AnimationUtils.loadAnimation(Main_activity.this, R.anim.shake);
                    et_output.startAnimation(animShake);

                } else {
                    Intent myIntent = new Intent(Main_activity.this, Full_screen_act.class);
                    myIntent.putExtra("mytext", result);
                    startActivity(myIntent);
                }
            }
        });

        gallery_img = findViewById(R.id.gallery_img);
        gallery_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Main_activity.this, Image_to_text_act.class);
                startActivity(i);
            }
        });

        more_img=findViewById(R.id.more_img);
        more_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Main_activity.this, more_img);
                popup.getMenuInflater()
                        .inflate(R.menu.opetion_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Rate:
                                Main_activity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + Main_activity.this.getApplication().getPackageName())));
                                return true;
                            case R.id.Share:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject/Title");
                                intent.putExtra("android.intent.extra.TEXT", " Voice Translator App Install Now : " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                                startActivity(Intent.createChooser(intent, "Choose sharing method"));
                                return true;
                            case R.id.More:
                                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps?hl=en")));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

        FastSave.getInstance().saveString(MyApplication.FIRST_LANG_CODE, "en");
        FastSave.getInstance().saveString(MyApplication.SEC_LANG_CODE, "ja");
        et_input = (EditText) findViewById(R.id.etInput);
        copy = (LinearLayout) findViewById(R.id.btnCopy);
        remove_paste = (LinearLayout) findViewById(R.id.btnRemovePaste);
        share = (LinearLayout) findViewById(R.id.btnShare);
        voice = (LinearLayout) findViewById(R.id.btnVoice);
        speak1 = (LinearLayout) findViewById(R.id.btnSpeak1);
        speak2 = (LinearLayout) findViewById(R.id.btnSpeak2);
        history = (ImageView) findViewById(R.id.img_history);
        img_swap = (ImageView) findViewById(R.id.img_change_lang);
        txt_first_lang = (TextView) findViewById(R.id.txt_first_lang);
        txtrlang = (TextView) findViewById(R.id.txtflang);
        txt_second_lang = (TextView) findViewById(R.id.txt_second_lang);
        txtslang = (TextView) findViewById(R.id.slang);
        rel_first_lang = (LinearLayout) findViewById(R.id.rel_first_lang);
        rel_second_lang = (LinearLayout) findViewById(R.id.rel_second_lang);
        iv_convert = (LinearLayout) findViewById(R.id.iv_convert);

        isOnline();

        iv_convert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String stringfrom = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
                String stringto = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, "ja");
                TranslateLanguage translate=new TranslateLanguage();
                translate.setOnTranslationCompleteListener(new TranslateLanguage.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {
                        // here you can perform initial work before translated the text like displaying progress bar
                    }

                    @Override
                    public void onCompleted(String text) {
                        // "text" variable will give you the translated text
                        et_output.setText(text);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
                translate.execute(et_input.getText().toString(),stringfrom,stringto);
                countad++;
                if (countad % 4 == 0) {

                    if (Main_activity.interstitial != null && Main_activity.interstitial.isLoaded()) {
                        Main_activity.interstitial.show();
                    }
                }

                if (Main_activity.this.isOnline()) {
                    int i = Main_activity.this.count % 7;
                    Main_activity.this.count++;
                    if (Main_activity.this.count % 8 == 0) {
                        Main_activity.this.isOnline();
                    }
                    String string = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
                    String string2 = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, "ja");
                    Main_activity translatorActivity = Main_activity.this;
                    translatorActivity.speech_lag2 = string;
                    translatorActivity.langList = translatorActivity.getResources().getStringArray(R.array.language_name);
                    Main_activity translatorActivity2 = Main_activity.this;
                    translatorActivity2.langCodeList = translatorActivity2.getResources().getStringArray(R.array.language_code);
//                    for (int i2 = 0; i2 < Main_activity.this.langCodeList.length; i2++) {
//                        if (Main_activity.this.langCodeList[i2].equals(string)) {
//                            Main_activity translatorActivity3 = Main_activity.this;
//                            translatorActivity3.speech_lag1 = string;
//                            translatorActivity3.txt_first_lang.setText(Main_activity.this.langList[i2]);
//                            translatorActivity3.txtrlang.setText(Main_activity.this.langList[i2]);
//                        }
//                        if (Main_activity.this.langCodeList[i2].equals(string2)) {
//                            Main_activity translatorActivity4 = Main_activity.this;
//                            translatorActivity4.speech_lag2 = string2;
//                            translatorActivity4.txt_second_lang.setText(Main_activity.this.langList[i2]);
//                            translatorActivity4.txtslang.setText(Main_activity.this.langList[i2]);
//                        }
//                    }
                    Main_activity.this.m4004k();
                    return;
                }
                Toast.makeText(Main_activity.this, "No Internet Connection..", Toast.LENGTH_SHORT).show();
            }
        });

        img_swap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String str = "en";
                String string = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, str);
                String str2 = "ja";
                String string2 = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, str2);
                Main_activity translatorActivity = Main_activity.this;
                translatorActivity.speech_lag2 = string;
                translatorActivity.langList = translatorActivity.getResources().getStringArray(R.array.language_name);
                Main_activity translatorActivity2 = Main_activity.this;
                translatorActivity2.langCodeList = translatorActivity2.getResources().getStringArray(R.array.language_code);
                FastSave.getInstance().saveString(MyApplication.FIRST_LANG_CODE, string2);
                FastSave.getInstance().saveString(MyApplication.SEC_LANG_CODE, string);
                String string3 = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, str);
                String string4 = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, str2);
                String str3 = "";
                Main_activity.et_input.setText(str3);
                Main_activity.et_output.setText(str3);
                Main_activity.this.txt_second_lang.setText(str3);
                Main_activity.this.txtslang.setText(str3);
                for (int i = 0; i < Main_activity.this.langCodeList.length; i++) {
                    if (Main_activity.this.langCodeList[i].equals(string3)) {
                        Main_activity translatorActivity3 = Main_activity.this;
                        translatorActivity3.speech_lag1 = string3;
                        translatorActivity3.txt_first_lang.setText(Main_activity.this.langList[i]);
                        translatorActivity3.txtrlang.setText(Main_activity.this.langList[i]);
                    }
                    if (Main_activity.this.langCodeList[i].equals(string4)) {
                        Main_activity translatorActivity4 = Main_activity.this;
                        translatorActivity4.speech_lag2 = string4;
                        translatorActivity4.txt_second_lang.setText(Main_activity.this.langList[i]);
                        translatorActivity4.txtslang.setText(Main_activity.this.langList[i]);
                    }
                }
                Main_activity.this.mCurrRotation %= 360;
                float f = (float) Main_activity.this.mCurrRotation;
                Main_activity translatorActivity5 = Main_activity.this;
                int i2 = translatorActivity5.mCurrRotation + 180;
                translatorActivity5.mCurrRotation = i2;
                RotateAnimation rotateAnimation = new RotateAnimation(f, (float) i2, (float) (Main_activity.this.img_swap.getWidth() / 2), (float) (Main_activity.this.img_swap.getHeight() / 2));
                rotateAnimation.setDuration(200);
                rotateAnimation.setFillAfter(true);
                Main_activity.this.img_swap.startAnimation(rotateAnimation);
            }
        });

        rel_first_lang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MyApplication.selected_lang_pos = 1;
                Main_activity translatorActivity = Main_activity.this;
                translatorActivity.startActivity(new Intent(translatorActivity, LanguageActivity.class));
            }
        });

        rel_second_lang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MyApplication.selected_lang_pos = 2;
                Main_activity translatorActivity = Main_activity.this;
                translatorActivity.startActivity(new Intent(translatorActivity, LanguageActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (new Random().nextInt(5) + 0 != 0) {
                    Main_activity translatorActivity = Main_activity.this;
                    translatorActivity.startActivity(new Intent(translatorActivity, HistoryActivity.class));
                } else {
                }
            }
        });

        String str = "";
        ArrayList arrayList = (ArrayList) new Gson().fromJson(getSharedPreferences("mPref", 0).getString("History", str), new TypeToken<ArrayList<SearchData>>() {
        }.getType());
        if (arrayList != null) {
            this.history_list.addAll(arrayList);
        }
        rvOutputWords = (RecyclerView) findViewById(R.id.rvOutputWords);
        scrollView = (ScrollView) findViewById(R.id.svOutput);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvOutputWords.setLayoutManager(linearLayoutManager);
        et_output = (TextView) findViewById(R.id.etOutput);
        progressbar = (ProgressBar) findViewById(R.id.progressBar1);
        progressbar.setVisibility(View.GONE);

        copy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TransUtils.m3925b(Main_activity.this, Main_activity.et_output.getText().toString());
            }
        });

        remove_paste.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String str = "";
                if (Main_activity.et_input.getText().length() == 0) {
                    ClipboardManager clipboardManager = (ClipboardManager) Main_activity.this.getSystemService(CLIPBOARD_SERVICE);
                    clipboardManager.getClass();
                    if (!clipboardManager.getText().equals(str)) {
                        Main_activity.et_input.setText(clipboardManager.getText());
                        Main_activity.et_input.setSelection(Main_activity.et_input.length());
                        return;
                    }
                    Toast.makeText(Main_activity.this.getApplicationContext(), "Clipboard is Empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                share.setVisibility(View.INVISIBLE);
                copy.setVisibility(View.INVISIBLE);
                remove_paste.setVisibility(View.INVISIBLE);
                speak1.setVisibility(View.INVISIBLE);
                speak2.setVisibility(View.INVISIBLE);
                et_input.setText(str);
                et_output.setText(str);
                et_input.requestFocus();
                if (Main_activity.this.input_manager != null) {
                    Main_activity.this.input_manager.showSoftInput(Main_activity.et_input, 0);
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String charSequence = Main_activity.et_output.getText().toString();
                if (charSequence.length() > 0) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType(HTTP.PLAIN_TEXT_TYPE);
                    intent.putExtra("android.intent.extra.TEXT", charSequence);
                    Main_activity.this.startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        voice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main_activity.this.VoiceInput();
            }
        });

        speak1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main_activity translatorActivity = Main_activity.this;
                translatorActivity.selected_lang = translatorActivity.speech_lag1;
                Main_activity.this.m3464b(0);
            }
        });

        speak2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main_activity translatorActivity = Main_activity.this;
                translatorActivity.selected_lang = translatorActivity.speech_lag2;
                Main_activity.this.m3464b(1);
            }
        });

        int2 = PreferenceTranslator.m3943a(this).m3955g();
        input_manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        int i = this.int2;

        copy.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        remove_paste.setVisibility(View.INVISIBLE);
        speak1.setVisibility(View.INVISIBLE);
        speak2.setVisibility(View.INVISIBLE);

        et_input.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (Main_activity.et_input.getText().toString().length() == 0) {
                    return;
                }
                Main_activity.this.remove_paste.setVisibility(View.VISIBLE);
            }
        });

        et_input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!PreferenceTranslator.m3943a(Main_activity.this).m3953e() || keyEvent.getAction() != 0 || i != 66) {
                    return false;
                }
                if (Main_activity.et_input.getText().toString().length() != 0) {
                    Main_activity.this.m4001v();
                }
                return true;
            }
        });
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null && HTTP.PLAIN_TEXT_TYPE.equals(type)) {
            m4003c(intent);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String str2 = "text1";
            if (extras.containsKey(str2)) {
                et_input.setText(extras.getString(str2));
                String string = extras.getString("text2");
                et_output.setText(str);
                m3985a(string);
                share.setVisibility(View.VISIBLE);
                copy.setVisibility(View.VISIBLE);
                remove_paste.setVisibility(View.VISIBLE);
                int2 = Integer.parseInt(extras.getString("taal"));
                if (TransUtils.m3924b(this.int2)) {
                    this.speak1.setVisibility(View.VISIBLE);
                }
                if (TransUtils.m3924b(TransUtils.m3918a(this.int2))) {
                    this.speak2.setVisibility(View.VISIBLE);
                }
                PreferenceTranslator.m3943a(this).m3948b(this.int2);
                int i2 = this.int2;
            }
        }

        m3982a(bundle);
        m3993n();

        mRecognitionListener = new RecognitionListener() {
            public void onBeginningOfSpeech() {
            }

            public void onBufferReceived(byte[] bArr) {
            }

            public void onEndOfSpeech() {
            }

            public void onError(int i) {
            }

            public void onEvent(int i, Bundle bundle) {
            }

            public void onPartialResults(Bundle bundle) {
            }

            public void onReadyForSpeech(Bundle bundle) {
            }

            public void onRmsChanged(float f) {
            }

            public void onResults(Bundle bundle) {
                StringBuilder sb = new StringBuilder();
                sb.append(">>> onResults");
                sb.append(bundle.getStringArrayList("results_recognition").toString());
            }
        };
    }

    public boolean isOnline() {
        try {
            return ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    public void onResume() {
        super.onResume();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String string = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
        String string2 = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, "hi");
        String str = "";
        int i = 0;
        String string3 = getSharedPreferences("mPref", 0).getString("History", str);
        if (!string3.equals(str)) {
            this.history_list = (ArrayList) new Gson().fromJson(string3, new TypeToken<ArrayList<SearchData>>() {
            }.getType());
        }
        this.speech_lag2 = string;
        this.langList = getResources().getStringArray(R.array.language_name);
        this.langCodeList = getResources().getStringArray(R.array.language_code);
        while (true) {
            String[] strArr = this.langCodeList;
            if (i < strArr.length) {
                if (strArr[i].equals(string)) {
                    this.speech_lag1 = string;
                    this.txt_first_lang.setText(this.langList[i]);
                    this.txtrlang.setText(this.langList[i]);
                }
                if (this.langCodeList[i].equals(string2)) {
                    this.speech_lag2 = string2;
                    this.txt_second_lang.setText(this.langList[i]);
                    this.txtslang.setText(this.langList[i]);
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void VoiceInput() {
        selected_lang = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        mSpeechRecognizer.setRecognitionListener(this.mRecognitionListener);
        mRecognizerIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        mRecognizerIntent.putExtra("android.speech.extra.LANGUAGE", this.selected_lang);
        mRecognizerIntent.putExtra("calling_package", getClass().getPackage().getName());
        mRecognizerIntent.putExtra("android.speech.extra.PROMPT", "Speak Now...");
        mSpeechRecognizer.startListening(this.mRecognizerIntent);
        try {
            startActivityForResult(this.mRecognizerIntent, this.REQUEST_OK);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Your device is not registered with TextToSpeech engine, please register through your device settings.", Toast.LENGTH_LONG).show();
        }
    }

    public void m4005l() {
        this.int2 = 1;
        PreferenceTranslator.m3943a(this).m3948b(this.int2);
        if (et_input.getText().toString().length() != 0) {
            m4001v();
        }
    }

    public void m4004k() {
        this.int2 = 0;
        PreferenceTranslator.m3943a(this).m3948b(this.int2);
        if (et_input.getText().toString().length() != 0) {
            m4001v();
        }
    }

    public void m4003c(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
        if (stringExtra != null) {
            et_input.setText(stringExtra);
            m4001v();
        }
    }

    private void m3993n() {
        if (Build.VERSION.SDK_INT >= 23) {
            CharSequence charSequenceExtra = getIntent().getCharSequenceExtra("android.intent.extra.PROCESS_TEXT");
            if (charSequenceExtra != null) {
                et_input.setText(charSequenceExtra.toString());
                m4001v();
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        String string = FastSave.getInstance().getString(MyApplication.FIRST_LANG_CODE, "en");
        String string2 = FastSave.getInstance().getString(MyApplication.SEC_LANG_CODE, "hi");
        speech_lag2 = string;
        langList = getResources().getStringArray(R.array.language_name);
        langCodeList = getResources().getStringArray(R.array.language_code);
        int i3 = 0;
        while (true) {
            String[] strArr = this.langCodeList;
            if (i3 >= strArr.length) {
                break;
            }
            if (strArr[i3].equals(string)) {
                speech_lag1 = string;
                txt_first_lang.setText(this.langList[i3]);
                txtrlang.setText(this.langList[i3]);
            }
            if (langCodeList[i3].equals(string2)) {
                speech_lag2 = string2;
                txt_second_lang.setText(this.langList[i3]);
                txtslang.setText(this.langList[i3]);
            }
            i3++;
        }
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.cancel();
        if (i == this.REQUEST_OK && i2 == -1) {
            this.thingsYouSaid = intent.getStringArrayListExtra("android.speech.extra.RESULTS");
            String str = (String) this.thingsYouSaid.get(0);
            if (et_input.getText().toString().length() > 0) {
                EditText editText = et_input;
                StringBuilder sb = new StringBuilder();
                sb.append(et_input.getText().toString());
                sb.append("");
                sb.append(str);
                editText.setText(sb.toString());
            } else {
                et_input.setText(str);
            }
            EditText editText2 = et_input;
            editText2.setSelection(editText2.length());
            if (this.selected_lang.equals(this.speech_lag1)) {
                m4004k();
            } else {
                m4005l();
            }
        }
    }

    public void m4001v() {
        TranslatorExecution translatorExecution = this.translatorExecution;
        if (translatorExecution != null) {
            translatorExecution.cancel(true);
        }
        translatorExecution = new TranslatorExecution(this);
        translatorExecution.execute(new Void[0]);
    }

    private void m3982a(Bundle bundle) {
        if (bundle != null) {
            String str = "etInput";
            if (bundle.containsKey(str)) {
                et_input.setText(bundle.getCharSequence(str));
                String string = bundle.getString("etOutput");
                et_output.setText("");
                m3985a(string);
                copy.setVisibility(bundle.getInt("btnCopy"));
                share.setVisibility(bundle.getInt("btnShare"));
                speak1.setVisibility(bundle.getInt("btnSpeak1"));
                speak2.setVisibility(bundle.getInt("btnSpeak2"));
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence("etInput", et_input.getText());
        bundle.putString("etOutput", et_output.getTag() != null ? et_output.getTag().toString() : et_output.getText().toString());
        bundle.putInt("btnCopy", this.copy.getVisibility());
        bundle.putInt("btnShare", this.share.getVisibility());
        bundle.putInt("btnSpeak1", this.speak1.getVisibility());
        bundle.putInt("btnSpeak2", this.speak2.getVisibility());
    }

    public void onStop() {
        super.onStop();
    }

    public void m3996q() {
        et_input.getText().toString();
        if (et_output.getTag() != null) {
            et_output.getTag().toString();
        } else {
            et_output.getText().toString();
        }
    }

    public void m3985a(String str) {
        String str2 = this.int2 == 1 ? this.speech_lag1 : this.speech_lag2;
        if (str != null && str.length() > 0) {
            String str3 = "\n\n###dict";
            String[] split = str.split(str3);
            if (split.length > 1) {
                this.rvOutputWords.setVisibility(View.VISIBLE);
                this.scrollView.setVisibility(View.GONE);
                ArrayList arrayList = new ArrayList();
                arrayList.add(new ListData(split[0], str2));
                for (String listData : split[1].split("\n")) {
                    arrayList.add(new ListData(listData, str2));
                }
                MyBaseAdapter myBaseAdapter = new MyBaseAdapter(arrayList, TransUtils.m3924b(TransUtils.m3918a(this.int2)), this);
                this.rvOutputWords.setAdapter(null);
                this.rvOutputWords.setAdapter(myBaseAdapter);
                et_output.setTag(str);
            } else {
                this.scrollView.setVisibility(View.VISIBLE);
            }
            StringBuilder sb = new StringBuilder();
            sb.append(et_output.getText());
            sb.append(str.replace(str3, "\n\n"));
            String sb2 = sb.toString();
            if (et_input.getText().length() > 0 && et_input.getText().toString().substring(0, 1).toUpperCase(Locale.getDefault()).equals(et_input.getText().toString().substring(0, 1))) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(Character.toString(sb2.charAt(0)).toUpperCase(Locale.getDefault()));
                sb3.append(String.valueOf(sb2).substring(1));
                sb2 = sb3.toString();
            }
            //et_output.setText(sb2);
            SearchData searchData = new SearchData();
            searchData.setSearchText(et_input.getText().toString());
            searchData.setResultText(et_output.getText().toString());
            this.history_list.add(searchData);
            String json = new Gson().toJson((Object) this.history_list);
            SharedPreferences.Editor edit = getSharedPreferences("mPref", 0).edit();
            edit.putString("History", json);
            edit.apply();
        }
    }

    public void m3464b(int i) {
        String str;
        String str2;
        if (i == 0) {
            try {
                String obj = et_input.getText().toString();
                if (this.int2 == 1) {
                    str2 = this.speech_lag2;
                } else {
                    str2 = this.speech_lag1;
                }
                new C0771n(this, str2, obj).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String charSequence = et_output.getText().toString();
            if (this.int2 == 1) {
                str = this.speech_lag1;
            } else {
                str = this.speech_lag2;
            }
            String str3 = "\n";
            String[] split = charSequence.split(str3);
            int length = split.length;
            if (length > 10) {
                length = 10;
            }
            String str4 = "";
            for (int i2 = 0; i2 < length; i2++) {
                StringBuilder sb = new StringBuilder();
                sb.append(str4);
                sb.append(str3);
                sb.append(split[i2]);
                str4 = sb.toString();
            }
            new C0771n(this, str, str4).start();
        }
    }

    private void showGoogleFull() {
        final AdRequest adRequest = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(Main_activity.this);
        interstitial.setAdUnitId(getString(R.string.interstitial_full));

        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                interstitial.loadAd(adRequest);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Back Again To Exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}