package main_app.utils;

import java.io.*;
import java.util.*;

public class FileManager
{
    public static ArrayList<String> readFile(String filePath)
    {
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line;

            while ((line = br.readLine()) != null)
                lines.add(line);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return lines;
    }

    public static void writeFile(String filePath, ArrayList<String> lines)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath)))
        {
            for (String line : lines)
            {
                bw.write(line);
                bw.newLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void appendFile(String filePath, String line)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true)))
        {
            bw.write(line);
            bw.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
