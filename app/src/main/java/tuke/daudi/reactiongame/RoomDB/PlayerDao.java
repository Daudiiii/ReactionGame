package tuke.daudi.reactiongame.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM Player order by score")
    List<Player> getAll();

    @Insert
    void insertPlayers(Player... players);

    @Delete
    void deletePlayers(Player player);

    @Query("SELECT * FROM Player WHERE psc = :psc order by score")
    List<Player> searchByPsc(String psc);

    @Query("select * from Player where nick = :nick order by score")
    List<Player> searchByNick(String nick);

    @Query("select * from Player where nick = :nick and psc = :psc order by score")
    List<Player> searchByNickAndPsc(String nick, String psc);

}
