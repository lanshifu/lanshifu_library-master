package library.lanshifu.com.myapplication.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/9/8.
 */

public class UserInfo extends DataSupport{


     private String userId;


    private String name;
    private String token;
    @Column(unique = true)
    private String imis;

    public UserInfo(String id, String name, String token) {
        this.userId = id;
        this.imis = id;
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImis() {
        return imis;
    }

    public void setImis(String imis) {
        this.imis = imis;
    }
}
