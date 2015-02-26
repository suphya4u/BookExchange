package in.co.gamedev.bookexchange.common;

import java.io.Serializable;

import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookData;

/**
 * Created by suhas on 2/23/2015.
 */
public class SerializableBook implements Serializable {

  private String bookId;
  private String title;
  private String author;
  private String imageURL;
  private String thumbnailUrl;
  private long isbnNumber;
  private float priceInUsd;

  public SerializableBook() {}

  public SerializableBook(BookData bookData) {
    setAuthor(bookData.getAuthor());
    setBookId(bookData.getBookId());
    setTitle(bookData.getTitle());
    setImageURL(bookData.getImageURL());
    setThumbnailUrl(bookData.getThumbnailUrl());
    setIsbnNumber(bookData.getIsbnNumber());
    setPriceInUsd(bookData.getPriceInUsd());
  }

  public BookData getBookData() {
    return new BookData()
    .setAuthor(getAuthor())
    .setBookId(getBookId())
    .setTitle(getTitle())
    .setImageURL(getImageURL())
    .setThumbnailUrl(getThumbnailUrl())
    .setIsbnNumber(getIsbnNumber())
    .setPriceInUsd(getPriceInUsd());
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public String getThumbnailUrl() {
    return thumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    this.thumbnailUrl = thumbnailUrl;
  }

  public long getIsbnNumber() {
    return isbnNumber;
  }

  public void setIsbnNumber(long isbnNumber) {
    this.isbnNumber = isbnNumber;
  }

  public float getPriceInUsd() {
    return priceInUsd;
  }

  public void setPriceInUsd(float priceInUsd) {
    this.priceInUsd = priceInUsd;
  }
}
