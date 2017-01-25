package id.bittersweet.kaban;

import java.io.Serializable;

/**
 * Created by naufal on 25/01/17.
 */

public class Fansub implements Serializable {
    private String name;
    private String feedUrl;
    private boolean shown;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public boolean getShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
