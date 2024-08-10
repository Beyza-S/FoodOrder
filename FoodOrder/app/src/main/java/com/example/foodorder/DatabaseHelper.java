package com.example.foodorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "foodorder.db";
    private static final int DATABASE_VERSION = 3; // Veritabanı versiyonunu artırdık
    private static final String TABLE_NAME_USERS = "users";
    private static final String TABLE_NAME_PRODUCTS = "products";
    private static final String TABLE_NAME_CART = "cart";
    private static final String COL_USER_ID = "ID";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_NAME_SURNAME = "NAME_SURNAME";
    private static final String COL_USERNAME = "USERNAME";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_PRODUCT_ID = "ID";
    private static final String COL_PRODUCT_NAME = "NAME";
    private static final String COL_PRODUCT_PRICE = "PRICE";
    private static final String COL_PRODUCT_DESCRIPTION = "DESCRIPTION";
    private static final String COL_PRODUCT_IMAGE = "IMAGE";
    private static final String COL_CART_ID = "ID";
    private static final String COL_CART_PRODUCT_ID = "PRODUCT_ID";
    private static final String COL_CART_QUANTITY = "QUANTITY";
    private static final String COL_CART_USER_ID = "user_id"; // Yeni sütun

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT, " +
                COL_NAME_SURNAME + " TEXT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_NAME_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_PRODUCT_PRICE + " REAL, " +
                COL_PRODUCT_DESCRIPTION + " TEXT, " +
                COL_PRODUCT_IMAGE + " BLOB)");

        db.execSQL("CREATE TABLE " + TABLE_NAME_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_PRODUCT_ID + " INTEGER, " +
                COL_CART_QUANTITY + " INTEGER, " +
                COL_CART_USER_ID + " INTEGER, " + // Kullanıcı ID'si eklendi
                "FOREIGN KEY(" + COL_CART_PRODUCT_ID + ") REFERENCES " + TABLE_NAME_PRODUCTS + "(" + COL_PRODUCT_ID + "))");

        insertSampleProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CART);
        onCreate(db);
    }

    public boolean insertUser(String email, String nameSurname, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_NAME_SURNAME, nameSurname);
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_NAME_USERS, null, contentValues);
        if (result != -1) {
            clearCart(getUserIdByUsername(username));
        }
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USER_ID + " FROM " + TABLE_NAME_USERS + " WHERE " + COL_USERNAME + "=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            cursor.close();
            return userId;
        }
        cursor.close();
        return -1;
    }

    public boolean insertProduct(String name, double price, String description, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PRODUCT_NAME, name);
        contentValues.put(COL_PRODUCT_PRICE, price);
        contentValues.put(COL_PRODUCT_DESCRIPTION, description);
        contentValues.put(COL_PRODUCT_IMAGE, image);
        long result = db.insert(TABLE_NAME_PRODUCTS, null, contentValues);
        return result != -1;
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_PRODUCTS, null);
    }

    public boolean addToCart(int productId, int userId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CART_PRODUCT_ID, productId);
        contentValues.put(COL_CART_USER_ID, userId); // Kullanıcı ID'si eklendi
        contentValues.put(COL_CART_QUANTITY, quantity);
        long result = db.insert(TABLE_NAME_CART, null, contentValues);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert into cart");
        }
        return result != -1;
    }

    public Cursor getCartItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT cart." + COL_CART_ID + ", cart." + COL_CART_QUANTITY + ", products." + COL_PRODUCT_NAME + ", products." + COL_PRODUCT_PRICE + ", products." + COL_PRODUCT_DESCRIPTION + ", products." + COL_PRODUCT_IMAGE +
                " FROM " + TABLE_NAME_CART + " AS cart" +
                " INNER JOIN " + TABLE_NAME_PRODUCTS + " AS products" +
                " ON cart." + COL_CART_PRODUCT_ID + " = products." + COL_PRODUCT_ID +
                " WHERE cart." + COL_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CART, COL_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public boolean deleteCartItem(int cartId, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_CART, COL_CART_ID + "=? AND " + COL_CART_USER_ID + "=?", new String[]{String.valueOf(cartId), String.valueOf(userId)}) > 0;
    }

    public Cursor getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_PRODUCTS + " WHERE " + COL_PRODUCT_ID + "=?", new String[]{String.valueOf(id)});
    }
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS + " WHERE " + COL_EMAIL + "=?", new String[]{email});
    }


    private void insertSampleProducts(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();

        // Örnek ürünler
        String[] productNames = {
                "Cheeseburger", "Double Cheeseburger", "Bacon Burger", "Veggie Burger", "Chicken Burger",
                "Fish Burger", "Spicy Burger", "BBQ Burger", "Mushroom Burger", "Classic Burger", "Water", "Peach Icetea", "Cola", "Mineral Water", "Lemon Icetea"
        };
        double[] productPrices = {5.99, 7.99, 6.99, 5.49, 6.49, 5.99, 6.29, 7.49, 6.79, 4.99, 1.99, 2.99, 2.55, 1.50, 2.99};
        String productDescription = "Delicious burger with fresh ingredients.";

        for (int i = 0; i < productNames.length; i++) {
            contentValues.put(COL_PRODUCT_NAME, productNames[i]);
            contentValues.put(COL_PRODUCT_PRICE, productPrices[i]);
            contentValues.put(COL_PRODUCT_DESCRIPTION, productDescription);

            // Ürün adıyla eşleşen görsel dosyasını byte dizisine dönüştürme
            String drawableName = productNames[i].toLowerCase().replace(" ", "_");
            int imageResource = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            if (imageResource != 0) { // Eğer kaynak bulunursa
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // JPG formatında sıkıştırma
                byte[] imageBytes = stream.toByteArray();
                contentValues.put(COL_PRODUCT_IMAGE, imageBytes);
            }

            db.insert(TABLE_NAME_PRODUCTS, null, contentValues);
        }
    }
}