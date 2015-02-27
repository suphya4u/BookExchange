package in.co.gamedev.bookexchange.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.co.gamedev.bookexchange.R;
import in.co.gamedev.server.bookexchange.bookExchangeService.model.Exchange;

/**
 * Created by suhas on 2/26/2015.
 */
public class ExchangeListRecyclerAdapter
    extends RecyclerView.Adapter<ExchangeListRecyclerAdapter.ExchangeItemViewHolder> {

  private final List<Exchange> exchangeList;

  public ExchangeListRecyclerAdapter(List<Exchange> exchangeList) {
    this.exchangeList = exchangeList;
  }

  public void addItems(List<Exchange> books) {
    exchangeList.addAll(books);
    notifyDataSetChanged();
  }

  @Override
  public ExchangeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.exchange_list_item, parent, false);
    final ExchangeItemViewHolder viewHolder = new ExchangeItemViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ExchangeItemViewHolder holder, int position) {
    holder.setExchange(exchangeList.get(position));
  }

  @Override
  public int getItemCount() {
    return exchangeList.size();
  }

  public class ExchangeItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView pickupBookThumbnail;
    private TextView pickupBookTitle;
    private TextView pickupBookAuthor;
    private ImageView dropBookThumbnail;
    private TextView dropBookTitle;
    private TextView dropBookAuthor;

    public ExchangeItemViewHolder(View itemView) {
      super(itemView);
      pickupBookThumbnail = (ImageView) itemView.findViewById(R.id.pickup_book_thumbnail);
      pickupBookTitle = (TextView) itemView.findViewById(R.id.pickup_book_title);
      pickupBookAuthor = (TextView) itemView.findViewById(R.id.pickup_book_author);

      dropBookThumbnail = (ImageView) itemView.findViewById(R.id.drop_book_thumbnail);
      dropBookTitle = (TextView) itemView.findViewById(R.id.drop_book_title);
      dropBookAuthor = (TextView) itemView.findViewById(R.id.drop_book_author);
    }

    private void setExchange(Exchange exchange) {
      pickupBookTitle.setText(exchange.getPickupBook().getTitle());
      pickupBookAuthor.setText(exchange.getPickupBook().getAuthor());
      new ImageLoaderTask(exchange.getPickupBook().getThumbnailUrl(), pickupBookThumbnail).execute();

      dropBookTitle.setText(exchange.getDropBook().getTitle());
      dropBookAuthor.setText(exchange.getDropBook().getAuthor());
      new ImageLoaderTask(exchange.getDropBook().getThumbnailUrl(), dropBookThumbnail).execute();
    }
  }
}