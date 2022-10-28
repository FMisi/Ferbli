package ferbli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Irdki {
    private static final File file = new File("src\\ferbli\\ki.txt");
    public static void Fajl(String data){
        FileWriter fr = null;
        try{
            fr = new FileWriter(file);
            fr.write(data + "\n");
        }
        catch (IOException e) {
            System.err.println("Oof! :(");
        }
        finally{
            try{
                if(fr != null){
                    fr.close();
                }
            }
            catch (IOException e) {
                System.err.println("Oof!!! :(");
            }
        }
    }
}
