package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.codepath.apps.twitterapp.models.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Table(name = "EntitiesEntity")
public class EntitiesEntity extends Model {
    @Column(name = "urls", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public List<UrlsEntity> urls;
    @SerializedName("user_mentions") @Column(name = "users", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public List<User> users;
    @Column(name = "media", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public List<Media> media;
    public EntitiesEntity() {
        super();
    }
}
