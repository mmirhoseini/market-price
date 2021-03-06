package com.mirhoseini.marketprice.network;

import com.mirhoseini.marketprice.BuildConfig;
import com.mirhoseini.marketprice.network.model.RestMarketPrice;
import com.mirhoseini.marketprice.utils.Constants;
import com.mirhoseini.marketprice.utils.TimeSpan;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Mohsen on 24/03/16.
 */
public class NetworkHelper {
    public static final String TAG = NetworkHelper.class.getSimpleName();

    static NetworkHelper instance;

    Retrofit mRetrofit;
    Api mApi;

    private NetworkHelper() {

        Timber.tag(TAG);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);

        //show retrofit logs if app is in Debug
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        builder.client(httpClient.build());

        mRetrofit = builder.build();

        mApi = mRetrofit.create(Api.class);
    }

    public static NetworkHelper getInstance() {
        if (instance == null)
            instance = new NetworkHelper();

        return instance;
    }

    public void loadMarketPriceValues(final TimeSpan timeSpan, final OnNetworkFinishedListener<RestMarketPrice> listener) {
        Timber.d("Starting network connection");

        Call<RestMarketPrice> valuesCall = mApi.getMarketPriceValues(timeSpan.getValue(), Constants.API_FORMAT_JSON);
        valuesCall.enqueue(new Callback<RestMarketPrice>() {

            @Override
            public void onResponse(Call<RestMarketPrice> call, Response<RestMarketPrice> response) {
                Timber.d("Received Network Response: %s", response.toString());

                if (response.isSuccess()) {
                    if (listener != null) {
                        listener.onSuccess(timeSpan, response.body());
                    }
                } else {
                    if (listener != null)
                        listener.onError(timeSpan, new Exception("API response is not success!!"));
                }
            }

            @Override
            public void onFailure(Call<RestMarketPrice> call, Throwable t) {
                Timber.d("Network Error: %s", t.getMessage());

                if (listener != null)
                    listener.onError(timeSpan, t);
            }
        });
    }
}
