package tuke.daudi.reactiongame;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import tuke.daudi.reactiongame.RoomDB.DBTools;
import tuke.daudi.reactiongame.RoomDB.Player;

public class DbGetData extends AsyncTask<Player,Integer,List<Player>> {
    private WeakReference<Context> activity;
    public DbGetData(WeakReference<Context> activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<Player> players) {
        super.onPostExecute(players);

    }

    @Override
    protected List<Player> doInBackground(Player... players) {
        DBTools.getDbContext(activity).getDao().insertPlayers(players);
        return DBTools.getDbContext(activity).getDao().getAll();
    }

}