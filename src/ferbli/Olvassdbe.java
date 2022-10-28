package ferbli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Olvassdbe{

    public static List<String> Fajl(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            return reader.lines().collect(Collectors.toCollection(ArrayList::new));
        }
        catch (IOException e){
            System.err.println("Elszurtad : (");
            return new ArrayList<>();
        }
    }
}
