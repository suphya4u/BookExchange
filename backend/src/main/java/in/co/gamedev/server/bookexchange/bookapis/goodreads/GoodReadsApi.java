package in.co.gamedev.server.bookexchange.bookapis.goodreads;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import in.co.gamedev.server.bookexchange.bookapis.BookApis;
import in.co.gamedev.server.bookexchange.data.storage.BookData;

/**
 * Created by suhas on 2/23/2015.
 */
public class GoodReadsApi extends BookApis {
  private static final Logger log = Logger.getLogger(GoodReadsApi.class.getName());

  public List<BookData> search(String query) {
    String urlStr = null;
    try {
      urlStr = GoodReadsConstants.SEARCH_URL + URLEncoder.encode(query, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    BookSearchXmlHandler xmlHandler = new BookSearchXmlHandler();
    callApi(urlStr, xmlHandler);
    return xmlHandler.getResult();
  }

  private void callApi(String urlStr, DefaultHandler xmlHandler) {
    URL url = null;
    try {
      url = new URL(urlStr);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      InputStream stream = url.openStream();
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(stream, xmlHandler);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
