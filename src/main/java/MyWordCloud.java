import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.*;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;

import java.util.List;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;


import java.util.logging.Logger;
import java.util.logging.*;

// TODO: put everything in seperate classes
public class MyWordCloud {
    public static void main(String[] args) {
        Logger logger = Logger.getAnonymousLogger();
        logger.setLevel(Level.FINER);
        URL res = null;
        File file = null;
        // Read the web data
        String text = null;
        final Dimension dimension = new Dimension(500, 312);
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);
        List<WordFrequency> wordFrequencies = null;

        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(0);

        wordCloud.setPadding(2);

        logger.log(Level.INFO,"Get webpage");

        text = getWebpageText();

        // Determine the frequency of the words
        wordFrequencies = getWordFrequencies(text, wordFrequencies, frequencyAnalyzer);





        // Generate a wordcloud



        // Load the mask for the wordcloud on which the words will be projected
        res = MyWordCloud.class.getResource("mask.png");

        try{
            file = Paths.get(res.toURI()).toFile();
        } catch(java.net.URISyntaxException ex){
            System.out.println("URI could not be read.");
        }

        // Save the wordcloud
        try{
            wordCloud.setBackground(new PixelBoundryBackground(file));
            wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
            wordCloud.setFontScalar(new LinearFontScalar(10, 40));
            wordCloud.build(wordFrequencies);

            // Ask the user to give the output path
            Scanner sc = new Scanner(System.in);
            System.out.println("Give the path to the output file.");
            String path = sc.nextLine();
            wordCloud.writeToFile(path); // "C:/Users/Robert/Pictures/wordcloud_small.png"
        } catch(IOException ex){
            System.out.println("File could not be written.");
        }
    }
    public static String getWebpageText(){
        Document doc = null;
        try{
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/Big_data").get();
        } catch(java.io.IOException ex){
            System.out.println("Webpage could not be read.");
        }
        String text = doc.body().text();
        return text;
    }

    public static List<WordFrequency> getWordFrequencies(String text, List<WordFrequency> wordFrequencies, FrequencyAnalyzer frequencyAnalyzer){
        try{
            InputStream targetStream = new ByteArrayInputStream(text.getBytes());
            wordFrequencies = frequencyAnalyzer.load(targetStream);
        } catch(IOException ex){
            System.out.println("Frequencies could not be loaded.");
        }
        return wordFrequencies;
    }
}
