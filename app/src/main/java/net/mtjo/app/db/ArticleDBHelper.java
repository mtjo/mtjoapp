package net.mtjo.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * 数据库类，服务于法律文章
 * @author zxp
 *
 */
public class ArticleDBHelper extends SQLiteOpenHelper {
	
	public static String DATABASE_NAME = "article.db";
	public static int DATABASE_VERSION = 1;
	public static String TABLE_NAME = "Article";
	
	public ArticleDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public ArticleDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists "+ TABLE_NAME +"("+ ArticleColumns.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ArticleColumns.ISREAD+" INTEGER, "+ArticleColumns.ARTICLE_ID+" text, "+ArticleColumns.OTHER+" text, "+ArticleColumns.USERID+" text) ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		 db.execSQL("ALTER TABLE "+TABLE_NAME+" ADD COLUMN other STRING");  
	}
	
	public static class  ArticleColumns implements BaseColumns{
		public static final String ID = "id";
		public static final String ARTICLE_ID = "article_id";
		public static final String ISREAD = "isRead";//采用int 类型 0，未读，1已读
		public static final String USERID = "uid";//用户ID
		public static final String OTHER = "other";
		
	}

}
