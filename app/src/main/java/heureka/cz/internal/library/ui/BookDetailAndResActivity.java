package heureka.cz.internal.library.ui;

/**
 * Created by Ondrej on 8. 5. 2016.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.ui.adapters.ViewPagerAdapter;
import heureka.cz.internal.library.ui.dialogs.SearchDialog;

public class BookDetailAndResActivity extends AppCompatActivity{

    public static final String KEY_CAN_BORROW = "can_borrow";
    public static final String KEY_CAN_RESERVE = "can_reserve";
    public static final String KEY_CODE = "code";
    public static final String MY_BOOK = "is_my_book";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.pager)
    ViewPager viewPager;

    private SearchDialog searchDialog = SearchDialog.newInstance();

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_detail_and_res);

        ((CodeCamp)getApplication()).getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setupToolbar();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.filter);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                FragmentManager fm = getSupportFragmentManager();
                searchDialog.show(fm, "fragment_search_dialog");
                break;
            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.shareTitle));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, adapter.getBook().getName());

                startActivity(Intent.createChooser(sharingIntent, getString(R.string.shareVia)));
                break;
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);

        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

    }
}
