package heureka.cz.internal.library.ui.dialogs;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Info;
import heureka.cz.internal.library.repository.Position;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;

/**
 * Created by tomas on 26.4.16.
 */
public class RateDialog extends DialogFragment {

    public static final String TAG_BOOK_ID = "bookId";

    private ApiDescription apiDescription;

    @Inject
    RetrofitBuilder retrofitBuilder;

    @Inject
    Settings settings;

    @Bind(R.id.editText1)
    EditText commentText;

    @Bind(R.id.ratingBar)
    RatingBar ratingBar;

    @Bind(R.id.spinner)
    Spinner spinner;

    @Bind(R.id.button)
    Button btnReturn;

    @OnClick(R.id.button)
    void returnBook() {

        if (settings.get() == null) {
            return;
        }
        btnReturn.setEnabled(false);

        String comment = commentText.getText().toString();
        Integer ratingValue = (int)ratingBar.getRating();
        Position locality = (Position)spinner.getSelectedItem();

        if(comment.length() == 0) {
            comment = " ";
        }

        apiDescription.returnBook(bookId, settings.get().getEmail(), locality.getId(), ratingValue, comment, new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                Toast.makeText(getContext(), ((Info)data).getInfo(), Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }

            @Override
            public void onFailure() {
                btnReturn.setEnabled(true);
            }
        });
    }

    private int bookId;

    public RateDialog() {
    }

    public static RateDialog newInstance() {
        RateDialog frag = new RateDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.dialog_rate, container, false);
        ButterKnife.bind(this, view);
        Bundle mArgs = getArguments();
        ((CodeCamp) getActivity().getApplication()).getApplicationComponent().inject(this);

        bookId = mArgs.getInt(TAG_BOOK_ID);

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));
        apiDescription.getPositions(new ApiDescription.ResponseHandler() {

            @Override
            public void onResponse(Object data) {
                ArrayAdapter<Position> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, (ArrayList<Position>) data);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure() {
                System.out.println("Positions fail");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

}
