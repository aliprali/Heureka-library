package heureka.cz.internal.library.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.gcm.RegistrationIntentService;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.Download;
import heureka.cz.internal.library.helpers.Filter;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Book;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;
import heureka.cz.internal.library.ui.dialogs.FilterDialog;
import heureka.cz.internal.library.ui.dialogs.SearchDialog;
import heureka.cz.internal.library.ui.dialogs.SettingsDialog;
import heureka.cz.internal.library.ui.fragments.AbstractBookFragment;
import heureka.cz.internal.library.ui.fragments.BookListFragment;
import heureka.cz.internal.library.ui.fragments.MyBookListFragment;
import heureka.cz.internal.library.ui.fragments.ParentBookListFragment;
import heureka.cz.internal.library.ui.fragments.UserHistoryFragment;

public class MainActivity extends AppCompatActivity implements AbstractBookFragment.BookDetailOpener, AbstractBookFragment.TitleSetter, FilterDialog.Filtered {

    public static final String MESSAGE_PROGRESS = "message_progress";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String TAG = "MainActivity";
    public static final String KEY_BOOK_DETAIL = "book_detail";

    private SearchDialog searchDialog = SearchDialog.newInstance();
    private FilterDialog filterDialog = FilterDialog.newInstance();

    ArrayList<Book> books = null;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    RetrofitBuilder retrofitBuilder;

    @Inject
    Settings settings;

    private ApiDescription apiDescription;

    private ActionBarDrawerToggle drawerToggle;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Download download = intent.getParcelableExtra("download");

            // TODO update - ukazat nekde progress

            /*mProgressBar.setProgress(download.getProgress());
            if(download.getProgress() == 100){
                mProgressText.setText("File Download Complete");
            } else {
                mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));
            }*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((CodeCamp)getApplication()).getApplicationComponent().inject(this);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(Config.API_BASE_URL));

        setSupportActionBar(toolbar);
        setupToolbar();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideIme();
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        if (savedInstanceState == null) {
            createFragmentForActionId(R.id.nav_books);
        }

        registerReceiver();
        initSettings();

    }

    private void registerReceiver(){
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void initSettings() {

        if(settings.get() == null) {
            SettingsDialog settingsDialog= SettingsDialog.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            settingsDialog.show(fm, "fragment_settings_dialog");
        } else if (settings.get().getActive() == 0) {
            Toast.makeText(this, R.string.notActivated, Toast.LENGTH_LONG).show();
        }
    }

    private void hideIme() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        // Listener na vybirani menu itemu
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);

                        createFragmentForActionId(menuItem.getItemId());
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void createFragmentForActionId(int menuItem) {
        Fragment f = null;
        switch (menuItem) {
            case R.id.nav_books:
                f = new ParentBookListFragment();
                break;
            case R.id.nav_my_books:
                f = new MyBookListFragment();
                break;
            case R.id.nav_user_history:
                f = new UserHistoryFragment();
                break;
        }

        if (f != null) {
            removeOldFragments();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, f, f.getClass().getName())
                    .commit();
        }
    }

    private void removeOldFragments() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void setupToolbar() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.share);
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
            case R.id.filter:
                FragmentManager fm2 = getSupportFragmentManager();
                filterDialog.show(fm2, "fragment_filter_dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            Log.d(TAG, "scan: " + scanResult.toString());

            FragmentManager fm = getSupportFragmentManager();
            searchDialog.setCode(scanResult.getContents());
            searchDialog.show(fm, "fragment_search_dialog");
        }
    }

    @Override
    public void bookDetail(Book book, boolean isMyBook) {
        Intent intent = new Intent(this, BookDetailAndResActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BOOK_DETAIL, book);
        bundle.putBoolean(BookDetailAndResActivity.KEY_CAN_RESERVE, !isMyBook);
        bundle.putBoolean(BookDetailAndResActivity.MY_BOOK, isMyBook);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void doFilter(Filter filter) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

        Log.d("TEST", "filter fragment start? " + f.getClass().getCanonicalName());
        if (f instanceof FilterDialog.Filtered) {
            Log.d("TEST", "filter fragment start!");
            ((FilterDialog.Filtered) f).doFilter(filter);
        }

    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }

    public void setBooks(ArrayList<Book> books){
        this.books = books;
    }

    public ArrayList<Book> getBooks(){
        return books;
    }

}
