package utils.authorization;

public abstract class AuthForm {
    public String username;
    public String password;

    public AuthForm() {

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }
}
