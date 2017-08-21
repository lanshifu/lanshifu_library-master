package library.lanshifu.com.myapplication.mvp.presenter;

import library.lanshifu.com.myapplication.model.Room;
import library.lanshifu.com.myapplication.model.RoomLine;
import library.lanshifu.com.myapplication.mvp.contract.LiveDetailContract;
import library.lanshifu.com.myapplication.net.MyObserver;
import library.lanshifu.com.myapplication.net.RetrofitHelper;
import library.lanshifu.com.myapplication.net.RxSchedulerHelper;

/**
 * Created by Administrator on 2017/8/21.
 */

public class LiveDetailPresenter extends LiveDetailContract.Presenter {

    @Override
    public void enterRoom(String uid, final boolean isShowing) {

        RetrofitHelper.getLiveApi().enterRoom(uid)
                .compose(RxSchedulerHelper.<Room>io_main())
                .subscribe(new MyObserver<Room>() {
                    @Override
                    public void _onNext(Room room) {
                        RoomLine ws = room.getLive().getWs();
//                        mView.playUrl();
                        RoomLine roomLine = room.getLive().getWs();

                        RoomLine.FlvBean flv = roomLine.getFlv();
                        String url = "";
                        if(flv!=null){
                            url = flv.getValue(isShowing).getSrc();
                        }else{
                            url = roomLine.getHls().getValue(isShowing).getSrc();
                        }
                         mView.playUrl(url);
                    }

                    @Override
                    public void _onError(String e) {
                        mView.showError(e);

                    }
                });

    }
}
