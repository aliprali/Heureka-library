package heureka.cz.internal.library.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.CollectionUtils;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Heurekoviny;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.services.DownloadService;
import heureka.cz.internal.library.ui.adapters.HeurekaRecyclerAdapter;

/**
 * Created by Ondrej on 18. 5. 2016.
 */
public class HeurekovinyListFragment extends Fragment{

    private static final int PERMISSION_REQUEST_CODE = 1;

    protected ApiDescription apiDescription;

    @Inject
    RetrofitBuilder retrofitBuilder;

    @Inject
    CollectionUtils collectionUtils;

    @Inject
    Settings settings;

    @Bind(R.id.todo_list_view3)
    RecyclerView recyclerView;

    protected HeurekaRecyclerAdapter adapter;

    public static final String TAG = "HeurekaListFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heurekoviny_list, container, false);

        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));
        ButterKnife.bind(this, view);

        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter(new ArrayList<Heurekoviny>());
        callApi();

    }


    protected void callApi() {
        apiDescription.getHeurekoviny(new ApiDescription.ResponseHandler() {

            @Override
            public void onResponse(Object data) {
                adapter.setData((ArrayList<Heurekoviny>) data);
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "fail");
            }
        });
    }

    private void initAdapter(final ArrayList<Heurekoviny> heurekoviny) {

        if(getActivity() == null) {
            return;
        }

        adapter = new HeurekaRecyclerAdapter(heurekoviny, collectionUtils);

        adapter.setListener(new HeurekaRecyclerAdapter.OnTaskItemClickListener() {

            @Override
            public void onItemClick(int taskPosition) {
                if(checkPermission()){
                    startDownload(adapter.getHeurekoviny().get(taskPosition).getId(), adapter.getHeurekoviny().get(taskPosition).getDate());
                } else {
                    // TODO info o tom, ze nema permission
                }
            }

            @Override
            public void onItemLongClick(int taskPosition) {}
        });

        recyclerView.setAdapter(adapter);


    }
    protected int getTitle() {
        return R.string.tit_books;
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    private void startDownload(String id, String date){
        Log.d("TEST", "start service");

        Bundle bundle = new Bundle();
        bundle.putString(DownloadService.KEY_ID, id);
        bundle.putString(DownloadService.KEY_NAME, "heurekoviny_"+date+".pdf");

        Intent intent = new Intent(getContext(), DownloadService.class);
        intent.putExtras(bundle);
        getActivity().startService(intent);
    }
}
