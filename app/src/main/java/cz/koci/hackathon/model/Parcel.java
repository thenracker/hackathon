package cz.koci.hackathon.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import cz.koci.hackathon.utils.AppDatabase;

/**
 * Created by Matej Danicek on 25.10.17.
 */

@Table(database = AppDatabase.class)
public class Parcel {

    @PrimaryKey(autoincrement = true)
    long id;

    @Column
    String name;
    @Column
    long createDate;
    @Column
    long uploadDate;
    @Column
    long downloadDate;

}