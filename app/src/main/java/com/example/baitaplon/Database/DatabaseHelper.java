package com.example.baitaplon.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baitaplon.R;
import com.example.baitaplon.model.Category;
import com.example.baitaplon.model.Product;
import com.example.baitaplon.model.User;
import com.example.baitaplon.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SQLite.db";
    private static final int DATABASE_VERSION = 8;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_ORDERS = "orders";

    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "pass";
    private static final String COL_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_EMAIL + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT," +
                COL_ROLE + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE_USERS);

        String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "image INTEGER," +
                "name TEXT)";
        db.execSQL(CREATE_TABLE_CATEGORY);

        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "image INTEGER," +
                "price INTEGER," +
                "categoryId INTEGER," +
                "FOREIGN KEY(categoryId) REFERENCES " + TABLE_CATEGORY + "(id))";
        db.execSQL(CREATE_TABLE_PRODUCTS);

        String CREATE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "productName TEXT," +
                "quantity INTEGER," +
                "price INTEGER," +
                "orderDate TEXT)";
        db.execSQL(CREATE_ORDERS);

        insertInitialCategory(db);
        insertInitialProducts(db);
        insertAdminUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    private void insertAdminUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, "admin@gmail.com");
        values.put(COL_PASSWORD, "admin123");
        values.put(COL_ROLE, 1);
        db.insert(TABLE_USERS, null, values);
    }

    private void insertInitialCategory(SQLiteDatabase db) {
        insertCategory(db, R.drawable.ic_mobile, "Thiết bị di động");
        insertCategory(db, R.drawable.ic_computer, "Máy tính");
        insertCategory(db, R.drawable.ic_clothing, "Quần áo");
        insertCategory(db, R.drawable.ic_snack, "Đồ ăn vặt");
    }

    private void insertCategory(SQLiteDatabase db, int imageResId, String name) {
        ContentValues values = new ContentValues();
        values.put("image", imageResId);
        values.put("name", name);
        db.insert(TABLE_CATEGORY, null, values);
    }

    private void insertInitialProducts(SQLiteDatabase db) {
        insertProduct(db, "Samsung Galaxy S23", R.drawable.mobile_samsung, 20000000, 1);
        insertProduct(db, "iPhone 14 Pro", R.drawable.mobile_iphone, 25000000, 1);
        insertProduct(db, "Laptop Dell XPS 15", R.drawable.computer_dell, 30000000, 2);
        insertProduct(db, "MacBook Pro", R.drawable.computer_macbook, 35000000, 2);
        insertProduct(db, "Áo thun nam", R.drawable.clothing_tshirt, 200000, 3);
        insertProduct(db, "Quần jean nữ", R.drawable.clothing_jeans, 300000, 3);
        insertProduct(db, "Snack khoai tây", R.drawable.snack_potato, 25000, 4);
        insertProduct(db, "Kẹo dẻo", R.drawable.snack_candy, 15000, 4);
    }

    private void insertProduct(SQLiteDatabase db, String name, int imageResId, int price, int categoryId) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", imageResId);
        values.put("price", price);
        values.put("categoryId", categoryId);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, 0);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkLogin(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND pass=?", new String[]{email, pass});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public int getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE email=?", new String[]{email});
        int role = 0;
        if (cursor.moveToFirst()) {
            role = cursor.getInt(cursor.getColumnIndexOrThrow("role"));
        }
        cursor.close();
        return role;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email, role FROM users", null);
        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(0);
                int role = cursor.getInt(1);
                list.add(new User(email, role));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, "email=?", new String[]{email});
    }
    public void deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("orders", "id=?", new String[]{String.valueOf(id)});
    }
    public int updateOrder(int id, String name, int quantity, int price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productName", name);
        values.put("quantity", quantity);
        values.put("price", price);
        values.put("orderDate", date);

        // Trả về số dòng bị ảnh hưởng (1 nếu thành công)
        return db.update("orders", values, "id=?", new String[]{String.valueOf(id)});
    }
    public void updateProduct(int id, String name, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        db.update("products", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("products", "id=?", new String[]{String.valueOf(id)});
    }




    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, image, name FROM category", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow("image"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                categories.add(new Category(id, image, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                product.setImage(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE categoryId = ?", new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                product.setImage(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")));
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void insertOrder(String name, int quantity, int price, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productName", name);
        values.put("quantity", quantity);
        values.put("price", price);
        values.put("orderDate", date);
        db.insert(TABLE_ORDERS, null, values);
    }
    public void insertProduct(String name, int image, int price, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("price", price);
        values.put("categoryId", categoryId);
        db.insert("products", null, values);
    }


    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);
        if (c.moveToFirst()) {
            do {
                Order o = new Order();
                o.setId(c.getInt(0));
                o.setProductName(c.getString(1));
                o.setQuantity(c.getInt(2));
                o.setPrice(c.getInt(3));
                o.setOrderDate(c.getString(4));
                list.add(o);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }
}
