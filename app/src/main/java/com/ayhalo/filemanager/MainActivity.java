package com.ayhalo.filemanager;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ayhalo.filemanager.adapter.FileAdapter;
import com.ayhalo.filemanager.base.FileBean;
import com.ayhalo.filemanager.base.FileType;
import com.ayhalo.filemanager.utils.FileUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE_WRITE_EXTERNAL_STORAGE =100;
    private ListView mListView;
    private String rootPath;
    private List<FileBean> beanList = new ArrayList<>();
    private FileAdapter adapter;
    private File rootFile;
    private LinearLayout empty_rel;
    private List<String> pathList = new ArrayList<>();
    private String TAG = "halo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new FileAdapter(this,R.layout.item_list_file, beanList);
        mListView = (ListView) findViewById(R.id.list_item);
        mListView.setAdapter(adapter);
        empty_rel = (LinearLayout) findViewById(R.id.empty_rel);
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("rootpath",rootPath);
        pathList.add(rootPath);
        // 先判断是否有权限。
        if(AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE )) {
            // 有权限，直接do anything.
            getFile(rootPath);
        } else {
            //申请权限。
            AndPermission.with(this)
                    .requestCode(PERMISSION_CODE_WRITE_EXTERNAL_STORAGE)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE )
                    .send();
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FileBean file = beanList.get(position);
                    FileType fileType = file.getFileType() ;
                    if ( fileType == FileType.directory) {
                        pathList.add(file.getPath());
                        getFile(file.getPath());
                    }else if ( fileType == FileType.txt ){
                        FileUtil.openTextIntent( MainActivity.this , file.getPath() );
                    }else {
                        Toast.makeText(getApplicationContext(),"不支持此类文件",Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (pathList.size() == 1) {
                Log.d("pathlist",pathList.get(pathList.size() - 1));
                finish();
            } else {
                pathList.remove(pathList.get(pathList.size() - 1));
                Log.d("pathlist",pathList.get(pathList.size() - 1));
                getFile(pathList.get(pathList.size() - 1));
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getFile(String path) {
        rootFile = new File(path + File.separator);
        new MyTask(rootFile).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }

    class MyTask extends AsyncTask {
        File file;

        MyTask(File file) {
            this.file = file;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            List<FileBean> fileBeenList = new ArrayList<>();
            if (file.isDirectory()) {
                File[] filesArray = file.listFiles();
                if (filesArray != null) {
                    List<File> fileList = new ArrayList<>();
                    Collections.addAll(fileList, filesArray);  //把数组转化成list
                    Collections.sort(fileList, FileUtil.comparator);  //按照名字排序
                    for (File f : fileList) {
                        if (f.isHidden()) continue;
                        FileBean fileBean = new FileBean();
                        fileBean.setName(f.getName());
                        fileBean.setPath(f.getAbsolutePath());
                        fileBean.setFileType(FileUtil.getFileType(f));
                        fileBean.setChildCount(FileUtil.getFileChildCount(f));
                        fileBean.setSize(f.length());
                        fileBeenList.add(fileBean);
                    }
                }
            }
            beanList = fileBeenList;
            return fileBeenList;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (beanList.size() > 0) {
                empty_rel.setVisibility(View.GONE);
            } else {
                empty_rel.setVisibility(View.VISIBLE);
            }
            Log.d(TAG, "onPostExecute: "+beanList.size());
            adapter.refresh(beanList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if(requestCode == PERMISSION_CODE_WRITE_EXTERNAL_STORAGE ) {
                getFile(rootPath);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            AndPermission.defaultSettingDialog( MainActivity.this, PERMISSION_CODE_WRITE_EXTERNAL_STORAGE )
                    .setTitle("权限申请失败")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();
        }
    };
}

