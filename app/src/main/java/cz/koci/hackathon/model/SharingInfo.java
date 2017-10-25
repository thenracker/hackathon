
package cz.koci.hackathon.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SharingInfo {

    @SerializedName("read_only")
    @Expose
    private Boolean readOnly;
    @SerializedName("parent_shared_folder_id")
    @Expose
    private String parentSharedFolderId;
    @SerializedName("modified_by")
    @Expose
    private String modifiedBy;

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getParentSharedFolderId() {
        return parentSharedFolderId;
    }

    public void setParentSharedFolderId(String parentSharedFolderId) {
        this.parentSharedFolderId = parentSharedFolderId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
