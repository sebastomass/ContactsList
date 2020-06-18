package com.example.contactsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactsList.utilities.SlidePagerAdapter;
import com.example.contactsList.utilities.Utils;
import com.example.contactsList.utilities.ZoomAnimation;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity implements UpdateFragment.GetUserData{
    String selectedItem;
    ConexionSQLiteHelper conn;
    EditText nameedit;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        this.selectedItem = getIntent().getStringExtra("ADAPTER_POSITION");
        conn = new ConexionSQLiteHelper(getApplicationContext(), Utils.TABLA_CONTACT, null, 1);
        List<Fragment> list = new ArrayList<>();
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Utils.TABLA_CONTACT, null);
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "NO DATA", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                list.add(UpdateFragment.newInstance(cursor.getInt(6)));
            }
            pager = findViewById(R.id.pager);
            pagerAdapter = new SlidePagerAdapter(getSupportFragmentManager(), list);
            pager.setAdapter(pagerAdapter);
            pager.setPageTransformer(true, new ZoomAnimation());
            pager.setCurrentItem(Integer.parseInt(this.selectedItem));
        }


    }



    @Override
    public void getUserData() {

    }
}
