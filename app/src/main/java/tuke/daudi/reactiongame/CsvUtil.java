package tuke.daudi.reactiongame;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvUtil {

    private File root;

    public CsvUtil(File enviroment) {
    }

    public void write(String nick, String age, String psc, String gender,
                              String glass, float x, float y, long time)
    {
        try
        {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root, "data.csv");
            FileWriter writer = new FileWriter(file, true);
            if(!file.exists()){
                writer.append("nick, age, psc, gender, glass, x, y, time");
            }
            writer.append(nick + ", " + age + ", " + psc + ", " + gender + ", " + glass
                    + " , " + x + ", " + y+ ", " +time);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
