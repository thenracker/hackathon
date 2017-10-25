package cz.koci.hackathon.model.table;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import cz.koci.hackathon.utils.AppDatabase;

/**
 * Created by Matej Danicek on 25.10.17.
 */

@Table(database = AppDatabase.class)
public class ReceivedLink extends BaseModel {
    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String link;

    public ReceivedLink() {
    }

    public ReceivedLink(String link) {
        this.link = link;
    }

    /**
     * @return value of {@link #id}
     **/
    public long getId() {
        return id;
    }

    /**
     * @param id {@link #id}
     * @return This ReceivedLink for setter chaining
     **/
    public ReceivedLink setId(long id) {
        this.id = id;
        return this;
    }

    /**
     * @return value of {@link #link}
     **/
    public String getLink() {
        return link;
    }

    /**
     * @param link {@link #link}
     * @return This ReceivedLink for setter chaining
     **/
    public ReceivedLink setLink(String link) {
        this.link = link;
        return this;
    }
}
