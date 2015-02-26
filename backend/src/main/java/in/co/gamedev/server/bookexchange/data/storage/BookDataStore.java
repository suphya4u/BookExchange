package in.co.gamedev.server.bookexchange.data.storage;

import com.google.common.base.Preconditions;

import in.co.gamedev.server.bookexchange.api.messages.RegisterUserRequest;

import static in.co.gamedev.server.bookexchange.OfyService.ofy;

/**
 * Created by suhas on 2/23/2015.
 */
public class BookDataStore {

  public BookData getBookData(String bookId) {
    return ofy().load().type(BookData.class).id(bookId).now();
  }

  public void createOrUpdateBookData(BookData bookData) {
    Preconditions.checkNotNull(bookData.getBookId(), "Provide book id");
    ofy().save().entity(bookData).now();
  }
}
