package heureka.cz.internal.library.repository;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tomas on 26.5.16.
 */
public class Lang {

    @SerializedName("short")
    public String langShort;

    public String lang;

    public Lang(String langShort, String lang) {
        this.langShort = langShort;
        this.lang = lang;
    }

    public String getLangShort() {
        return langShort;
    }

    public void setLangShort(String langShort) {
        this.langShort = langShort;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return lang;
    }
}
