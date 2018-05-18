package cz.koci.hackathon.model.dto;

import com.dropbox.core.v2.sharing.RequestedVisibility;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by daniel on 25.10.17.
 */

public class SharedLinkSettings {

    @SerializedName("requested_visibility")
    @Expose
    private String requestedVisibility;

    @SerializedName("link_password")
    @Expose
    private String password;

    @SerializedName("expires")
    @Expose
    private String expires;

    public SharedLinkSettings() {
        this.requestedVisibility = "public";
        setupExpiryDate();
    }

    public SharedLinkSettings(String password) {
        this.requestedVisibility = "password";
        this.password = password;
       setupExpiryDate();
    }

    private void setupExpiryDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.WEEK_OF_YEAR, 2);

        this.expires = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(calendar.getTime());
    }
}
