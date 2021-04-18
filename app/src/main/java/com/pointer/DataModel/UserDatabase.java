package com.pointer.DataModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = User.class,
        version = 1,
        exportSchema = false
)
@TypeConverters(DataConverter.class)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase userDB = null;

    public abstract UserDAO userDao();

    public static synchronized  UserDatabase getInstance(Context context){
        if (userDB == null){
            userDB = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserDatabase.class,
                    "user19b2"
            ).allowMainThreadQueries()
                    .build();
        }
        return userDB;
    }
}
