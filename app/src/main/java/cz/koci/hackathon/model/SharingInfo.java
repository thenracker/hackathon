
package cz.koci.hackathon.model;


public class SharingInfo {

    private Boolean readOnly;
    private String parentSharedFolderId;
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
