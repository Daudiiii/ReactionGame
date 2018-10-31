package tuke.daudi.reactiongame.RoomDB;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.lang.ref.WeakReference;

public class DBTools {
    private static PlayerDatabase db;
    public DBTools(WeakReference<Context> refContext){
        getDbContext(refContext);
    }
    public static PlayerDatabase getDbContext(WeakReference<Context> refContext){
        if(db !=null)
            return db;
        return Room.databaseBuilder(refContext.get(),PlayerDatabase.class,"player-db").build();
    }
}
