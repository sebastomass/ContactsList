package com.example.contactsList;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactsList.utilities.Utils;

import java.io.ByteArrayOutputStream;

public class InsertContact extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText campoName;
    EditText campoSurname;
    EditText campoNumber;
    EditText campoEmail;
    EditText campoBirthdate;
    ImageView userphoto;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_contact);
        this.campoName = findViewById(R.id.nameInputUpdate);
        this.campoSurname = findViewById(R.id.surnameInput);
        this.campoNumber = findViewById(R.id.numberInput);
        this.campoEmail = findViewById(R.id.emailInput);
        this.campoBirthdate = findViewById(R.id.birthInput);
        this.userphoto = (ImageView) findViewById(R.id.userPicture);
    }

    public void addNewProduct(View view) {
        if(this.userphoto.getDrawable() == null){
            Toast.makeText(getApplicationContext(), "You need to add a picture", Toast.LENGTH_SHORT).show();
        }else{
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, Utils.TABLA_CONTACT, null, 1);
            SQLiteDatabase db = conn.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Utils.CAMPO_NAME, campoName.getText().toString());
            values.put(Utils.CAMPO_SURNAME, campoSurname.getText().toString());
            values.put(Utils.CAMPO_PHONENUMBER, campoNumber.getText().toString());
            values.put(Utils.CAMPO_EMAIL, campoEmail.getText().toString());
            values.put(Utils.CAMPO_BIRTHDATE, campoBirthdate.getText().toString());
            BitmapDrawable drawable = (BitmapDrawable) this.userphoto.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] imageArray = bos.toByteArray();
            values.put(Utils.CAMPO_PHOTO, imageArray);
            Long idResultante = db.insert(Utils.TABLA_CONTACT, null, values);
            Toast.makeText(getApplicationContext(), "New Contact Added with id: " + idResultante, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void getPicture(View view) {
        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("asd");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.userphoto.setImageBitmap(imageBitmap);
        }
    }

    public void showDialog(View view) {
        final TextView date = this.campoBirthdate;
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                date.setText(selectedDate);
            }
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }
}
