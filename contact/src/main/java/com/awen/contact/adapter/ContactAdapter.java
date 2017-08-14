package com.awen.contact.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awen.contact.R;
import com.awen.contact.model.ContactsInfo;
import com.awen.contact.model.ContactsPickerHelper;
import com.awen.contact.view.ContactSelectorActivity;
import com.awen.contact.widget.GlideCircleTransform;
import com.awen.contact.widget.RBaseViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by AwenZeng on 2017/2/16.
 */

public class ContactAdapter extends RModelAdapter<ContactsInfo> {

    private CheckBox checkBox;
    private LinearLayout itemLayout;
    private ImageView contactImg;
    private TextView nameTv;
    private TextView phoneTv;

    public ContactAdapter(Context context) {
        super(context);
    }

    public ContactAdapter(Context context, List<ContactsInfo> datas) {
        super(context, datas);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_contacts_layout;
    }

    @Override
    protected void onBindCommonView(final RBaseViewHolder holder, final int position, final ContactsInfo bean) {
        checkBox = holder.getView(R.id.checkbox);
        itemLayout = holder.getView(R.id.item_layout);
        contactImg = holder.getView(R.id.image_view);
        nameTv = holder.getView(R.id.name_view);
        phoneTv = holder.getView(R.id.phone_view);
        if (getModel() == MODEL_SINGLE) {
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.VISIBLE);
        }
        nameTv.setText(bean.getName());
        phoneTv.setText(bean.getPhone());
        Glide.with(mContext)
                .load(ContactsPickerHelper.getPhotoByte(mContext, bean.getContactId()))
                .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.man)
                .into(contactImg);

        itemLayout.setTag(checkBox);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getModel() == MODEL_SINGLE) {
                    String temp = bean.getPhone();
                    if (temp.contains(" ")) {
                        temp = temp.replaceAll("\\s*", "");
                    } else if (temp.contains("-")) {
                        temp = temp.replaceAll("-", "");
                    }
                    bean.setPhone(temp);
                    ((ContactSelectorActivity) mContext).onSelectDone(bean);
                } else {
                    setSelectorPosition(position, (CheckBox)v.getTag());
                }
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectorPosition(position);
            }
        });
    }

    @Override
    protected void onBindModelView(int model, boolean isSelector, RBaseViewHolder holder, int position, ContactsInfo bean) {
        ((CompoundButton) checkBox).setChecked(isSelector);
    }

    @Override
    protected void onBindNormalView(RBaseViewHolder holder, int position, ContactsInfo bean) {

    }
}
