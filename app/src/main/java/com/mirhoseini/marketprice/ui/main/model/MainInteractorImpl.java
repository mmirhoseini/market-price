package com.mirhoseini.marketprice.ui.main.model;


import android.os.AsyncTask;

import com.mirhoseini.marketprice.database.DatabaseHelper;
import com.mirhoseini.marketprice.database.model.PriceValue;
import com.mirhoseini.marketprice.network.NetworkHelper;
import com.mirhoseini.marketprice.network.OnNetworkFinishedListener;
import com.mirhoseini.marketprice.network.converters.RestModelConverter;
import com.mirhoseini.marketprice.network.model.RestMarketPrice;
import com.mirhoseini.marketprice.network.model.RestPriceValue;
import com.mirhoseini.marketprice.utils.TimeSpan;

import java.util.List;


/**
 * Created by Mohsen on 24/03/16.
 */
public class MainInteractorImpl implements MainInteractor {
    NetworkHelper mNetworkHelper;
    DatabaseHelper mDatabaseHelper;

    public MainInteractorImpl() {
        mNetworkHelper = NetworkHelper.getInstance();
        mDatabaseHelper = DatabaseHelper.getInstance();
    }

    @Override
    public void loadMarketPrice(TimeSpan timeSpan, final OnMainNetworkFinishedListener listener) {
        if (mNetworkHelper != null) {//check if object is not destroyed

            // load Market Price data from network
            mNetworkHelper.loadMarketPriceValues(timeSpan, new OnNetworkFinishedListener<RestMarketPrice>() {
                @Override
                public void onSuccess(TimeSpan timeSpan, RestMarketPrice restResponse) {
                    if (listener != null)
                        listener.onNetworkSuccess(timeSpan, restResponse);
                }

                @Override
                public void onError(TimeSpan timeSpan, Throwable throwable) {
                    if (listener != null)
                        listener.onNetworkError(timeSpan, throwable);
                }
            });

        }
    }

    @Override
    public List<PriceValue> loadPriceValuesFromDatabase(TimeSpan timeSpan) {
        List<PriceValue> items = null;

        if (mDatabaseHelper != null) {//check if object is not destroyed

            //load Priva Values from database
            items = mDatabaseHelper.loadPriceValues(timeSpan.getPosition());

        }

        return items;
    }

    @Override
    public void deletePriceValues(TimeSpan timeSpan) {
        if (mDatabaseHelper != null) {//check if object is not destroyed

            // delete a specific TimeSpan cached data from Database
            mDatabaseHelper.deletePriceValues(timeSpan.getPosition());

        }
    }

    @Override
    public void onDestroy() {
        mDatabaseHelper = null;
        mNetworkHelper = null;
    }

    @Override
    public void saveMarketPrice(final TimeSpan timeSpan, final RestMarketPrice restMarketPrice, final OnMainDatabaseFinishedListener listener) {
        // using AsyncTask to avoid main thread stock to this long lasting process for big amount of data
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                deletePriceValues(timeSpan);

                for (RestPriceValue restPriceValue : restMarketPrice.getValues()) {
                    if (mDatabaseHelper != null) {//check if object is not destroyed
                        mDatabaseHelper.insertPriceValue(RestModelConverter.convertRestModelToPriceValue(timeSpan, restPriceValue));
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (listener != null)
                    listener.onDatabaseSuccess(timeSpan);
            }
        }.execute();

    }

}
