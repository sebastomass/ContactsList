package com.example.contactsList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.contactsList.entities.Contact;
import com.example.contactsList.utilities.Utils;

import java.util.ArrayList;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolderDatos> implements View.OnClickListener{
    private View.OnClickListener listener;

    ArrayList<Contact> listDatos;
    private Context context;
    private ClickListener clickListener;
    private Contact mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public AdapterData(ArrayList<Contact> listDatos, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.listDatos = listDatos;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = listDatos.get(position);
        mRecentlyDeletedItemPosition = position;
        listDatos.remove(position);
        notifyItemRemoved(position);
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this.context, Utils.TABLA_CONTACT, null, 1);
        conn.delete(mRecentlyDeletedItem.getId());
        Toast toast = Toast.makeText(this.context, mRecentlyDeletedItem.getName().toString()+" was deleted.", Toast.LENGTH_SHORT);
        toast.show();
//        showUndoSnackbar();
    }



    public interface ClickListener{
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist, null, false), listener;
        view.setOnClickListener(this);
        this.context = parent.getContext();
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.name.setText(listDatos.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if(this.listener != null){
            listener.onClick(v);
        }
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        ImageButton editButton;
        ImageButton deleteButton;
        ConexionSQLiteHelper conn;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
        }




        @Override
        public void onClick(View v) {
            conn = new ConexionSQLiteHelper(v.getContext(), Utils.TABLA_CONTACT, null, 1);
            if(v.getId() == deleteButton.getId()){
                Toast toast = Toast.makeText(v.getContext(), this.name.getText()+" was deleted.", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        }
    }
}
