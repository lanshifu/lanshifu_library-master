// IDemandManager.aidl
package lanshifu.myapp3;
//导入所需要使用的非默认支持数据类型的包
import lanshifu.myapp3.MessageBean;
parcelable MessageBean;//parcelable是小写

// Declare any non-default types here with import statements
//作用是定义方法接口   semicolon

//interface IDemandManager {
//    MessageBean getDemand();
//
//    void setDemandIn( MessageBean msg);//客户端->服务端
//
//    //out和inout都需要重写MessageBean的readFromParcel方法
//    void setDemandOut( MessageBean msg);//服务端->客户端
//
//    void setDemandInOut( MessageBean msg);//客户端<->服务端
//}
