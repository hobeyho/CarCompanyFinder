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

        textViewHelp.setText("This App is used for determining car logos that you think is adequate. " +
                "This App provides you a variety of answer of car logo that you do not know. " +
                "This App uses Watson Visual Recognition ApI. When you press the button \"photo\", " +
                "the camera screen appears at the screen. When You take a good shot, the picture gets " +
                "uploaded to Watson and the AI sends the value back to our app. Please make your shot as good as it would be in a google image if you search the word car logo. ");

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
