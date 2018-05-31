package com.huangdali.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hdl.hricheditorview.R;
import com.huangdali.bean.ItemType;
import com.huangdali.bean.OnContentItemClickListener;

/**
 * -------------------------------------------
 * Dec:选择富文本编辑 popwindow
 * Created by: Luojiusan on 2018/5/30--:10:53
 * Modify by:
 * -------------------------------------------
 **/
public class ChoiceRichEditTypePopWindow extends PopupWindow {
    Context mContext;
    View rootView;
    public LinearLayout ll_add_img;
    public LinearLayout ll_add_txt;

    public ChoiceRichEditTypePopWindow(Context context) {
        super(context);
        mContext = context;
        rootView = LayoutInflater.from(mContext).inflate(R.layout.pop_edit_choice, null);
        this.ll_add_img = (LinearLayout) rootView.findViewById(R.id.ll_add_img);
        this.ll_add_txt = (LinearLayout) rootView.findViewById(R.id.ll_add_txt);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setFocusable(true); //设置点击menu以外其他地方以及返回键退出
        setOutsideTouchable(true);
        setContentView(rootView);
        initClick();
//        initAnim();
    }

    private void initClick() {
        ll_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(ItemType.IMG);
                }
            }
        });
        ll_add_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(ItemType.TXT);
                }
            }
        });
    }

    public void showDownByView(View rightView) {
        rootView.measure(0, 0);
        int mShowMorePopupWindowWidth = -rootView.getMeasuredWidth();
        int measuredWidth = rightView.getMeasuredWidth();
        showAsDropDown(rightView, mShowMorePopupWindowWidth / 2 + measuredWidth / 2, 0);
    }
    public void showTopByView(View rightView) {
        rootView.measure(0, 0);
        int mShowMorePopupWindowWidth = -rootView.getMeasuredWidth();
        int mShowMorePopupWindowHeight = -rootView.getMeasuredHeight();
        int measuredWidth = rightView.getMeasuredWidth();
        int measuredHeight = rightView.getMeasuredHeight();
        showAsDropDown(rightView, mShowMorePopupWindowWidth / 2 + measuredWidth / 2, mShowMorePopupWindowHeight+measuredHeight);
    }

    /**
     * 设置item的单击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnContentItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnContentItemClickListener onItemClickListener;
}
