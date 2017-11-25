package in.mobiant.medical.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.mobiant.medical.MedicalDashboardActivity;
import in.mobiant.medical.MedicalLoginActivity;
import in.mobiant.medical.MedicalLoginActivity.MedicinesDownloadCallback;
import in.mobiant.medical.models.ATCClassification;
import in.mobiant.medical.models.AlertInfoItem;
import in.mobiant.medical.models.BillResponse.MediStock;
import in.mobiant.medical.models.MedicineItemNew;
import in.mobiant.medical.models.MedicineItemNew.Presentation;
import in.mobiant.medical.models.MedicinesItem;
import in.mobiant.medical.models.StockMedicineInfo;
import in.mobiant.medical.models.StockMedicineInfo.Details;
import in.mobiant.medical.models.UserInfoItem;
import in.mobiant.medical.net.ApiCallInterface;
import in.mobiant.medical.utility.ProgressDialog;
import in.mobiant.medical.utility.Utils;
import in.mobiant.medical.utility.Variables;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MediDBHelper extends SQLiteOpenHelper {
    public static final String ATC_CLASSIFICATION = "ATC_CLASSIFICATION";
    public static final String CIMS_CLASS = "CIMS_CLASS";
    public static final String CONTENTS = "CONTENTS";
    public static final String DB_NAME = "medicines_db";
    public static final String DRUG_TYPE = "DRUG_TYPE";
    public static final String D_BUY = "D_BUY";
    public static final String D_SELL = "D_SELL";
    public static final String EXPIRY = "EXPIRY";
    public static final String MANUFACTURER = "MANUFACTURER";
    public static final String MEDICINE_TYPE = "MEDICINE_TYPE";
    public static final String M_BUY = "M_BUY";
    public static final String M_SELL = "M_SELL";
    public static final String PACKED_QTY = "PACKED_QTY";
    public static final String PRICE = "PRICE";
    public static final String REFERENCED_GENERIC_MEDICINE = "REFERENCED_GENERIC_MEDICINE";
    public static final String STOCK_DETAILS = "STOCK_DETAILS";
    public static final String STOCK_QTY = "STOCK_QTY";
    public static final String TABLE_NAME = "medicines";
    public static final String UNIQ_VALUE = "UNIQ_VALUE";
    public static final int VERSION = 1;
    public static final String W_BUY = "W_BUY";
    public static final String W_SELL = "W_SELL";
    public static final String _ID = "_ID";
    private List<MedicinesItem> allMedicines = new ArrayList();
    private Gson gson = new Gson();
    private List<MedicinesItem> medicineSubs;
    private Type type = new C05431().getType();
    private Type type2 = new C05442().getType();
    private Type type3 = new C05453().getType();

    class C05431 extends TypeToken<MedicinesItem> {
        C05431() {
        }
    }

    class C05442 extends TypeToken<List<String>> {
        C05442() {
        }
    }

    class C05453 extends TypeToken<ATCClassification> {
        C05453() {
        }
    }

    class C05464 extends TypeToken<List<StockMedicineInfo>> {
        C05464() {
        }
    }

    class C05475 extends TypeToken<List<StockMedicineInfo>> {
        C05475() {
        }
    }

    public MediDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v(Variables.TAG, "Database created");
        sqLiteDatabase.execSQL("CREATE TABLE medicines(_ID TEXT ,MEDICINE_TYPE TEXT,REFERENCED_GENERIC_MEDICINE TEXT,MANUFACTURER TEXT,ATC_CLASSIFICATION TEXT,D_SELL INTEGER,W_SELL INTEGER,M_SELL INTEGER,D_BUY INTEGER,W_BUY INTEGER,M_BUY INTEGER,CONTENTS TEXT,CIMS_CLASS TEXT,DRUG_TYPE TEXT ,PRICE REAL,EXPIRY INTEGER,PACKED_QTY TEXT, STOCK_QTY INTEGER,STOCK_DETAILS TEXT,  UNIQUE(_ID,DRUG_TYPE))");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v(Variables.TAG, "Database upgraded");
    }

    public MedicinesItem getMedicineItemFromCursor(Cursor cur) {
        MedicinesItem item = new MedicinesItem();
        item.setId(cur.getString(cur.getColumnIndex(_ID)));
        item.setMeicineType(cur.getString(cur.getColumnIndex(MEDICINE_TYPE)));
        item.setReferencedGenericMedicine((List) this.gson.fromJson(cur.getString(cur.getColumnIndex(REFERENCED_GENERIC_MEDICINE)), this.type2));
        item.setManufacturer(cur.getString(cur.getColumnIndex(MANUFACTURER)));
        item.setContents(cur.getString(cur.getColumnIndex(CONTENTS)));
        item.setCIMSClass(cur.getString(cur.getColumnIndex(CIMS_CLASS)));
        item.setDrugType(cur.getString(cur.getColumnIndex(DRUG_TYPE)));
        item.setStockQty(cur.getInt(cur.getColumnIndex(STOCK_QTY)));
        item.setPackedPrice(cur.getFloat(cur.getColumnIndex(PRICE)));
        item.setExpiry(cur.getLong(cur.getColumnIndex(EXPIRY)));
        item.setPackedQty(cur.getString(cur.getColumnIndex(PACKED_QTY)));
        item.setD_sell(cur.getInt(cur.getColumnIndex(D_SELL)));
        item.setW_sell(cur.getInt(cur.getColumnIndex(W_SELL)));
        item.setM_sell(cur.getInt(cur.getColumnIndex(M_SELL)));
        item.setD_buy(cur.getInt(cur.getColumnIndex(D_BUY)));
        item.setW_buy(cur.getInt(cur.getColumnIndex(W_BUY)));
        item.setM_buy(cur.getInt(cur.getColumnIndex(M_BUY)));
        return item;
    }

    public List<MedicinesItem> getAllMedicines(String str) {
        this.allMedicines.clear();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            Cursor cur = sqLiteDatabase.query(true, TABLE_NAME, null, "_ID like \"%" + str + "%\"", null, null, null, null, "5");
            Log.d(Variables.TAG, "New records found " + cur.getCount());
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                this.allMedicines.add(getMedicineItemFromCursor(cur));
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return this.allMedicines;
    }

    public List<MedicinesItem> getMedicinesSubs(String str) {
        if (this.medicineSubs == null) {
            this.medicineSubs = new ArrayList();
        } else {
            this.medicineSubs.clear();
        }
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            sqLiteDatabase.beginTransaction();
            Cursor cur = sqLiteDatabase.query(true, TABLE_NAME, null, "REFERENCED_GENERIC_MEDICINE like \"%" + str + "%\"", null, null, null, "PRICE ASC", "5");
            Log.d(Variables.TAG, "New records found " + cur.getCount());
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                this.medicineSubs.add(getMedicineItemFromCursor(cur));
                cur.moveToNext();
            }
            cur.close();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        } catch (Exception e) {
        }
        return this.medicineSubs;
    }

    public void addStock(StockMedicineInfo stockItem) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL("UPDATE medicines SET STOCK_QTY = " + stockItem.getUpdatedStock() + "," + EXPIRY + " = " + stockItem.getMinExpiry() + "," + STOCK_DETAILS + " = \"" + URLEncoder.encode(new Gson().toJson(stockItem.getDetails(), new C05464().getType()), "UTF-8") + "\" ," + D_BUY + " = " + D_BUY + " + " + ((Details) stockItem.getDetails().get(stockItem.getDetails().size() - 1)).getQuantity() + " ," + W_BUY + " = " + W_BUY + " + " + ((Details) stockItem.getDetails().get(stockItem.getDetails().size() - 1)).getQuantity() + " ," + M_BUY + " = " + M_BUY + " + " + ((Details) stockItem.getDetails().get(stockItem.getDetails().size() - 1)).getQuantity() + " WHERE " + _ID + " = \"" + stockItem.getId() + "\" and " + DRUG_TYPE + " = \"" + stockItem.getType() + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    public void updateStockAfterBill(List<MediStock> stockItems) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            Gson gson = new Gson();
            Type type = new C05475().getType();
            for (MediStock stockItem : stockItems) {
                sqLiteDatabase.execSQL("UPDATE medicines SET STOCK_QTY = " + stockItem.getUpdatedStock() + "," + D_SELL + " = " + D_SELL + " + 1 ," + W_SELL + " = " + W_SELL + " + 1 ," + M_SELL + " = " + M_SELL + " + 1  WHERE " + _ID + " = \"" + stockItem.getId() + "\" and " + DRUG_TYPE + " = \"" + stockItem.getType() + "\"");
            }
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMedicines(Context context, ProgressDialog progressDialog, List<MedicineItemNew> medicinesItems, ApiCallInterface apiCallInterface, MedicinesDownloadCallback medicinesDownloadCallback, UserInfoItem userInfoItem, int skip, int limit) {
        final List<MedicineItemNew> list = medicinesItems;
        final int i = limit;
        final int i2 = skip;
        final Context context2 = context;
        final ProgressDialog progressDialog2 = progressDialog;
        final MedicinesDownloadCallback medicinesDownloadCallback2 = medicinesDownloadCallback;
        final ApiCallInterface apiCallInterface2 = apiCallInterface;
        final UserInfoItem userInfoItem2 = userInfoItem;
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void[] objects) {
                SQLiteDatabase sqLiteDatabase = MediDBHelper.this.getWritableDatabase();
                sqLiteDatabase.beginTransaction();
                for (MedicineItemNew itemNew : list) {
                    try {
                        ContentValues cv = new ContentValues();
                        cv.put(MediDBHelper._ID, itemNew.getId());
                        cv.put(MediDBHelper.MEDICINE_TYPE, itemNew.getMedicineType());
                        cv.put(MediDBHelper.REFERENCED_GENERIC_MEDICINE, MediDBHelper.this.gson.toJson(itemNew.getReferencedGenericMedicine()));
                        cv.put(MediDBHelper.MANUFACTURER, itemNew.getManufacturer());
                        cv.put(MediDBHelper.ATC_CLASSIFICATION, MediDBHelper.this.gson.toJson(itemNew.getATCClassification()));
                        cv.put(MediDBHelper.CONTENTS, itemNew.getContents());
                        cv.put(MediDBHelper.CIMS_CLASS, itemNew.getCIMSClass());
                        for (Presentation med_pres : itemNew.getPresentation()) {
                            cv.put(MediDBHelper.DRUG_TYPE, med_pres.getType());
                            cv.put(MediDBHelper.STOCK_QTY, Integer.valueOf(0));
                            cv.put(MediDBHelper.PRICE, med_pres.getPrice());
                            cv.put(MediDBHelper.EXPIRY, Integer.valueOf(0));
                            cv.put(MediDBHelper.PACKED_QTY, med_pres.getQuantity());
                            Log.d(Variables.TAG, "UNIQ_VALUE : " + itemNew.getPresentation().size());
                            Log.d(Variables.TAG, "ROWID : " + sqLiteDatabase.insertOrThrow(MediDBHelper.TABLE_NAME, null, cv));
                        }
                    } catch (Exception e) {
                    }
                }
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                sqLiteDatabase.close();
                publishProgress(new Void[0]);
                return null;
            }

            protected void onProgressUpdate(Void[] values) {
                super.onProgressUpdate(values);
                if (list.size() < i) {
                    Log.d(Variables.TAG, " all medicines downloaded " + (i2 + list.size()));
                    Utils.saveMedicinesDownloaded(context2, true);
                    if (progressDialog2 != null) {
                        progressDialog2.dismiss();
                    }
                    context2.startActivity(new Intent(context2, MedicalDashboardActivity.class));
                    ((MedicalLoginActivity) context2).finish();
                    return;
                }
                medicinesDownloadCallback2.setSkip(i2 + i);
                apiCallInterface2.getMedicines(userInfoItem2.getUsername(), userInfoItem2.getMobile(), userInfoItem2.getRole(), Utils.getDeviceID(context2), Integer.valueOf(i2 + i), Integer.valueOf(i)).enqueue(medicinesDownloadCallback2);
                Log.d(Variables.TAG, " medicines downloading " + (i2 + i));
            }
        }.execute(new Void[0]);
        Log.d(Variables.TAG, "Database name : " + getDatabaseName());
    }

    public List<MedicinesItem> getMedicinesExpiring(boolean isExpired) {
        List<MedicinesItem> expiringMedicines = new ArrayList();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            if (!isExpired) {
                calendar.add(5, 15);
            }
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            sqLiteDatabase.beginTransaction();
            Cursor cur = sqLiteDatabase.query(true, TABLE_NAME, null, "EXPIRY < " + calendar.getTimeInMillis(), null, null, null, "EXPIRY ASC", "50");
            Log.d(Variables.TAG, "Records found " + cur.getCount());
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                expiringMedicines.add(getMedicineItemFromCursor(cur));
                cur.moveToNext();
            }
            cur.close();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expiringMedicines;
    }

    public List<MedicinesItem> getBuySell(int type) {
        String columnName = "";
        switch (type) {
            case 1:
                columnName = D_SELL;
                break;
            case 2:
                columnName = W_SELL;
                break;
            case 3:
                columnName = M_SELL;
                break;
            case 4:
                columnName = D_BUY;
                break;
            case 5:
                columnName = W_BUY;
                break;
            case 6:
                columnName = M_BUY;
                break;
        }
        List<MedicinesItem> medicines = new ArrayList();
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            sqLiteDatabase.beginTransaction();
            Cursor cur = sqLiteDatabase.query(true, TABLE_NAME, null, columnName + " > 0", null, null, null, null, "10");
            Log.d(Variables.TAG, columnName + " Records found " + cur.getCount());
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                medicines.add(getMedicineItemFromCursor(cur));
                cur.moveToNext();
            }
            cur.close();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public boolean resetBuySell(String columnName) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            ContentValues cv = new ContentValues();
            cv.put(columnName, Integer.valueOf(0));
            Log.d(Variables.TAG, "Records updated " + sqLiteDatabase.update(TABLE_NAME, cv, null, null));
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AlertInfoItem> getAlertInfo() {
        List<AlertInfoItem> items = new ArrayList();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            AlertInfoItem alertInfo2Days = new AlertInfoItem();
            Calendar calendar = Calendar.getInstance();
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.add(5, 2);
            long count2Days = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME, "EXPIRY < " + calendar.getTimeInMillis());
            alertInfo2Days.setDays(2);
            alertInfo2Days.setCount(count2Days);
            items.add(alertInfo2Days);
            Log.d(Variables.TAG, "Records found " + count2Days);
            AlertInfoItem alertInfo7Days = new AlertInfoItem();
            calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.add(5, 7);
            long count7Days = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME, "EXPIRY < " + calendar.getTimeInMillis());
            alertInfo7Days.setDays(7);
            alertInfo7Days.setCount(count7Days);
            items.add(alertInfo7Days);
            Log.d(Variables.TAG, "Records found " + count7Days);
            AlertInfoItem alertInfo15Days = new AlertInfoItem();
            calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.add(5, 15);
            long count15Days = DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME, "EXPIRY < " + calendar.getTimeInMillis());
            alertInfo15Days.setDays(15);
            alertInfo15Days.setCount(count15Days);
            items.add(alertInfo15Days);
            Log.d(Variables.TAG, "Records found " + count15Days);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return items;
    }
}
