package com.awen.contact.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by AwenZeng on 2017/2/16.
 */

public class ContactsInfo implements Parcelable {
    /**
     * 联系人的ID
     */
    private String contactId;

    /**
     * 联系人名称的首字母
     */
    private String letter;

    /**
     * 联系人显示的名称
     */
    private String name;
    /**
     * 联系人的手机号码, 有可能是多个. 同一个联系人的不同手机号码,视为多个联系人
     */
    private String phone;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(contactId);
        out.writeString(letter);
        out.writeString(name);
        out.writeString(phone);
    }

    public static final Parcelable.Creator<ContactsInfo> CREATOR = new Creator<ContactsInfo>()
    {
        @Override
        public ContactsInfo[] newArray(int size)
        {
            return new ContactsInfo[size];
        }

        @Override
        public ContactsInfo createFromParcel(Parcel in)
        {
            return new ContactsInfo(in);
        }
    };

    public ContactsInfo() {
    }

    public ContactsInfo(Parcel in)
    {
        contactId = in.readString();
        letter = in.readString();
        name = in.readString();
        phone = in.readString();
    }

    @Override
    public String toString() {
        return " [" + contactId + "]" + " [" + letter + "]" + " [" + name + "]" + " [" + phone + "]";
    }
}
