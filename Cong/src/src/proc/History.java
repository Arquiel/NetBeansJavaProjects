package src.proc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class History {
    String csvFileName;
    private int start_y = 135;
    private int spacing_y = 17;
    private int maxrecords = 15;
    private String[] records;
    String abspath = new File("").getAbsolutePath();
    File historyFile = new File(abspath + "/" + csvFileName);
    File tempFile = new File(abspath + "/" + csvFileName);
    
    public History(String csvFileName){
        this.csvFileName = csvFileName;
        // Load history data into memory
        loadHistory();
    }
    
    public void render(Graphics g){
        // Draw History contents table
        Font fnt0 = new Font("arial", Font.ITALIC, 10);
        g.setFont(fnt0);
        g.setColor(Color.green);
        g.drawString("(LAST 15 GAMES)", (Cong.WIDTH * Cong.SCALE) / 2 - 45, start_y - 37);
        Font fnt1 = new Font("arial", Font.BOLD, 12);
        g.setFont(fnt1);
        g.drawString("DATE", 25, start_y);
        g.drawString("P1", 100, start_y);
        g.drawString("P2", 300, start_y);
        g.drawString("SCORE", 500, start_y);
        g.drawString("DURATION", 550, start_y);
        
        String line = "";
        String cvsSplitBy = ",";
        int gameCount = 0;
        
        Font fnt2 = new Font("arial", Font.ITALIC, 10);
        g.setFont(fnt2);
        String[] record;
        for (int i = 0; i< records.length; i++) {
            if(records[i] != null){
                gameCount++;
                // use comma as separator
                record = records[i].split(cvsSplitBy);
                g.drawString(record[0], 25, start_y + (spacing_y * gameCount));
                g.drawString(record[1], 100, start_y + (spacing_y * gameCount));
                g.drawString(record[2], 300, start_y + (spacing_y * gameCount));
                g.drawString(record[3], 500, start_y + (spacing_y * gameCount));
                g.drawString(record[4], 550, start_y + (spacing_y * gameCount));
            }
        }
    }
    
    public void loadHistory(){
        BufferedReader br = null;
        InputStream is = null;
        InputStreamReader isr = null;
            
            try {
                if(historyFile.exists()){
                    // Read History log from generated file
                    System.out.println("generated file read");
                    br = new BufferedReader(new FileReader(historyFile));
                }
                else{
                    // Read History log from internal file
                    System.out.println("internal file read");
                    is = getClass().getResourceAsStream("/" + csvFileName);
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                }
                String linecontents = "";
                int lineCount = 0;
                while ((linecontents = br.readLine()) != null){ 
                    System.out.println(linecontents);
                    lineCount++;
                }

                br.close();

                // max out how many lines are read
                if(lineCount > maxrecords){
                    lineCount = maxrecords;
                }

                records = new String[lineCount];
                lineCount = 0;
                String line;

                is = getClass().getResourceAsStream("/" + csvFileName);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                while ((line = br.readLine()) != null){ 
                    if((lineCount + 1)< records.length){
                        records[lineCount] = line;
                    }
                    lineCount++;
                }

            } 
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } 
            catch (IOException e) {
                e.printStackTrace();
            } 
            finally {
                if (br != null) {
                    try {
                        br.close();
                    } 
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        
           
    }
    
    public void addHistory(String date, String name1, String name2, String score, String duration) throws IOException {
        // Add a new record to the top of the log
        String savedGame = date + "," + name1 + "," + name2 + "," + score + "," + duration;
        String temp = "temp-history.csv";
        String p = null;
        BufferedReader br = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedWriter bw = null;
        
        try {
            // Create temp file to combine old data with new record
            if(historyFile.exists()){
                // Read History log from generated file
                
                br = new BufferedReader(new FileReader(historyFile));
            }
            else{
                // Read History log from internal file
                
                is = getClass().getResourceAsStream("/" + csvFileName);
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
            }
            
            bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write(savedGame);
            bw.newLine();
            while ((p = br.readLine()) != null ){
                bw.write(p);
                bw.newLine();
            }
            br.close();
            bw.close();

            // Overwrite old file with temp file contents
//            is = getClass().getResourceAsStream("/" + temp);
//            isr = new InputStreamReader(is);
//            br = new BufferedReader(isr);
//            System.out.println(new File("").getAbsolutePath() + "/res");
            
            br = new BufferedReader(new FileReader(tempFile));
            bw = new BufferedWriter(new FileWriter(historyFile));
            p = null;
            while ((p = br.readLine()) != null ){
                bw.write(p);
                bw.newLine();
            }
            br.close();
            bw.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            if (br != null) {
                try {
                    br.close();
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }   
        // Reload the history data into memory
        loadHistory();
    }
}
