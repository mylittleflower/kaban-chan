package id.bittersweet.kaban.BaseLibrary;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Wim on 9/23/16.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    private BaseListAdapter.OnItemClickListener onItemClickListener;
    private boolean hasFooter = false;

    public BaseViewHolder(View itemView, BaseListAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.onItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T item);

    public boolean isHasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
