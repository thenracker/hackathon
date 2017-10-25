package cz.koci.hackathon.model.dto;

/**
 * Created by daniel on 25.10.17.
 */

public class SharedLinkArgument {

    private final String path;
    private final SharedLinkSettings settings;

    public SharedLinkArgument(String path, SharedLinkSettings settings) {
        this.path = path;
        this.settings = settings;
    }
}
