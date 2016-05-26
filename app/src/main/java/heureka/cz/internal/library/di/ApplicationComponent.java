package heureka.cz.internal.library.di;

import heureka.cz.internal.library.gcm.RegistrationIntentService;
import heureka.cz.internal.library.ui.BookDetailAndResActivity;
import heureka.cz.internal.library.ui.MainActivity;
import heureka.cz.internal.library.ui.dialogs.FilterDialog;
import heureka.cz.internal.library.ui.dialogs.SettingsDialog;
import heureka.cz.internal.library.ui.dialogs.RateDialog;
import heureka.cz.internal.library.ui.fragments.UserHistoryFragment;
import heureka.cz.internal.library.ui.dialogs.SearchDialog;
import heureka.cz.internal.library.ui.fragments.BookDetailFragment;
import heureka.cz.internal.library.ui.fragments.BookHistoryFragment;
import heureka.cz.internal.library.ui.fragments.BookListFragment;
import heureka.cz.internal.library.ui.fragments.HeurekovinyListFragment;
import heureka.cz.internal.library.ui.fragments.MyBookListFragment;
import heureka.cz.internal.library.ui.fragments.ParentBookListFragment;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by tomas on 13.4.16.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    // fragments
    void inject(BookListFragment taskFragment);
    void inject(HeurekovinyListFragment heurekaListFragmnent);
    void inject(MyBookListFragment taskFragment);
    void inject(BookDetailFragment bookDetailFragment);
    void inject(BookHistoryFragment bookHistoryFragment);
    void inject(UserHistoryFragment userHistoryFragment);
    void inject(ParentBookListFragment parentBookListFragmnent);

    // activity
    void inject(BookDetailAndResActivity bookDetailAndResActivity);
    void inject(MainActivity mainActivity);

    // dialogs
    void inject(SearchDialog searchDialog);
    void inject(SettingsDialog settingsDialog);
    void inject(RateDialog rateDialog);
    void inject(FilterDialog filterDialog);

    // services
    void inject(RegistrationIntentService registrationIntentService);

}
