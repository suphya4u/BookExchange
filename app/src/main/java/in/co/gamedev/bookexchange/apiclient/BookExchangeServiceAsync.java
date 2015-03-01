package in.co.gamedev.bookexchange.apiclient;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import in.co.gamedev.bookexchange.common.Constants;
import in.co.gamedev.server.bookexchange.bookExchangeService.BookExchangeService;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.AddBookRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.AddBookResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookSearchRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookSearchResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.ChangeExchangeApprovalRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.ChangeExchangeApprovalResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.FetchExchangeDetailsRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.FetchExchangeDetailsResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.GetBookListRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.GetBookListResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.RegisterUserRequest;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.RegisterUserResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.ServiceResponse;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.UpdateLocationRequest;

/**
 * Created by suhas on 2/20/2015.
 */
public class BookExchangeServiceAsync {

  private static BookExchangeServiceAsync selfInstance = null;
  private static BookExchangeService bookExchangeService = null;

  private BookExchangeServiceAsync() {
  }

  public static BookExchangeServiceAsync getInstance() {
    if (selfInstance == null) {
      selfInstance = new BookExchangeServiceAsync();
    }
    return selfInstance;
  }

  public RegisterUserResponse registerUser(RegisterUserRequest registerUserRequest)
      throws IOException {
    final BookExchangeService service = getService();
    return service.registerUser(registerUserRequest).execute();
  }

  public ServiceResponse updateLocation(UpdateLocationRequest updateLocationRequest)
      throws IOException {
    final BookExchangeService service = getService();
    return service.updateLocation(updateLocationRequest).execute();
  }

  public BookSearchResponse searchBook(BookSearchRequest bookSearchRequest) throws IOException {
    final BookExchangeService service = getService();
    return service.searchBook(bookSearchRequest).execute();
  }

  public GetBookListResponse getBookList(final GetBookListRequest getBookListRequest)
      throws IOException {
    final BookExchangeService service = getService();
    return service.fetchBookList(getBookListRequest).execute();
  }

  public AddBookResponse addBookToList(final AddBookRequest addBookRequest) throws IOException {
    final BookExchangeService service = getService();
    return service.addBookToList(addBookRequest).execute();
  }

  public FetchExchangeDetailsResponse fetchExchangeDetails(
      final FetchExchangeDetailsRequest fetchExchangeDetailsRequest) throws IOException {
    final BookExchangeService service = getService();
    return service.fetchExchangeDetails(fetchExchangeDetailsRequest).execute();
  }

  public ChangeExchangeApprovalResponse changeExchangeApproval(
      final ChangeExchangeApprovalRequest changeExchangeApprovalRequest) throws IOException {
    final BookExchangeService service = getService();
    return service.changeExchangeApproval(changeExchangeApprovalRequest).execute();
  }

  private BookExchangeService getService() {
    if (bookExchangeService != null) {
      return bookExchangeService;
    }
    BookExchangeService.Builder serviceBuilder = new BookExchangeService.Builder(
        AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
    if (!Constants.IS_LOCAL) {
      serviceBuilder.setRootUrl(Constants.SERVER_URL);
    } else {
      // Need setRootUrl and setGoogleClientRequestInitializer only for local testing.
      serviceBuilder.setRootUrl(Constants.LOCAL_SERVER)
          .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                throws IOException {
              abstractGoogleClientRequest.setDisableGZipContent(true);
            }
          });
    }
    return serviceBuilder.build();
  }
}
