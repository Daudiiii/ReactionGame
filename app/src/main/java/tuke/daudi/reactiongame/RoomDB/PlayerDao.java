package tuke.daudi.reactiongame.RoomDB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM Player")
    List<Player> getAll();

    @Query("SELECT * FROM Player WHERE Id LIKE :Id")
    Player getById(int Id);

    @Insert
    void insertPlayers(Player... players);

    @Delete
    void deletePlayers(Player player);


}
