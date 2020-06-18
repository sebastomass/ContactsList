package com.example.contactsList.utilities;

public class Utils {
    public static final String TABLA_CONTACT = "products";
    public static final String CAMPO_NAME = "name";
    public static final String CAMPO_SURNAME = "surname";
    public static final String CAMPO_PHONENUMBER = "number";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_BIRTHDATE = "birthdate";
    public static final String CAMPO_PHOTO = "photo";
    public static final String CAMPO_ID = "id";
    public static final String CREATE_TABLE_CONTACT = "CREATE TABLE " + TABLA_CONTACT + "("+ CAMPO_NAME+" TEXT," + CAMPO_SURNAME +" TEXT," +  CAMPO_PHONENUMBER +" TEXT," + CAMPO_EMAIL +" TEXT," + CAMPO_BIRTHDATE +" TEXT," + CAMPO_PHOTO +" BLOB," +"id INTEGER PRIMARY KEY"+ ")";
}
