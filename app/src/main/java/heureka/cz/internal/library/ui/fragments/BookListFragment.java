package heureka.cz.internal.library.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.Filter;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.MainActivity;
import heureka.cz.internal.library.ui.dialogs.FilterDialog;

/**
 * Created by tomas on 6.4.16.
 */
public class BookListFragment extends AbstractBookFragment implements FilterDialog.Filtered {

    ArrayList<Book> books = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);
        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));
        return v;
    }

    @Override
    protected void callApi() {
        if (books == null) {
            apiDescription.getBooks(new ApiDescription.ResponseHandler() {
                @Override
                public void onResponse(Object data) {
                    books = (ArrayList<Book>) data;
                    adapter.setData(books);
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, "fail");
                }
            });
        } else {
            adapter.setData(books);
        }
    }

    @Override
    public void doFilter(Filter filter) {
        ArrayList<Book> finalBooks = new ArrayList<Book>();

        for (Book book : books) {

            Log.d("TEST", "book: " +book.getLang() + ", filter: " + filter.getLang().getLang() + ": " + book.getLang().equals(filter.getLang().getLang()));
            Log.d("TEST", "book: " +book.getForm() + ", filter: " + filter.getForm().getForm() + ": " + book.getForm().equals(filter.getForm().getForm()));

            if ((filter.getLang().getLangShort() == null || book.getLang().equals(filter.getLang().getLang())) &&
                            (filter.getForm().getFormShort() == null || book.getForm().equals(filter.getForm().getForm()))) {
                finalBooks.add(book);
            }
        }
        adapter.setData(finalBooks);
    }

    @Override
    protected int getTitle() {
        return R.string.tit_books;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("ONRESUME LIST");
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.callSuper(view, savedInstanceState);
        System.out.println("ONVIEWCREATED BookList");



        if(getActivity() instanceof TitleSetter) {
            ((TitleSetter)getActivity()).setTitle(getTitle());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        super.initAdapter(new ArrayList<Book>());

        if (savedInstanceState != null) {
            adapter.setData(savedInstanceState.<Book>getParcelableArrayList(KEY_BOOKS));
        } else {
            callApi();
        }
    }


}


