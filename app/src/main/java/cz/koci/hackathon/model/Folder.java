package cz.koci.hackathon.model;
import java.util.List;


/**
 * Created by daniel on 25.10.17.
 */

public class Folder {

    private List<Metadata> entries = null;
    private String cursor;
    private Boolean hasMore;

    public List<Metadata> getEntries() {
        return entries;
    }

    public void setEntries(List<Metadata> entries) {
        this.entries = entries;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

}