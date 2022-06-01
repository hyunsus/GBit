package kr.ac.hs.beet;

public class Todo {
    public static final String TABLE_NAME = "todo";
    public static final String TODO_ID = "id";
    public static final String CONTENT = "content";
    public static final String WRITEDATE= "writeDate";

    private static final String SQL_CREATE_TODO =
            "CREATE TABLE " + Todo.TABLE_NAME + " (" +
                    Todo.TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Todo.CONTENT + " TEXT NOT NULL," +
                    Todo.WRITEDATE + " TEXT NOT NULL)";

    private static final String SQL_DELETE_TODO =
            "DROP TABLE IF EXISTS " + Todo.TABLE_NAME;
}