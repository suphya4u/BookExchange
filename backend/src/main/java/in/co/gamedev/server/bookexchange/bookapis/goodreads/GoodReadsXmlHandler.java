package in.co.gamedev.server.bookexchange.bookapis.goodreads;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import in.co.gamedev.server.bookexchange.data.storage.BookData;

/**
 * Created by suhas on 2/23/2015.
 */
public abstract class GoodReadsXmlHandler<T> extends DefaultHandler {

  protected String currentPosition;

  public GoodReadsXmlHandler() {
    currentPosition = "";
  }

  public void startElement(String uri, String localName,String qName, Attributes attributes)
      throws SAXException {
    currentPosition = currentPosition + qName + ":";
    handleStartTag(qName);
  }

  public void endElement(String uri, String localName, String qName)
      throws SAXException {

    String tokens[] = currentPosition.split(":");
    if (tokens.length > 0) {
      if (tokens[tokens.length - 1].equalsIgnoreCase(qName)) {
        String newPosition = "";
        for (int i = 0; i < tokens.length - 1; i++) {
          newPosition = newPosition + tokens[i] + ":";
        }
        currentPosition = newPosition;
      }
    }
    handleEndTag(qName);
  }

  public void characters(char ch[], int start, int length) throws SAXException {
    handleData(new String(ch, start, length));
  }

  abstract void handleStartTag(String tag);
  abstract void handleEndTag(String tag);
  abstract void handleData(String s);
  abstract T getResult();
}
