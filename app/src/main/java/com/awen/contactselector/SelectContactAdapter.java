package com.awen.contactselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awen.contact.model.ContactsInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/11.
 */

public class SelectContactAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ContactsInfo> mListData;

    public SelectContactAdapter(Context context) {
        mContext = context;
    }



    @Override
    public int getCount() {
        return mListData==null?0:mListData.size();
    }

    @Override
    public ContactsInfo getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_contact, null);
        }
        ContactsInfo contactsInfos = mListData.get(position);
        textView = (TextView)convertView.findViewById(R.id.itemContentTv);
        textView.setText(String.format("姓名：%s\n电话：%s",contactsInfos.getName(),contactsInfos.getPhone()));
        return convertView;
    }

    public ArrayList<ContactsInfo> getmListData() {
        return mListData;
    }

    public void setmListData(ArrayList<ContactsInfo> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }
}
