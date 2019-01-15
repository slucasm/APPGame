package dsa.eetac.upc.edu.appgame.models;

public class BodyUser {

    private String userName;
    private String password;

    public BodyUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public BodyUser(){}

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
}
