package cz.koci.hackathon.model;

/**
 * Created by daniel on 25.10.17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkPermissions {

    @SerializedName("can_revoke")
    @Expose
    private Boolean canRevoke;
    @SerializedName("resolved_visibility")
    @Expose
    private ResolvedVisibility resolvedVisibility;

    public Boolean getCanRevoke() {
        return canRevoke;
    }

    public void setCanRevoke(Boolean canRevoke) {
        this.canRevoke = canRevoke;
    }

    public ResolvedVisibility getResolvedVisibility() {
        return resolvedVisibility;
    }

    public void setResolvedVisibility(ResolvedVisibility resolvedVisibility) {
        this.resolvedVisibility = resolvedVisibility;
    }


}