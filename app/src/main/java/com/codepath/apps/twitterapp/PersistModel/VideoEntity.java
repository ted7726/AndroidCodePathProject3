package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by wilsonsu on 2/20/16.
 */

@Table(name = "VideoEntity")
public class VideoEntity extends Model{
    @Column(name = "aspect_ratio")
    public List<Integer> aspect_ratio;
    @Column(name = "variants")
    public List<VariantsEntity> variants;

    @Table(name = "VariantsEntity")
    public static class VariantsEntity extends Model{
        @Column(name = "content_type")
        public String content_type;
        @Column(name = "url", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
        public String url;

        public VariantsEntity() {
            super();
        }
    }
    public VideoEntity() {
        super();
    }
}