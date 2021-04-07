package com.example.currency.data;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CurrencyInterface {
    @GET("latest?access_key=3b4a3a55e87e35553161c02c4c260fd3&format=1")
    Call<JsonObject> getExchangeCurrency();
}
