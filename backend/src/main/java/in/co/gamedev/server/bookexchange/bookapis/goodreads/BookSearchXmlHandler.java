package in.co.gamedev.server.bookexchange.bookapis.goodreads;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import in.co.gamedev.server.bookexchange.data.storage.BookData;

/**
 * Created by suhas on 2/23/2015.
 */
public class BookSearchXmlHandler extends GoodReadsXmlHandler<List<BookData>> {

  private static final String TOKEN_PREFIX = "GoodreadsResponse:search:results:work:best_book:";

  private BookData currentBook;
  private List<BookData> bookList;

  BookSearchXmlHandler() {
    this.bookList = new ArrayList();
    currentBook = null;
  }

  @Override
  void handleStartTag(String tag) {
    if (tag.equalsIgnoreCase("best_book")) {
      currentBook = new BookData();
    }
  }

  @Override
  void handleEndTag(String tag) {
    if (tag.equalsIgnoreCase("best_book")
        && currentBook.getBookId() != null
        && currentBook.getTitle() != null) {
      bookList.add(currentBook);
    }
  }

  @Override
  void handleData(String data) {
    if (currentBook == null) {
      return;
    }
    if (currentPosition.equalsIgnoreCase(TOKEN_PREFIX + "id:")) {
      currentBook.setBookId(data);
    } else if (currentPosition.equalsIgnoreCase(TOKEN_PREFIX + "title:")) {
      currentBook.setTitle(data);
    } else if (currentPosition.equalsIgnoreCase(TOKEN_PREFIX + "author:name:")) {
      currentBook.setAuthor(data);
    } else if (currentPosition.equalsIgnoreCase(TOKEN_PREFIX + "image_url:")) {
      currentBook.setImageURL(data);
    } else if (currentPosition.equalsIgnoreCase(TOKEN_PREFIX + "small_image_url:")) {
      currentBook.setThumbnailUrl(data);
    }
  }

  @Override
  List<BookData> getResult() {
    return bookList;
  }
}
