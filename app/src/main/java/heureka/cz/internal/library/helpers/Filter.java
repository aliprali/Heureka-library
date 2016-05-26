package heureka.cz.internal.library.helpers;

import heureka.cz.internal.library.repository.Form;
import heureka.cz.internal.library.repository.Lang;

/**
 * Created by tomas on 26.5.16.
 */
public class Filter {

    private Lang lang;

    private Form form;

    public Filter(Lang lang, Form form) {
        this.lang = lang;
        this.form = form;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
