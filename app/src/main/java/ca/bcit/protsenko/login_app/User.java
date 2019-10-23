package ca.bcit.protsenko.login_app;

public class User {

    private String email_stored;
    private String pw_stored;
    private String loc;

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public User(){
    }

    public String getEmail_stored() {
        return email_stored;
    }

    public void setEmail_stored(String email_stored) {
        this.email_stored = email_stored;
    }

    public String getPw_stored() {
        return pw_stored;
    }

    public void setPw_stored(String pw_stored) {
        this.pw_stored = pw_stored;
    }

    @Override
    public String toString() {
        return "User{" +
                "email_stored='" + email_stored + '\'' +
                ", pw_stored='" + pw_stored + '\'' +
                ", loc='" + loc + '\'' +
                '}';
    }
}
