import java.util.HashSet;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MyWebCrawler {

    public HashSet<String> links;
    private static Logger logger = Logger.getLogger(MyWordCloud.class.getName());

    public MyWebCrawler() {
        links = new HashSet<String>();
    }

    /**
     * getPagelinks is called recursively to get all the links on a webpage.
     * @param URL: get all the links from this URL.
     * @param depth: links on a page are visited until a certain depth.
     */
    // TODO: multithreaded: links are retreived and the words could be read from the page immediately.
    public void getPageLinks(String URL, Integer depth, Integer counter) {

        // Exit the recursion when the depth is reached.
        if (counter > depth){
//            logger.log(Level.INFO, "Depth reached.");
            return;
        }

        counter += 1;
        // If the link has not already been visited.
        if (!links.contains(URL)) {
            // We do not need to visit images
            // TODO: filter for other strings?
            if (!URL.contains("png")) {
                try {
                    if (links.add(URL)) {
                        if (depth == 1){
                            logger.log(Level.INFO, URL);
                        }
                    }
                    // Fetch the HTML code
                    Document document = Jsoup.connect(URL).get();
                    // Parse the HTML to extract links to other URLs
                    Elements linksOnPage = document.select("a[href]");
                    // For each link, enter the recursion, and get the links on the current page.
                    for (Element page : linksOnPage) {
                        getPageLinks(page.attr("abs:href"), depth, counter);

                    }
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "For '" + URL + "': " + e.getMessage());
                }
            }
        }
    }

//    public static void main(String[] args) {
//        //1. Pick a URL from the frontier
//        new MyWebCrawler().getPageLinks("https://en.wikipedia.org/wiki/Big_data", 1, 0);
//    }
}

