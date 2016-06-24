package com.tallty.smart_life_android.custom;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kang on 16/6/23.
 * 实现RecycleView 的点击事件
 */
public abstract class RecyclerVIewItemTouchListener implements RecyclerView.OnItemTouchListener{
    private GestureDetectorCompat gestureDetectorCompat;
    private RecyclerView recyclerView;

    public RecyclerVIewItemTouchListener(RecyclerView recyclerView){
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
     * @param vh
     */
    public abstract void onItemClick(RecyclerView.ViewHolder vh);


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder VH = recyclerView.getChildViewHolder(child);
                onItemClick(VH);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder VH = recyclerView.getChildViewHolder(child);
                onItemClick(VH);
            }
        }
    }
}
