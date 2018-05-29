package com.huangdali.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.huangdali.bean.RichEditorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HDL on 2017/3/17.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private RichEditorAdapter mAdapter;
    private RecyclerView recyclerView;

    public SimpleItemTouchHelperCallback(RichEditorAdapter adapter, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mAdapter = adapter;
    }

    /**
     * 针对drag状态，滑动超过百分之多少的距离可以可以调用onMove()函数(注意哦，这里指的是onMove()函数的调用，并不是随手指移动的那个view哦)
     */
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        return 0.35f;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //允许上下左右的拖动
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;//长按启用拖拽
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; //不启用拖拽删除
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        //通过接口传递拖拽交换数据的起始位置和目标位置的ViewHolder
        mAdapter.onItemMove(source, target);
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //移动删除回调,如果不用可以不用理
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        switch (actionState) {
            case ItemTouchHelper.ACTION_STATE_IDLE:
                //空闲状态
//                if (mTouchDragListnerList != null) {
//                    for (onTouchDragListner listner : mTouchDragListnerList) {
//                        listner.onStop();
//                    }
//                }
                break;
            case ItemTouchHelper.ACTION_STATE_DRAG:
                mAdapter.onItemSelect(viewHolder);
                if (mTouchDragListnerList != null) {
                    for (onTouchDragListner listner : mTouchDragListnerList) {
                        listner.onStart();
                    }
                }
                //拖动状态
                break;
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (mAdapter != null) {
            mAdapter.setDrag(false);
            mAdapter.onItemClear(viewHolder);
            mAdapter.notifyDataSetChanged();
        }
        super.clearView(recyclerView, viewHolder);
    }


    public void addTouDragListner(onTouchDragListner mTouchDragListner) {
        if (mTouchDragListnerList == null) {
            mTouchDragListnerList = new ArrayList<>(6);
        }
        mTouchDragListnerList.add(mTouchDragListner);
    }

    /**
     * 针对swipe和drag状态，整个过程中一直会调用这个函数,随手指移动的view就是在super里面做到的(和ItemDecoration里面的onDraw()函数对应)
     */
    private List<onTouchDragListner> mTouchDragListnerList;

    /**
     * 拖拽开始与结束回调
     */
    public interface onTouchDragListner {
        void onStart();

        void onStop();
    }
}
