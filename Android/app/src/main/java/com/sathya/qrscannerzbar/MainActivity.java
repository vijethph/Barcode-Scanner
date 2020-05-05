package com.sathya.qrscannerzbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class MainActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    public static final int PERMISSION_REQUEST = 200;
    private ZBarScannerView mScannerView;
    @Override
    //After Orientation changed then onCreate(Bundle savedInstanceState) will call and recreate the activity and load all data from savedInstanceState
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create a ZBarScannerView
        mScannerView = new ZBarScannerView(MainActivity.this);
        //Accept permissions to access Camera
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        //Set the ZBarScannerView
        setContentView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        //onResume start camera and setResultHandler
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    public void onPause() {
        super.onPause();
        //onPause stop camera
        mScannerView.stopCamera();
    }
    @Override
    public void handleResult(Result rawResult) {
        //Intent is used to transfer data between activities.
        Intent intent = new Intent(MainActivity.this,OutputActivity.class);
        intent.putExtra("URL",rawResult.getContents());
        //Start a new activity called OutputActivity
        startActivity(intent);
    }
}
