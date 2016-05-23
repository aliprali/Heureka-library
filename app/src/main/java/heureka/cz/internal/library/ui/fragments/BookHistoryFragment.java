package heureka.cz.internal.library.ui.fragments;

/**
 * Created by Ondrej on 6. 5. 2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.CollectionUtils;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.BookHolders;
import heureka.cz.internal.library.repository.Holder;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.adapters.BookRecyclerAdapter;
import heureka.cz.internal.library.ui.adapters.HistoryRecyclerAdapter;
import retrofit2.Retrofit;

public class BookHistoryFragment extends Fragment {

    @Inject
    RetrofitBuilder retrofitBuilder;

    @Inject
    CollectionUtils collectionUtils;

    @Bind(R.id.todo_list_view2)
    RecyclerView recyclerView;

    String code= "";

    protected ApiDescription apiDescription;
    protected HistoryRecyclerAdapter adapter;

    public static final String TAG = "BookHistoryFragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_history, container, false);
        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(Config.API_BASE_URL));
        ButterKnife.bind(this, view);
        return view;
    }


    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter(new ArrayList<BookHolders>());
        Bundle bundle = this.getArguments();
        code = bundle.getString("concreteBook");
        callApi();

    }
    protected void callApi() {

        apiDescription.historyOneBook(code, new ApiDescription.ResponseHandler() {
            @Override
            public void onResponse(Object data) {
                Log.d(TAG, "load books");

                ArrayList<BookHolders> list = (ArrayList<BookHolders>)data;
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("list"+i+list.get(i).user_name);
                }
                adapter.setData((ArrayList<BookHolders>) data);

            }

            @Override
            public void onFailure() {
                Log.d(TAG, "fail");
            }
        });
    }

    private void initAdapter(ArrayList<BookHolders> holders) {
        Log.d(TAG, "set adapter");

        if(getActivity() == null) {
            return;
        }

        adapter = new HistoryRecyclerAdapter(holders, collectionUtils);
        recyclerView.setAdapter(adapter);

//        adapter.setListener(new HistoryRecyclerAdapter.OnTaskItemClickListener() {
//            @Override
//            public void onItemClick(int taskPosition) {
//                Log.d(TAG, "on click");
//
//
//            }
//
//            @Override
//            public void onItemLongClick(int taskPosition) {
//                Log.d(TAG, "on long click");
//            }
//
////            @Override
////            public boolean onBackupClick(int taskPosition) {
//////                Log.d(TAG, "backup click");
//////                try {
//////                    adapter.getBooks().get(taskPosition).setDbTags(collectionUtils.implode(",", adapter.getBooks().get(taskPosition).getTags()));
//////                    adapter.getBooks().get(taskPosition).save();
//////                    return true;
//////                } catch (Exception e) {
//////                    Log.w(TAG, e);
//////                    return false;
//////                }
////            }
//        });
    }

}