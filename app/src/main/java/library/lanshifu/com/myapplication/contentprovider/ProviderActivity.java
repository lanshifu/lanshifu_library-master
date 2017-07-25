package library.lanshifu.com.myapplication.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseActivity;
import library.lanshifu.com.myapplication.R;

public class ProviderActivity extends BaseActivity {


    @Bind(R.id.editText)
    EditText editText;
    @Bind(R.id.editText2)
    EditText editText2;
    @Bind(R.id.editText3)
    EditText editText3;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.editText4)
    EditText editText4;
    @Bind(R.id.editText5)
    EditText editText5;
    @Bind(R.id.editText6)
    EditText editText6;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.tv_log)
    TextView tvLog;
    @Bind(R.id.fab)
    FloatingActionButton fab;


    @Override
    public int getLayoutId() {
        return R.layout.activity_provider;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.button, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                saveUserInfo();
                button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryPostCode();
                    }
                }, 1000);
                break;
            case R.id.button2:
                saveCompany();
                button2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryCompany();
                    }
                },1000);
                break;
        }
    }

    private void queryPostCode() {
        String tel = editText2.getText().toString();
        Uri uri = Uri.parse("content://com.lanshifu.userinfo_provider/user_info/");
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor.moveToFirst()){
            tvLog.setText("结果是：\r\n");
            while (cursor.moveToNext()){
                tvLog.append("\r\n："+cursor.getString(2));
            }
        }
        cursor.close();

    }

    private void queryCompany(){
        String _id = editText6.getText().toString();
        Uri uri = Uri.parse("content://com.lanshifu.userinfo_provider/company/");
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if(cursor.moveToFirst()){
            showShortToast("地址是"+cursor.getString(2));
            tvLog.setText("结果是：");
            while (cursor.moveToNext()){
                tvLog.append("\r\n地址："+cursor.getString(2));
            }
        }
        cursor.close();

    }

    private void saveCompany() {
        ContentValues newRecord = new ContentValues();
        newRecord.put(UserInfoDbHelper.COMP_ID_COLUMN,editText4.getText().toString());
        newRecord.put(UserInfoDbHelper.BUSINESS_COLUMN,editText5.getText().toString());
        newRecord.put(UserInfoDbHelper.ADDR_COLUMN,editText6.getText().toString());
        getContentResolver().insert(UserInfoProvider.COMPANY_URI,newRecord);
    }

    private void saveUserInfo() {
        ContentValues newRecord = new ContentValues();
        newRecord.put(UserInfoDbHelper.ID_COLUMN,editText3.getText().toString());
        newRecord.put(UserInfoDbHelper.TEL_COLUMN,editText2.getText().toString());
        newRecord.put(UserInfoDbHelper.DESC_COLUMN,editText.getText().toString());
        getContentResolver().insert(UserInfoProvider.POST_CODE_URI,newRecord);
    }
}
