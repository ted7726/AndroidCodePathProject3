package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wilsonsu on 2/20/16.
 */

@Table(name = "User")
public class UserEntity extends Model {
    @SerializedName("id_str") @Column(name = "userId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String id;
    @Column(name = "name")
    public String name;
    @SerializedName("screen_name") @Column(name = "screenName")
    public String screenName;
    @Column(name = "description")
    public String description;
    @Column(name = "url")
    public String url;
    @SerializedName("profile_image_url") @Column(name = "profileImageUrl")
    public String profileImageUrl;

    public UserEntity() {
        super();
    }
}
