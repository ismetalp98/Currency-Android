package com.example.currency;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.currency.data.CurrencyData;
import com.example.currency.data.CurrencyInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        view = findViewById(R.id.splashmain);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        CurrencyInterface retrofitInterface = CurrencyData.getRetrofitInstance().create(CurrencyInterface.class);
        Call<JsonObject> call = retrofitInterface.getExchangeCurrency();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                JsonObject rates = res.getAsJsonObject("rates");
                String trlira = rates.get("TRY").toString();
                String usd = rates.get("USD").toString();
                String jpy = rates.get("JPY").toString();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("usd",usd);
                        intent.putExtra("try",trlira);
                        intent.putExtra("jpy",jpy);
                        startActivity(intent);
                        finish();
                    }
                },1500);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(SplashScreen.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
