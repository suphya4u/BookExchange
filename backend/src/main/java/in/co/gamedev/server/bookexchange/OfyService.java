package in.co.gamedev.server.bookexchange;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import in.co.gamedev.server.bookexchange.data.storage.BookData;
import in.co.gamedev.server.bookexchange.data.storage.UserBook;
import in.co.gamedev.server.bookexchange.data.storage.UserData;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 */
public class OfyService {

  static {
    ObjectifyService.register(UserData.class);
    ObjectifyService.register(BookData.class);
  }

  public static Objectify ofy() {
    return ObjectifyService.ofy();
  }

  public static ObjectifyFactory factory() {
    return ObjectifyService.factory();
  }
}
