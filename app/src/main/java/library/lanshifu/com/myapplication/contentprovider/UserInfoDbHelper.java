package library.lanshifu.com.myapplication.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/7/24.
 */

public class UserInfoDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "userinfo.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_USER_INFO = "user_info";
    public static final String TABLE_COMPANY = "company";
    public static final String TEL_COLUMN = "tel";
    public static final String DESC_COLUMN = "desc";
    public static final String COMP_ID_COLUMN = "comp_id";
    public static final String ID_COLUMN = "user_id";
    public static final String BUSINESS_COLUMN = "business";
    public static final String ADDR_COLUMN = "addr";

    private static final String POSTCODE_TABLE_SQL = "CREATE TABLE " + TABLE_USER_INFO + "("
            + ID_COLUMN + " TEXT ,"
            + TEL_COLUMN + " TEXT ,"
            + DESC_COLUMN + " TEXT"
            + ")";

    private static final String COMPANY_TABLE_SQL = "CREATE TABLE " + TABLE_COMPANY + "("
            + COMP_ID_COLUMN + " TEXT PRIMARY KEY,"
            + BUSINESS_COLUMN + " TEXT,"
            + ADDR_COLUMN + " TEXT"
            + ")";


    public UserInfoDbHelper(Context context) {
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(POSTCODE_TABLE_SQL);
        db.execSQL(COMPANY_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
