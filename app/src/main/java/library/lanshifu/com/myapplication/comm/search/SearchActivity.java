package library.lanshifu.com.myapplication.comm.search;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import library.lanshifu.com.lsf_library.utils.ArraySharePres;
import library.lanshifu.com.lsf_library.utils.StringUtil;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.comm.BaseAppCompatActivity;


/**
 * Author:  [xWX371834\许纯震].
 * Date:    2016/8/29.
 * Description:
 *
 */
public class SearchActivity extends BaseAppCompatActivity {
    private String KEY_SP_SEARCH_CACHE = "search_cache";
    public static final String EXTRA_ACTIVITY_NAME = "activity_name";

    private TagCloudView tagCloudView;
    private SearchView searchView;
    private List<String> mCache;

    public static final String SEARCH_CONTENT = "search_content";

    // 历史记录点击时的监听
    private TagCloudView.OnTagClickListener onTagClickListener = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int position) {
            searchView.setQuery(mCache.get(position), true);
        }
    };

    // SearchView搜索时的监听
    private final SearchView.OnQueryTextListener mOnQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    ArraySharePres.getInstance().putSPArrayItem(KEY_SP_SEARCH_CACHE, query);
//                    CustomToast.shortShow(query + "内容已经显示出来了");
                    if( mCache!=null &&! mCache.contains(query)){
                        mCache.add(query);
                        tagCloudView.addTag(query);
                    }
                    Intent intent = new Intent();
                    intent.putExtra(SEARCH_CONTENT,query);
                    setResult(RESULT_OK,intent);
                    finish();
                    return true;
                }
            };

    // SearchView关闭时的监听
    private SearchView.OnCloseListener onCloseListener = new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            SearchActivity.this.finish();
            return true;
        }
    };

    // 删除历史记录监听
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tagCloudView.removeAllTags();
            ArraySharePres.getInstance().cleanSPArray(KEY_SP_SEARCH_CACHE);
            mCache.clear();
        }
    };


    @Override
    protected int onGetTBMenuId() {
        return -1;
    }

    @Override
    protected int getContentView() {
        return R.layout.search_activity;
    }

    @Override
    protected void initView() {
        hideKeyboard();
        // SearchView初始化
        searchView = new SearchView(this);
//        if( getSubjectType() == SUBJECT_BLUE ){
//            TextView searchTv = (TextView) searchView.findViewById(R.id.search_src_text);
//            searchTv.setTextColor(Color.WHITE);
//        }
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(mOnQueryTextListener);
        searchView.setOnCloseListener(onCloseListener);

        // 将SearchView添加到ToolBar上
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        searchView.setPadding(getTBHeight(), 0, 0, 0);
        addCustomerViewToTB(searchView);

        // 缓存的搜索数据
        String activity = getIntent().getStringExtra(EXTRA_ACTIVITY_NAME);
        if (!StringUtil.isEmpty(activity)) {
            KEY_SP_SEARCH_CACHE = KEY_SP_SEARCH_CACHE + "_" + activity;
        }
        mCache = new ArrayList<String>();
        String[] spArray = ArraySharePres.getInstance().getSPArray(KEY_SP_SEARCH_CACHE);
        for(String s:spArray){
            mCache.add(s);
        }

        // 显示缓存的数据在横向流式布局中
        tagCloudView = (TagCloudView) getView().findViewById(R.id.search_tcv);
        tagCloudView.setTags(mCache);
        tagCloudView.setOnTagClickListener(onTagClickListener);

        // 清空按钮
        getView().findViewById(R.id.search_delete_all).setOnClickListener(onClickListener);
    }


}
