package cz.koci.hackathon.model.dto;

import android.support.annotation.Nullable;

/**
 * Created by daniel on 25.10.17.
 */

public class SharedLinkArgument {

    private final String url;
    private final String path;
    private final SharedLinkSettings settings;

    public SharedLinkArgument(String path, SharedLinkSettings settings) {
        this.path = path;
        this.settings = settings;
        this.url = null;
    }

    public SharedLinkArgument(String url, @Nullable String path) {
        this.url = url;
        this.path = path;
        this.settings = null;
    }
}
