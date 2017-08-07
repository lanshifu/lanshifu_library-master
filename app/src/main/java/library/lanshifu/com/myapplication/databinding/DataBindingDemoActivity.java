package library.lanshifu.com.myapplication.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import library.lanshifu.com.myapplication.R;

public class DataBindingDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_data_binding_demo);

        ActivityDataBindingDemoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding_demo);
        User user = new User("name","lastname");
        user.setIcon(R.mipmap.icon_menu6);
        binding.setUser(user);



    }
}
