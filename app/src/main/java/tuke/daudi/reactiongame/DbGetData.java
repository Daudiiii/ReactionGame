package tuke.daudi.reactiongame;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import tuke.daudi.reactiongame.RoomDB.DBTools;
import tuke.daudi.reactiongame.RoomDB.Player;

public class DbGetData extends AsyncTask<Player,Integer,List<Player>> {
    private WeakReference<Context> activity;
    private String parameter1, parameter2;
    private boolean par1, par2;

    public DbGetData(WeakReference<Context> activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(List<Player> players) {
        super.onPostExecute(players);
        parameter1 = "";
        par1 = false; par2 = false;
        parameter2 = "";
    }

    @Override
    protected List<Player> doInBackground(Player... players) {
        DBTools.getDbContext(activity).getDao().insertPlayers(players);
        if(par1 && par2) {
            par1 = false; par2 = false;
            return DBTools.getDbContext(activity).getDao().searchByNickAndPsc(parameter1,parameter2);
        }
        else if(par1){
            par1 = false;
            return DBTools.getDbContext(activity).getDao().searchByNick(parameter1);
        } else if (par2){
            par2 = false;
            return DBTools.getDbContext(activity).getDao().searchByPsc(parameter2);
        }else
            return DBTools.getDbContext(activity).getDao().getAll();
    }


    public void setPar1(boolean par1) {
        this.par1 = par1;
    }

    public void setPar2(boolean par2) {
        this.par2 = par2;
    }

    public void setParameter(String parameter) {
        this.parameter1 = parameter;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public void setParams(String p1, String p2){
        par1 = !p1.equals("");
        par2 = !p2.equals("");

        setParameter(p1);
        setParameter2(p2);
    }
}