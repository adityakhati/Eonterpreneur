package Group_Activities;

public class Group_Users {
    String uid;
    String name;

    public Group_Users(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group_Users(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
}
