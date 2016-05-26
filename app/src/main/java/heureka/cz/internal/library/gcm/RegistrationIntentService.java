package heureka.cz.internal.library.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import javax.inject.Inject;

import heureka.cz.internal.library.R;
import heureka.cz.internal.library.application.CodeCamp;
import heureka.cz.internal.library.di.ApplicationComponent;
import heureka.cz.internal.library.helpers.Config;
import heureka.cz.internal.library.helpers.RetrofitBuilder;
import heureka.cz.internal.library.repository.Settings;
import heureka.cz.internal.library.rest.ApiDescription;

/**
 * Register for push notifications
 *
 * @author Michal Kučera [michal.kucera@ackee.cz]
 * @since {28/04/16}
 **/
public class RegistrationIntentService extends IntentService {
    public static final String TAG = RegistrationIntentService.class.getName();


    @Inject
    Settings settings;

    @Inject
    RetrofitBuilder retrofitBuilder;

    private ApiDescription apiDescription;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "oncreate service");

        ((CodeCamp)getApplication()).getApplicationComponent().inject(this);
        apiDescription = new ApiDescription(retrofitBuilder.provideRetrofit(settings.get() != null ? settings.get().getApiAddress() : Config.API_BASE_URL));
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public RegistrationIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent() called with: " + "intent = [" + intent + "]");
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(TAG, "GCM Registration Token: " + token);


            if(settings.get() == null) {
                return;
            }

            // Poslat token na server aby vedel komu poslat pushky
            apiDescription.registerToken(settings.get().getEmail(), token, new ApiDescription.ResponseHandler() {
                @Override
                public void onResponse(Object data) {}

                @Override
                public void onFailure() {}
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}