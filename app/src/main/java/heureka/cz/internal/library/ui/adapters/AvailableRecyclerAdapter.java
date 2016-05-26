package heureka.cz.internal.library.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.repository.BookAvailable;

/**
 * Created by tomas on 27.4.16.
 */
public class AvailableRecyclerAdapter extends RecyclerView.Adapter<AvailableRecyclerAdapter.ViewHolder>  {

    private ArrayList<BookAvailable> bookAvailable;

    public AvailableRecyclerAdapter(@NonNull ArrayList<BookAvailable> bookAvailable) {
        this.bookAvailable = bookAvailable;
    }

    @Override
    public AvailableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailableRecyclerAdapter.ViewHolder holder, int position) {
        final BookAvailable available = bookAvailable.get(position);

        holder.place.setText(available.getPlace());
        holder.quantity.setText("" + available.getAvailable());
    }

    @Override
    public int getItemCount() {
        return bookAvailable.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.place)
        public TextView place;

        @Bind(R.id.quantity)
        public TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
