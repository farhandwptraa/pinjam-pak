package id.pinjampak.pinjam_pak.dto;

public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;

    // Getter & Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}