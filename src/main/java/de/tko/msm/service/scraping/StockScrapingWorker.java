package de.tko.msm.service.scraping;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import de.tko.msm.model.StockDo;
import de.tko.msm.service.exception.ScrapingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URL;

@ApplicationScoped
@Slf4j
public class StockScrapingWorker {

    private static final String BASE_URL = "https://www.finanzen.net/";

    public StockDo scrapeStock(String wknOrIsin) throws ScrapingException {
        if (StringUtils.isEmpty(wknOrIsin)) {
            throw new ScrapingException("WKN or ISIN must be provided.");
        }

        try {
            val targetUri = UriBuilder.fromUri(BASE_URL)
                    .path("suchergebnis.asp")
                    .queryParam("_search", wknOrIsin)
                    .build();

            val pageContent = getPageAsXml(targetUri.toURL());
            if (pageContent.isBlank()) {
                throw new ScrapingException("Unable to parse page from URI: " + targetUri);
            }

            val doc = Jsoup.parse(pageContent, targetUri.toString());

            return StockDo.builder()
                    .name(extractName(doc))
                    .wkn(extractWkn(doc))
                    .isin(extractIsin(doc))
                    .build();

        } catch (IOException e) {
            throw new ScrapingException("Unable to scrape for: " + wknOrIsin, e);
        }
    }

    private String extractName(Document document) throws ScrapingException {

        var stockName = getHeadlineTextContainerNode(document)
                .childNodes()
                .stream()
                .filter(node -> node instanceof TextNode)
                .map(node -> (TextNode) node)
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find Name in text-container for given document on page: " + document.baseUri()));

        if(stockName.text().contains("Aktie")) {
            return stockName.text().substring(0, stockName.text().indexOf("Aktie") - 1).trim();
        }

        return stockName.text().trim();
    }

    private String extractWkn(Document document) throws ScrapingException {

        val h1Container = getHeadlineTextContainerNode(document)
                .childNodes()
                .stream()
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node)
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find WKN in text-container for given document on page: " + document.baseUri()));

        val wkn = h1Container
                .childNodes()
                .stream()
                .filter(node -> node instanceof TextNode)
                .map(node -> (TextNode) node)
                .filter(textNode -> textNode.text().contains("WKN"))
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find WKN in text-container for given document on page: " + document.baseUri()));

        return wkn.text().replace("WKN:", "").trim();
    }

    private String extractIsin(Document document) throws ScrapingException {

        val h1Container = getHeadlineTextContainerNode(document)
                .childNodes()
                .stream()
                .filter(node -> node instanceof Element)
                .map(node -> (Element) node)
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find WKN in text-container for given document on page: " + document.baseUri()));

        val isinContainer = h1Container
                .childNodes()
                .stream()
                .filter(node -> node instanceof TextNode)
                .map(node -> (TextNode) node)
                .filter(textNode -> textNode.text().contains("ISIN"))
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find WKN in text-container for given document on page: " + document.baseUri()));

        val isinSubs = isinContainer.text().substring(isinContainer.text().indexOf("ISIN:"));

        return isinSubs.replace("ISIN:", "").trim();
    }

    /**
     * The headline of finanzen.net contains the WKN, ISIN and Stock name. This method returns the appropriate container for it.
     *
     * @param document - root document.
     * @return Node which contains name, wkn and isin.
     * @throws ScrapingException raise this exception with self explained description as it could not find the node.
     */
    private Node getHeadlineTextContainerNode(Document document) throws ScrapingException {
        val headline = document.select(".snapshot-headline")
                .stream()
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find headline for given document on page: " + document.baseUri()));


        val children = headline
                .childNodes()
                .stream()
                .filter(node -> node instanceof Element)
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find headline-children for given document on page: " + document.baseUri()));

        return children
                .childNodes()
                .stream()
                .filter(subChild -> subChild instanceof Element)
                .findFirst()
                .orElseThrow(() -> new ScrapingException("Unable to find text-container in child node for given document on page: " + document.baseUri()));
    }

    protected String getPageAsXml(URL targetUrl) throws IOException {
        try (val webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getCookieManager().setCookiesEnabled(false);
            webClient.getOptions().setTimeout(10 * 1000); // Set the connection timeout
            webClient.getOptions().setDownloadImages(false);
            webClient.getOptions().setGeolocationEnabled(false);
            webClient.getOptions().setAppletEnabled(false);

            HtmlPage page = webClient.getPage(targetUrl);
//            webClient.waitForBackgroundJavaScript(
//                    30 * 1000); // Wait for js to execute in the background for 30 seconds
            webClient.setJavaScriptTimeout(35 * 1000);

            return page.asXml();
        }
    }
}
