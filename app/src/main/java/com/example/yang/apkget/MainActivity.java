package com.example.yang.apkget;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private AppInfoAdapter adapter;
    private ArrayList<AppInfo> data;
    private String basePath = "/sdcard/Download/";
    private ProgressDialog progressDialog;

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private final int TOAST = 2;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TOAST:
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            int status = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if(status == PackageManager.PERMISSION_DENIED){
                requestPermissions(permissions, 1);
            }
        }

        listView = (ListView) findViewById(R.id.list);
        data = new ArrayList<>();
        init();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public void init(){
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        for(PackageInfo packageInfo : packages){
            ApplicationInfo app = packageInfo.applicationInfo;
            Drawable drawable = app.loadIcon(pm);
            String name = app.loadLabel(pm).toString();

            AppInfo appInfo = new AppInfo();
            appInfo.icon = drawable;
            appInfo.name = name;
            appInfo.ApkDir = app.sourceDir;
            data.add(appInfo);
        }
        adapter = new AppInfoAdapter(MainActivity.this, data);
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getApk(data.get(position));
                progressDialog.dismiss();
            }
        }).start();
        progressDialog.show();
    }

    public void getApk(AppInfo appInfo){
        try {
            String apkSrcPath = appInfo.ApkDir;
            String apkDesPath = basePath + appInfo.name + ".apk";
            File out = new File(apkDesPath);
            if(out.exists()){
                out.delete();
            }
            out.createNewFile();
            FileInputStream inputStream= new FileInputStream(apkSrcPath);
            FileOutputStream outputStream = new FileOutputStream(out);
            byte[] buffer = new byte[1024*10];
            int len;
            while((len = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, len);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            showToast("已导出至" + apkDesPath);


        } catch (IOException e) {
            showToast("IO error " + e.toString());
            e.printStackTrace();
        }
    }

    public void showToast(String s){
        Message message = Message.obtain();
        message.what = TOAST;
        message.obj = s;
        mHandler.sendMessage(message);

    }
}
