package heureka.cz.internal.library.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.helpers.CollectionUtils;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.adapters.BookRecyclerAdapter;
import retrofit2.Retrofit;

/**
 * Created by tomas on 28.4.16.
 */
public abstract class AbstractBookFragment extends Fragment {

    public static final String TAG = "AbstractBookFragment";
    public static final String KEY_BOOKS = "books";

    @Bind(R.id.todo_list_view)
    RecyclerView recyclerView;

    @Inject
    Retrofit retrofit;

    @Inject
    CollectionUtils collectionUtils;

    protected BookRecyclerAdapter adapter;
    protected ApiDescription apiDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        if(getArguments() != null) {

        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initAdapter(new ArrayList<Book>());

        if (savedInstanceState != null) {
            adapter.setData(savedInstanceState.<Book>getParcelableArrayList(KEY_BOOKS));
        } else {
            callApi();
        }
    }

    protected abstract void callApi();

    private void initAdapter(ArrayList<Book> books) {
        Log.d(TAG, "set adapter");

        if(getActivity() == null) {
            return;
        }

        adapter = new BookRecyclerAdapter(books, collectionUtils);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new BookRecyclerAdapter.OnTaskItemClickListener() {
            @Override
            public void onItemClick(int taskPosition) {
                Log.d(TAG, "on click");

                if(getActivity() instanceof BookDetailOpener) {
                    Log.d(TAG, "open?");
                    ((BookDetailOpener)getActivity()).bookDetail(adapter.getBooks().get(taskPosition));
                }
            }

            @Override
            public void onItemLongClick(int taskPosition) {
                Log.d(TAG, "on long click");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_BOOKS, adapter.getBooks());
    }

    public interface BookDetailOpener {

        public void bookDetail(Book book);

    }

}