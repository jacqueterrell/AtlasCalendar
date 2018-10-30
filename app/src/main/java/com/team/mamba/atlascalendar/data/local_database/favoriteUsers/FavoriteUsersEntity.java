package com.team.mamba.atlascalendar.data.local_database.favoriteUsers;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "FavoriteUsers")
public class FavoriteUsersEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId = "";

    public FavoriteUsersEntity(){


    }

    @Ignore
    public FavoriteUsersEntity(String userId){

        this.userId = userId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}
