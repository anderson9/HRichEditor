package com.huangdali.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hdl.hricheditorview.R;
import com.huangdali.editweb.ARichEditor;

/**
 * -------------------------------------------
 * Dec:文本编辑
 * Created by: Luojiusan on 2018/5/30--:11:26
 * Modify by:
 * -------------------------------------------
 **/
public class RichEditTextActivity extends AppCompatActivity {
    ARichEditor mRichEditor;
    RelativeLayout rl_layout;
    private String mHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_richedittext);
        mHtml = getIntent().getStringExtra("html");
        initView();
    }

    private void initView() {
        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
        mRichEditor = new ARichEditor(this, rl_layout,mHtml);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mRichEditor.onActivityResult(requestCode, resultCode, data);
    }

    public void onSubmit(View view) {
        if (TextUtils.isEmpty(mRichEditor.getHtml())) {
            Toast.makeText(this, getString(R.string.title_dont_null), Toast.LENGTH_SHORT);
            return;
        }
        Intent data = new Intent();
        data.putExtra("html", mRichEditor.getHtml());
        this.setResult(Activity.RESULT_OK, data);
        mRichEditor.onDestroy();
        finish();
    }
}
