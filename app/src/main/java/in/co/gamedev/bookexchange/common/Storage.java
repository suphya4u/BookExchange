package in.co.gamedev.bookexchange.common;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;

import in.co.gamedev.server.bookexchange.bookExchangeService.model.BookData;

/**
 * Created by suhas on 2/23/2015.
 */
public class Storage<T> {

  public void save(Context context, String fileName, T data) throws IOException {
    FileOutputStream fos = null;
      fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      ObjectOutputStream os = new ObjectOutputStream(fos);
      os.writeObject(data);
      os.close();
      fos.close();
  }

  public T read(Context context, String fileName) throws IOException, ClassNotFoundException {
    FileInputStream fis = null;
      fis = context.openFileInput(fileName);
      ObjectInputStream is = new ObjectInputStream(fis);
      T data = (T) is.readObject();
      is.close();
      fis.close();
      return data;
  }
}
