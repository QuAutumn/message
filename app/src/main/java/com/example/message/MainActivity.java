package com.example.message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mlist_view;
    private ContactAdapter mAdapter;
    private List<Contact> mContatcList = new ArrayList<>();
    public static final int REQ_CODE_CONTACT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mlist_view = findViewById(R.id.list_view);
        mAdapter = new ContactAdapter(MainActivity.this, R.layout.contact_item, mContatcList);
        mlist_view.setAdapter(mAdapter);
        checkContactPermission();
        mlist_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact = mContatcList.get(i);
                Intent intent = new Intent(MainActivity.this,MessageActivity.class);
                intent.putExtra("name",contact.getName());
                intent.putExtra("number",contact.getNumber());
                startActivity(intent);
            }
        });
    }

    /**
     * 检查申请联系人权限
     */
    private void checkContactPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //未获取到读取联系人权限

            //向系统申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQ_CODE_CONTACT);
        } else {
            query();
        }
    }

    private void query() {
        ContentResolver contentResolver = this.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        Log.d("MainActivity", ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString());
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Contact contact = new Contact(name,number);
                mContatcList.add(contact);
            }
            //更新数据
            mAdapter.notifyDataSetChanged();
            cursor.close();

        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //判断用户是否，同意 获取联系人授权
        if (requestCode == REQ_CODE_CONTACT && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //获取到读取联系人权限
            query();
        } else {
            Toast.makeText(this, "未获取到联系人权限", Toast.LENGTH_SHORT).show();
        }
    }
}