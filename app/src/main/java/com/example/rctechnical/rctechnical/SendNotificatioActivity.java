package com.example.rctechnical.rctechnical;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class SendNotificatioActivity extends AppCompatActivity implements View.OnClickListener {


    //This is the pic pdf code used in file chooser
    final static int PICK_PDF_CODE = 2342;

    //component init
    private EditText notificationTitleEd;
    private EditText notificationDescriptioEd;
    private EditText notificationFileNameEd;
    private Button chooseFileBtn;
    private Button sendNotificationBtn;
    private TextView notificationFileNameTv;
    private ProgressBar notficationProgressBar;
    private Uri selectedFileIntent;

    //The firebase objects for storage and database
    StorageReference mStorageReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notificatio);
        //getting firebase objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        initView();


    }

    private void initView() {

        notificationTitleEd = findViewById(R.id.activity_send_notification_title_ed);
        notificationDescriptioEd = findViewById(R.id.activity_send_notification_description_ed);
        notificationFileNameEd = findViewById(R.id.activity_send_notification_file_name_ed);
        chooseFileBtn = findViewById(R.id.activity_send_notification_choose_file_btn);
        sendNotificationBtn = findViewById(R.id.activity_send_notification_send_btn);
        notificationFileNameTv = findViewById(R.id.activity_send_notification_file_status_tv);
        notficationProgressBar = findViewById(R.id.notification_progressbar);

        chooseFileBtn.setOnClickListener(this);
        sendNotificationBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.activity_send_notification_choose_file_btn:
                Toast.makeText(this, "CHOOSE FILE", Toast.LENGTH_SHORT).show();
                getPDF();
                break;

            case R.id.activity_send_notification_send_btn:
                sendNotificationToStudent();
                break;


        }


    }

    private void sendNotificationToStudent() {
        if (selectedFileIntent != null) {

        } else {

        }
    }

    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        } //creating an intent for file chooser

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                File file = new File(data.getData().getPathSegments().toString());
                notificationFileNameTv.setText(file.getName().substring(0, file.getName().length() - 1));
                selectedFileIntent = data.getData();
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
