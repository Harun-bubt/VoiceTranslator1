package com.example.voicetranslator.voice_translator.trans;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Iterator;

public class Language {
    private static int value;

    public static ArrayList<String> mLangList(String str, int i) {
        value = i;
        ArrayList arrayList = new ArrayList();
        ArrayList<String> arrayList2 = new ArrayList<>();
        BreakIterator sentenceInstance = BreakIterator.getSentenceInstance();
        sentenceInstance.setText(str);
        int first = sentenceInstance.first();
        while (true) {
            int next = sentenceInstance.next();
            if (next == -1) {
                break;
            }
            arrayList.add(str.substring(first, next));
            first = next;
        }
        Iterator it = arrayList.iterator();
        String str2 = "";
        while (it.hasNext()) {
            String str3 = (String) it.next();
            if (str2.length() + str3.length() > value) {
                if (str2.length() > 0) {
                    arrayList2.add(str2);
                }
                if (str3.length() >= value) {
                    m3904a(arrayList2, str3);
                } else {
                    str2 = str3;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str3);
                str2 = sb.toString();
            }
        }
        arrayList2.add(str2);
        return arrayList2;
    }

    private static void m3904a(ArrayList<String> arrayList, String str) {
        String[] split = str.split("(?=[\\s\\.])");
        int i = 0;
        while (split.length > i) {
            StringBuilder sb = new StringBuilder();
            while (split.length > i && sb.length() + split[i].length() < value) {
                sb.append(split[i]);
                i++;
            }
            arrayList.add(sb.toString());
        }
    }
}
