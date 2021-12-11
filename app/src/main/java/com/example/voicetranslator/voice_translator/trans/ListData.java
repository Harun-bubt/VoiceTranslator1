package com.example.voicetranslator.voice_translator.trans;

import java.io.Serializable;

public class ListData implements Serializable {
    private String string;
    private String string1;

    public ListData(String str, String str2) {
        this.string1 = str;
        this.string = str2;
    }

    public String m3892a() {
        return this.string;
    }

    public String m3893b() {
        return this.string1;
    }
}
