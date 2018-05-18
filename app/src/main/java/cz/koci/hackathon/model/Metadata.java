
package cz.koci.hackathon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.File;
import java.util.List;

import cz.koci.hackathon.utils.AppDatabase;

@Table(database = AppDatabase.class)
public class Metadata extends BaseModel {


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

    @Column
    @SerializedName("url")
    @Expose
    private String url;

    @Column
    @SerializedName(".tag")
    @Expose
    private String tag;

    @Column
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;

    @Column
    @SerializedName("client_modified")
    @Expose
    private String clientModified;

    @Column
    @SerializedName("server_modified")
    @Expose
    private String serverModified;

    @Column
    @SerializedName("rev")
    @Expose
    private String rev;

    @Column
    @SerializedName("size")
    @Expose
    private Long size;

    @Column
    @SerializedName("path_lower")
    @Expose
    private String pathLower;

    @Column
    @SerializedName("path_display")
    @Expose
    private String pathDisplay;

    @Column
    @SerializedName("has_explicit_shared_members")
    @Expose
    private Boolean hasExplicitSharedMembers;

    @Column
    @SerializedName("content_hash")
    @Expose
    private String contentHash;


    @SerializedName("sharing_info")
    @Expose
    private SharingInfo sharingInfo;

    @SerializedName("property_groups")
    @Expose
    private List<PropertyGroup> propertyGroups = null;

    @SerializedName("link_permissions")
    @Expose
    private LinkPermissions linkPermissions;

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

    public Boolean isHasExplicitSharedMembers() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkPermissions getLinkPermissions() {
        return linkPermissions;
    }

    public void setLinkPermissions(LinkPermissions linkPermissions) {
        this.linkPermissions = linkPermissions;
    }

    public Type getType() {
        if (type == null) type = Type.getByValue(tag);

        return type;
    }

    @Column
    private boolean downloaded;

    private transient boolean downloading;

    @Column
    private String localPath;

    @Column
    private boolean shared;

    public boolean isDownloaded() {
        return downloaded && localPath != null && new File(localPath).exists();
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
