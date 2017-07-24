package library.lanshifu.com.myapplication.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/7/24.
 */

public class UserInfoProvider extends ContentProvider {

    private static final String CONTENT = "content://";
    public static final String AUTHORITY = "com.lanshifu.userinfo_provider";

    /**
     * contentprovider 所返回的数据类型定义，数据集合
     */
    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY;
    /**
     * 单项数据
     */
    private static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY;

    public static final Uri POST_CODE_URI = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_USER_INFO);
    public static final Uri COMPANY_URI = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_COMPANY);

    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static final int USER_INFO = 1;
    static final int USER_INFO_ITEM = 2;
    static final int COMPANY = 3;
    static final int COMPANY_ITEM = 4;
    private SQLiteDatabase mDatabase;

    static {
        uriMatcher.addURI(AUTHORITY,"user_info",USER_INFO);
        uriMatcher.addURI(AUTHORITY,"user_info/*",USER_INFO);
        uriMatcher.addURI(AUTHORITY,"company",USER_INFO);
        uriMatcher.addURI(AUTHORITY,"company/#",USER_INFO);
    }


    @Override
    public boolean onCreate() {
        mDatabase = new UserInfoDbHelper(getContext()).getWritableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case USER_INFO:
                cursor = mDatabase.query(UserInfoDbHelper.TABLE_USER_INFO,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case USER_INFO_ITEM:
                String tel = uri.getPathSegments().get(1);
                cursor = mDatabase.query(UserInfoDbHelper.TABLE_USER_INFO,projection,
                        "tel_num = ?",new String[]{tel},null,null,sortOrder);

                break;

            case COMPANY:
                cursor = mDatabase.query(UserInfoDbHelper.TABLE_COMPANY,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            case COMPANY_ITEM:
                String cid = uri.getPathSegments().get(1);
                cursor = mDatabase.query(UserInfoDbHelper.TABLE_COMPANY,projection,
                        "id = ?",new String[]{cid},null,null,sortOrder);

                break;
            default:
                throw new RuntimeException("错误的uri");

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USER_INFO:
            case COMPANY:
                return CONTENT_TYPE;

            case USER_INFO_ITEM:
            case COMPANY_ITEM:
                return CONTENT_TYPE_ITEM;

            default:
                throw new RuntimeException("错误的uri");

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long newId = 0;
        Uri newUri = null;
        switch (uriMatcher.match(uri)) {
            case USER_INFO:
                newId = mDatabase.insert(UserInfoDbHelper.TABLE_USER_INFO, null, contentValues);
                newUri = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_USER_INFO + "/" + newId);
                break;

            case COMPANY:
                newId = mDatabase.insert(UserInfoDbHelper.TABLE_COMPANY, null, contentValues);
                newUri = Uri.parse(CONTENT + AUTHORITY + "/" + UserInfoDbHelper.TABLE_COMPANY + "/" + newId);
                break;

        }

        if (newId > 0) {
            return newUri;
        }
        throw new RuntimeException("failed to insert:uri = "+uri);


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
