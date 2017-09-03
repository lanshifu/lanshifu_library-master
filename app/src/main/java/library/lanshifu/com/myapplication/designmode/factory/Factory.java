package library.lanshifu.com.myapplication.designmode.factory;

import library.lanshifu.com.myapplication.designmode.factory.impl.Book;

/**
 * Created by lanshifu on 2017/9/3.
 */

public class Factory {

    public static IProduct createBook(){
        return new Book();
    }
}
