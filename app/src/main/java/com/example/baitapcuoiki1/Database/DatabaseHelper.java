package com.example.baitapcuoiki1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.baitapcuoiki1.R;
import com.example.baitapcuoiki1.model.Category;
import com.example.baitapcuoiki1.model.Order;
import com.example.baitapcuoiki1.model.Product;
import com.example.baitapcuoiki1.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên và phiên bản cơ sở dữ liệu
    private static final String DATABASE_NAME = "SQLite2.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng và cột của bảng user
    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "pass";
    private static final String COL_ROLE = "role";

    // Tên các bảng khác
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CART = "cart";

    private static final String TABLE_ORDERS = "orders";


    // Constructor khởi tạo helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Hàm tạo CSDL và bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng người dùng
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_EMAIL + " TEXT UNIQUE," +
                COL_PASSWORD + " TEXT,"+
                COL_ROLE + " INTEGER DEFAULT 0)";

        db.execSQL(CREATE_TABLE_USERS);

        // Tạo bảng danh mục
        String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "image INTEGER," +
                "name TEXT)";
        db.execSQL(CREATE_TABLE_CATEGORY);

        // Tạo bảng sản phẩm
        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "image INTEGER," +
                "price INTEGER," +
                "categoryId INTEGER," +
                "FOREIGN KEY(categoryId) REFERENCES " + TABLE_CATEGORY + "(id))";
        db.execSQL(CREATE_TABLE_PRODUCTS);



        String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "productId INTEGER," +
                "name TEXT," +
                "image INTEGER," +
                "price INTEGER)";
        db.execSQL(CREATE_TABLE_CART);


        String CREATE_TABLE_ORDERS = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userEmail TEXT," +  // ← thêm dòng này để phân biệt người đặt
                "productId INTEGER," +
                "name TEXT," +
                "image INTEGER," +
                "price INTEGER," +
                "orderTime TEXT)";
        db.execSQL(CREATE_TABLE_ORDERS);

        // Chèn dữ liệu mẫu ban đầu
        insertInitialCategory(db);
        insertInitialProducts(db);
        insertAdminUser(db);
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

    // Thêm một danh mục vào CSDL
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

    // Thêm một sản phẩm vào CSDL
    private void insertProduct(SQLiteDatabase db, String name, int imageResId, int price, int categoryId) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", imageResId);
        values.put("price", price);
        values.put("categoryId", categoryId);
        db.insert(TABLE_PRODUCTS, null, values);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db); // Tạo lại bảng và dữ liệu
    }

    // Kiểm tra xem email đã tồn tại chưa
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_EMAIL + "=?", new String[]{email}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Đăng ký tài khoản người dùng mới
    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra trùng email
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        // Thêm người dùng mới
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, 0);
        long result = db.insert(TABLE_USERS, null, values);


        Log.d("DB_PATH", "DB Path: " + db.getPath());
        Log.d("REGISTER_USER", "Insert result = " + result);

        return result != -1;
    }

    // Kiểm tra đăng nhập (đúng email và mật khẩu)
    public boolean checkLogin(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND pass=?", new String[]{email, pass});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }
    //LAY VAI TRO NGUOI DUNG
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


    // Xóa tài khoản
    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "email=?", new String[]{email});
    }

    // Lấy toàn bộ danh mục sản phẩm
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

    // Lấy toàn bộ sản phẩm
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

    // Lấy sản phẩm theo ID danh mục
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





    // Thêm sản phẩm vào giỏ
    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productId", product.getId());
        values.put("name", product.getName());
        values.put("image", product.getImage());
        values.put("price", product.getPrice());
        db.insert(TABLE_CART, null, values);
    }


    public List<Product> getCartItems() {
        List<Product> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);
        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow("productId")));
                p.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                p.setImage(cursor.getInt(cursor.getColumnIndexOrThrow("image")));
                p.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow("price")));
                cartItems.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    // xoas tuwngf sp
    public void removeFromCart(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "productId = ?", new String[]{String.valueOf(productId)});
        db.close();
    }



    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
    }

    // Tính tổng tiền
    public int getCartTotal() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(price) FROM cart", null);
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }
    //luu dơn hàng
    public void saveOrder(Product product, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userEmail", userEmail);  // ← thêm dòng này
        values.put("productId", product.getId());
        values.put("name", product.getName());
        values.put("image", product.getImage());
        values.put("price", product.getPrice());
        values.put("orderTime", String.valueOf(System.currentTimeMillis()));
        db.insert("orders", null, values);
    }


    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders ORDER BY orderTime DESC", null);
        if (cursor.moveToFirst()) {
            do {
                Order o = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("image")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("orderTime"))
                );
                orders.add(o);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }


    // HUY đơn hàng theo ID sản phẩm
    public void deleteOrderByProductId(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("orders", "productId = ?", new String[]{String.valueOf(productId)});
    }


    public List<Order> getOrdersByEmail(String email) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE userEmail = ? ORDER BY orderTime DESC", new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                Order o = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow("productId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("image")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("orderTime"))
                );
                orders.add(o);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }





    // Thêm sản phẩm mới
    public void insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("image", product.getImage());
        values.put("price", product.getPrice());
        values.put("categoryId", product.getCategoryId());
        db.insert("products", null, values);
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("image", product.getImage());
        values.put("price", product.getPrice());
        values.put("categoryId", product.getCategoryId());
        db.update("products", values, "id=?", new String[]{String.valueOf(product.getId())});
    }

    // Xóa sản phẩm
    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("products", "id=?", new String[]{String.valueOf(id)});
    }





}
