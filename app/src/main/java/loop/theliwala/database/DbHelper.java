package loop.theliwala.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import loop.theliwala.models.Addresses;
import loop.theliwala.models.CacheServiceCallData;
import loop.theliwala.models.Data;
import loop.theliwala.models.MyBasket;
import loop.theliwala.models.OrderDetails;
import loop.theliwala.utilities.Contants;

/**
 * Created by lalit on 7/25/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userData");
        db.execSQL("DROP TABLE IF EXISTS GetAllAddressEntity");
        db.execSQL("DROP TABLE IF EXISTS CityDataEntity");
        db.execSQL("DROP TABLE IF EXISTS LocalityDataEntity");
        db.execSQL("DROP TABLE IF EXISTS CacheServiceCall");
        db.execSQL("DROP TABLE IF EXISTS StoreListDataEntity");
        db.execSQL("DROP TABLE IF EXISTS CategoryListDataEntity");
        db.execSQL("DROP TABLE IF EXISTS ProductListDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MyOrderDataEntity");
        db.execSQL("DROP TABLE IF EXISTS FavouriteStoresDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MyOrderHistoryDataEntity");
        db.execSQL("DROP TABLE IF EXISTS TrackOrderDataEntity");
        db.execSQL("DROP TABLE IF EXISTS SelectedStoreDataEntity");
        db.execSQL("DROP TABLE IF EXISTS MenuListDataEntity");
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_user_TABLE = "CREATE TABLE userData(LoginID INTEGER,PhoneNumber TEXT,Otp INTEGER,status INTEGER)";
        db.execSQL(CREATE_user_TABLE);
        String CREATE_getAllAddress_TABLE = "CREATE TABLE GetAllAddressEntity(AddressId INTEGER,CompleteAddress TEXT,CityId INTEGER,ZipCode TEXT,LoginID INTEGER,PhoneNumber TEXT,LandMark TEXT,LocalityId INTEGER,LocalityName TEXT)";//
        db.execSQL(CREATE_getAllAddress_TABLE);
        String CREATE_City_TABLE = "CREATE TABLE CityDataEntity(CityId INTEGER, CityName TEXT)";
        db.execSQL(CREATE_City_TABLE);
        String CREATE_Locality_TABLE = "CREATE TABLE LocalityDataEntity(localityId INTEGER, localityName TEXT,CityId INTEGER)";
        db.execSQL(CREATE_Locality_TABLE);
        String CREATE_CacheServiceCalls_TABLE = "CREATE TABLE CacheServiceCall(id INTEGER PRIMARY KEY, url TEXT, ServerRequestDateTime Text)";
        db.execSQL(CREATE_CacheServiceCalls_TABLE);
        String CREATE_StoreList_TABLE = "CREATE TABLE StoreListDataEntity(StoreId INTEGER, StoreName TEXT,StorePhoneNumber TEXT,StoreEmailId TEXT,StoreAddress TEXT,LocalityId INTEGER,StorePicturesUrl TEXT,FavouriteStore TEXT,OpeningTime TEXT,ClosingTime TEXT,StoreStatus TEXT)";
        db.execSQL(CREATE_StoreList_TABLE);
        String CREATE_CategoryList_TABLE = "CREATE TABLE CategoryListDataEntity(CategoryId INTEGER, CategoryName TEXT,StoreId INTEGER, ImageUrl TEXT,CategoryDescription TEXT)";
        //CategoryId,CategoryName,StoreId,ImageUrl,CategoryDescription CategoryListDataEntity
        db.execSQL(CREATE_CategoryList_TABLE);
        String CREATE_ProductList_TABLE = "CREATE TABLE ProductListDataEntity(ProductId INTEGER, ProductName TEXT,CategoryId INTEGER,UnitPrice REAL,Discount REAL,GST REAL,TaxType TEXT,UOM TEXT,ProductDetails TEXT,ImageUrl TEXT)";
        //ProductId,ProductName,CategoryId,UnitPrice,Discount,GST,TaxType,ProductDetails ProductListDataEntity
        db.execSQL(CREATE_ProductList_TABLE);

        String CREATE_MyOrder_TABLE = "CREATE TABLE MyOrderDataEntity(ProductId INTEGER,ProductName TEXT, StoreId INTEGER,Quantity REAL,Price REAL, OrderTime TEXT,CategoryId INTEGER,discount REAL,UOM TEXT)";
        //ProductId,ProductName,StoreId,Quantity,Price,OrderTime,CategoryId,discount  MyOrderDataEntity
        db.execSQL(CREATE_MyOrder_TABLE);
        String CREATE_FavouriteStores_TABLE = "CREATE TABLE FavouriteStoresDataEntity(StoreId INTEGER,StoreName TEXT,StorePhoneNumber TEXT,StoreEmailId TEXT,StoreAddress TEXT,LocalityId INTEGER,StorePicturesUrl TEXT,FavouriteStore TEXT,OpeningTime TEXT,ClosingTime TEXT,StoreStatus TEXT)";
        // StoreId , StoreName, StorePhoneNumber ,StoreEmailId , StoreAddress , LocalityId , StorePicturesUrl
        db.execSQL(CREATE_FavouriteStores_TABLE);
        String CREATE_MyOrderHistory_TABLE = "CREATE TABLE MyOrderHistoryDataEntity(OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,UOM TEXT,OrderDetails TEXT,StoreName TEXT)";//NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,
        db.execSQL(CREATE_MyOrderHistory_TABLE);
        String CREATE_TrackOrder_TABLE = "CREATE TABLE TrackOrderDataEntity(OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,UOM TEXT,OrderDetails TEXT,StoreName TEXT)";//UOM TEXT,
        db.execSQL(CREATE_TrackOrder_TABLE);

        String CREATE_Selected_Store_TABLE = "CREATE TABLE SelectedStoreDataEntity(StoreId INTEGER, storeName TEXT,categoryId INTEGER)";
        //StoreId,storeName SelectedStoreDataEntity
        db.execSQL(CREATE_Selected_Store_TABLE);

        String CREATE_Menu_List_TABLE = "CREATE TABLE MenuListDataEntity(MenuId INTEGER, MenuName TEXT,ImageUrl TEXT)";
        //            "MenuId","MenuName","ImageUrl"
        db.execSQL(CREATE_Menu_List_TABLE);

    }

    public boolean upsertGetAllAddress(Addresses ob) {
        boolean done = false;
        Addresses data = null;
        if (ob.getAddressId() != 0) {
            data = getAllAddressesData(ob.getAddressId());
            if (data == null) {
                done = insertGetAddressData(ob);
            } else {
                done = updateGetAddressData(ob);
            }
        }
        return done;
    }

    //GetAll Address
    private void populateGetAddressData(Cursor cursor, Addresses ob) {
        ob.setAddressId(cursor.getInt(0));
        ob.setCompleteAddress(cursor.getString(1));
        ob.setCityId(cursor.getInt(2));
        ob.setZipCode(cursor.getString(3));
        ob.setLoginID(cursor.getInt(4));
        ob.setPhoneNumber(cursor.getString(5));
        ob.setLandMark(cursor.getString(6));
        ob.setLocalityId(cursor.getInt(7));
        ob.setLocalityName(cursor.getString(8));

    }

    //show  Addresses list data
    public List<Addresses> GetAllAddressesData() {
        ArrayList list = new ArrayList<>();
        String query = "Select * FROM GetAllAddressEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Addresses ob = new Addresses();
                populateGetAddressData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //AddressId,CompleteAddress,CityId,ZipCode,LoginID,PhoneNumber,LandMark GetAllAddressEntity
    //insert GetAllAddress data
    public boolean insertGetAddressData(Addresses ob) {
        ContentValues values = new ContentValues();

        values.put("AddressId", ob.getAddressId());
        values.put("CompleteAddress", ob.getCompleteAddress());
        values.put("CityId", ob.getCityId());
        values.put("ZipCode", ob.getZipCode());
        values.put("LoginID", ob.getLoginID());
        values.put("PhoneNumber", ob.getPhoneNumber());
        values.put("LandMark", ob.getLandMark());
        values.put("LocalityId", ob.getLocalityId());
        values.put("LocalityName", ob.getLocalityName());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("GetAllAddressEntity", null, values);
        db.close();
        return i > 0;
    }


    //GetAllAddress data by id
    public Addresses getAllAddressesData(int addressId) {

        String query = "Select * FROM GetAllAddressEntity WHERE AddressId= " + addressId + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Addresses data = new Addresses();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAddressData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }


    //update GetAddAddress data
    public boolean updateGetAddressData(Addresses ob) {
        ContentValues values = new ContentValues();

        values.put("AddressId", ob.getAddressId());
        values.put("CompleteAddress", ob.getCompleteAddress());
        values.put("CityId", ob.getCityId());
        values.put("ZipCode", ob.getZipCode());
        values.put("LoginID", ob.getLoginID());
        values.put("PhoneNumber", ob.getPhoneNumber());
        values.put("LandMark", ob.getLandMark());
        values.put("LocalityId", ob.getLocalityId());
        values.put("LocalityName", ob.getLocalityName());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("GetAllAddressEntity", values, "AddressId = " + ob.getAddressId() + " ", null);

        db.close();
        return i > 0;
    }

    // delete Address Data from addressId
    public boolean deleteAddressData(int addressId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "addressId = '" + addressId + "' ";
        db.delete("GetAllAddressEntity", query, null);
        db.close();
        return result;
    }

    // delete all Address Data
    public boolean deleteAllAddressData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("GetAllAddressEntity", null, null);
        db.close();
        return result;
    }

    //--------------city data maniputate ------------------------
    //city DataEntity
    public boolean upsertCityData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getCityId() != 0) {
            data = getCityDataById(ob.getCityId());
            if (data == null) {
                done = insertCityData(ob);
            } else {
                done = updateCityData(ob);
            }
        }
        return done;
    }

    //get city data by id
    public Data getCityDataById(int id) {
        String query = "Select CityId , CityName FROM CityDataEntity WHERE CityId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data ob = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateCityData(cursor, ob);

            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    //populate state data
    private void populateCityData(Cursor cursor, Data ob) {
        ob.setCityId(cursor.getInt(0));
        ob.setCityName(cursor.getString(1));
    }

    //get all city data
    public List<Data> getAllCityData() {
        String query = "Select * FROM CityDataEntity ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<Data> list = new ArrayList<Data>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateCityData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert city data
    public boolean insertCityData(Data ob) {
        ContentValues values = new ContentValues();
        values.put("CityId", ob.getCityId());
        values.put("CityName", ob.getCityName());
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("CityDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //update city data
    public boolean updateCityData(Data ob) {
        ContentValues values = new ContentValues();
        values.put("CityId", ob.getCityId());
        values.put("CityName", ob.getCityName());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("CityDataEntity", values, "CityId = " + ob.getCityId() + " ", null);

        db.close();
        return i > 0;
    }

    public List<Data> getAllCityBySearchField(String charText, int id) {
        List<Data> list = new ArrayList<Data>();
        String query = "Select * FROM CityDataEntity WHERE  CityId=" + id + " AND (CityName like '%" + charText + "%') ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateCityData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    /////////////////Locality data


    //locality DataEntity
    public boolean upsertGetAllLocalitiesData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getLocalityId() != 0) {
            data = getAllLocalitiesDataById(ob.getLocalityId());
            if (data == null) {
                done = insertGetAllLocalitiesData(ob);
            } else {
                done = updateGetAllLocalitiesData(ob);
            }
        }
        return done;
    }

    ////////  //get locality data by id//////////////////////
    public Data getAllLocalitiesDataById(int id) {
        String query = "Select localityId , localityName ,CityId  FROM LocalityDataEntity WHERE localityId = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data ob = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAllLocalitiesData(cursor, ob);

            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    //populate state data
    private void populateGetAllLocalitiesData(Cursor cursor, Data ob) {
        ob.setLocalityId(cursor.getInt(0));
        ob.setLocalityName(cursor.getString(1));
        ob.setCityId(cursor.getInt(2));
    }

    //insert locality data
    public boolean insertGetAllLocalitiesData(Data ob) {
        ContentValues values = new ContentValues();
        values.put("localityId", ob.getLocalityId());
        values.put("localityName", ob.getLocalityName());
        values.put("CityId", ob.getCityId());
        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.insert("LocalityDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //update locality data
    public boolean updateGetAllLocalitiesData(Data ob) {
        ContentValues values = new ContentValues();
        values.put("localityId", ob.getLocalityId());
        values.put("localityName", ob.getLocalityName());
        values.put("CityId", ob.getCityId());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("LocalityDataEntity", values, "localityId = " + ob.getLocalityId() + " ", null);

        db.close();
        return i > 0;
    }

    //get all locality data
    public List<Data> getAllLocalitiesData() {
        String query = "Select * FROM LocalityDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<Data> list = new ArrayList<Data>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateGetAllLocalitiesData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    public List<Data> getAllLocalitiesBySearchField(String charText) {
        List<Data> list = new ArrayList<Data>();
        String query = "Select * FROM LocalityDataEntity  WHERE localityName like '%" + charText + "%' ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateGetAllLocalitiesData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

/////////////////////////// Cache Service DataEntity   ////////////////////

    public boolean upsertCacheServiceCallData(CacheServiceCallData cacheData) {
        boolean done = false;
        CacheServiceCallData data = null;
        if (cacheData.getServerRequestDateTime() != null) {
            data = getCacheServiceCallByUrl(cacheData.getUrl());
            if (data == null) {
                done = insertCacheServiceCallData(cacheData);
            } else {
                done = updateCacheServiceCallData(cacheData);
            }
        }
        return done;
    }

    //
//    //get cache data by url
    public CacheServiceCallData getCacheServiceCallByUrl(String url) {
        String query = "Select id , url, ServerRequestDateTime FROM CacheServiceCall WHERE url = '" + url + "' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        CacheServiceCallData ob = new CacheServiceCallData();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateCacheServiceCallData(cursor, ob);

            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    //
//    //populate cache data
    private void populateCacheServiceCallData(Cursor cursor, CacheServiceCallData ob) {
        //ob.setId(cursor.getInt(0));
        ob.setUrl(cursor.getString(1));
        ob.setServerRequestDateTime(cursor.getString(2));
    }

    //
//
//    //insert cache data
    public boolean insertCacheServiceCallData(CacheServiceCallData ob) {
        ContentValues values = new ContentValues();
        values.put("url", ob.getUrl());
        values.put("ServerRequestDateTime", ob.getServerRequestDateTime());
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("CacheServiceCall", null, values);
        db.close();
        return i > 0;
    }

    //    //update cache data
    public boolean updateCacheServiceCallData(CacheServiceCallData ob) {
        ContentValues values = new ContentValues();
        values.put("url", ob.getUrl());
        values.put("ServerRequestDateTime", ob.getServerRequestDateTime());

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("CacheServiceCall", values, " url = '" + ob.getUrl() + "' ", null);

        db.close();
        return i > 0;
    }

    //
//    //get all CacheServiceCallData data
    public List<CacheServiceCallData> getAllCacheServiceCallData() {
        String query = "Select id , url,ServerRequestDateTime  FROM CacheServiceCall ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<CacheServiceCallData> list = new ArrayList<CacheServiceCallData>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                CacheServiceCallData ob = new CacheServiceCallData();
                populateCacheServiceCallData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    // delete CacheServiceCallData Data
    public boolean deleteCacheServiceCallData() {
        boolean result = false;
        String urlCitiesList = Contants.SERVICE_BASE_URL + Contants.GET_ALL_LOCALITIES_URL;
        String urlLocalitiesList = Contants.SERVICE_BASE_URL + Contants.SELECT_CITY_URL;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "url != '" + urlCitiesList + "' AND url != '" + urlLocalitiesList + "' ";//
        db.delete("CacheServiceCall", query, null);
        db.close();
        return result;
    }

    //--------------------------GetAllStore---------------
    public boolean upsertAllStore(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getStoreId() != 0) {
            data = getStoreData(ob.getStoreId());
            if (data == null) {
                done = insertStoreData(ob);
            } else {
                done = updateStoreData(ob);
            }
        }
        return done;
    }

    //GetAll Store
    private void populateStoreData(Cursor cursor, Data ob) {
        ob.setStoreId(cursor.getInt(0));
        ob.setStoreName(cursor.getString(1));
        ob.setStorePhoneNumber(cursor.getString(2));
        ob.setStoreEmailId(cursor.getString(3));
        ob.setStoreAddress(cursor.getString(4));
        ob.setLocalityId(cursor.getInt(5));
        ob.setStorePicturesUrl(cursor.getString(6));

        boolean favorite = Boolean.parseBoolean(cursor.getString(7));
        ob.setFavouriteStore(favorite);
        ob.setOpeningTime(cursor.getString(8));
        ob.setClosingTime(cursor.getString(9));
        ob.setStoreStatus(cursor.getString(10));

    }

    //show  Store list data
    public List<Data> GetAllStoreData() {
        ArrayList<Data> list = new ArrayList<Data>();
        String query = "Select * FROM StoreListDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateStoreData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Store data
    public boolean insertStoreData(Data ob) {
        ContentValues values = new ContentValues();
        populateStoreValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("StoreListDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //Store data by id
    public Data getStoreData(int StoreId) {
        String query = "Select * FROM StoreListDataEntity WHERE StoreId= " + StoreId + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateStoreData(cursor, data);
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //StoreTiming TEXT,StoreStatus TEXT
    private void populateStoreValue(Data ob, ContentValues values) {
        values.put("StoreId", ob.getStoreId());
        values.put("StoreName", ob.getStoreName());
        values.put("StorePhoneNumber", ob.getStorePhoneNumber());
        values.put("StoreEmailId", ob.getStoreEmailId());
        values.put("StoreAddress", ob.getStoreAddress());
        values.put("LocalityId", ob.getLocalityId());
        values.put("StorePicturesUrl", ob.getStorePicturesUrl());
        values.put("FavouriteStore", String.valueOf(ob.isFavouriteStore()));
//        OpeningTime TEXT,ClosingTime TEXT,StoreStatus TEXT
        values.put("OpeningTime", String.valueOf(ob.getOpeningTime()));
        values.put("ClosingTime", String.valueOf(ob.getClosingTime()));
        values.put("StoreStatus", String.valueOf(ob.getStoreStatus()));
    }

    //update Store data
    public boolean updateStoreData(Data ob) {
        ContentValues values = new ContentValues();
        populateStoreValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("StoreListDataEntity", values, "StoreId = " + ob.getStoreId() + " ", null);

        db.close();
        return i > 0;
    }

    //update Store favority value data
    public boolean updateStoreFavorityValueData(int storeId, String favValue) {
        ContentValues values = new ContentValues();
        values.put("FavouriteStore", favValue);
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("StoreListDataEntity", values, "StoreId = " + storeId + " ", null);
        db.close();
        return i > 0;
    }

    //Store data by id
    public List<Data> getAllStoreData() {

        String query = "Select * FROM StoreListDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<Data> list = new ArrayList<Data>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateStoreData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    public boolean deleteAllStoreData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("StoreListDataEntity", null, null);
        db.close();
        return result;
    }


    //--------------------------get menu list by store---------------
    public boolean upsertMenuData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getMenuId() != 0) {
            data = getMenuData(ob.getMenuId());
            if (data == null) {
                done = insertMenuData(ob);
            } else {
                done = updateMenuData(ob);
            }
        }
        return done;
    }

    //GetAll menu
    private void populateMenuListData(Cursor cursor, Data ob) {
        ob.setMenuId(cursor.getInt(0));
        ob.setMenuName(cursor.getString(1));
        ob.setImageUrl(cursor.getString(2));
    }

    //show menu list data
    public List<Data> getMenuListData() {
        ArrayList<Data> list = new ArrayList<Data>();
        String query = "Select * FROM MenuListDataEntity";  // WHERE StoreId= " + storeId + "
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateMenuListData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert menu data
    public boolean insertMenuData(Data ob) {
        ContentValues values = new ContentValues();
        populateMenuValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.insert("MenuListDataEntity", null, values);
        db.close();
        return i > 0;
    }


    //menu data by id
    public Data getMenuData(int menuId) {
        String query = "Select * FROM MenuListDataEntity WHERE MenuId= " + menuId + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateMenuListData(cursor, data);
            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }


    private void populateMenuValue(Data ob, ContentValues values) {
        values.put("MenuId", ob.getMenuId());
        values.put("MenuName", ob.getMenuName());
        values.put("ImageUrl", ob.getImageUrl());
    }

    //update menu data
    public boolean updateMenuData(Data ob) {
        ContentValues values = new ContentValues();
        populateMenuValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("MenuListDataEntity", values, "MenuId = " + ob.getMenuId() + "", null);
        db.close();
        return i > 0;
    }

    // delete all  Data
    public boolean deleteAllMenuData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MenuListDataEntity", null, null);
        db.close();
        return result;
    }


    //--------------------------GetAllCategoryData---------------
    public boolean upsertAllCategory(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getCategoryId() != 0) {
            data = getCategoryData(ob.getCategoryId());
            if (data == null) {
                done = insertCategoryData(ob);
            } else {
                done = updateCategoryData(ob);
            }
        }
        return done;
    }

    //GetAll Category
    private void populateCategoryData(Cursor cursor, Data ob) {
        ob.setCategoryId(cursor.getInt(0));
        ob.setCategoryName(cursor.getString(1));
        ob.setStoreId(cursor.getInt(2));
        ob.setCategoryPictures(cursor.getString(3));
        ob.setCategoryDescription(cursor.getString(4));
    }

    //show  Category list data
    public List<Data> GetAllCategoryData() {
        ArrayList<Data> list = new ArrayList<Data>();
        String query = "Select * FROM CategoryListDataEntity ";//WHERE MenuId= " + menuId + "

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateCategoryData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Category data
    public boolean insertCategoryData(Data ob) {
        ContentValues values = new ContentValues();
        populateCategoryValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("CategoryListDataEntity", null, values);
        db.close();
        return i > 0;
    }


    //Category data by id
    public Data getCategoryData(int categoryId) {

        String query = "Select * FROM CategoryListDataEntity WHERE CategoryId= " + categoryId + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateCategoryData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //CategoryId,CategoryName,StoreId CategoryListDataEntity
    private void populateCategoryValue(Data ob, ContentValues values) {
        values.put("CategoryId", ob.getCategoryId());
        values.put("CategoryName", ob.getCategoryName());
        values.put("StoreId", ob.getStoreId());
        values.put("ImageUrl", ob.getCategoryPictures());
        values.put("CategoryDescription", ob.getCategoryDescription());
    }

    //update Category data
    public boolean updateCategoryData(Data ob) {
        ContentValues values = new ContentValues();
        populateCategoryValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("CategoryListDataEntity", values, "CategoryId = " + ob.getCategoryId() + "", null);

        db.close();
        return i > 0;
    }

    // delete all  Data
    public boolean deleteAllCategoryData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CategoryListDataEntity", null, null);
        db.close();
        return result;
    }


    //------------basket Order data----------------
    public boolean upsertBasketOrderData(MyBasket ob) {
        boolean done = false;
        MyBasket data = null;
        if (ob.getProductId() != 0) {
            data = getBasketOrderData(ob.getProductId());
            if (data == null) {
                done = insertBasketOrderData(ob);
            } else {
                done = updateBasketOrderData(ob);
            }
        }
        return done;
    }

    //GetAll Basket Order
    private void populateBasketOrderData(Cursor cursor, MyBasket ob) {
        ob.setProductId(cursor.getInt(0));
        ob.setProductName(cursor.getString(1));
        ob.setStoreId(cursor.getInt(2));
        ob.setQuantity(cursor.getFloat(3));
        ob.setPrice(cursor.getFloat(4));
        ob.setOrderTime(cursor.getString(5));
        ob.setCategoryId(cursor.getInt(6));
        ob.setDiscount(cursor.getFloat(7));
        ob.setUOM(cursor.getString(8));
    }

    //show  Basket Order list data
    public List<MyBasket> GetAllBasketOrderData() {
        ArrayList<MyBasket> list = new ArrayList<MyBasket>();
        String query = "Select * FROM MyOrderDataEntity ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                MyBasket ob = new MyBasket();
                populateBasketOrderData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //show  Basket Order list data
    public List<MyBasket> GetAllBasketOrderDataBasedOnStoreId(int storeId) {
        ArrayList<MyBasket> list = new ArrayList<MyBasket>();
        String query = "Select * FROM MyOrderDataEntity WHERE StoreId= " + storeId + "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                MyBasket ob = new MyBasket();
                populateBasketOrderData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Basket Order data
    public boolean insertBasketOrderData(MyBasket ob) {
        ContentValues values = new ContentValues();
        populateBasketOrderValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("MyOrderDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //Basket Order data by id
    public MyBasket getBasketOrderData(int id) {
        String query = "Select * FROM MyOrderDataEntity WHERE ProductId= " + id + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MyBasket data = new MyBasket();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateBasketOrderData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    private void populateBasketOrderValue(MyBasket ob, ContentValues values) {
        values.put("ProductId", ob.getProductId());
        values.put("ProductName", ob.getProductName());
        values.put("StoreId", ob.getStoreId());
        values.put("Quantity", ob.getQuantity());
        values.put("Price", ob.getPrice());
        values.put("OrderTime", ob.getOrderTime());
        values.put("CategoryId", ob.getCategoryId());
        values.put("discount", ob.getDiscount());
        values.put("UOM", ob.getUOM());

    }

    //ProductId,ProductName,BasketId,StoreId,Quantity,Price,OrderTime  MyOrderDataEntity
    //update Basket Order data
    public boolean updateBasketOrderData(MyBasket ob) {
        ContentValues values = new ContentValues();
        populateBasketOrderValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("MyOrderDataEntity", values, "ProductId = " + ob.getProductId() + "", null);
        db.close();
        return i > 0;
    }

    // delete Basket Order Data By Product Id ...........
    public boolean deleteBasketOrderDataByProductId(int productId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "ProductId = " + productId + " ";
        db.delete("MyOrderDataEntity", query, null);
        db.close();
        return result;
    }

    // delete Basket Order Data By Store Id ...........
    public boolean deleteBasketOrderDataByStoreId(int storeId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "StoreId = " + storeId + " ";
        db.delete("MyOrderDataEntity", query, null);
        db.close();
        return result;
    }

    // delete all  Data
    public boolean deleteAllBasketOrderData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MyOrderDataEntity", null, null);
        db.close();
        return result;
    }

    // get Basket OrderData
    public MyBasket getBasketOrderData() {

        String query = "Select * FROM MyOrderDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MyBasket data = new MyBasket();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateBasketOrderData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }
    //................ . Favourite store by user  Data.....................

    //----------------------------------- FavouriteStoresDataEntity ------------------------
    public boolean upsertFavouriteStoreData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getStoreId() != 0) {
            data = getFavouriteStoreDataByStoreId(ob.getStoreId());
            if (data == null) {
                done = insertFavouritStoreData(ob);
            } else {
                done = updateFavouriteStoreData(ob);
            }
        }
        return done;
    }


    //insert Favorite Store data
    public boolean insertFavouritStoreData(Data ob) {
        ContentValues values = new ContentValues();
        populateFavouriteStoreValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = db.insert("FavouriteStoresDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //update Favorite Store data by store id......
    public boolean updateFavouriteStoreData(Data ob) {
        ContentValues values = new ContentValues();
        populateFavouriteStoreValueData(values, ob);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("FavouriteStoresDataEntity", values, " StoreId = " + ob.getStoreId() + " ", null);

        db.close();
        return i > 0;
    }

    // StoreId , StoreName, StorePhoneNumber ,StoreEmailId , StoreAddress , StoreAddress , StorePicturesUrl
    private void populateFavouriteStoreValueData(ContentValues values, Data ob) {
        values.put("StoreId", ob.getStoreId());
        values.put("StoreName", ob.getStoreName());
        values.put("StorePhoneNumber", ob.getStorePhoneNumber());
        values.put("StoreEmailId", ob.getStoreEmailId());
        values.put("StoreAddress", ob.getStoreAddress());
        values.put("LocalityId", ob.getLocalityId());
        values.put("StorePicturesUrl", ob.getStorePicturesUrl());
        values.put("FavouriteStore", String.valueOf(ob.isFavouriteStore()));
        values.put("OpeningTime", String.valueOf(ob.getOpeningTime()));
        values.put("ClosingTime", String.valueOf(ob.getClosingTime()));
        values.put("StoreStatus", String.valueOf(ob.getStoreStatus()));
    }

    //get Favorite Store data by  store id
    public Data getFavouriteStoreDataByStoreId(int storeId) {
        String query = "Select * FROM FavouriteStoresDataEntity WHERE StoreId = " + storeId + "  ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data ob = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateFavouriteStoreData(cursor, ob);
            cursor.close();
        } else {
            ob = null;
        }
        db.close();
        return ob;
    }

    private void populateFavouriteStoreData(Cursor cursor, Data ob) {
        ob.setStoreId(cursor.getInt(0));
        ob.setStoreName(cursor.getString(1));
        ob.setStorePhoneNumber(cursor.getString(2));
        ob.setStoreEmailId(cursor.getString(3));
        ob.setStoreAddress(cursor.getString(4));
        ob.setLocalityId(cursor.getInt(5));
        ob.setStorePicturesUrl(cursor.getString(6));
        ob.setFavouriteStore(Boolean.parseBoolean(cursor.getString(7)));
        ob.setOpeningTime(cursor.getString(8));
        ob.setClosingTime(cursor.getString(9));
        ob.setStoreStatus(cursor.getString(10));


    }

    //get all favorite stores data..........
    public List<Data> getAllFavouriteStoresData() {
        String query = "Select* FROM FavouriteStoresDataEntity ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        List<Data> list = new ArrayList<Data>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateFavouriteStoreData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    //delete Fav Store data..................
    public boolean deleteFavouriteStoreData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("FavouriteStoresDataEntity", null, null);
        db.close();
        return result;
    }

    // delete deleteFavouriteStore Data By store Id
    public boolean deleteFavouriteStoreDataById(int storeId) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "StoreId = " + storeId + " ";
        db.delete("FavouriteStoresDataEntity", query, null);
        db.close();
        return result;
    }

    //----------------My order history Data-----------......................

    // upsert for my all order history data..............
    public boolean upsertMyAllOrderHistoryData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getOrderId() != 0) {
            data = getMyAllOrderHistoryDataByOrderId(ob.getOrderId());
            if (data == null) {
                done = insertMyAllOrderHistoryData(ob);
            } else {
                done = updateMyAllOrderHistoryData(ob);
            }
        }
        return done;
    }


    //Get All My Order history................
    private void populateMyAllOrderHistoryData(Cursor cursor, Data ob) {
        ob.setOrderId(cursor.getInt(0));
        ob.setOrderNumber(cursor.getString(1));
        ob.setStoreId(cursor.getInt(2));
        ob.setProductId(cursor.getInt(3));
        ob.setLoginId(cursor.getInt(4));
        ob.setQuantity(cursor.getFloat(5));
        ob.setOrderTime(cursor.getString(6));
        ob.setTotalPrice(cursor.getFloat(7));
        ob.setNetPrice(cursor.getFloat(8));
        ob.setSpecialDiscount(cursor.getFloat(9));
        ob.setSubTotal(cursor.getFloat(10));
        ob.setTotalGST(cursor.getFloat(11));
        ob.setGrandTotal(cursor.getFloat(12));
        ob.setShippingCharge(cursor.getFloat(13));
//        ob.setPromoDiscount(cursor.getFloat(14));
        ob.setOrderStatus(cursor.getString(15));
        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(16), new TypeToken<OrderDetails[]>() {
        }.getType());
        ob.setOrderDetails(orderDetailses);
        ob.setStoreName(cursor.getString(18));
//        ob.setOrderId(cursor.getInt(0));
//        ob.setOrderNumber(cursor.getString(1));
//        ob.setStoreId(cursor.getInt(2));
//        ob.setProductId(cursor.getInt(3));
//        ob.setLoginId(cursor.getInt(4));
//        ob.setQuantity(cursor.getFloat(5));
//        ob.setOrderTime(cursor.getString(6));
//        ob.setTotalPrice(cursor.getFloat(7));
//        ob.setOrderStatus(cursor.getString(8));
//        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(9), new TypeToken<OrderDetails[]>() {
//        }.getType());
//        ob.setOrderDetails(orderDetailses);
    }

    //get  Myorder history data.................
    public List<Data> GetMyAllOrderHistoryData() {

        String query = "Select * FROM MyOrderHistoryDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateMyAllOrderHistoryData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //get  Myorder history data.................
    public List<Data> GetMyAllOrderHistoryDataByOrderId(String OrderNumber) {

        String query = "Select * FROM MyOrderHistoryDataEntity WHERE  OrderNumber= '" + OrderNumber + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateMyAllOrderHistoryData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert MyOrder history data..................
    public boolean insertMyAllOrderHistoryData(Data ob) {
        ContentValues values = new ContentValues();
        populateMyAllOrderHistoryValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("MyOrderHistoryDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //MyOrder  history data by order id..................
    public Data getMyAllOrderHistoryDataByOrderId(int OrderId) {

        String query = "Select * FROM MyOrderHistoryDataEntity WHERE OrderId= " + OrderId + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateMyAllOrderHistoryData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //OrderId INTEGER,OrderNumber TEXT,StoreId INTEGER,ProductId INTEGER,LoginId INTEGER,Quantity REAL,
    // OrderTime TEXT,TotalPrice REAL,NetPrice REAL,SpecialDiscount REAL,SubTotal REAL,TotalGST REAL,
    // GrandTotal REAL,shippingCharge REAL,PromoDiscount REAL,OrderStatus TEXT,OrderDetails TEXT)";
    private void populateMyAllOrderHistoryValue(Data ob, ContentValues values) {
        values.put("OrderId", ob.getOrderId());
        values.put("OrderNumber", ob.getOrderNumber());
        values.put("StoreId", ob.getStoreId());
        values.put("ProductId", ob.getProductId());
        values.put("LoginId", ob.getLoginId());
        values.put("Quantity", ob.getQuantity());
        values.put("OrderTime", ob.getOrderTime());
        values.put("TotalPrice", ob.getTotalPrice());
        values.put("NetPrice", ob.getNetPrice());
        values.put("SpecialDiscount", ob.getSpecialDiscount());
        values.put("SubTotal", ob.getSubTotal());
        values.put("TotalGST", ob.getTotalGST());
        values.put("GrandTotal", ob.getGrandTotal());
        values.put("shippingCharge", ob.getShippingCharge());
//        values.put("PromoDiscount", ob.getPromoDiscount());
        values.put("Orderstatus", ob.getOrderStatus());
        if (ob.getOrderDetails() != null) {
            String orderDetails = new Gson().toJson(ob.getOrderDetails());
            values.put("OrderDetails", orderDetails);
        }
        values.put("StoreName", ob.getStoreName());
//        values.put("OrderId", ob.getOrderId());
//        values.put("OrderNumber", ob.getOrderNumber());
//        values.put("StoreId", ob.getStoreId());
//        values.put("ProductId", ob.getProductId());
//        values.put("LoginId", ob.getLoginId());
//        values.put("Quantity", ob.getQuantity());
//        values.put("OrderTime", ob.getOrderTime());
//        values.put("TotalPrice", ob.getTotalPrice());
//        values.put("Orderstatus", ob.getOrderStatus());
//        if (ob.getOrderDetails() != null) {
//            String orderDetails = new Gson().toJson(ob.getOrderDetails());
//            values.put("OrderDetails", orderDetails);
//        }
    }

    //update MyOrder  history data..............
    public boolean updateMyAllOrderHistoryData(Data ob) {
        ContentValues values = new ContentValues();
        populateMyAllOrderHistoryValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("MyOrderHistoryDataEntity", values, "OrderId = " + ob.getOrderId() + "", null);
        db.close();
        return i > 0;
    }

    // delete all  Data
    public boolean deleteMyAllOrderHistoryData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MyOrderHistoryDataEntity", null, null);
        db.close();
        return result;
    }
    //----------------------Get All Order By User(Track Order) Data------------------

    // -------------upsert for get all order by user Data.......................
    public boolean upsertGetAllOrderByUserData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getOrderId() != 0) {
            data = getGetAllOrderByUserDataByOrderId(ob.getOrderId());
            if (data == null) {
                done = insertGetAllOrderByUserData(ob);
            } else {
                done = updateGetAllOrderByUserData(ob);
            }
        }
        return done;
    }

    // -------------populate for get all order by user Data.......................
    private void populateGetAllOrderByUserData(Cursor cursor, Data ob) {
//        ob.setOrderId(cursor.getInt(0));
//        ob.setOrderNumber(cursor.getString(1));
//        ob.setStoreId(cursor.getInt(2));
//        ob.setProductId(cursor.getInt(3));
//        ob.setLoginId(cursor.getInt(4));
//        ob.setQuantity(cursor.getFloat(5));
//        ob.setOrderTime(cursor.getString(6));
//        ob.setTotalPrice(cursor.getFloat(7));
//        ob.setOrderStatus(cursor.getString(8));
//        ob.setStoreName(cursor.getString(9));
//        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(10), new TypeToken<OrderDetails[]>() {
//        }.getType());
//        ob.setOrderDetails(orderDetailses);
        //  ob.setUOM(cursor.getString(11));

        ob.setOrderId(cursor.getInt(0));
        ob.setOrderNumber(cursor.getString(1));
        ob.setStoreId(cursor.getInt(2));
        ob.setProductId(cursor.getInt(3));
        ob.setLoginId(cursor.getInt(4));
        ob.setQuantity(cursor.getFloat(5));
        ob.setOrderTime(cursor.getString(6));
        ob.setTotalPrice(cursor.getFloat(7));
        ob.setNetPrice(cursor.getFloat(8));
        ob.setSpecialDiscount(cursor.getFloat(9));
        ob.setSubTotal(cursor.getFloat(10));
        ob.setTotalGST(cursor.getFloat(11));
        ob.setGrandTotal(cursor.getFloat(12));
        ob.setShippingCharge(cursor.getFloat(13));
//        ob.setPromoDiscount(cursor.getFloat(14));
        ob.setOrderStatus(cursor.getString(15));
//        ob.setUOM(cursor.getString(16));
        OrderDetails[] orderDetailses = new Gson().fromJson(cursor.getString(17), new TypeToken<OrderDetails[]>() {
        }.getType());
        ob.setOrderDetails(orderDetailses);
        ob.setStoreName(cursor.getString(18));
    }

    //  Get All Order By User data..................
    public List<Data> getGetAllOrderByUserData() {

        String query = "Select * FROM TrackOrderDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<Data>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateGetAllOrderByUserData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Get All Order By User Data..................

    public boolean insertGetAllOrderByUserData(Data ob) {
        ContentValues values = new ContentValues();
        populateGetAllOrderByUserValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("TrackOrderDataEntity", null, values);
        db.close();
        return i > 0;
    }
    //get all order by user daqta by order id.............

    public Data getGetAllOrderByUserDataByOrderId(int OrderId) {

        String query = "Select * FROM TrackOrderDataEntity WHERE OrderId= " + OrderId + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAllOrderByUserData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    public Data getGetAllOrderByUserDataForStoreId() {

        String query = "Select * FROM TrackOrderDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateGetAllOrderByUserData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //get  Myorder history data.................
    public List<Data> getGetAllOrderByUserDataByOrderNumber(String OrderNumber) {
        String query = "Select * FROM TrackOrderDataEntity WHERE OrderNumber= '" + OrderNumber + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateGetAllOrderByUserData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //populate get all order by user data............
    private void populateGetAllOrderByUserValue(Data ob, ContentValues values) {
//        values.put("OrderId", ob.getOrderId());
//        values.put("OrderNumber", ob.getOrderNumber());
//        values.put("StoreId", ob.getStoreId());
//        values.put("ProductId", ob.getProductId());
//        values.put("LoginId", ob.getLoginId());
//        values.put("Quantity", ob.getQuantity());
//        values.put("OrderTime", ob.getOrderTime());
//        values.put("TotalPrice", ob.getTotalPrice());
//        values.put("Orderstatus", ob.getOrderStatus());
//        values.put("StoreName",ob.getStoreName());
//        if (ob.getOrderDetails() != null) {
//            String orderDetails = new Gson().toJson(ob.getOrderDetails());
//            values.put("OrderDetails", orderDetails);
//        }
        //  values.put("UOM", ob.getUOM());

        values.put("OrderId", ob.getOrderId());
        values.put("OrderNumber", ob.getOrderNumber());
        values.put("StoreId", ob.getStoreId());
        values.put("ProductId", ob.getProductId());
        values.put("LoginId", ob.getLoginId());
        values.put("Quantity", ob.getQuantity());
        values.put("OrderTime", ob.getOrderTime());
        values.put("TotalPrice", ob.getTotalPrice());
        values.put("NetPrice", ob.getNetPrice());
        values.put("SpecialDiscount", ob.getSpecialDiscount());
        values.put("SubTotal", ob.getSubTotal());
        values.put("TotalGST", ob.getTotalGST());
        values.put("GrandTotal", ob.getGrandTotal());
        values.put("shippingCharge", ob.getShippingCharge());
//        values.put("PromoDiscount", ob.getPromoDiscount());
        values.put("Orderstatus", ob.getOrderStatus());
//        values.put("UOM", ob.getUOM());
        if (ob.getOrderDetails() != null) {
            String orderDetails = new Gson().toJson(ob.getOrderDetails());
            values.put("OrderDetails", orderDetails);
        }
        values.put("StoreName", ob.getStoreName());
    }

    //update get all order by user data............
    public boolean updateGetAllOrderByUserData(Data ob) {
        ContentValues values = new ContentValues();
        populateGetAllOrderByUserValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("TrackOrderDataEntity", values, "OrderId = " + ob.getOrderId() + "", null);

        db.close();
        return i > 0;
    }

    // delete deleteFavouriteStore Data By store Id
    public boolean deleteGetAllOrderDataById(String OrderNumber) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "OrderNumber = '" + OrderNumber + "' ";
        db.delete("TrackOrderDataEntity", query, null);
        db.close();
        return result;
    }

    // delete all  Data
    public boolean deleteTrackOrderData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TrackOrderDataEntity", null, null);
        db.close();
        return result;
    }

    //------------selected store details-------------
    public boolean upsertSelectedStoreData(Data ob) {
        boolean done = false;
        Data data = null;
        if (ob.getStoreId() != 0) {
            data = getSelectedStoreByStoreId(ob.getStoreId());
            if (data == null) {
                done = insertSelectedStoreData(ob);
            } else {
                done = updateSelectedStoreData(ob);
            }
        }
        return done;
    }

    // -------------populate for Selected Store.......................
    private void populateSelectedStoreData(Cursor cursor, Data ob) {
        ob.setStoreId(cursor.getInt(0));
        ob.setStoreName(cursor.getString(1));
        ob.setCategoryId(cursor.getInt(2));
    }

    //  Get All Selected Store..................
    public List<Data> getAllSelectedStoreData() {

        String query = "Select * FROM SelectedStoreDataEntity";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        List<Data> list = new ArrayList<Data>();
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Data ob = new Data();
                populateSelectedStoreData(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //insert Selected Store data..................
    public boolean insertSelectedStoreData(Data ob) {
        ContentValues values = new ContentValues();
        populateSelectedStoreValue(ob, values);
        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("SelectedStoreDataEntity", null, values);
        db.close();
        return i > 0;
    }

    //get all Selected Store id.............
    public Data getSelectedStoreByStoreId(int id) {
        String query = "Select * FROM SelectedStoreDataEntity WHERE StoreId= " + id + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Data data = new Data();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateSelectedStoreData(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //populate get all SelectedStore data............
    private void populateSelectedStoreValue(Data ob, ContentValues values) {
        values.put("StoreId", ob.getStoreId());
        values.put("storeName", ob.getStoreName());
        values.put("categoryId", ob.getCategoryId());
    }

    //update get all SelectedStore............
    public boolean updateSelectedStoreData(Data ob) {
        ContentValues values = new ContentValues();
        populateSelectedStoreValue(ob, values);

        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("SelectedStoreDataEntity", values, "StoreId = " + ob.getStoreId() + "", null);

        db.close();
        return i > 0;
    }

    // delete SelectedStore Data By store Id
    public boolean deleteSelectedStoreById(int id) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "StoreId = " + id + " ";
        db.delete("SelectedStoreDataEntity", query, null);
        db.close();
        return result;
    }

    // delete all  Data
    public boolean deleteSelectedStoreData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("SelectedStoreDataEntity", null, null);
        db.close();
        return result;
    }
}
