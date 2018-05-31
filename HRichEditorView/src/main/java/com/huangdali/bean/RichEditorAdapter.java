package com.huangdali.bean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hdl.hricheditorview.R;
import com.huangdali.editweb.ImageUtils;
import com.huangdali.utils.ItemTouchHelperAdapter;
import com.huangdali.utils.SimpleItemTouchHelperCallback;
import com.huangdali.view.ChoiceRichEditTypePopWindow;
import com.huangdali.view.RichEditTextActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * -------------------------------------------
 * Dec:主要类 富文本原生 列表 适配器。。 控制数据传递 控制视图移动
 * Created by: Luojiusan on 2018/5/30--:11:26
 * Modify by:
 * -------------------------------------------
 **/
public class RichEditorAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {
    private List<EContent> mDatas;
    private Activity mContext;
    private static final int REQUEST_CODE_CHOOSE_ITEM_IMG = 1002;//更改item图片
    private static final int REQUEST_CODE_EDIT_TXT = 1005;//编辑文本
    private static final int REQUEST_CODE_NEW_TXT = 1004;//新建文本
    private static final int REQUEST_CODE_NEW_IMG = 1003;//新建图片

    private int curClickItemIndex = 0;//当前点击的item


    private boolean isDrag = false;//是否正在拖拽
    private ItemTouchHelper mTouchHelper;
    SimpleItemTouchHelperCallback mTouchHelperCallBack;
    ChoiceRichEditTypePopWindow mPop;


    public static final int TYPE_EMPTY = 2;  //空视图
    public static final int TYPE_LIST = 3;  //有数据

    /**
     * 适配器需要适配的布局
     */
    public static int mAdapterType = TYPE_EMPTY;//默认加载中

    public RichEditorAdapter(Activity Context, List<EContent> Datas) {
        this.mDatas = Datas;
        this.mContext = Context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mAdapterType == TYPE_EMPTY) {
            return TYPE_EMPTY;
        } else if (mAdapterType == TYPE_LIST) {
            return TYPE_LIST;
        } else {
            return TYPE_EMPTY;
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //创建SimpleItemTouchHelperCallback
        if (mTouchHelper == null && mTouchHelperCallBack == null) {
            mTouchHelperCallBack = new SimpleItemTouchHelperCallback(this, recyclerView);
            //用Callback构造ItemtouchHelper
            //调用ItemTouchHelper的attachToRecyclerView方法建立联系
            mTouchHelper = new ItemTouchHelper(mTouchHelperCallBack);
            mTouchHelper.attachToRecyclerView(recyclerView);
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                EmptyAddholder emptyAddholder = new EmptyAddholder(LayoutInflater.from(mContext).inflate(R.layout.item_empty_editlist, parent, false));
                return emptyAddholder;
            case TYPE_LIST:
                MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycleview_item, parent, false));
                if (mTouchHelperCallBack != null) {
                    mTouchHelperCallBack.addTouDragListner(myViewHolder);
                }
                return myViewHolder;
        }
        EmptyAddholder emptyAddholder = new EmptyAddholder(LayoutInflater.from(mContext).inflate(R.layout.item_empty_editlist, parent, false));
        return emptyAddholder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_EMPTY:
                EmptyAddholder VH = (EmptyAddholder) holder;
                VH.iv_additem_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initPop(v, 0, false);
                    }
                });
                break;
            case TYPE_LIST:
                binList((MyViewHolder) holder, position);
                break;
        }
    }

    /**
     * 绑定列表
     *
     * @param position
     */
    private void binList(final MyViewHolder holder, final int position) {
        final EContent eContent = mDatas.get(position);
        /**
         * 隐藏第一个item的上箭头和最后一个item的下箭头
         */
        if (position == 0 && !isDrag) {
            holder.iv_additem_add_top.setVisibility(View.VISIBLE);
        } else {
            holder.iv_additem_add_top.setVisibility(View.GONE);
        }
        if (!isDrag) {
            holder.iv_additem_add.setVisibility(View.VISIBLE);
        }
        //设置内容
        holder.tvDesc.setText(TextUtils.isEmpty(eContent.getContent()) ? mContext.getString(R.string.rich_click_add_txt) : StripHT(eContent.getContent()));
        /**
         * 根据类型显示item的图片
         */
        switch (eContent.getType()) {
            case ItemType.IMG:
                if (TextUtils.isEmpty(eContent.getUrl())) {
                    holder.ivPic.setImageResource(R.mipmap.img);
                } else {
                    Glide.with(mContext)
                            .load(eContent.getUrl())
                            .into(holder.ivPic);
                }
                break;
            case ItemType.TXT:
                holder.ivPic.setImageResource(R.mipmap.txt_item);
                break;
            case ItemType.VIDEO:
                holder.ivPic.setImageResource(R.mipmap.video_item);
                break;
        }
        /**
         * 选择item图片
         */
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eContent.getType().equals(ItemType.IMG)) {
                    curClickItemIndex = position;
                    toChoiseItemPic(REQUEST_CODE_CHOOSE_ITEM_IMG);
                }

            }
        });
        holder.iv_item_select.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mTouchHelper != null) {
                        mTouchHelper.startDrag(holder);
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    holder.iv_item_select.performClick();
                    onItemClear(holder);
                }
                return true;
            }
        });
        /**
         * 编辑文本
         */
        holder.tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curClickItemIndex = position;
                toTxtEditorPage(position, REQUEST_CODE_EDIT_TXT);
            }
        });
        holder.ivDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        /**
         * 添加item监听
         */
        holder.iv_additem_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPop(v, position, true);
            }
        });
        holder.iv_additem_add_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPop(v, position, false);
            }
        });
    }

    private void initPop(View v, final int position, final boolean isBottom) {
        if (mPop == null) {
            mPop = new ChoiceRichEditTypePopWindow(mContext);
        }
        mPop.setOnItemClickListener(new OnContentItemClickListener() {
            @Override
            public void onClick(String itemTyper) {
                if (isBottom) { //下标需要设置下一个item 的position
                    curClickItemIndex = position + 1;
                }
                switch (itemTyper) {
                    case ItemType.TXT:
                        toTxtEditorPage(position, REQUEST_CODE_NEW_TXT);
                        break;
                    case ItemType.IMG:
                        toChoiseItemPic(REQUEST_CODE_NEW_IMG);
                        break;
                    default:
                }
            }
        });
        if (position + 1 == mDatas.size() && isBottom) {
            mPop.showTopByView(v);
        } else {
            mPop.showDownByView(v);
        }
    }


    /**
     * 跳转到文本编辑页面
     *
     * @param index
     */
    private void toTxtEditorPage(int index, int code) {
        Intent intent = new Intent(mContext, RichEditTextActivity.class);
        Bundle bundle = new Bundle();
        if (mDatas.size() > 0 && index < mDatas.size()) {
            bundle.putString("html", mDatas.get(index).getContent());
        }
        intent.putExtras(bundle);
        mContext.startActivityForResult(intent, code);
    }

    /**
     * 更换item的图片
     */
    private void toChoiseItemPic(int code) {
        ImageUtils.showcamrae(mContext, 1, code);
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mDatas.size() && toPosition < mDatas.size()) {
            //交换数据位置
            Collections.swap(mDatas, fromPosition, toPosition);
            //刷新位置交换
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder viewHolder) {
        //当拖拽选中时设置背景
        isDrag = true;
        MyViewHolder vH = (MyViewHolder) viewHolder;
        vH.rl_item.setBackgroundResource(R.drawable.background_editor_item);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder viewHolder) {
        //拖拽结束后恢复view的状态
        isDrag = false;
        MyViewHolder vH = (MyViewHolder) viewHolder;
        vH.rl_item.setBackgroundResource(R.drawable.background_editor_item_no_stroke);

    }

    /**
     * 向上向下监听器对象
     */
    public interface OnDownUpChangeListener {

        void onDrop(View view, int postion);
    }


    @Override
    public int getItemCount() {
        if (mDatas.size() == 0) {
            mAdapterType = TYPE_EMPTY;
        } else {
            mAdapterType = TYPE_LIST;
        }

        return mDatas.size() == 0 ? 1 : mDatas.size();
    }

    /**
     * 获取当前单击的item的下标
     *
     * @return
     */
    public int getCurClickItemIndex() {
        return curClickItemIndex;
    }

    /**
     * 创建viewholder类
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements SimpleItemTouchHelperCallback.onTouchDragListner {
        View rootView;
        ImageView ivPic, ivDrop, iv_additem_add, iv_item_select, iv_additem_add_top;
        TextView tvDesc;
        RelativeLayout rl_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivPic = (ImageView) itemView.findViewById(R.id.iv_item_pic);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            iv_additem_add = (ImageView) itemView.findViewById(R.id.iv_additem_add);
            iv_additem_add_top = (ImageView) itemView.findViewById(R.id.iv_additem_add_top);
            iv_item_select = (ImageView) itemView.findViewById(R.id.iv_item_select);
            ivDrop = (ImageView) itemView.findViewById(R.id.iv_item_delete);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_item_desc);
        }

        @Override
        public void onStart() {
            if (iv_additem_add_top.getVisibility() == View.VISIBLE) {
                iv_additem_add_top.setVisibility(View.INVISIBLE);
            }
            if (iv_additem_add.getVisibility() == View.VISIBLE) {
                iv_additem_add.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onStop() {
        }
    }

    public static class EmptyAddholder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView iv_additem_add;

        public EmptyAddholder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.iv_additem_add = (ImageView) rootView.findViewById(R.id.iv_additem_add);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_ITEM_IMG && resultCode == Activity.RESULT_OK) {//编辑图片
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                for (int i = 0; i < selectList.size(); i++) {
                    mDatas.get(getCurClickItemIndex()).setUrl(selectList.get(i).getCompressPath());
                }
            }
            notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_EDIT_TXT && resultCode == Activity.RESULT_OK) {//编辑文字
            String html = data.getStringExtra("html");
            mDatas.get(getCurClickItemIndex()).setContent(html);
            notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_NEW_TXT && resultCode == Activity.RESULT_OK) {//新建文本
            String html = data.getStringExtra("html");
            EContent content = new EContent();
            content.setContent(html);
            content.setType(ItemType.TXT);
            mDatas.add(curClickItemIndex, content);
            notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_NEW_IMG && resultCode == Activity.RESULT_OK) {//新建图片
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            if (selectList != null && selectList.size() > 0) {
                // 获取返回的图片列表
                List<String> imagePaths = new ArrayList<>();
                for (int i = 0; i < selectList.size(); i++) {
                    EContent content = new EContent();
                    content.setUrl(selectList.get(i).getCompressPath());
                    content.setType(ItemType.IMG);
                    mDatas.add(curClickItemIndex + i, content);
                    notifyDataSetChanged();
                }
            }
        }
    }

    //从html中提取纯文本
    public static String StripHT(String strHtml) {
        String txtcontent = strHtml.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
        return txtcontent;
    }
}
