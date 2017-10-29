// BookManager.aidl
package lanshifu.myapp3;
import lanshifu.myapp3.Book;
import lanshifu.myapp3.MessageBean;
import lanshifu.myapp3.IDemandListener;

// Declare any non-default types here with import statements

interface BookManager {

//     //所有的返回值前都不需要加任何东西，不管是什么数据类型
//        List<Book> getBooks();
//
//        //传参时除了Java基本类型以及String，CharSequence之外的类型
//        //都需要在前面加上定向tag，具体加什么量需而定
//        void addBook(in Book book);

         MessageBean getDemand();

         void setDemandIn(in MessageBean msg);//客户端->服务端

         //out和inout都需要重写MessageBean的readFromParcel方法
         void setDemandOut(out MessageBean msg);//服务端->客户端

         void setDemandInOut(inout MessageBean msg);//客户端<->服务端

         void registerListener(IDemandListener listener);
         void unregisterListener(IDemandListener listener);
}
