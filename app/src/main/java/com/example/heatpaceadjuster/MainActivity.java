package com.example.heatpaceadjuster;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.heatpaceadjuster.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //todo - rename this, because this is going to be the connection to Strava
    public void sendAdjustPace(View view){
        //todo - display a calculated pace from the entered pace
        Intent intent = new Intent(this, DisplayAdjustedPaceActivity.class);
        EditText editText = (EditText) findViewById(R.id.editGoalPace);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void sendCurrentAdjustPace(View view){
        EditText editGoalPaceText = (EditText) findViewById(R.id.editGoalPace);
        String messageGoalPace = editGoalPaceText.getText().toString();

        EditText editAdjustedPace = (EditText) findViewById(R.id.editAdjustedPace);
        String messageAdjustedPace = editAdjustedPace.getText().toString();

        if(messageGoalPace.isEmpty())
        {
            // todo - PaceCalculator convert
            editGoalPaceText.setText(messageAdjustedPace);
        }else if (messageAdjustedPace.isEmpty())
        {
            // todo - PaceCalculator convert
            editAdjustedPace.setText(messageGoalPace);
        }
    }
}