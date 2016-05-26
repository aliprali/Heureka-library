package heureka.cz.internal.library.repository;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tomas on 26.5.16.
 */
public class Form {

    @SerializedName("short")
    public String formShort;

    public String form;

    public Form(String formShort, String form) {
        this.formShort = formShort;
        this.form = form;
    }

    public String getFormShort() {
        return formShort;
    }

    public void setFormShort(String formShort) {
        this.formShort = formShort;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return form;
    }
}
