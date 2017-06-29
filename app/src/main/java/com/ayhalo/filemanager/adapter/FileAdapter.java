package com.ayhalo.filemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayhalo.filemanager.FileUtil;
import com.ayhalo.filemanager.R;
import com.ayhalo.filemanager.base.FileBean;
import com.ayhalo.filemanager.base.FileType;

import java.util.List;

/**
 * Created by Halo on 2017/6/29.
 */

public class FileAdapter extends ArrayAdapter<FileBean> {

    private int resourceId;
    private Context mContext;

    public FileAdapter(Context context, int id, List<FileBean> objects) {
        super(context, id, objects);
        resourceId = id;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileBean fileBean = getItem(position);
        View view;
        FileHolder fileHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
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

}

class FileHolder {

    ImageView fileIcon;
    TextView fileName;
    TextView fileChildCount;
    TextView fileSize;
}