package tuke.daudi.reactiongame.RoomDB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Player.class}, version = 1)
public abstract class PlayerDatabase extends RoomDatabase {
    public abstract PlayerDao getDao();
}
