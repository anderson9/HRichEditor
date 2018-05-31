package com.hdl.hricheditor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hdl.hricheditor.runtimepermissions.PermissionsManager;
import com.hdl.hricheditor.runtimepermissions.PermissionsResultAction;
import com.huangdali.view.RichEditorActivity;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_EDIT = 192;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", "onCreate(MainActivity.java:16)");
        requestPermission();
    }

    public void onStart(View view) {
        startActivityForResult(new Intent(this, RichEditorActivity.class), REQUEST_CODE_EDIT);
    }

    /**
     * android6.0动态权限申请
     */
    private void requestPermission() {

        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
        }
    }

}
