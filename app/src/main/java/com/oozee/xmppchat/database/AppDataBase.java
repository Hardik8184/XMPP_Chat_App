package com.oozee.xmppchat.database;

/**
 * AppDbAdapter represents SQLite database for offline storage
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.oozee.xmppchat.bean.ChatMessage;

import java.util.ArrayList;

public class AppDataBase {

    private static final String DATABASE_NAME = "chat_user1.db";

    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_MSG_ID = "msg_id";
    private static final String KEY_SENDER = "sender";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_IS_MSG_READ = "is_msg_read";   //flag value = "1"/"0"

    private static final String DATABASE_TABLE_NAME = "my_chatting_data";

    private static final int DATABASE_VERSION = 1;

    /*create_table_query = create table my_chatting_data(id integer primary key autoincrement,
     user_id text, group_name text, msg_id text, sender text, reciever text, message text,
     date_time text, is_msg_read text)*/
    private static final String CREATE_DATABASE = "create table " + DATABASE_TABLE_NAME +
            "(" + KEY_ID + " integer primary key autoincrement, " + KEY_USER_ID + " text, " + KEY_MSG_ID + " text, "
            + KEY_SENDER + " text , " + KEY_MESSAGE + " text," + KEY_DATE_TIME + " text," + KEY_IS_MSG_READ + " text);";

    private final Context context;

    private SQLiteDatabase db;

    private SQLiteStatement stmt;

    private DataBaseHelper dbHelper;

    public AppDataBase(Context context) {

        this.context = context;

        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public AppDataBase open() throws SQLException {

        db = dbHelper.getWritableDatabase();

        return this;

    }

    private void close() {

        db.close();

    }

    //-------------------------  TODO : Get All Messages From Database  --------------------------//
    public synchronized ArrayList<ChatMessage> getAllMessages(String userName) {

        Cursor chatMsgsListCursor = null;

        ArrayList<ChatMessage> chatMessagesList = new ArrayList<>();

        try {

            open();

            /* selectMsgesQuery = select * from my_chatting_data where group_name = '123' order by
             * date_time  */
//            String selectMsgesQuery = "select * from " + DATABASE_TABLE_NAME + " where " +
//                    KEY_SENDER + " = '" + sender_name + "' group by  " + KEY_MESSAGE + " order by " + KEY_DATE_TIME + " ASC";
            String selectMsgesQuery = "select * from " + DATABASE_TABLE_NAME + " where + " + KEY_USER_ID + " ='" + userName + "' group by  "
                    + KEY_MESSAGE + " order by " + KEY_DATE_TIME + " ASC";

            chatMsgsListCursor = db.rawQuery(selectMsgesQuery, null);

            if (chatMsgsListCursor != null && chatMsgsListCursor.getCount() > 0) {

                System.out.println("DB_Count --> " + chatMsgsListCursor.getCount());

                for (int i = 0; i < chatMsgsListCursor.getCount(); i++) {

                    chatMsgsListCursor.moveToPosition(i);

                    chatMessagesList.
                            add(new ChatMessage(chatMsgsListCursor.getString(chatMsgsListCursor.
                                    getColumnIndexOrThrow(KEY_USER_ID)),
                                    chatMsgsListCursor.getString(chatMsgsListCursor.
                                            getColumnIndexOrThrow(KEY_MSG_ID)),
                                    chatMsgsListCursor.getString(chatMsgsListCursor.
                                            getColumnIndexOrThrow(KEY_SENDER)),
                                    chatMsgsListCursor.getString(chatMsgsListCursor.
                                            getColumnIndexOrThrow(KEY_MESSAGE)),
                                    chatMsgsListCursor.getString(chatMsgsListCursor.
                                            getColumnIndexOrThrow(KEY_DATE_TIME)),
                                    chatMsgsListCursor.getString(chatMsgsListCursor.
                                            getColumnIndexOrThrow(KEY_IS_MSG_READ))));

                }
            }


            System.out.println("DB_Count_Array --> " + chatMessagesList.size());

            chatMsgsListCursor.close();

            close();

        } catch (Exception e) {

            close();

            System.out.println("dataBase_getAllMessages --> " + e.getMessage());

        }

        return chatMessagesList;

    }

    //----------------------------  TODO : Insert Messages Into Database  ------------------------//
    public synchronized void insertMessagesToDB(String user_id, String msg_id,
                                                String sender, String message, String date_time,
                                                String is_msg_read) {

        try {

            open();

            /* insertQuery = insert or replace into my_chatting_data (user_id, group_name, msg_id,
             * sender, reciever, message, date_time, is_msg_read) values( ?,?,?,?,?,?,?,?) */
            String insertQuery = "insert or replace into " + DATABASE_TABLE_NAME + " (" +
                    KEY_USER_ID + ", " + KEY_MSG_ID + ", " + KEY_SENDER +
                    ", " + KEY_MESSAGE + ", " + KEY_DATE_TIME + ", " +
                    KEY_IS_MSG_READ + ") values( ?,?,?,?,?,?)";

            stmt = db.compileStatement(insertQuery);

            stmt.bindString(1, user_id);
            stmt.bindString(2, msg_id);
            stmt.bindString(3, sender);
            stmt.bindString(4, message);
            stmt.bindString(5, date_time);
            stmt.bindString(6, is_msg_read);

            stmt.execute();
            stmt.clearBindings();

            System.out.println("DB_Insert --> \"" + message + "\" inserted");

            close();

        } catch (Exception e) {

            close();

            System.out.println("dataBase_insertMessagesToDB --> " + e.getMessage());

        }
    }

    //-------------------------------  TODO : Unread Messages Counts  ----------------------------//
    public String getNewUnreadMessagesCounts(String user_name) {

        Cursor unreadMessagesCountsCursor = null;

        String numberOfCounts = "0";

        /* unreadMessagesCountsQuery = select COUNT(*) AS numberOfCount from my_chatting_data where
         * is_msg_read='false' AND sender!='raj'. */
        String unreadMessagesCountsQuery = "select COUNT(*) AS numberOfCount from " +
                DATABASE_TABLE_NAME + " where " + KEY_IS_MSG_READ + "='false' AND " + KEY_SENDER +
                "!='" + user_name + "'";

        try {

            open();

            unreadMessagesCountsCursor = db.rawQuery(unreadMessagesCountsQuery, null);
            unreadMessagesCountsCursor.moveToFirst();
            if (unreadMessagesCountsCursor != null && unreadMessagesCountsCursor.getCount() > 0) {

                numberOfCounts = unreadMessagesCountsCursor.getString(0);

            }

            unreadMessagesCountsCursor.close();

            close();

        } catch (Exception e) {

            close();

            System.out.println("dataBase_getNewUnreadMessagesCounts --> " + e.getMessage());

        }

        System.out.println("dataBase_unreadMessageCount --> " + numberOfCounts);

        return numberOfCounts;

    }

    //-------------------------------  TODO : Set All Messages As Read  --------------------------//
    public boolean setAllMessagesAsRead() {

        boolean isUpdated = false;

        Cursor allMessagesAsReadCursor = null;

        /* allMessagesAsReadQuery = update my_chatting_data set is_msg_read='true' WHERE
         * is_msg_read='false'. */
        String allMessagesAsReadQuery = "update " + DATABASE_TABLE_NAME + " set " +
                KEY_IS_MSG_READ + "='true' where " + KEY_IS_MSG_READ + "='false'";

        try {

            open();

            allMessagesAsReadCursor = db.rawQuery(allMessagesAsReadQuery, null);
            allMessagesAsReadCursor.moveToFirst();

            allMessagesAsReadCursor.close();

            close();

            isUpdated = true;

        } catch (Exception e) {

            isUpdated = false;

            close();

            System.out.println("dataBase_setAllMessagesAsRead --> " + e.getMessage());

        }

        return isUpdated;

    }

    //---------------------  TODO : Show Notification For New Unread Messages  -------------------//
    public ArrayList<String> getNewUnreadMessagesForNotification(String user_name) {

        Cursor unreadMessagesNotificationCursor = null;

        /* unreadMessagesNotificationQuery = select sender,message from my_chatting_data where
         * is_msg_read='false' AND sender!='raj'. */
        String unreadMessagesNotificationQuery = "select " + KEY_SENDER + "," + KEY_MESSAGE +
                " from " + DATABASE_TABLE_NAME + " where " + KEY_IS_MSG_READ + "='false' AND " +
                KEY_SENDER + "!='" + user_name + "'";

        ArrayList<String> unreadMessagesForNotification = null;

        try {

            open();

            unreadMessagesNotificationCursor = db.rawQuery(unreadMessagesNotificationQuery, null);
            unreadMessagesNotificationCursor.moveToFirst();
            if (unreadMessagesNotificationCursor != null && unreadMessagesNotificationCursor.
                    getCount() > 0) {

                unreadMessagesForNotification = new ArrayList<>();

                for (int i = 0; i < unreadMessagesNotificationCursor.getCount(); i++) {

                    unreadMessagesNotificationCursor.moveToPosition(i);

                    String completeMessage = unreadMessagesNotificationCursor.
                            getString(unreadMessagesNotificationCursor.
                                    getColumnIndexOrThrow(KEY_SENDER)).concat(" : ").
                            concat(unreadMessagesNotificationCursor.
                                    getString(unreadMessagesNotificationCursor.
                                            getColumnIndexOrThrow(KEY_MESSAGE)));

                    unreadMessagesForNotification.add(completeMessage);

                }
            }

            unreadMessagesNotificationCursor.close();

            close();

        } catch (Exception e) {

            close();

            System.out.println("dataBase_getNewUnreadNotificationException --> " + e.getMessage());

        }

        if (unreadMessagesForNotification != null) {

            System.out.println("dataBase_getNewUnreadNotification --> " +
                    unreadMessagesForNotification.size());

        }

        return unreadMessagesForNotification;

    }

    //------------------------------  TODO : To Get Only New Messages  ---------------------------//
    public long getMaxTimeMillis() {

        long maxTimeMillis = System.currentTimeMillis() - (400000000);

        /* timeMilliesQuery = select max(date_time) AS max_count from my_chatting_data. */
        String timeMilliesQuery = "select max(" + KEY_DATE_TIME + ") AS max_count from " +
                DATABASE_TABLE_NAME;

        try {

            open();

            Cursor maxTimeCursor = db.rawQuery(timeMilliesQuery, null);

            if (maxTimeCursor != null && maxTimeCursor.getCount() > 0) {

                maxTimeCursor.moveToFirst();

                maxTimeMillis = 0L;

                String millis = maxTimeCursor.getString(0);

                maxTimeMillis = Long.parseLong(millis);

            }

            maxTimeCursor.close();

            close();

        } catch (Exception e) {

            close();

            maxTimeMillis = System.currentTimeMillis() - (600000000);

            System.out.println("getMaxTimeMillie_DB --> " + e.getMessage());

        }

        System.out.println("maxTimeMillis --> " + maxTimeMillis);

        return (maxTimeMillis + 20000);

    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {

                db.execSQL(CREATE_DATABASE);

            } catch (Exception e) {

                System.out.println("");

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}