package com.awen.contact.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awen.contact.R;
import com.awen.contact.adapter.ContactAdapter;
import com.awen.contact.adapter.RModelAdapter;
import com.awen.contact.model.ContactGroupCallBack;
import com.awen.contact.model.ContactsInfo;
import com.awen.contact.model.ContactsPickerHelper;
import com.awen.contact.model.SearchContactModel;
import com.awen.contact.widget.RGroupItemDecoration;
import com.awen.contact.widget.RRecyclerView;
import com.awen.contact.widget.WaveSideBarView;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by AwenZeng on 2017/2/16.
 */

public class ContactSelectorActivity extends AppCompatActivity {

    private RRecyclerView mRecyclerView;
    private Toolbar toolbar;
    private TextView okTv;
    private EditText mSearchEdit;
    private ImageView clearImg;
    private ProgressBar mProgressBar;
    private ContactAdapter mContactAdapter;
    private WaveSideBarView waveSideBarView;
    private ArrayList<ContactsInfo> searchList;
    private List<ContactsInfo> contactsList;
    private int chooseMode = RModelAdapter.MODEL_NORMAL;
    public static final String CHOOSE_MODE = "CHOOSE_MODE";
    public final static String REQUEST_OUTPUT = "outputList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_select);
        init();
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSearchEdit = (EditText) findViewById(R.id.searchEditText);
        clearImg = (ImageView) findViewById(R.id.clearDataImg);
        mRecyclerView = (RRecyclerView) findViewById(R.id.recycler_view);
        okTv = (TextView) findViewById(R.id.okTv);
        mProgressBar = (ProgressBar)findViewById(R.id.mProgressBar);
        toolbar.setTitle("选择联系人");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chooseMode = getIntent().getIntExtra("CHOOSE_MODE",RModelAdapter.MODEL_NORMAL);
        searchList = new ArrayList<>();
        mContactAdapter = new ContactAdapter(this);
        switch (chooseMode){
            case RModelAdapter.MODEL_MULTI:
                mContactAdapter.setModel(RModelAdapter.MODEL_MULTI);//多选模式
                break;
            case RModelAdapter.MODEL_SINGLE:
                mContactAdapter.setModel(RModelAdapter.MODEL_SINGLE);//单选模式
                break;
            case RModelAdapter.MODEL_NORMAL:
                mContactAdapter.setModel(RModelAdapter.MODEL_NORMAL);//正常模式
                break;
        }
        mRecyclerView.setAdapter(mContactAdapter);
        okTv.setVisibility(mContactAdapter.getModel() == RModelAdapter.MODEL_MULTI ? View.VISIBLE : View.GONE);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectDone(mContactAdapter.getSelectorData());
            }
        });
        waveSideBarView = (WaveSideBarView) findViewById(R.id.side_bar_view);
        waveSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                scrollToLetter(letter);
            }
        });
        clearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchEdit.setText("");
            }
        });
        mRecyclerView.addItemDecoration(new RGroupItemDecoration(new ContactGroupCallBack(this, mContactAdapter)));
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchStr = mSearchEdit.getText().toString();
                searchList.clear();
                if (contactsList != null && contactsList.size() > 0) {
                    searchContacts(searchStr);
                }
                if (TextUtils.isEmpty(searchStr)) {
                    clearImg.setVisibility(View.GONE);
                    waveSideBarView.setVisibility(View.VISIBLE);
                    searchList.clear();
                    mContactAdapter.resetData(contactsList);
                } else {
                    clearImg.setVisibility(View.VISIBLE);
                    mContactAdapter.resetData(searchList);
                    if (searchList == null || searchList.size() <= 0) {
                        waveSideBarView.setVisibility(View.GONE);
                    } else {
                        waveSideBarView.setVisibility(View.VISIBLE);
                    }
                }
                mContactAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startLoading();
    }

    private void startLoading() {
        ContactsPickerHelper
                .getContactsListObservable(ContactSelectorActivity.this)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ContactsInfo>>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<ContactsInfo> contactsInfos) {
                        mContactAdapter.resetData(sort(contactsInfos));
                        contactsList = mContactAdapter.getAllDatas();
                    }
                });
    }

    /**
     * on select done
     *
     * @param contactsInfos
     */
    public void onSelectDone(List<ContactsInfo> contactsInfos) {
        ArrayList<ContactsInfo> infos = new ArrayList<>();
        for (ContactsInfo info : contactsInfos) {
            infos.add(info);
        }
        onResult(infos);
    }

    public void onSelectDone(ContactsInfo info) {
        ArrayList<ContactsInfo> contactsInfos = new ArrayList<>();
        contactsInfos.add(info);
        onResult(contactsInfos);
    }

    public void onResult(ArrayList<ContactsInfo> contactsInfos) {
        setResult(RESULT_OK, new Intent().putParcelableArrayListExtra(REQUEST_OUTPUT, contactsInfos));
        finish();
    }

    private List<ContactsInfo> sort(List<ContactsInfo> list) {
        Collections.sort(list, new Comparator<ContactsInfo>() {
            @Override
            public int compare(ContactsInfo o1, ContactsInfo o2) {
                return o1.getLetter().compareTo(o2.getLetter());
            }
        });
        return list;
    }

    /**
     * 搜索联系人
     *
     * @param searchStr
     */
    private void searchContacts(String searchStr) {
        SearchContactModel searchContactModel = new SearchContactModel();
        for (ContactsInfo info : contactsList) {
            if (searchContactModel.searchContact(searchStr,info)) {
                searchList.add(info);
            }
        }
    }

    /**
     * 列表根据字母滑动
     *
     * @param letter
     */
    private void scrollToLetter(String letter) {
        if (TextUtils.equals(letter, "#")) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
            return;
        }
        for (int i = 0; i < mContactAdapter.getAllDatas().size(); i++) {
            if (TextUtils.equals(letter, mContactAdapter.getAllDatas().get(i).getLetter())) {
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }

}

