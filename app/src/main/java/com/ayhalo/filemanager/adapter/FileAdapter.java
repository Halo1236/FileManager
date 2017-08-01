package com.ayhalo.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayhalo.filemanager.utils.FileUtil;
import com.ayhalo.filemanager.R;
import com.ayhalo.filemanager.base.FileBean;
import com.ayhalo.filemanager.base.FileType;

import java.util.List;

/**
 *
 * Created by Halo on 2017/6/29.
 */

public class FileAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int resourceId;
    private Context mContext;
    private List<FileBean> list;

    public FileAdapter(Context context, int id, List<FileBean> objects) {
        resourceId = id;
        mContext = context;
        list = objects;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileBean fileBean = getItem(position);
        View view;
        FileHolder fileHolder;
        if (convertView == null) {
            view = inflater.inflate(resourceId,parent,false);
            fileHolder = new FileHolder();
            fileHolder.fileIcon = (ImageView) view.findViewById(R.id.fileIcon);
            fileHolder.fileName = (TextView) view.findViewById(R.id.fileName);
            fileHolder.fileChildCount = (TextView) view.findViewById(R.id.fileChildCount);
            fileHolder.fileSize = (TextView) view.findViewById(R.id.fileSize);
            view.setTag(fileHolder);
        }else {
            view = convertView;
            fileHolder = (FileHolder)view.getTag();
        }

        fileHolder.fileName.setText(fileBean.getName());
        FileType fileType = fileBean.getFileType();

        if (fileType == FileType.directory) {
            fileHolder.fileChildCount.setVisibility(View.VISIBLE);
            fileHolder.fileChildCount.setText(fileBean.getChildCount() + "项");

            fileHolder.fileSize.setVisibility(View.GONE);

        } else {
            fileHolder.fileChildCount.setVisibility(View.GONE);

            fileHolder.fileSize.setVisibility(View.VISIBLE);
            fileHolder.fileSize.setText(FileUtil.sizeToChange(fileBean.getSize()));

        }

        //设置图标
        if (fileType == FileType.directory) {
            fileHolder.fileIcon.setImageResource(R.drawable.file_icon_dir);
        } else if (fileType == FileType.txt) {
            fileHolder.fileIcon.setImageResource(R.drawable.file_icon_txt);
        } else {
            fileHolder.fileIcon.setImageResource(R.drawable.file_icon_other);
        }
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FileBean getItem(int positon) {
        return list.get( positon );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(List<FileBean> list){
        this.list = list;
    }
}

class FileHolder {

    ImageView fileIcon;
    TextView fileName;
    TextView fileChildCount;
    TextView fileSize;
}