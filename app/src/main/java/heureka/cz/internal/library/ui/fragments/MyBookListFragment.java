package heureka.cz.internal.library.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;

/**
 * Created by tomas on 6.4.16.
 */
public class MyBookListFragment extends AbstractBookFragment {

    @Inject
    Settings settings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);
        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));

        return v;
    }

    @Override
    protected boolean isMyBook() {
        return true;
    }

    @Override
    protected void callApi() {

        if(settings.get() == null) {
            return;
        }

        apiDescription.getMyBooks(settings.get().getEmail(), new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                Log.d(TAG, "load books");

                adapter.setData((ArrayList<Book>) data);

            }

            @Override
            public void onFailure() {
                Log.d(TAG, "fail");
            }
        });
    }

    @Override
    protected int getTitle() {
        return R.string.tit_my_books;
    }


}
