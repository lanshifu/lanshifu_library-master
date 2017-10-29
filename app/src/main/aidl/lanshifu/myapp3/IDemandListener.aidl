// IDemandListener.aidl
package lanshifu.myapp3;
import lanshifu.myapp3.MessageBean;

// Declare any non-default types here with import statements

interface IDemandListener {

    void onDemandReceiver(in MessageBean msg);//客户端->服务端
}
