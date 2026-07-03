public class PasswordEntry {
    String site, username, encodedPassword, notes;

    public PasswordEntry(String site, String username, String encodedPassword, String notes) {
        this.site = site;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.notes = notes != null ? notes : "";
    }

    public String getPlainPassword() {
        return Encoder.decode(encodedPassword);
    }
}