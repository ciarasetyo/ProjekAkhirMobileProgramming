package com.example.binusezyfoods2021;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Binus_ezyfoods";

    private static final String BRANCH_TABLE = "branch";
    private static final String MENU_TABLE = "menu";
    private static final String USER_TABLE = "user";
    private static final String CART_TABLE = "cart";
    private static final String TRANSACTION_TABLE = "transactions";
    private static final String DETAIL_TABLE = "detail";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Create table and initial data
        String[] branchName = {"Bekasi", "Kemanggisan", "Alam Sutera"};
        Double[] branchCoorX = {-6.2200771, -6.2019433, -6.2247123};
        Double[] branchCoorY = {106.997531, 106.7787429, 106.6480861};

        String createTableBranch = "CREATE TABLE " + BRANCH_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, coorx REAL, coory REAL)";
        db.execSQL(createTableBranch);

        for(int i=0; i<branchName.length; i++){
            ContentValues branchValues = new ContentValues();
            branchValues.put("name", branchName[i]);
            branchValues.put("coorx", branchCoorX[i]);
            branchValues.put("coory", branchCoorY[i]);
            db.insert(BRANCH_TABLE, null, branchValues);
        }

        String createTableUser = "CREATE TABLE " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, cash INTEGER, last_restaurant INT)";
        db.execSQL(createTableUser);

        ContentValues userValues = new ContentValues();
        userValues.put("name", "Ciara");
        userValues.put("cash", 0);
        userValues.put("last_restaurant", -1);
        db.insert(USER_TABLE, null, userValues);

        String[] menuName = {"Air Mineral", "Jus Apel", "Jus Mangga", "Jus Alpukat", "Kentang Goreng", "Nasi Hainam"};
        String[] menuCategory = {"Drinks", "Drinks", "Drinks", "Drinks", "Snacks", "Foods"};
        int[] menuPrice = {8000, 25000, 25000, 25000, 20000, 35000};
        int[][] menuStock = {{2,4,10,1,9,20},{21,12,2,9,1,14},{21,4,9,12,2,1}};
        int menuImage[] = {R.drawable.mineralwater,R.drawable.applejuice,R.drawable.mangojuice,R.drawable.avocadojuice,R.drawable.snack,R.drawable.foods};

        String createTableMenu = "CREATE TABLE " + MENU_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, branch_id INTEGER, name TEXT, category TEXT, price INTEGER, stock INTEGER, image INTEGER)";
        db.execSQL(createTableMenu);

        for(int i=0; i<branchName.length; i++){
            for(int j=0; j<menuName.length; j++) {
                ContentValues catValues = new ContentValues();
                catValues.put("branch_id", i+1);
                catValues.put("name", menuName[j]);
                catValues.put("category", menuCategory[j]);
                catValues.put("price", menuPrice[j]);
                catValues.put("stock", menuStock[i][j]);
                catValues.put("image", menuImage[j]);
                db.insert(MENU_TABLE, null, catValues);
            }
        }

        String createTableCart = "CREATE TABLE " + CART_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, menu_id INTEGER, quantity INTEGER)";
        db.execSQL(createTableCart);

        String createTableTransaction = "CREATE TABLE " + TRANSACTION_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, place TEXT)";
        db.execSQL(createTableTransaction);

        String createTableDetail = "CREATE TABLE " + DETAIL_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, trans_id INTEGER, food_id INTEGER, quantity INTEGER)";
        db.execSQL(createTableDetail);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + BRANCH_TABLE);
        onCreate(db);
    }

    public boolean addDataCart(int menu_id, int qty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("menu_id", menu_id);
        contentValues.put("quantity", qty);

        long result = db.insert(CART_TABLE, null, contentValues);

        if (result == -1){
            return false;
        } else{
            return true;
        }
    }

    public boolean addDataTransaction(String date, String place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("place", place);

        long result = db.insert(TRANSACTION_TABLE, null, contentValues);

        if (result == -1){
            return false;
        } else{
            return true;
        }
    }

    public boolean addDataDetail(int trans_id, int food_id, int qty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("trans_id", trans_id);
        contentValues.put("food_id",food_id);
        contentValues.put("quantity", qty);

        long result = db.insert(DETAIL_TABLE, null, contentValues);

        if (result == -1){
            return false;
        } else{
            return true;
        }
    }

    public Cursor getData(String TABLE_NAME){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
//
    public Cursor getItemByID(String TABLE_NAME, int idCompare){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID = '" +
                idCompare + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemCategory(int branch_id_compare, String category_compare){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + MENU_TABLE + " WHERE branch_id = '" +
                branch_id_compare + "' AND CATEGORY = '" + category_compare +"'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemDetail(int trans_id_compare){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DETAIL_TABLE + " WHERE trans_id = '" +
                trans_id_compare + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemCategorySelected(int branch_id_compare, String name_compare){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + MENU_TABLE + " WHERE branch_id = '" +
                branch_id_compare + "' AND name ='" + name_compare + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemCategorySelectedPreviousData(int menu_id_compare){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CART_TABLE + " WHERE menu_id = '" +
                menu_id_compare + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }
//
    public void updateName(String TABLE_NAME, String colChange, int newName, int idCompare){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_NAME + " SET " + colChange +
                " = '" + newName + "' WHERE ID = '" + idCompare + "'";

        db.execSQL(query);
    }
//
    public void deleteName(String TABLE_NAME, int id_delete){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE " +
                "ID = '" + id_delete + "'";

        db.execSQL(query);
    }
}
