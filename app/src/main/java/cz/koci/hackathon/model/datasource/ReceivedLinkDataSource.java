package cz.koci.hackathon.model.datasource;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import cz.koci.hackathon.model.table.ReceivedLink;

/**
 * Created by Matej Danicek on 25.10.17.
 */

public class ReceivedLinkDataSource {

    public static void createOrUpdate(ReceivedLink receivedLink) {
        receivedLink.save();
    }

    public static List<ReceivedLink> findNewlyReceivedLink() {
        return SQLite.select().from(ReceivedLink.class).queryList();
    }
}
