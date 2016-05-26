package heureka.cz.internal.library.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.helpers.Filter;
import heureka.cz.internal.library.ui.adapters.ViewPagerAdapterMain;
import heureka.cz.internal.library.ui.dialogs.FilterDialog;

/**
 * Created by Ondrej on 19. 5. 2016.
 */
public class ParentBookListFragment extends Fragment implements FilterDialog.Filtered {

    ViewPagerAdapterMain adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, container, false);
        ((CodeCamp)getActivity().getApplication()).getApplicationComponent().inject(this);

        ButterKnife.bind(this, view);
        return view;
    }


    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ViewPagerAdapterMain(getActivity().getSupportFragmentManager());

        ViewPager viewPager = (ViewPager)getView().findViewById(R.id.pager3);
        viewPager.setAdapter(null);
        viewPager.setAdapter(adapter);

        getActivity().setTitle(R.string.tit_books);
    }

    @Override
    public void doFilter(Filter filter) {
        adapter.doFilter(filter);
    }
}
