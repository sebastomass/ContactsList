package com.example.contactsList;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contactsList.utilities.Utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {
    ConexionSQLiteHelper conn;
    EditText nameedit;
    int userId;
    String name;
    String surname;
    int number;
    String email;
    String birthdate;
    byte[] photo;

    GetUserData mCallback;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters


    public UpdateFragment() {
        // Required empty public constructor
    }


    public interface GetUserData{
        void getUserData();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(int param1) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.userId = getArguments().getInt(ARG_PARAM1);
        }
        conn = new ConexionSQLiteHelper(getContext(), Utils.TABLA_CONTACT, null, 1);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(this.userId != 0){
            nameedit = getView().findViewById(R.id.nameInputUpdate);
            updateUser(this.userId);
        }else{
            this.getView().setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false);
    }

    public void updateUser(int id) {
        this.getView().setVisibility(View.VISIBLE);
        this.userId = id;
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utils.TABLA_CONTACT + " WHERE " + Utils.CAMPO_ID + " = " + id, null);
        cursor.moveToFirst();
        this.name = cursor.getString(cursor.getColumnIndex(Utils.CAMPO_NAME));
        this.surname = cursor.getString(cursor.getColumnIndex(Utils.CAMPO_SURNAME));
        this.number = cursor.getInt(cursor.getColumnIndex(Utils.CAMPO_PHONENUMBER));
        this.birthdate = cursor.getString(cursor.getColumnIndex(Utils.CAMPO_BIRTHDATE));
        this.email = cursor.getString(cursor.getColumnIndex(Utils.CAMPO_EMAIL));
        this.photo = cursor.getBlob(cursor.getColumnIndex(Utils.CAMPO_PHOTO));
        Bitmap bm = BitmapFactory.decodeByteArray(this.photo, 0 ,this.photo.length);
        ImageView image = (ImageView) getView().findViewById(R.id.userPicture);
        image.setImageBitmap(bm);
        EditText nameInput = getView().findViewById(R.id.nameInputUpdate);
        EditText surnameInput = getView().findViewById(R.id.surnameInputUpdate);
        EditText phoneInput = getView().findViewById(R.id.phoneInputUpdate);
        EditText emailInput = getView().findViewById(R.id.emailInputUpdate);
        EditText birthdateInput = getView().findViewById(R.id.birthdateInputUpdate);
        nameInput.setText(this.name);
        surnameInput.setText(this.surname);
        phoneInput.setText(""+this.number);
        emailInput.setText(this.email);
        birthdateInput.setText(this.birthdate);
        birthdateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView date = v.findViewById(R.id.birthdateInputUpdate);
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = day + " / " + (month+1) + " / " + year;
                        date.setText(selectedDate);
                    }
                });

                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        Button updateButton = getView().findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyUser();
            }
        });
    }

    public void modifyUser(){
        ContentValues cv = new ContentValues();
        EditText name = getView().findViewById(R.id.nameInputUpdate);
        cv.put(Utils.CAMPO_NAME, name.getText().toString());
        EditText surname = getView().findViewById(R.id.surnameInputUpdate);
        cv.put(Utils.CAMPO_SURNAME, surname.getText().toString());
        EditText number = getView().findViewById(R.id.phoneInputUpdate);
        cv.put(Utils.CAMPO_PHONENUMBER, number.getText().toString());
        EditText email = getView().findViewById(R.id.emailInputUpdate);
        cv.put(Utils.CAMPO_EMAIL, email.getText().toString());
        EditText birth = getView().findViewById(R.id.birthdateInputUpdate);
        cv.put(Utils.CAMPO_BIRTHDATE, birth.getText().toString());
        SQLiteDatabase db = conn.getWritableDatabase();
        db.update(Utils.TABLA_CONTACT, cv, "id="+this.userId, null);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }






}