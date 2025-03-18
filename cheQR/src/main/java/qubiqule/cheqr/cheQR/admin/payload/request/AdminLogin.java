package qubiqule.cheqr.cheQR.admin.payload.request;


public class AdminLogin {
    private String username;
    private String password;
    
    public AdminLogin() {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "AdminLogin [username=" + username + ", password=" + password + "]";
    }

    
}
