# ApkGet
检测手机中的所有APP，导出指定的APP的apk文件

1. 通过PackageManager获取设备中已安装的应用信息，这里主要用到应用名、icon和apk路径。
2. 将apk信息显示到ListView中，当点击item时将对应的apk文件导出到指定路径下。

``` java
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
```
