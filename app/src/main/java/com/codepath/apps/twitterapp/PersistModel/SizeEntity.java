package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Table(name = "SizeEntity")
public class SizeEntity extends Model{
    @Column(name = "w")
    public int w;
    @Column(name = "h")
    public int h;
    @Column(name = "resize")
    public String resize;

    public SizeEntity() {
        super();
    }
}