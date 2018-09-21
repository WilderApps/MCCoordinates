package com.wilderapps.android.mccoordinates.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Owner on 12/12/2016.
 */

public class CoordinateContract {
    public static final String CONTENT_AUTHORITY = "com.wilderapps.android.mccoordinates";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content:" + CONTENT_AUTHORITY);

    public static final String PATH_WORLD = "world";
    public static final String PATH_COORDINATE = "coordinate";
    public static final String PATH_REALM = "realm";

    public static final class WorldEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORLD).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORLD;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORLD;

        public static final String TABLE_NAME = "world";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_SEED = "seed";

        public static Uri buildWorldUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class RealmEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REALM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REALM;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REALM;

        public static final String TABLE_NAME = "realm";

        public static final String COLUMN_NAME = "name";

        public static Uri buildRealmUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CoordinateEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COORDINATE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COORDINATE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COORDINATE;

        public static final String TABLE_NAME = "coordinate";

        public static final String COLUMN_WORLD_KEY = "world_id";

        public static final String COLUMN_REALM_KEY = "realm_id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_X_COORDINATE = "x_coordinate";

        public static final String COLUMN_Y_COORDINATE = "y_coordinate";

        public static final String COLUMN_Z_COORDINATE = "z_coordinate";

        public static Uri buildCoordinateUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
