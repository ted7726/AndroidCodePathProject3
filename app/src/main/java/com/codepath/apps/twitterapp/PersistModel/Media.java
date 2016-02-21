package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Table(name = "Media")
public class Media {
    @SerializedName("id_str") @Column(name = "MediaId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;
    @Column(name = "media_url")
    public String media_url;
    @Column(name = "url")
    public String url;
    @Column(name = "type")
    public String type;
    @Column(name = "type")
    public SizesEntity sizes;
    @SerializedName("video_info")
    @Column(name = "video", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public VideoEntity video;
    public Media() {
        super();
    }
}
