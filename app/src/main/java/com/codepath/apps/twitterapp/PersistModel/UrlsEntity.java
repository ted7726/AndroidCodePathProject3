package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Table(name = "UrlsEntity")
public class UrlsEntity extends Model{
    @Column(name = "url", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String url;
    @Column(name = "expanded_url")
    public String expanded_url;

    public UrlsEntity() {
        super();
    }
}
