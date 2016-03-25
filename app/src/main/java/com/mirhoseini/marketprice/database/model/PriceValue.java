package com.mirhoseini.marketprice.database.model;

import com.mirhoseini.marketprice.database.DatabaseHelper;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Mohsen on 24/03/16.
 */
@Table(database = DatabaseHelper.class, name = "pricevalue")
public class PriceValue extends BaseModel {
    @PrimaryKey
    private int timeSpanId;
    @PrimaryKey
    private long x;//timestamp
    @Column
    private float y;

    public PriceValue(int timeSpanId, long x, float y) {
        this.timeSpanId = timeSpanId;
        this.x = x;
        this.y = y;
    }

    public PriceValue() {
    }

    public int getTimeSpanId() {
        return timeSpanId;
    }

    public void setTimeSpanId(int timeSpanId) {
        this.timeSpanId = timeSpanId;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "PriceValue [timeSpanId = " + timeSpanId + ", x = " + x + ", y = " + y + "]";
    }

}