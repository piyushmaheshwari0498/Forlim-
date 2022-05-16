package com.fashion.forlempopoli.Entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterData {
    @SerializedName("city")
    @Expose
    private List<CityDatum> city = null;
    @SerializedName("state")
    @Expose
    private List<StateDatum> state = null;
    @SerializedName("country")
    @Expose
    private List<CountryDatum> country = null;

    public List<CityDatum> getCity() {
        return city;
    }

    public void setCity(List<CityDatum> city) {
        this.city = city;
    }

    public List<StateDatum> getState() {
        return state;
    }

    public void setState(List<StateDatum> state) {
        this.state = state;
    }

    public List<CountryDatum> getCountry() {
        return country;
    }

    public void setCountry(List<CountryDatum> country) {
        this.country = country;
    }
}
