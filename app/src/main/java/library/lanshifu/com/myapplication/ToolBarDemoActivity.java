package library.lanshifu.com.myapplication;

import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;

import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;

public class ToolBarDemoActivity extends BaseToolBarActivity {


    @Override
    protected boolean onIfShowTB() {
        return true;
    }

    @Override
    protected int getLayoutid() {
        return R.layout.activity_tool_bar_demo;
    }

    @Override
    protected void onViewCreate() {

        setTBTitle("ToorBarDemo");

        addTBMenuItem("item", MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        addTBMenuItem("icon1", getResources().getDrawable(R.drawable.ic_search_black_24dp),MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        addTBMenuItem("icon4", getResources().getDrawable(R.drawable.ic_search_black_24dp),MenuItemCompat.SHOW_AS_ACTION_NEVER);


        addTBMore(new String[]{"item1","item2"});
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String title = item.getTitle().toString();
        showShortToast(title);

        return super.onOptionsItemSelected(item);
    }
}
