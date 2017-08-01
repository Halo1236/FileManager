package com.ayhalo.filemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ayhalo.filemanager.base.FileTxt;

public class TxtFile extends AppCompatActivity {

    private FileTxt fileTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_file);
        String path = getIntent().getStringExtra("path");
        Log.d("TxtFile", "onCreate: "+path);
        fileTxt = (FileTxt)findViewById(R.id.fileTxt);
        fileTxt.init(path);
    }
}
