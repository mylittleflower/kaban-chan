package id.bittersweet.kaban;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.crazyhitty.chdev.ks.rssmanager.OnRssLoadListener;
import com.crazyhitty.chdev.ks.rssmanager.RssItem;
import com.crazyhitty.chdev.ks.rssmanager.RssReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements OnRssLoadListener {

    @BindView(R.id.main_list)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    boolean backButtonHasBeenClickedOnce = false;
    private RssItemAdapter rssItemAdapter;
    private ArrayList<CharSequence> fansubName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupActivity();

        rssItemAdapter = new RssItemAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backButtonHasBeenClickedOnce) {
            super.onBackPressed();
            return;
        }
        this.backButtonHasBeenClickedOnce = true;
        Toast.makeText(this, "Tekan tombol back sekali lagi untuk keluar.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backButtonHasBeenClickedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onSuccess(List<RssItem> rssItems) {
        // sort rssItems based on published date.
        Collections.sort(rssItems, new Comparator<RssItem>() {
            @Override
            public int compare(RssItem o1, RssItem o2) {
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy H:mm:ss Z");
                Date d1 = null, d2 = null;
                try {
                    d1 = inputDateFormat.parse(o1.getPubDate());
                    d2 = inputDateFormat.parse(o2.getPubDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return d2.compareTo(d1);
            }
        });
        // then process rssItems
        rssItemAdapter.clear();
        recyclerView.setAdapter(rssItemAdapter);
        rssItemAdapter.addAll(rssItems);
        rssItemAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.toolbar_setting)
    protected void onSettingClicked() {
        CharSequence fansubNameCharSequence[] = fansubName.toArray(new CharSequence[fansubName.size()]);
        ArrayList<Boolean> currentSetting = new ArrayList<>();
        for (Fansub fansub : getApp().fansubList) {
            currentSetting.add(getApp().sharedPreferenceHelper.getBoolean(fansub.getFeedUrl()));
        }
        final ArrayList<Integer> selectedItem = new ArrayList<>();
        final ArrayList<Integer> unSelectedItem = new ArrayList<>();
        new AlertDialog.Builder(this)
                .setTitle("Pilih Fansub Kesayanganmu")
                .setMultiChoiceItems(fansubNameCharSequence, convertBooleans(currentSetting), new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectedItem.add(which);
                        } else {
                            unSelectedItem.add(which);
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int selected : selectedItem) {
                            getApp().sharedPreferenceHelper.putBoolean(
                                    getApp().fansubList.get(selected).getFeedUrl(), true
                            );
                        }
                        for (int unSelected : unSelectedItem) {
                            getApp().sharedPreferenceHelper.putBoolean(
                                    getApp().fansubList.get(unSelected).getFeedUrl(), false
                            );
                        }
                        refresh();
                    }
                })
                .setCancelable(true)
                .show();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        new RssReader(MainActivity.this)
                .showDialog(true)
                .urls(selectedFansubUrl())
//                .urls(urlArr)
                .showDialog(false)
                .parse(this);
    }

    private LittleFlowerApplication getApp() {
        return (LittleFlowerApplication) getApplicationContext();
    }

    private void setupActivity() {
        List<Fansub> fansubList = getApp().fansubList;
        for (Fansub fansub : fansubList) {
            fansub.setShown(getApp().sharedPreferenceHelper.getBoolean(fansub.getFeedUrl()));
            fansubName.add(fansub.getName());
        }
    }

    private boolean[] convertBooleans(List<Boolean> booleans) {
        boolean[] ret = new boolean[booleans.size()];
        Iterator<Boolean> iterator = booleans.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().booleanValue();
        }
        return ret;
    }

    private String[] selectedFansubUrl() {
        ArrayList<String> selectedFansubUrlFeed = new ArrayList<>();
        for (Fansub fansub : getApp().fansubList) {
            boolean selected = getApp().sharedPreferenceHelper.getBoolean(fansub.getFeedUrl());
            if (selected) {
                selectedFansubUrlFeed.add(fansub.getFeedUrl());
            }
        }
        return selectedFansubUrlFeed.toArray(new String[selectedFansubUrlFeed.size()]);
    }
}
