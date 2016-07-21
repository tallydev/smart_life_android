package com.tallty.smart_life_android.custom;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kang on 16/6/23.
 * 实现RecycleView 的点击事件
 * RecyclerView实例调用addOnItemTouchListener(new 本类)即可
 */
public abstract class RecyclerVIewItemTouchListener implements RecyclerView.OnItemTouchListener{
    private GestureDetectorCompat gestureDetectorCompat;
    private RecyclerView recyclerView;
    private RecyclerView.ViewHolder VH;
    private View child;
    private int position;

    protected RecyclerVIewItemTouchListener(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        gestureDetectorCompat = new GestureDetectorCompat(recyclerView.getContext(),new MyGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * 暴露给外部调用的抽象方法
     * 点击事件
     * @param vh
     */
    public abstract void onItemClick(RecyclerView.ViewHolder vh, int position);

    /**
     * 长按事件
     * @param vh
     */
    public abstract void onItemLongPress(RecyclerView.ViewHolder vh, int position);


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                VH = recyclerView.getChildViewHolder(child);
                position = recyclerView.getChildAdapterPosition(child);
                onItemClick(VH, position);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                VH = recyclerView.getChildViewHolder(child);
                position = recyclerView.getChildAdapterPosition(child);
                onItemLongPress(VH, position);
            }
        }
    }
}
