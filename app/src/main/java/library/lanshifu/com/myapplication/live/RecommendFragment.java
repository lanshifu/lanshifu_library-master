package library.lanshifu.com.myapplication.live;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import library.lanshifu.com.lsf_library.adapter.recyclerview.CommonAdapter;
import library.lanshifu.com.lsf_library.adapter.recyclerview.base.ViewHolder;
import library.lanshifu.com.lsf_library.base.BaseFragment;
import library.lanshifu.com.lsf_library.basemvp.BaseModle;
import library.lanshifu.com.lsf_library.basemvp.NullModle;
import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.model.Recommend;
import library.lanshifu.com.myapplication.mvp.contract.RecommendFragmentContract;
import library.lanshifu.com.myapplication.mvp.model.RecommendFragmentModel;
import library.lanshifu.com.myapplication.mvp.presenter.RecommendFragmentPresenter;
import library.lanshifu.com.myapplication.widget.CommRecyclerView;

/**
 * Created by Administrator on 2017/8/20.
 */

public class RecommendFragment extends BaseFragment<RecommendFragmentPresenter,BaseModle> implements RecommendFragmentContract.View{
    @Bind(R.id.comm_recyclerView)
    CommRecyclerView commRecyclerView;

    private List<Recommend.RoomBean> listData = new ArrayList<>();
    private CommonAdapter<Recommend.RoomBean> roomBeanCommonAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_comm_recyclerview;
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this,mModle);
    }

    @Override
    protected void initView() {

        commRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        roomBeanCommonAdapter = new CommonAdapter<Recommend.RoomBean>(getActivity(), R.layout.list_remmend_item, listData) {


            @Override
            protected void convert(ViewHolder holder, Recommend.RoomBean roomBean, int position) {

                ImageView iv = holder.getView(R.id.iv);
                Glide.with(getContext()).load(roomBean.getIcon())
                        .error(R.mipmap.ic_launcher)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv);

                holder.setText(R.id.tvCategroy, roomBean.getName());
                holder.setOnClickListener(R.id.tvMore, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        T.showShort("more");
                    }
                });

                RecyclerView recyclerView = holder.getView(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(new CommonAdapter<Recommend.RoomBean.ListBean>(getContext(),
                        R.layout.list_live_item, roomBean.getList()) {
                    @Override
                    protected void convert(ViewHolder holder, Recommend.RoomBean.ListBean data, int position) {

                        ImageView iv = holder.getView(R.id.iv);
                        Glide.with(getContext()).load(data.getThumb())
                                .placeholder(R.mipmap.ic_launcher)
                                .error(R.mipmap.ic_launcher)
                                .crossFade().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(iv);

                        holder.setText(R.id.tvTitle, data.getTitle());
                        holder.setText(R.id.tvName, data.getNick());
                        holder.setText(R.id.tvViewer, data.getViews());

                        final String uid = String.valueOf(data.getUid());

                        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),LiveDetailActivity.class);
                                intent.putExtra("uid",uid);
                                startActivity(intent);
                            }
                        });


                    }
                });

            }

        };
        commRecyclerView.setAdapter(roomBeanCommonAdapter);

        mPresenter.getRecommendInfo();

    }


    @Override
    public void showProgressDialog(String text) {
        startProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        stopProgressDialog();

    }

    @Override
    public void showError(String error) {
        T.showShort(error);

    }

    @Override
    public void returnRecommenInfos(Recommend recommend) {
        T.showShort("数据："+recommend.getRoom().size());
        roomBeanCommonAdapter.addAll(recommend.getRoom());

    }
}
