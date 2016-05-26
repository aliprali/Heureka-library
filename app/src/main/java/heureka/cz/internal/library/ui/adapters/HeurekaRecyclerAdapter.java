package heureka.cz.internal.library.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import heureka.cz.internal.library.R;
import heureka.cz.internal.library.helpers.CollectionUtils;
import heureka.cz.internal.library.repository.Heurekoviny;

/**
 * Created by Ondrej on 18. 5. 2016.
 */
public class HeurekaRecyclerAdapter extends RecyclerView.Adapter<HeurekaRecyclerAdapter.ViewHolder>{

    private ArrayList<Heurekoviny> heurekoviny;
    private OnTaskItemClickListener listener;

    private CollectionUtils collectionUtils;

    public HeurekaRecyclerAdapter(@NonNull ArrayList<Heurekoviny> heurekoviny, CollectionUtils collectionUtils) {
        this.heurekoviny = heurekoviny;
        this.collectionUtils = collectionUtils;
    }

    public void setData(@NonNull ArrayList<Heurekoviny> heurekoviny) {
        this.heurekoviny = heurekoviny;
        notifyDataSetChanged();
    }

    public void setListener(OnTaskItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_heurekoviny, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder heurekovinyH, int position) {
        final Heurekoviny heurekovina = heurekoviny.get(position);
        heurekovinyH.name.setText("Heurekoviny");
        heurekovinyH.date.setText(heurekovina.getDate());

    }

    public ArrayList<Heurekoviny> getHeurekoviny() {
        return heurekoviny;
    }

    @Override
    public int getItemCount() {
        return heurekoviny.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.heurekovinyId)
        public TextView name;

        @Bind(R.id.heurekovinyDate)
        public TextView date;

        @Bind(R.id.heurekoviny_button)
        public Button downloadBtm;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            downloadBtm.setOnClickListener(this);
            downloadBtm.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                listener.onItemLongClick(getAdapterPosition());
            }
            return false;
        }
    }

    public interface OnTaskItemClickListener {
        void onItemClick(int taskPosition);
        void onItemLongClick(int taskPosition);
    }
}
