package com.huangdali.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdl.hricheditorview.R;
import com.huangdali.bean.EContent;
import com.huangdali.bean.RichEditorAdapter;
import com.huangdali.editweb.ImageUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 富文本原生页面 主要页面
 */
public class RichEditorActivity extends Activity implements View.OnClickListener {
    Activity mContext;
    /**
     * 整形区
     */
    private static final int REQUEST_CODE_CHOOSE_BG = 1001;//选择背景
    private static final int REQUEST_CODE_SET_TITLE = 1003;//设置标题
    /**
     * 字符区
     */
    private String articleTitle;

    /**
     * 组件区
     */
    private RecyclerView rv_List;
    private LinearLayoutManager linearLayoutManager;

    private RichEditorAdapter mAdapter;
    private TextView tvArtTitle;

    private ImageView ivArtBGImg;

    /**
     * 数据区
     */
    private List<EContent> mDatas;

    private Uri bgUri;//背景图片的uri


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_richeditor);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        tvArtTitle = (TextView) findViewById(R.id.tv_richeditor_title);
        ivArtBGImg = (ImageView) findViewById(R.id.iv_richeditor_bg);
        /**
         * 初始化RecyclerView
         */
        rv_List = (RecyclerView) findViewById(R.id.rv_itemlist);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_List.setLayoutManager(linearLayoutManager);
        rv_List.setItemAnimator(new DefaultItemAnimator());
        mDatas = new ArrayList<>();
//        rvItemList.setHasFixedSize(true);//最重要的这句，不然recycleview不显示
        mAdapter = new RichEditorAdapter(this, mDatas);
        rv_List.setAdapter(mAdapter);
    }


    /**
     * 更换背景
     *
     * @param view
     */
    public void onChangeBG(View view) {
        ImageUtils.showcamrae(this, 1, REQUEST_CODE_CHOOSE_BG);
    }


    /**
     * 设置标题
     *
     * @param view
     */
    public void onSetTitle(View view) {
        startActivityForResult(new Intent(this, TitleEidtorActivity.class).putExtra("title", TextUtils.isEmpty(articleTitle) ? "" : articleTitle), REQUEST_CODE_SET_TITLE);
    }

    /**
     * 页面跳转回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_BG && resultCode == RESULT_OK) {//选择背景

        } else if (requestCode == REQUEST_CODE_SET_TITLE && resultCode == REQUEST_CODE_SET_TITLE) {//设置标题回调
            articleTitle = data.getStringExtra("title");//记录文章标题
            tvArtTitle.setText(articleTitle);
        } else {
            if (mAdapter != null) {
                mAdapter.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
    }
}
