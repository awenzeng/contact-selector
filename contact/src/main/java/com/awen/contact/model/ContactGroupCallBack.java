package com.awen.contact.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.View;

import com.awen.contact.adapter.ContactAdapter;
import com.awen.contact.widget.RGroupItemDecoration;

import java.util.List;

/**
 * Created by AwenZeng on 2017/2/16.
 */

public class ContactGroupCallBack implements RGroupItemDecoration.GroupCallBack {
    private Context mContext;
    private TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF = new RectF();
    private Rect rect = new Rect();
    private ContactAdapter contactAdapter;
    public ContactGroupCallBack(Context context,ContactAdapter adapter) {
        mContext = context;
        mPaint.setTextSize(mContext.getResources().getDisplayMetrics().scaledDensity * 20);
        contactAdapter = adapter;
    }

    private int dp2px(int dp) {
        return (int) (mContext.getResources().getDisplayMetrics().density) * dp;
    }

    @Override
    public int getGroupHeight() {
        return dp2px(30);
    }

    @Override
    public String getGroupText(int position) {
         return contactAdapter.getAllDatas().get(position).getLetter();
    }

    @Override
    public void onGroupDraw(Canvas canvas, View view, int position) {
        mPaint.setColor(Color.parseColor("#33969696"));
        mPaint.setTextSize(50);
        mPaint.setStrokeWidth(5);
        rectF.set(view.getLeft(), view.getTop() - getGroupHeight(), view.getRight(), view.getTop());
        canvas.drawRect(rectF, mPaint);
        mPaint.setColor(Color.parseColor("#969696"));

        final String letter = contactAdapter.getAllDatas().get(position).getLetter();
        mPaint.getTextBounds(letter, 0, letter.length(), rect);
        canvas.drawText(letter, view.getLeft() + dp2px(10), view.getTop() - (getGroupHeight() - rect.height()) / 2, mPaint);
    }

    @Override
    public void onGroupOverDraw(Canvas canvas, View view, int position, int offset) {
    }
}
