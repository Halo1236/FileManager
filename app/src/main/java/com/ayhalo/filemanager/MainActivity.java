package com.ayhalo.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ayhalo.filemanager.adapter.FileAdapter;
import com.ayhalo.filemanager.base.FileBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private String rootPath;
    private List<FileBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileAdapter adapter = new FileAdapter(MainActivity.this,R.layout.item_list_file,beanList);
        mListView = (ListView)findViewById(R.id.list_item);
        mListView.setAdapter(adapter);

        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
