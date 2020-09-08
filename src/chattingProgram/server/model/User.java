package chattingProgram.server.model;

import chattingProgram.server.database.table.UserInfo;

public class User {
    private String id;

    public User(UserInfo userinfo){
        this.id = userinfo.id;
    }

    /* Getters and Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
