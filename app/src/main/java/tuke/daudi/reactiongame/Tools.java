package tuke.daudi.reactiongame;

import java.util.ArrayList;
import java.util.List;

import tuke.daudi.reactiongame.RoomDB.Player;

public class Tools {
    public static List<Player> getMovieData(){
        List<Player> outputModel = new ArrayList<>();
//        for(int i=0;i<50;i++)
//        {
//            outputModel.add(new Player(
//                    "Nick: " + Integer.toString(i),
//                    "Released " + Integer.toString(i),
//                    "Director " + Integer.toString(i)
//            ));
//        }
        return outputModel;
    }
}
