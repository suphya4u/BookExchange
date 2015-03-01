package in.co.gamedev.server.bookexchange;

/**
 * Created by suhas on 2/23/2015.
 */
public class Constants {

  /**
   * Api Keys can be obtained from the google cloud console
   */
  public static final String API_KEY = System.getProperty("gcm.api.key");

  public static final String BOOK_LIST_TYPE_MY_BOOKS = "MY_BOOKS";
  public static final String BOOK_LIST_TYPE_EXPECTED_BOOKS = "EXPECTED_BOOKS";

  public static final String NOTIFICATION_TYPE = "notificationType";
  public static final String NOTIFICATION_TYPE_UPDATE_ON_EXCHANGE = "UPDATE_ON_EXCHANGE";
  public static final String NOTIFICATION_KEY_TITLE = "NOTIFICATION_TITLE";
  public static final String NOTIFICATION_KEY_TEXT = "NOTIFICATION_TEXT";
}
