package library.lanshifu.com.myapplication.designmode.factory.impl;

import library.lanshifu.com.lsf_library.utils.T;
import library.lanshifu.com.myapplication.designmode.factory.IProduct;

/**
 * Created by Administrator on 2017/9/3.
 */

public class Book implements IProduct {
    @Override
    public void show() {
        T.showShort("产品：书");
    }

    @Override
    public int getPrice() {
        return 10;
    }
}
