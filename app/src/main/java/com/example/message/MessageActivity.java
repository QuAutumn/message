package com.example.message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private TextView textView;
    private ListView mess_list;
    private MessageAdapter_send messageAdapter_send;
    private MessageAdapter_receive messageAdapter_receive;
    private List<Message> messagesList = new ArrayList<>();
    private EditText edit_mess;
    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;
    private Button send_mess;


    private Button take_photo;
    private ImageView picture;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess);

        Intent intent = new Intent();
        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        textView = (TextView) findViewById(R.id.text_title);
        textView.setText("收信人:"+name+" "+number);

        mess_list = (ListView) findViewById(R.id.list_mess);

        send_mess = (Button) findViewById(R.id.button_send);
        edit_mess = (EditText) findViewById(R.id.edit_mess);
        send_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageAdapter_send = new MessageAdapter_send(MessageActivity.this,R.layout.message_send_item,messagesList);
                mess_list.setAdapter(messageAdapter_send);
                String mess = edit_mess.getText().toString();
                if(!mess.equals("")){
                    messagesList.add(new Message(null,mess));
                    messageAdapter_send.notifyDataSetChanged();
                    mess_list.setSelection(messagesList.size());
                    edit_mess.setText("");
                }
            }
        });

        messageAdapter_receive = new MessageAdapter_receive(MessageActivity.this,R.layout.message_receive,messagesList);
        mess_list.setAdapter(messageAdapter_receive);
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiveFilter.setPriority(100);
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver,receiveFilter);

        //拍照点击
        take_photo = (Button) findViewById(R.id.button_photo);
        picture = (ImageView) findViewById(R.id.picture);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                File fileDir = new File(Environment.getExternalStorageDirectory(),"Pictures");
                if (!fileDir.exists()) {
                    fileDir.mkdir();
                }
                String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
                String filepath = fileDir.getAbsolutePath()+"/"+ fileName;
                Uri uri = null;
                ContentValues contentValues = new ContentValues();
                //设置文件名

                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageUri = FileProvider.getUriForFile(MessageActivity.this,"com.example.message",outputImage);
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Pictures");
                }else {
                    imageUri = Uri.fromFile(outputImage);
                    contentValues.put(MediaStore.Images.Media.DATA, filepath);
                }
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }

    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[]pdus= (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0;i < messages.length;i++){
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String address = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for (SmsMessage message : messages){
                fullMessage += message.getMessageBody();
            }
            messagesList.add(new Message(fullMessage,null));
            messageAdapter_receive.notifyDataSetChanged();
            mess_list.setSelection(messagesList.size());
        }
    }
}