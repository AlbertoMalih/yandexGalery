package com.golegion2001.galery.data.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.golegion2001.galery.model.Photo;

import java.util.List;

import static com.golegion2001.galery.data.db.AppDbHelperKt.DESCRIPTION_TABLE_DB_NAME;


@Dao
public interface PhotosDao {
    String FIELD_FOR_ORDER = "createdDate";
    int COUNT_LOADED_PHOTOS = 60;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(Photo photo);

    @Query("SELECT * FROM " + DESCRIPTION_TABLE_DB_NAME + " ORDER BY " + FIELD_FOR_ORDER + " LIMIT " + COUNT_LOADED_PHOTOS + " OFFSET :skipRows")
    List<Photo> loadPhotos( int skipRows);
}