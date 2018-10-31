package tuke.daudi.reactiongame.RoomDB;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Player {

    public Player() {
    }

    public Player(String psc, String nick, String age, String gender, String glasses) {
        this.psc = psc;
        this.nick = nick;
        this.age = age;
        this.gender = gender;
        this.glasses = glasses;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="psc")
    private String psc;
    @ColumnInfo(name="nick")
    private String nick;
    @ColumnInfo(name="age")
    private String age;
    @ColumnInfo(name="gender")
    private String gender;
    @ColumnInfo(name="glasses")
    private String glasses;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGlasses() {
        return glasses;
    }

    public void setGlasses(String glasses) {
        this.glasses = glasses;
    }
}
