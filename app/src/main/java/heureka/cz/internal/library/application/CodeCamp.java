package heureka.cz.internal.library.application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.facebook.stetho.Stetho;

import heureka.cz.internal.library.di.ApplicationComponent;
import heureka.cz.internal.library.di.ApplicationModule;
import heureka.cz.internal.library.di.DaggerApplicationComponent;

/**
 * Created by tomas on 13.4.16.
 */
public class CodeCamp extends Application implements ProvideApplicationComponent {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO if developer
        Stetho.initializeWithDefaults(this);

        ActiveAndroid.initialize(this);
        initInjector();
    }

    private void initInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
