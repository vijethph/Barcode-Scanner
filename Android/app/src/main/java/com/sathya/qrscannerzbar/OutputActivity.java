package com.sathya.qrscannerzbar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OutputActivity extends AppCompatActivity {
    //After Orientation changed then onCreate(Bundle savedInstanceState) will call and recreate the activity and load all data from savedInstanceState
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Retrieve the URL passed from MainActivity
        Intent intent = getIntent();
        final String link = intent.getStringExtra("URL");
        //Set OutputActivity view
        setContentView(R.layout.activity_output);
        TextView url = findViewById(R.id.url);
        Button button = findViewById(R.id.btnAction);
        try {
            url.setText(link);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To add open with pop up with options to choose browser
                Intent browserIntent = new Intent(Intent.ACTION_WEB_SEARCH);
                //Set the search string for the browser as the generated output from scanner
                browserIntent.putExtra(SearchManager.QUERY,link);
                //Start this activity
                startActivity(browserIntent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Go back to MainActivity
        Intent intent = new Intent(OutputActivity.this, MainActivity.class);
        //Clears the current Activity from the stack
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Start the activity
        startActivity(intent);
    }
}
