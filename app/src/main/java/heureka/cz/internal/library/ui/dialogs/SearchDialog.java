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
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.BookDetailAndResActivity;
import heureka.cz.internal.library.ui.MainActivity;

/**
 * Created by tomas on 26.4.16.
 */
public class SearchDialog extends DialogFragment {

    private static final String TAG = "SearchDialog";

    private String code = "";

    @Inject
    RetrofitBuilder retrofitBuilder;

    private ApiDescription apiDescription;

    @NotEmpty(messageId = R.string.not_empty)
    @Bind(R.id.search_value)
    EditText searchValue;

    @OnClick(R.id.barcode)
    void barcode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.initiateScan();
        dismiss();
    }

    @OnClick(R.id.search)
    void search() {

        if(!FormValidator.validate(this, new SimpleErrorPopupCallback(getContext()))) {
            return;
        }

        final String bookCode = searchValue.getText().toString();
        apiDescription.getBook(bookCode, new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                Intent intent = new Intent(getActivity(), BookDetailAndResActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MainActivity.KEY_BOOK_DETAIL, (Book)data);
                bundle.putBoolean(BookDetailAndResActivity.KEY_CAN_BORROW, true);
                bundle.putString(BookDetailAndResActivity.KEY_CODE, bookCode);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "fail");
            }
        });


    }

    public SearchDialog() {
    }

    public static SearchDialog newInstance() {
        SearchDialog frag = new SearchDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_search, container);

        ButterKnife.bind(this, view);
        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(Config.API_BASE_URL));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.search);
        searchValue.setText(code);
        searchValue.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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

    public void setCode(String code) {
        this.code = code;
    }
}
