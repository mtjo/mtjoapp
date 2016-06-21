package net.mtjo.app.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.mtjo.app.entity.Articles;

public class ArticleDBManager {
	private ArticleDBHelper dbHelper;
	private SQLiteDatabase db;
	
	public ArticleDBManager(Context context) {
		this.dbHelper = new ArticleDBHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	/**
	 * add articles data
	 * @param id
	 */
	public void addArticles(String id){
		
		try {
				if (id != null) {
					 db.execSQL("insert into "+ dbHelper.TABLE_NAME +" values (?,?,?,?,?)", new Object[]{null,1, id,null,null});
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * update article is read
	 * @param id
	 */
	public void updateReadArticle(String id){
		ContentValues values = new ContentValues();
		values.put(ArticleDBHelper.ArticleColumns.ISREAD, 1);
		int i = db.update(dbHelper.TABLE_NAME, values, ArticleDBHelper.ArticleColumns.ARTICLE_ID +" = ?", new String[]{id});
		
	}
	
	/**
	 * getArticleList 
	 * @param id
	 * @return
	 */
	public ArrayList<Articles> getArticleListType(ArrayList<Articles> id) {
		for (Articles article : id) {
			Cursor cursor = getCursor(article.getId());
			if (cursor.moveToFirst()) {
				int isRead = cursor.getInt(cursor.getColumnIndex(ArticleDBHelper.ArticleColumns.ISREAD));
				article.setIsRead(isRead);
			}else{
//				addArticles(article.getId());
				article.setIsRead(0);
			}
		}
		return id;
		
	}

	public Cursor getCursor(String id){
		String sql = "select * from "+dbHelper.TABLE_NAME +" where "+ ArticleDBHelper.ArticleColumns.ARTICLE_ID  +" = "+ id;
		Cursor cursor = db.query(dbHelper.TABLE_NAME, new String[]{ArticleDBHelper.ArticleColumns.ISREAD}, ArticleDBHelper.ArticleColumns.ARTICLE_ID + " = " + id, null, null, null, null);
		int i = cursor.getCount();
		return cursor;
	}
	
	public void delete(){
		
		db.delete(dbHelper.TABLE_NAME, null, null);
	}
	
	/**
	 * close database
	 */
	public void close(){
		
		db.close();
	}
	
	

}
