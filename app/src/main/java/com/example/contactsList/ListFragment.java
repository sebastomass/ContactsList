package com.example.contactsList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsList.entities.Contact;
import com.example.contactsList.utilities.SwipeToDelete;
import com.example.contactsList.utilities.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements Filterable {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Contact> listContact;
    ArrayList<Contact> anotherContactList;
    RecyclerView recyclerViewProduct;
    ConexionSQLiteHelper conn;
    ItemClicked mCallback;
    AdapterData adapter;
    Filter filter;


    public AdapterData getAdapter(){
        return adapter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        conn = new ConexionSQLiteHelper(getContext(), Utils.TABLA_CONTACT, null, 1);



    }




    public interface ItemClicked{
        void sendId(int id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ItemClicked) {
            mCallback = (ItemClicked) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerViewProduct =getView().findViewById(R.id.RecyclerId);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        listContact = new ArrayList<>();
        queryProducts();

        anotherContactList = new ArrayList<>(listContact);
        this.adapter = new AdapterData(listContact, new AdapterData.ClickListener() {

            @Override
            public void onEditClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                System.out.println("asd");
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDelete(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerViewProduct);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    for(int i = 0; i < recyclerViewProduct.getChildCount(); i++){
                        recyclerViewProduct.getChildAt(i).setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    recyclerViewProduct.getChildAt(recyclerViewProduct.getChildAdapterPosition(v)).setBackgroundColor(Color.parseColor("#C8CCC8"));
                    mCallback.sendId(listContact.get(recyclerViewProduct.getChildAdapterPosition(v)).getId());
                } else {
                    Intent intent = new Intent(v.getContext(), UpdateActivity.class);
                    intent.putExtra("ADAPTER_POSITION", ""+recyclerViewProduct.getChildAdapterPosition(v));
                    v.getContext().startActivity(intent);
                }


            }
        });
        recyclerViewProduct.setAdapter(adapter);
        FloatingActionButton fab = getView().findViewById(R.id.floatingActionButton3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InsertContact.class);
                startActivity(intent);
            }
        });
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contact> filteredList = new ArrayList<>();
                if(constraint == "null" || constraint.length() == 0){
                    filteredList.addAll(anotherContactList);
                }else{
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for(Contact contact : anotherContactList){
                        if(contact.getName().toLowerCase().startsWith(filterPattern)){
                            filteredList.add(contact);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listContact.clear();
                List values = (List) results.values;
                for(int i = 0; i < values.size(); i++){
                    System.out.println(values.get(i));
                }
                listContact.addAll((List) results.values);
                adapter.notifyDataSetChanged();
            }
        };
    }



    @Override
    public Filter getFilter() {
        return this.filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    private void queryProducts() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Contact contact = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utils.TABLA_CONTACT, null);

        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setName(cursor.getString(0));
            contact.setSurname(cursor.getString(1));
            contact.setNumber(cursor.getInt(2));
            contact.setEmail(cursor.getString(3));
            contact.setBirthdate(cursor.getString(4));
            contact.setPhoto(cursor.getBlob(5));
            contact.setId(cursor.getInt(6));
            listContact.add(contact);
        }
    }

}