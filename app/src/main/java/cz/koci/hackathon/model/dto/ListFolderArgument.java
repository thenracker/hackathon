package cz.koci.hackathon.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel on 25.10.17.
 */

public class ListFolderArgument {

    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("recursive")
    @Expose
    private Boolean recursive;
    @SerializedName("include_media_info")
    @Expose
    private Boolean includeMediaInfo;
    @SerializedName("include_deleted")
    @Expose
    private Boolean includeDeleted;
    @SerializedName("include_has_explicit_shared_members")
    @Expose
    private Boolean includeHasExplicitSharedMembers;
    @SerializedName("include_mounted_folders")
    @Expose
    private Boolean includeMountedFolders;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }

    public Boolean getIncludeMediaInfo() {
        return includeMediaInfo;
    }

    public void setIncludeMediaInfo(Boolean includeMediaInfo) {
        this.includeMediaInfo = includeMediaInfo;
    }

    public Boolean getIncludeDeleted() {
        return includeDeleted;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    public Boolean getIncludeHasExplicitSharedMembers() {
        return includeHasExplicitSharedMembers;
    }

    public void setIncludeHasExplicitSharedMembers(Boolean includeHasExplicitSharedMembers) {
        this.includeHasExplicitSharedMembers = includeHasExplicitSharedMembers;
    }

    public Boolean getIncludeMountedFolders() {
        return includeMountedFolders;
    }

    public void setIncludeMountedFolders(Boolean includeMountedFolders) {
        this.includeMountedFolders = includeMountedFolders;
    }

}
