package id.bittersweet.kaban;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FansubListHelper {

    public static List<Fansub> getList(@NonNull InputStream inputStream) throws IOException {
        List<Fansub> list = new ArrayList<>();
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String item;
        while ((item = bufferedReader.readLine()) != null) {
            String splittedItem[] = item.split(";");
            Fansub fansub = new Fansub();
            fansub.setName(splittedItem[0]);
            fansub.setFeedUrl(splittedItem[1]);
            fansub.setShown(false);
            list.add(fansub);
        }
        inputStream.close();
        return list;
    }
}
