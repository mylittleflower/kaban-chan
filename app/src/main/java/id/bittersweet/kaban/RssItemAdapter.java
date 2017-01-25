package id.bittersweet.kaban;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.rssmanager.RssItem;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import id.bittersweet.kaban.BaseLibrary.BaseListAdapter;
import id.bittersweet.kaban.BaseLibrary.BaseViewHolder;

/**
 * Created by naufal on 25/01/17.
 */

public class RssItemAdapter extends BaseListAdapter<RssItem, RssItemAdapter.RssItemViewHolder> {
    public RssItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.view_item;
    }

    @Override
    public RssItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RssItemViewHolder(getView(parent, viewType), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position).getLink()));
                context.startActivity(browserIntent);
            }
        });
    }

    class RssItemViewHolder extends BaseViewHolder<RssItem> {

        @BindView(R.id.item_title)
        TextView itemTitle;

        @BindView(R.id.item_date)
        TextView itemDate;

        @BindView(R.id.item_description)
        TextView itemDescription;

        @BindView(R.id.item_source_url)
        TextView itemSourceUrl;

        @BindView(R.id.item_image)
        CircularImageView itemImage;

        RssItemViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
        }

        @Override
        public void bind(RssItem item) {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy H:mm:ss Z");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM dd");
            Date item_date = null;
            try {
                item_date = inputDateFormat.parse(item.getPubDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemTitle.setText(item.getTitle());
            itemDate.setText(outputDateFormat.format(item_date));
            itemDescription.setText(Html.fromHtml(Jsoup.parse(item.getDescription()).text()));
            itemSourceUrl.setText(item.getSourceUrlShort());
            Picasso.with(itemImage.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.kaban_chan)
                    .into(itemImage);
        }
    }
}
