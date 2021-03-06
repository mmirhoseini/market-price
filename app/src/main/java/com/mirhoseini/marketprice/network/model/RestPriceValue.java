package com.mirhoseini.marketprice.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 24/03/16.
 */
public class RestPriceValue {
    @SerializedName("y")
    private String y;

    @SerializedName("x")
    private String x;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "RestPriceValues [y = " + y + ", x = " + x + "]";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        RestPriceValue restPriceValue = (RestPriceValue) object;

        return this.getX() == restPriceValue.getX() && this.getY() == restPriceValue.getY();
    }
}
