package heureka.cz.internal.library.ui.dialogs;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.Filter;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Form;
import heureka.cz.internal.library.repository.Lang;
import heureka.cz.internal.library.repository.Position;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.MainActivity;

/**
 * Created by Ondrej on 25. 5. 2016.
 */
public class FilterDialog extends DialogFragment {

    private static final String TAG = "FilterDialog";

    private ApiDescription apiDescription;

    @Inject
    Settings settings;

    @Inject
    RetrofitBuilder retrofitBuilder;

    @Bind(R.id.sp_lang)
    Spinner lang;

    @Bind(R.id.sp_type)
    Spinner form;

    @OnClick(R.id.filterit)
    void filterit() {
        if(!FormValidator.validate(this, new SimpleErrorPopupCallback(getContext()))) {
            return;
        }

        Filter filter = new Filter(
                ((Lang)lang.getSelectedItem()),
                ((Form)form.getSelectedItem())
        );

        if(getActivity() instanceof Filtered) {
            ((Filtered)getActivity()).doFilter(filter);
            getDialog().dismiss();
        }
    }

    public FilterDialog() {
    }

    public static FilterDialog newInstance() {
        FilterDialog frag = new FilterDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container);

        ButterKnife.bind(this, view);
        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get().getApiAddress() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.filter_title);

        apiDescription.getLang(new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                ArrayList<Lang> dataAdapterList = new ArrayList<>();
                dataAdapterList.add(new Lang(null, getString(R.string.all)));
                dataAdapterList.addAll((ArrayList<Lang>) data);

                ArrayAdapter<Lang> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dataAdapterList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lang.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure() {}
        });

        apiDescription.getForms(new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                ArrayList<Form> dataAdapterList = new ArrayList<>();
                dataAdapterList.add(new Form(null, getString(R.string.all)));
                dataAdapterList.addAll((ArrayList<Form>) data);

                ArrayAdapter<Form> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dataAdapterList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                form.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure() {}
        });

    }

    public void onResume() {
        super.onResume();
        initWidth();
    }

    private void initWidth() {
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    public interface Filtered {
        void doFilter(Filter filter);
    }

}
