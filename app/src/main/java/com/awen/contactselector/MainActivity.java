package com.awen.contactselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.awen.contact.adapter.RModelAdapter;
import com.awen.contact.model.ContactsInfo;
import com.awen.contact.model.PermissionsModel;
import com.awen.contact.view.ContactSelectorActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button selectOneContactBtn;
    private Button selectMoreContactBtn;
    private ListView mListView;
    private SelectContactAdapter contactAdapter;
    private PermissionsModel mPermissionsModel;

    private static final int RESULT_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectOneContactBtn = (Button)findViewById(R.id.selectOneContactBtn);
        selectMoreContactBtn= (Button)findViewById(R.id.selectMoreContactBtn);
        mListView = (ListView)findViewById(R.id.mListView);
        mPermissionsModel = new PermissionsModel(this);
        contactAdapter = new SelectContactAdapter(this);
        contactAdapter.setmListData(new ArrayList<ContactsInfo>());
        mListView.setAdapter(contactAdapter);

        selectOneContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPermissionsModel.checkContactsPermission(new PermissionsModel.PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if(isPermission){
                            Intent intent = new Intent(MainActivity.this, ContactSelectorActivity.class);
                            intent.putExtra(ContactSelectorActivity.CHOOSE_MODE, RModelAdapter.MODEL_SINGLE);
                            startActivityForResult(intent, RESULT_CODE);
                        }
                    }
                });

            }
        });
        selectMoreContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPermissionsModel.checkContactsPermission(new PermissionsModel.PermissionListener() {
                    @Override
                    public void onPermission(boolean isPermission) {
                        if(isPermission){
                            Intent intent = new Intent(MainActivity.this, ContactSelectorActivity.class);
                            intent.putExtra(ContactSelectorActivity.CHOOSE_MODE, RModelAdapter.MODEL_MULTI);
                            startActivityForResult(intent, RESULT_CODE);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case RESULT_CODE:
                ArrayList<ContactsInfo> contactsInfos = (ArrayList<ContactsInfo>) data.getSerializableExtra(ContactSelectorActivity.REQUEST_OUTPUT);
                contactAdapter.setmListData(contactsInfos);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
