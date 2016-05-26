package heureka.cz.internal.library.ui.dialogs;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Api;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.MainActivity;

/**
 * Created by tomas on 18.5.16.
 */
public class SettingsDialog extends DialogFragment {

    private static final String TAG = "SearchDialog";

    @Inject
    RetrofitBuilder retrofitBuilder;

    private ApiDescription apiDescription;

    @Bind(R.id.api)
    Spinner api;

    @NotEmpty(messageId = R.string.not_empty)
    @Bind(R.id.name)
    EditText name;

    @NotEmpty(messageId = R.string.not_empty)
    @Bind(R.id.email)
    EditText email;

    @OnClick(R.id.set)
    void set() {

        Settings settings = new Settings();
        settings.setApiAddress(((Api)api.getSelectedItem()).getAddress());
        settings.setActive(0);
        settings.setName(name.getText().toString());
        settings.setEmail(email.getText().toString());
        settings.save();

        Log.d("TEST", settings.getApiAddress());
        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.getApiAddress()));
        apiDescription.addUser(Config.URL_USERS, settings.getName(), settings.getEmail(), new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {}

            @Override
            public void onFailure() {}
        });

        // restart aktivity
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        dismiss();
    }

    public SettingsDialog() {
    }

    public static SettingsDialog newInstance() {
        SettingsDialog frag = new SettingsDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_settings, container);

        ButterKnife.bind(this, view);
        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);

        /**
         * apiDescription pro seznam API, netreba brat url ze settings
         * */
        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(Config.API_BASE_URL));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.settings);

        initApi();
    }

    private void initApi() {
        apiDescription.apiList(new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                ArrayAdapter<Api> adapter = new ArrayAdapter<>(SettingsDialog.this.getContext(),
                        R.layout.spinner_api, (ArrayList<Api>) data);
                api.setAdapter(adapter);
            }

            @Override
            public void onFailure() {}
        });

        email.setText(getEmail());
    }

    private String getEmail() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                return account.name;
            }
        }
        return "";
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
}
