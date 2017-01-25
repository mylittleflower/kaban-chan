package id.bittersweet.kaban;

import android.app.Application;

import java.io.IOException;
import java.util.List;

import id.bittersweet.kaban.BaseLibrary.SharedPreferenceHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by naufal on 25/01/17.
 */

public class LittleFlowerApplication extends Application {

    public List<Fansub> fansubList;
    public SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AlegreyaSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        try {
            fansubList = FansubListHelper.getList(getAssets().open("fansublist.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
    }
}
