
package cz.koci.hackathon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Metadata {
    public enum Type {
        FOLDER("folder"), FILE("file"), UNKNOWN(null);

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Type getByValue(String value) {
            if (value == null) {
                return UNKNOWN;
            }

            for (Type type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }

            return UNKNOWN;
        }
    }

    @SerializedName(".tag")
    @Expose
    private String tag;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("client_modified")
    @Expose
    private String clientModified;
    @SerializedName("server_modified")
    @Expose
    private String serverModified;
    @SerializedName("rev")
    @Expose
    private String rev;
    @SerializedName("size")
    @Expose
    private Long size;
    @SerializedName("path_lower")
    @Expose
    private String pathLower;
    @SerializedName("path_display")
    @Expose
    private String pathDisplay;
    @SerializedName("sharing_info")
    @Expose
    private SharingInfo sharingInfo;
    @SerializedName("property_groups")
    @Expose
    private List<PropertyGroup> propertyGroups = null;
    @SerializedName("has_explicit_shared_members")
    @Expose
    private Boolean hasExplicitSharedMembers;
    @SerializedName("content_hash")
    @Expose
    private String contentHash;

    private transient Type type;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientModified() {
        return clientModified;
    }

    public void setClientModified(String clientModified) {
        this.clientModified = clientModified;
    }

    public String getServerModified() {
        return serverModified;
    }

    public void setServerModified(String serverModified) {
        this.serverModified = serverModified;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPathLower() {
        return pathLower;
    }

    public void setPathLower(String pathLower) {
        this.pathLower = pathLower;
    }

    public String getPathDisplay() {
        return pathDisplay;
    }

    public void setPathDisplay(String pathDisplay) {
        this.pathDisplay = pathDisplay;
    }

    public SharingInfo getSharingInfo() {
        return sharingInfo;
    }

    public void setSharingInfo(SharingInfo sharingInfo) {
        this.sharingInfo = sharingInfo;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return propertyGroups;
    }

    public void setPropertyGroups(List<PropertyGroup> propertyGroups) {
        this.propertyGroups = propertyGroups;
    }

    public Boolean getHasExplicitSharedMembers() {
        return hasExplicitSharedMembers;
    }

    public void setHasExplicitSharedMembers(Boolean hasExplicitSharedMembers) {
        this.hasExplicitSharedMembers = hasExplicitSharedMembers;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public Type getType() {
        if (type == null) type = Type.getByValue(tag);
        
        return type;
    }

}
