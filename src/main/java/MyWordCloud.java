import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.*;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;

import java.util.ArrayList;
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

// TODO: put frequency analyzer, wordcloud and file I/O in seperate classes
// TODO: maybe make my own freqyency analyzer?
public class MyWordCloud {

    private static Logger logger = Logger.getLogger(MyWordCloud.class.getName());

    public static void main(String[] args) {
        String text;
        List<WordFrequency> wordFrequencies = new ArrayList<WordFrequency>();
        final Dimension dimension = new Dimension(500, 312);
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.RECTANGLE);

        frequencyAnalyzer = initFrequencyAnalyzer(frequencyAnalyzer);
        wordCloud = initWordCloud(wordCloud);

        // Get webpage data.
        text = getWebpageText();

        // Determine the frequency of the words
        wordFrequencies = getWordFrequencies(text, wordFrequencies, frequencyAnalyzer);

        // Generate a wordcloud
        generateWordCloud(wordCloud, wordFrequencies);
    }


    public static FrequencyAnalyzer initFrequencyAnalyzer(FrequencyAnalyzer frequencyAnalyzer){
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        frequencyAnalyzer.setMinWordLength(0);
        return frequencyAnalyzer;
    }


    public static WordCloud initWordCloud(WordCloud wordCloud){
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.setPadding(2);
        return wordCloud;
    }


    public static String getWebpageText(){
        logger.log(Level.INFO,"Getting webpage data.");
        Document doc = null;
        try{
            doc = Jsoup.connect("https://en.wikipedia.org/wiki/Big_data").get();
        } catch(java.io.IOException ex){
            logger.log(Level.INFO, "Webpage could not be read.");
        }
        String text = doc.body().text();
        return text;
    }


    public static List<WordFrequency> getWordFrequencies(String text, List<WordFrequency> wordFrequencies, FrequencyAnalyzer frequencyAnalyzer){
        logger.log(Level.INFO, "Calculating word frequencies.");
        try{
            InputStream targetStream = new ByteArrayInputStream(text.getBytes());
            wordFrequencies = frequencyAnalyzer.load(targetStream);
        } catch(IOException ex){
            logger.log(Level.SEVERE, "Frequencies could not be loaded.");
        }
        return wordFrequencies;
    }


    public static void generateWordCloud(WordCloud wordCloud, List<WordFrequency> wordFrequencies){
        // Load the mask for the wordcloud on which the words will be projected
        File file = null;
        file = getMask(file);

        // Save the wordcloud
        try{
            wordCloud.setBackground(new PixelBoundryBackground(file));
            wordCloud.build(wordFrequencies);

            String path = getUserFileLocation();

            wordCloud.writeToFile(path); // "C:/Users/Robert/Pictures/wordcloud_small.png"
            System.out.println("Wordcloud saved.");
        } catch(IOException ex){
            logger.log(Level.SEVERE, "Wordcloud could not be saved.");
        }
    }


    public static String getUserFileLocation(){
        // Ask the user to give the output path
        Scanner sc = new Scanner(System.in);
        System.out.println("Give the path to the output file.");
        String path = sc.nextLine();
        return path;
    }


    public static File getMask(File file){
        URL res;
        res = MyWordCloud.class.getResource("mask.png");
        try{
            //TODO fix file
            file = Paths.get(res.toURI()).toFile();
        } catch(java.net.URISyntaxException ex){
            logger.log(Level.SEVERE, "URI could not be read.");
        }
        return file;
    }

}
