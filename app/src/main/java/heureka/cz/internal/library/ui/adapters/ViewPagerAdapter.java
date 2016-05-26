package heureka.cz.internal.library.ui.adapters;

/**
 * Created by Ondrej on 6. 5. 2016.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.ui.fragments.BookDetailFragment;
import heureka.cz.internal.library.ui.fragments.BookHistoryFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;

    BookDetailFragment detailKnihy;

    // Tab Titles
    private String tabtitles[] = new String[] { "Detail knihy", "Historie knihy" };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {


            case 0:
                detailKnihy = new BookDetailFragment();
                return detailKnihy;
            case 1:
                System.out.println("CASE1");
                Bundle bundle = new Bundle();
                bundle.putString("concreteBook","300254123");
                BookHistoryFragment historieKnihy = new BookHistoryFragment();
                historieKnihy.setArguments(bundle);
                return historieKnihy;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

    public Book getBook() {
        return detailKnihy.getBook();
    }
}
