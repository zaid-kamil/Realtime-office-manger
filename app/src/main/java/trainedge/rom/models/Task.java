package trainedge.rom.models;

import com.google.firebase.database.DataSnapshot;


public class Task {

    private final String key;

    public boolean getStatus() {
        return status;
    }

    String from;
    String email;
    String task;
    String type;
    Boolean seen;
    Boolean status;

    public Task(DataSnapshot snapshot) {
        from = snapshot.child("from").getValue(String.class);
        email = snapshot.child("email").getValue(String.class);
        task = snapshot.child("task").getValue(String.class);
        type = snapshot.child("type").getValue(String.class);
        seen = snapshot.child("seen").getValue(Boolean.class);
        status = snapshot.child("status").getValue(Boolean.class);


        key = snapshot.getKey();
    }

    public String getKey() {
        return key;
    }

    public String getFrom() {
        return from;
    }

    public String getEmail() {
        return email;
    }

    public String getTask() {
        return task;
    }

    public String getType() {
        return type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
