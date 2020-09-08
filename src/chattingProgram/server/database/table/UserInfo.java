package chattingProgram.server.database.table;

import java.io.Serializable;

public class UserInfo implements Serializable {
    public String id;
    public String password;
    
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", password=" + password + "]";
	}
}
