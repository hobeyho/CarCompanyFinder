package com.kwontaehoon.carcompanyfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    Button btnDone, btnLearn;
    TextView textViewResult;
    ImageView imageViewResult;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnDone= (Button)findViewById(R.id.btnDone);
        btnLearn = (Button)findViewById(R.id.btnLearn);
        textViewResult = (TextView)findViewById(R.id.textViewResult);
        imageViewResult = (ImageView)findViewById(R.id.imageViewResult);

        url = "https://en.wikipedia.org/wiki/Main_Page";

        Bundle extras = getIntent().getExtras();
        String result = "";
        if (extras != null) {
            result = extras.getString("result");
        }
        Log.d("debugging",result);

        HashMap<String, Double> list = new HashMap<String, Double>();

        String ans = "";

        int position = result.indexOf("\"class\"");
        while(position > 0){
            result = result.substring(position+10);
            int position2 = result.indexOf("\"");
            ans = result.substring(0, position2);
            int position3 = result.indexOf("\"score\"");
            int position4 = result.indexOf("}");
            double score = Double.parseDouble(result.substring(position3+10, position4-4));
            Log.d("debugging", "ANS: " + ans + " SCORE: " + score);
            list.put(ans, score);
            position = result.indexOf("\"class\"");
        }

        Log.d("debugging", ""+list);

        String car = "";
        double sco = 0;
        for(Map.Entry<String, Double> entry: list.entrySet()){
            String car2 =  entry.getKey();
            double sco2 = entry.getValue();
            if(sco2 > sco){
                sco = sco2;
                car = car2;
            }
        }

        ans = car;

        if(result.equals("")){
            ans = "error";
        }

        Log.d("debugging",ans);

        if(ans.equals("benz")){
            textViewResult.setText("Mercedez Benz");
            imageViewResult.setImageResource(R.drawable.benz);
            url = "https://en.wikipedia.org/wiki/Mercedes-Benz";
        } else if(ans.equals("audi")){
            textViewResult.setText("Audi");
            imageViewResult.setImageResource(R.drawable.audi);
            url = "https://en.wikipedia.org/wiki/Audi";
        } else if (ans.equals("bmw")) {
            textViewResult.setText("BMW");
            imageViewResult.setImageResource(R.drawable.bmw);
            url = "https://en.wikipedia.org/wiki/BMW";
        } else if(ans.equals("kia")){
            textViewResult.setText("Kia");
            imageViewResult.setImageResource(R.drawable.kia);
            url = "https://en.wikipedia.org/wiki/Kia_Motors";
        } else if(ans.equals("hyundai")){
            textViewResult.setText("Hyundai");
            imageViewResult.setImageResource(R.drawable.hyundai);
            url = "https://en.wikipedia.org/wiki/Hyundai";
        } else if(ans.equals("chevolet")){
            textViewResult.setText("Chevolet");
            imageViewResult.setImageResource(R.drawable.chevrolet);
            url = "https://en.wikipedia.org/wiki/Chevrolet";
        } else if(ans.equals("equus")){
            textViewResult.setText("Equus");
            imageViewResult.setImageResource(R.drawable.equus);
            url = "https://en.wikipedia.org/wiki/Hyundai_Equus";
        } else if(ans.equals("genesis")){
            textViewResult.setText("Genesis");
            imageViewResult.setImageResource(R.drawable.genesis);
            url = "https://en.wikipedia.org/wiki/Genesis_Motors";
        } else if(ans.equals("honda")){
            textViewResult.setText("Honda");
            imageViewResult.setImageResource(R.drawable.honda);
            url = "https://en.wikipedia.org/wiki/Honda";
        } else if(ans.equals("lexus")){
            textViewResult.setText("Lexus");
            imageViewResult.setImageResource(R.drawable.lexus);
            url = "https://en.wikipedia.org/wiki/Lexus";
        } else if(ans.equals("opirus")){
            textViewResult.setText("Opirus");
            imageViewResult.setImageResource(R.drawable.oprius);
            url = "https://en.wikipedia.org/wiki/Kia_Opirus";
        } else if(ans.equals("samsung")){
            textViewResult.setText("Samsung");
            imageViewResult.setImageResource(R.drawable.samsung);
            url = "https://en.wikipedia.org/wiki/Renault_Samsung_Motors";
        } else if(ans.equals("ssangyong")){
            textViewResult.setText("Ssangyong");
            imageViewResult.setImageResource(R.drawable.ssangyong);
            url = "https://en.wikipedia.org/wiki/SsangYong_Motor";
        } else if(ans.equals("toyota")){
            textViewResult.setText("Toyota");
            imageViewResult.setImageResource(R.drawable.toyota);
            url = "https://en.wikipedia.org/wiki/Toyota";
        } else if(ans.equals("volkswagon")){
            textViewResult.setText("Volkswagon");
            imageViewResult.setImageResource(R.drawable.volkswagen);
            url = "https://en.wikipedia.org/wiki/Volkswagen";
        } else {
            textViewResult.setText("오류");
            btnLearn.setEnabled(false);
        }

        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLearn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Activity fromActivity = ResultActivity.this;
                Class toActivity = WebActivity.class;
                Intent intent = new Intent (fromActivity, toActivity);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
}
