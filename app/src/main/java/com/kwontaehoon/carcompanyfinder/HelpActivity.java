package com.kwontaehoon.carcompanyfinder;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    TextView textViewHelp;
    Button btnSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textViewHelp = (TextView) findViewById(R.id.textViewHelp);
        btnSample = (Button) findViewById(R.id.btnSample);

        textViewHelp.setText("This App is used for finding car companies using their logos. " +
                "This App uses Watson Visual Recognition Api. When you press the button \"photo\", " +
                "the camera screen appears at the screen.(Please set camera resolution to 2M for best performance). " +
                "When You take a good shot, the picture gets " +
                "uploaded to Watson and the AI finds the most similar logo and sends the value back to our app." +
                "Please view sample photos to see how you should take the photo. " +
                "Due to lack of samples, this app can only determine between 5 car companies at the moment: Mercedes-Benz, BMW, Equus, Hyundai, Lexus. " +
                "I will work on adding more companies in the future!");

        btnSample.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showSample();
            }
        });

    }
    public void showSample(){
        Activity fromActivity = HelpActivity.this;
        Class toActivity = SampleActivity.class;
        Intent intent = new Intent (fromActivity, toActivity);
        startActivity(intent);
    }
}
