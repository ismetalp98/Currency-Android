package com.example.currency;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.currency.data.CurrencyData;
import com.example.currency.data.CurrencyInterface;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    CurrencyInterface retrofitInterface;
    Intent data;
    String usdprev,tryprev,jpyprev;
    String usdnow,trynow,jpynow;
    @BindView(R.id.usdnow) TextView usdn;
    @BindView(R.id.usdprew) TextView usdp;
    @BindView(R.id.trynow) TextView tryn;
    @BindView(R.id.tryprew) TextView tryp;
    @BindView(R.id.jpynow) TextView jpyn;
    @BindView(R.id.jpyprew) TextView jpyp;
    @BindView(R.id.lastUpdate) TextView lastUpdate;
    @BindView(R.id.usdstat) ImageView usdstat;
    @BindView(R.id.trystat) ImageView trystat;
    @BindView(R.id.jpystat) ImageView jpystat;
    //int i = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        begining();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
                /*i++;
                if(i % 3 == 0) i = 1;*/
            }
        }, 2000, 2000);

    }

    private void begining(){
        data = getIntent();

        usdnow = data.getStringExtra("usd");
        trynow = data.getStringExtra("try");
        jpynow = data.getStringExtra("jpy");

        usdn.setText("Current  -  " + usdnow);
        tryn.setText("Current  -  " + trynow);
        jpyn.setText("Current  -  " + jpynow);

        usdp.setText("Previous - #");
        tryp.setText("Previous - #");
        jpyp.setText("Previous - #");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        lastUpdate.setText("Last Update: " + formatter.format(date));
    }

    private void update(){
                retrofitInterface = CurrencyData.getRetrofitInstance().create(CurrencyInterface.class);
                Call<JsonObject> call = retrofitInterface.getExchangeCurrency();
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("rates");
                        String trlira = rates.get("TRY").toString();
                        String usd = rates.get("USD").toString();
                        String jpy = rates.get("JPY").toString();

                        usdprev = usdnow;
                        tryprev =  trynow;
                        jpyprev = jpynow;

                        usdnow = usd;
                        trynow =  trlira;
                        jpynow = jpy;

                        /*usdnow = String.valueOf(i);
                        trynow =  String.valueOf(i);
                        jpynow = String.valueOf(i);*/

                        if(Double.valueOf(usdnow) > Double.valueOf(usdprev)){
                            usdstat.setImageResource(R.drawable.ic_polygon_4);
                        }
                        else if(Double.valueOf(usdnow) < Double.valueOf(usdprev)){
                            usdstat.setImageResource(R.drawable.ic_polygon_3);
                        }
                        else
                            usdstat.setImageResource(R.drawable.ic_line_2);

                        if(Double.valueOf(trynow) > Double.valueOf(tryprev)){
                            trystat.setImageResource(R.drawable.ic_polygon_4);
                        }
                        else if(Double.valueOf(trynow) < Double.valueOf(tryprev)){
                            trystat.setImageResource(R.drawable.ic_polygon_3);
                        }
                        else
                            trystat.setImageResource(R.drawable.ic_line_2);

                        if(Double.valueOf(jpynow) > Double.valueOf(jpyprev)){
                            jpystat.setImageResource(R.drawable.ic_polygon_4);
                        }
                        else if(Double.valueOf(jpynow) < Double.valueOf(jpyprev)){
                            jpystat.setImageResource(R.drawable.ic_polygon_3);
                        }
                        else
                            jpystat.setImageResource(R.drawable.ic_line_2);


                        usdp.setText("Previous - " + usdprev);
                        tryp.setText("Previous - " + tryprev);
                        jpyp.setText("Previous - " + jpyprev);

                        usdn.setText("Current  -  " + usdnow);
                        tryn.setText("Current  -  " + trynow);
                        jpyn.setText("Current  -  " + jpynow);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = new Date();
                        lastUpdate.setText("Last Update: " + formatter.format(date));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        System.out.println("Connection Error");
                    }
                });
    }
}

