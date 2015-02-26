package in.co.gamedev.server.bookexchange.data.storage;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;

/**
 * Created by suhas on 2/23/2015.
 */
@Entity
public class BookData implements Serializable {

  @Id
  private String bookId;

  private String title;
  private String author;
  private String imageURL;
  private String thumbnailUrl;
  private long isbnNumber;
  private float priceInUsd;

  public String getBookId() {
    return bookId;
  }

  public BookData setBookId(String bookId) {
    this.bookId = bookId;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public BookData setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getAuthor() {
    return author;
  }

  public BookData setAuthor(String author) {
    this.author = author;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public BookData setImageURL(String imageURL) {
    this.imageURL = imageURL;
    return this;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public BookData setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
    return this;
  }

  public long getIsbnNumber() {
    return isbnNumber;
  }

  public BookData setIsbnNumber(long isbnNumber) {
    this.isbnNumber = isbnNumber;
    return this;
  }

  public float getPriceInUsd() {
    return priceInUsd;
  }

  public BookData setPriceInUsd(float priceInUsd) {
    this.priceInUsd = priceInUsd;
    return this;
  }
}
