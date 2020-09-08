package chattingProgram.server.module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;

import chattingProgram.server.database.table.Letter;
import chattingProgram.server.database.table.UserInfo;
import chattingProgram.server.model.request.SignUpRequest;

public class ServerRepository {
    private final String USER_INFO_DATABASE = "./src/chattingProgram/server/database/UserInfo.table";
    private final String LETTER_DATABASE = "./src/chattingProgram/server/database/Letter.table";

    /**
     * 서버를 키기 전에 DB를 체크한다.
     * 만일 DB가 없다면 추가한다.
     */
    public synchronized  void checkDatabaseState() throws Exception{
        System.out.println("=====데이터베이스 검사중=====");

        if(!checkDatabsePath(USER_INFO_DATABASE)){
            createDatabaseTable(USER_INFO_DATABASE);
        }

        if(!checkDatabsePath(LETTER_DATABASE)){
            createDatabaseTable(LETTER_DATABASE);
        }

    }

    private boolean checkDatabsePath(String databasePath){
        try{
            ObjectInputStream check = new ObjectInputStream(new FileInputStream(databasePath));
            System.out.println("check status - ok : " + databasePath);
            check.close();
            return true;
        }catch(Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private boolean createDatabaseTable(String databasePath){
        try {
            ObjectOutputStream create = new ObjectOutputStream(new FileOutputStream(databasePath));
            String databaseName = databasePath.split("/")[databasePath.split("/").length -1];

            if(databaseName.equals("UserInfo.table")){
                HashSet<UserInfo> tableData  =new HashSet<UserInfo>();
                create.writeObject(tableData);
                System.out.println("createDatbase - ok : " + databasePath);
                return true;
            }
            if(databaseName.equals("Letter.table")){
                HashSet<Letter> tableData  =new HashSet<Letter>();
                create.writeObject(tableData);
                System.out.println("createDatbase - ok : " + databasePath);
                return true;
            }

            return false;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    /**
     * 만일 true로 return하면 로그인 성공.
     * false로 return하면 로그인 실패.
     */
    public synchronized boolean findUserInfo(String id, String password) throws Exception{
        ObjectInputStream fromDatabase = new ObjectInputStream(new FileInputStream(USER_INFO_DATABASE));
        HashSet<UserInfo> data = (HashSet<UserInfo>)fromDatabase.readObject();

        Iterator<UserInfo> itr = data.iterator();

        while(itr.hasNext()){
            UserInfo tmp = itr.next();
            if(tmp.id.equals(id) && tmp.password.equals(password)){
                return true;
            }
        }
        return false;
    }

    /**
     * 만일 true로 return하면 database에 해당 ID가 있는거고, 이때는 해당 ID를 쓸 수 없다.
     * false로 return하면 database에 해당 ID가 없다는 것이니, 써도된다.
     */
    public synchronized boolean findUserInfo(String id) throws Exception{
        ObjectInputStream fromDatabase = new ObjectInputStream(new FileInputStream(USER_INFO_DATABASE));
        HashSet<UserInfo> data = (HashSet<UserInfo>)fromDatabase.readObject();

        Iterator<UserInfo> itr = data.iterator();

        while(itr.hasNext()){
            UserInfo tmp = itr.next();
            if(tmp.id.equals(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * 회원가입 요청을 보낸 유저의 정보를 DB에 저장한다.
     */
    public synchronized boolean updateUserInfo(SignUpRequest request) throws Exception{
        ObjectInputStream fromDatabase = new ObjectInputStream(new FileInputStream(USER_INFO_DATABASE));
        HashSet<UserInfo> data = (HashSet<UserInfo>)fromDatabase.readObject();

        UserInfo toSave = new UserInfo();
        toSave.id = request.getId();
        toSave.password = request.getPassword();
        data.add(toSave);

        ObjectOutputStream toDatabase = new ObjectOutputStream(new FileOutputStream(USER_INFO_DATABASE));
        toDatabase.writeObject(data);

        return true;
    }

    /**
     * 유저 정보를 얻는다.
     */
    public synchronized UserInfo getUserInfo(String id) throws Exception{
        ObjectInputStream fromDatabase = new ObjectInputStream(new FileInputStream(USER_INFO_DATABASE));
        HashSet<UserInfo> data = (HashSet<UserInfo>)fromDatabase.readObject();

        Iterator<UserInfo> itr = data.iterator();

        while(itr.hasNext()){
            UserInfo tmp = itr.next();
            if(tmp.id.equals(id)){
                return tmp;
            }
        }
        return null;
    }

}
