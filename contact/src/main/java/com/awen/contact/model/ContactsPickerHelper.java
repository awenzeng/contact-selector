package com.awen.contact.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

import static android.database.Cursor.FIELD_TYPE_STRING;

/**
 * Created by angcyo on 2017-01-08.
 */

public class ContactsPickerHelper {

    /**
     * 联系人头像缓存
     */
    private static Map<String, Bitmap> photoMap = new HashMap<>();
    private static Map<String, byte[]> photoMap2 = new HashMap<>();

    /**
     * 返回一个可以订阅的对象
     */
    public static Observable<List<ContactsInfo>> getContactsListObservable(final Context context) {
        return Observable.create(new Observable.OnSubscribe<List<ContactsInfo>>() {
            @Override
            public void call(Subscriber<? super List<ContactsInfo>> subscriber) {
                subscriber.onStart();
                final List<ContactsInfo> contactsList = getContactsList(context);
                if (contactsList == null) {
                    subscriber.onNext(new ArrayList<ContactsInfo>());
                } else {
                    subscriber.onNext(contactsList);
                }
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 同步返回联系人列表
     */
    public static List<ContactsInfo> getContactsList(Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{"_id"}, null, null, null);
        List<ContactsInfo> contactsInfos = new ArrayList<>();
        if (cursor != null) {
            //枚举所有联系人的id
            if (cursor.getCount() > 0) {
                int count = 0;
                if (cursor.moveToFirst()) {
                    do {
                        int contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);//获取 id 所在列的索引
                        String contactId = cursor.getString(contactIdIndex);//联系人id

                        final List<String> phones = getData(contentResolver, contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                        if (phones.isEmpty()) {
                            continue;
                        } else {
                            String name;
                            final List<String> names = getData(contentResolver, contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                            if (names.isEmpty()) {
                                name = phones.get(0);
                            } else {
                                name = names.get(0);
                            }
                            //相同联系人的不同手机号码视为不同的联系人
                            for (String phone : phones) {
                                ContactsInfo info = new ContactsInfo();
                                info.setContactId(contactId);
                                info.setName(name);
                                info.setPhone(phone);
                                info.setLetter(String.valueOf(Pinyin.toPinyin(name.charAt(0)).toUpperCase().charAt(0)));
                                contactsInfos.add(info);
                            }
                        }
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        return contactsInfos;
    }

    /**
     * 获取联系人的图片
     */
    public static Bitmap getPhoto(final Context context, String contactId) {
        Bitmap bitmap = photoMap.get(contactId);
        if (bitmap == null) {
            byte[] bytes = getPhotoByte(context, contactId);
            if (bytes != null) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                photoMap.put(contactId, bitmap);
            }
        }
        return bitmap;
    }

    public static byte[] getPhotoByte(final Context context, String contactId) {
        byte[] bytes = photoMap2.get(contactId);
        if (bytes == null || bytes.length <= 0) {
            Cursor dataCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    new String[]{"data15"},
                    ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                            + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
                    new String[]{String.valueOf(contactId)}, null);
            if (dataCursor != null) {
                if (dataCursor.getCount() > 0) {
                    dataCursor.moveToFirst();
                    bytes = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));
                    photoMap2.put(contactId, bytes);
                }
                dataCursor.close();
            }
        }
        return bytes;
    }

    /**
     * 根据MIMETYPE类型, 返回对应联系人的data1字段的数据
     */
    private static List<String> getData(final ContentResolver contentResolver, String contactId, final String mimeType) {
        List<String> dataList = new ArrayList<>();

        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.DATA1},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "='" + mimeType + "'",
                new String[]{String.valueOf(contactId)}, null);
        if (dataCursor != null) {
            if (dataCursor.getCount() > 0) {
                if (dataCursor.moveToFirst()) {
                    do {
                        final int columnIndex = dataCursor.getColumnIndex(ContactsContract.Data.DATA1);
                        final int type = dataCursor.getType(columnIndex);
                        if (type == FIELD_TYPE_STRING) {
                            final String data = dataCursor.getString(columnIndex);
                            if (!TextUtils.isEmpty(data)) {
                                dataList.add(data);
                            }
                        }
                    } while (dataCursor.moveToNext());
                }
            }
            dataCursor.close();
        }

        return dataList;
    }
}
