package com.ayhalo.filemanager.utils;

import android.content.Context;
import android.content.Intent;

import com.ayhalo.filemanager.TxtFile;
import com.ayhalo.filemanager.base.FileType;

import java.io.File;
import java.util.Comparator;


public class FileUtil {


    public static FileType getFileType(File file) {
        if (file.isDirectory()) {
            return FileType.directory;
        }
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".mp3")) {
            return FileType.music;
        }

        if (fileName.endsWith(".mp4")) {
            return FileType.video;
        }

        if (fileName.endsWith(".txt") ) {
            return FileType.txt;
        }

        if (fileName.endsWith(".zip")) {
            return FileType.zip;
        }

        if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
            return FileType.image;
        }

        if (fileName.endsWith(".apk")) {
            return FileType.apk;
        }

        return FileType.other;
    }

    public static Comparator comparator = new Comparator<File>() {
        @Override
        public int compare(File file1, File file2) {
            if (file1.isDirectory() && file2.isFile()) {
                return -1;
            } else if (file1.isFile() && file2.isDirectory()) {
                return 1;
            } else {
                return file1.getName().compareTo(file2.getName());
            }
        }
    };

    public static int getFileChildCount(File file) {
        int count = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isHidden()) continue;
                count++;
            }
        }
        return count;
    }

    public static String sizeToChange(long size) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  //字符格式化，为保留小数做准备

        double G = size * 1.0 / 1024 / 1024 / 1024;
        if (G >= 1) {
            return df.format(G) + " GB";
        }

        double M = size * 1.0 / 1024 / 1024;
        if (M >= 1) {
            return df.format(M) + " MB";
        }

        double K = size * 1.0 / 1024;
        if (K >= 1) {
            return df.format(K) + " KB";
        }

        return size + " B";
    }


    public static void openTextIntent(Context context, String path) {
        Intent intent = new Intent(context,TxtFile.class);
        intent.putExtra("path",path);
        context.startActivity(intent);
    }

}
