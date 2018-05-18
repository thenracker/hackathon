package cz.koci.hackathon.model;

/**
 * Created by daniel on 25.10.17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolvedVisibility {

    @SerializedName(".tag")
    @Expose
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}