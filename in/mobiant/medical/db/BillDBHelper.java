package in.mobiant.medical.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import in.mobiant.medical.interfaces.OnYearlyBillDataListener;
import in.mobiant.medical.models.BillInfo;
import in.mobiant.medical.models.BillMedicineItem;
import in.mobiant.medical.models.PatientInfo;
import in.mobiant.medical.utility.Variables;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BillDBHelper extends SQLiteOpenHelper {
    private static final String AMOUNT = "AMOUNT";
    private static final String BILL_NO = "BILL_NO";
    private static final String DATE = "DATE";
    private static final String DB_NAME = "bill_db";
    private static final String MEDICINES = "MEDICINES";
    private static final String PATIENT_INFO = "PATIENT_INFO";
    private static final String PATIENT_NAME = "PATIENT_NAME";
    private static final String TABLE_NAME = "bills";
    private static final int version = 1;

    class C05391 extends TypeToken<PatientInfo> {
        C05391() {
        }
    }

    class C05422 extends TypeToken<List<BillMedicineItem>> {
        C05422() {
        }
    }

    public BillDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE bills(BILL_NO TEXT,DATE INTEGER,AMOUNT TEXT,PATIENT_INFO TEXT,PATIENT_NAME TEXT,MEDICINES TEXT )");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean addBillRecord(BillInfo billInfo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            Gson gson = new Gson();
            Type type = new C05391().getType();
            Type type2 = new C05422().getType();
            ContentValues cv = new ContentValues();
            cv.put(BILL_NO, billInfo.getBillNo());
            cv.put(AMOUNT, Float.valueOf(billInfo.getAmount()));
            cv.put(DATE, Long.valueOf(billInfo.getDate()));
            cv.put(MEDICINES, gson.toJson(billInfo.getBillMedicinesList(), type2));
            cv.put(PATIENT_INFO, gson.toJson(billInfo.getPatientInfo(), type));
            cv.put(PATIENT_NAME, billInfo.getPatientInfo().getFullName());
            Log.d(Variables.TAG, "ROWID : " + sqLiteDatabase.insert(TABLE_NAME, null, cv));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return false;
    }

    public float getDailyCash() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        float cash = 0.0f;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            Cursor cur = sqLiteDatabase.rawQuery("SELECT SUM(AMOUNT) FROM bills WHERE BILL_NO > " + calendar.getTimeInMillis(), null);
            if (cur.moveToFirst()) {
                cash = cur.getFloat(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        return cash;
    }

    public BillInfo getBillInfoFromCursor(Cursor cursor, Gson gson, Type type, Type type2) {
        BillInfo billInfo = new BillInfo();
        billInfo.setBillNo(cursor.getString(cursor.getColumnIndex(BILL_NO)));
        billInfo.setAmount(cursor.getFloat(cursor.getColumnIndex(AMOUNT)));
        billInfo.setDate(cursor.getLong(cursor.getColumnIndex(DATE)));
        billInfo.setBillMedicinesList((List) gson.fromJson(cursor.getString(cursor.getColumnIndex(MEDICINES)), type2));
        billInfo.setPatientInfo((PatientInfo) gson.fromJson(cursor.getString(cursor.getColumnIndex(PATIENT_INFO)), type));
        return billInfo;
    }

    public void getYearlyBill(final boolean getNames, final String str, final OnYearlyBillDataListener onYearlyBillDataListener) {
        new AsyncTask<Void, Void, Void>() {
            List<BillInfo> billInfoList = new ArrayList();

            class C05401 extends TypeToken<PatientInfo> {
                C05401() {
                }
            }

            class C05412 extends TypeToken<List<BillMedicineItem>> {
                C05412() {
                }
            }

            protected Void doInBackground(Void... voids) {
                SQLiteDatabase sqLiteDatabase = BillDBHelper.this.getReadableDatabase();
                sqLiteDatabase.beginTransaction();
                try {
                    Cursor cur;
                    if (getNames) {
                        cur = sqLiteDatabase.query(true, BillDBHelper.TABLE_NAME, null, "PATIENT_NAME like \"%" + str + "%\"", null, null, null, null, null);
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(5, 1);
                        calendar.set(2, 3);
                        calendar.add(1, -1);
                        long fromDate = calendar.getTimeInMillis();
                        Log.d(Variables.TAG, "from " + calendar.getTime().toString());
                        calendar.set(5, 31);
                        calendar.set(2, 2);
                        calendar.add(1, 1);
                        long toDate = calendar.getTimeInMillis();
                        Log.d(Variables.TAG, "to " + calendar.getTime().toString());
                        cur = sqLiteDatabase.query(true, BillDBHelper.TABLE_NAME, null, "PATIENT_NAME like \"" + str + "\" and " + BillDBHelper.DATE + " > " + fromDate + " and " + BillDBHelper.DATE + " < " + toDate, null, null, null, null, null);
                    }
                    Log.d(Variables.TAG, "New records found " + cur.getCount());
                    Gson gson = new Gson();
                    Type type = new C05401().getType();
                    Type type2 = new C05412().getType();
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {
                        this.billInfoList.add(BillDBHelper.this.getBillInfoFromCursor(cur, gson, type, type2));
                        cur.moveToNext();
                    }
                    cur.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sqLiteDatabase.endTransaction();
                sqLiteDatabase.close();
                publishProgress(new Void[0]);
                return null;
            }

            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
                if (getNames) {
                    onYearlyBillDataListener.onNameChanged(this.billInfoList);
                } else {
                    onYearlyBillDataListener.onDataFetched(this.billInfoList);
                }
            }
        }.execute(new Void[0]);
    }
}
