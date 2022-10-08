package dto;

public class UserDto {

    private String userId;
    private String password;
    private String name;
    private String email;

    public UserDto(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isInAll() {
        if(userId.isEmpty()) {
            return false;
        }
        if(password.isEmpty()) {
            return false;
        }
        if(name.isEmpty()) {
            return false;
        }
        if(email.isEmpty()) {
            return false;
        }
        return true;
    }
}
