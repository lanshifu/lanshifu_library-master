package library.lanshifu.com.myapplication.ui;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import library.lanshifu.com.lsf_library.base.BaseToolBarActivity;
import library.lanshifu.com.lsf_library.utils.L;
import library.lanshifu.com.myapplication.R;
import library.lanshifu.com.myapplication.widget.Html5WebView;

public class GaoKaoSearchActivity extends BaseToolBarActivity {


    @Bind(R.id.webView)
    Html5WebView webView;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_search)
    Button btnSearch;

    //初始化变量
    String []  date = new String[49];
    private int position = 0;

    @Override
    protected int getLayoutid() {
        return R.layout.activity_gao_kao_search;
    }

    @Override
    protected void onViewCreate() {
        initData();
        webView.loadUrl("http://service.southcn.com/ksy-lqcx");
    }

    private void initData() {

        date[0]="9601";
        date[1]="9602";
        date[2]="9603";
        date[3]="9604";
        date[4]="9605";
        date[5]="9606";
        date[6]="9607";
        date[7]="9608";
        date[8]="9609";
        date[9]="9610";
        date[10]="9611";
        date[11]="9612";
        date[12]="9701";
        date[13]="9702";
        date[14]="9703";
        date[15]="9704";
        date[16]="9705";
        date[17]="9706";
        date[18]="9707";
        date[19]="9708";
        date[20]="9601";
        date[21]="9709";
        date[22]="9710";
        date[23]="9711";
        date[24]="9712";
        date[25]="9801";
        date[26]="9802";
        date[27]="9803";
        date[28]="9804";
        date[29]="9805";
        date[30]="9806";
        date[31]="9807";
        date[32]="9808";
        date[33]="9809";
        date[34]="9810";
        date[35]="9811";
        date[36]="9812";
        date[37]="9901";
        date[38]="9902";
        date[39]="9903";
        date[40]="9904";
        date[41]="9905";
        date[42]="9906";
        date[43]="9907";
        date[44]="9908";
        date[45]="9909";
        date[46]="9910";
        date[47]="9911";
        date[48]="9912";
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {

       setTBTitle("正在查询");
//        request();
        startSearch();
    }


    Handler handler = new Handler();
    private void startSearch() {
        if ((position >= date.length)) {
            return;
        }
        handler.postDelayed(new Runnable() {

            private String code;
            private String zkzh;

            @Override
            public void run() {
                zkzh = etNumber.getText().toString();
                code = etCode.getText().toString();
                String url = "http://service.southcn.com/ksy/?c=core&a=call&_m=gklq.search&zkzh="+ zkzh +"&csrq="+date[position]+"&code="+ code;
                webView.loadUrl(url);
                position ++;
                webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode,
                                                String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                    }

                });
            }
        },10);

    }


    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String html) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.removeJavascriptInterface("local_obj");
                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                    webView.setWebViewClient(null);

                    if(html.contains("ok")){
                        //包含ok字符
                        showDialog("破解成功,出生年月：19"+date[position-1],html);
                        setTBTitle("查询成功");
                        L.e(html);
                        Log.d("success", html);
                    }else if(html.contains("验证码不正确")){
                        setTBTitle("验证码不正确，次数 ："+position);
                        showDialog("验证码不正确","");
                        position = 0;
                    }else{
                        startSearch();
                    }
                }
            });
        }
    }



    private void showDialog(String title,String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create()
                .show();
    }


}
