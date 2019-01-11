package dsa.eetac.upc.edu.appgame.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("userName")
    @Expose
   public String userName;
    @SerializedName("password")
    @Expose
   public String password;
    @SerializedName("isAdmin")
    @Expose
   public int isAdmin;
    @SerializedName("money")
    @Expose
   public double money;
    @SerializedName("isBanned")
    @Expose
   public int isBanned;
/*
    @SerializedName("userName")
    @Expose
    public String userName;
    @SerializedName("money")
    @Expose
    public double money;
    @SerializedName("isBanned")
    @Expose
    public int isBanned;
    @SerializedName("isAdmin")
    @Expose
    private int isAdmin;
    @SerializedName("password")
    @Expose
    private String password;*/

    public User (String userName, String password, int isAdmin, int money,int isBanned){
        //this.idUser = idUser;
        this.userName = userName;
        this.password = password;
        this.isAdmin = isAdmin;
        this.money = money;
        this.isBanned = isBanned;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }
}
