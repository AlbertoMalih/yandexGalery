package com.golegion2001.galery.data.db.dao;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.golegion2001.galery.model.Photo;

@Database(entities = {Photo.class}, version = 1)
public abstract class PhotoDatabase extends RoomDatabase {
    public abstract PhotosDao photosDao();
}