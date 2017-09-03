package library.lanshifu.com.myapplication.designmode.factory.impl;

import library.lanshifu.com.myapplication.designmode.factory.IFactory;
import library.lanshifu.com.myapplication.designmode.factory.IProduct;

/**
 * Created by Administrator on 2017/9/3.
 */

public class BookIFactory implements IFactory {
    @Override
    public IProduct createProduct() {
        return new Book();
    }
}
