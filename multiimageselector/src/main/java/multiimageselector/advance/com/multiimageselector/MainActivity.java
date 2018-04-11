package multiimageselector.advance.com.multiimageselector;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int osVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        if (osVersion>22){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                MainActivityPermissionsDispatcher.MyNeedsPermissionWithPermissionCheck(MainActivity.this);
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startMultiImage();
                }
            }else{
                startMultiImage();
            }
        }else{
            //如果SDK小于6.0则不去动态申请权限
            startMultiImage();
        }
    }

    private void startMultiImage() {
        Intent intent = new Intent(MainActivity.this,MultiImageSelectorActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"授权成功",Toast.LENGTH_SHORT).show();
    }


    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void MyNeedsPermission() {
        Toast.makeText(this, "获取存储卡权限", Toast.LENGTH_SHORT).show();
    }

    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        Log.e("gongzibiao","requestCode = " + requestCode);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            startMultiImage();
        }else{
            Toast.makeText(getApplicationContext(),"授权拒绝",Toast.LENGTH_SHORT).show();
        }
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void MyOnShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("权限测试")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//再次执行请求
                    }
                })
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void MyOnPermissionDenied() {
        Toast.makeText(this, "真的不给权限吗", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void MyOnNeverAskAgain() {
        Toast.makeText(this, "好的不问了", Toast.LENGTH_SHORT).show();
    }
}
