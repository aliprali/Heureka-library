package heureka.cz.internal.library.helpers;

import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tomas on 19.5.16.
 */
public class RetrofitBuilder {

    private Gson gson;

    private OkHttpClient okHttpClient;

    @Inject
    public RetrofitBuilder(Gson gson, OkHttpClient okHttpClient) {
        this.gson = gson;
        this.okHttpClient = okHttpClient;
    }

    public Retrofit provideRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

}

