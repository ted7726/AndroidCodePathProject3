package com.codepath.apps.twitterapp.PersistModel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by wilsonsu on 2/20/16.
 */
@Table(name = "SizesEntity")
public class SizesEntity extends Model{
    @Column(name = "large", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public SizeEntity large;
    @Column(name = "medium", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public SizeEntity medium;
    @Column(name = "thumb", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public SizeEntity thumb;
    @Column(name = "small", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public SizeEntity small;

    public SizesEntity() {
        super();
    }
}
