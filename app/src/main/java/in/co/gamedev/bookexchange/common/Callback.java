package in.co.gamedev.bookexchange.common;

import in.co.gamedev.server.bookexchange.bookExchangeService.model.ServiceResponse;

/**
 * Created by suhas on 2/22/2015.
 */
public interface Callback {

  void call(ServiceResponse response);
}
