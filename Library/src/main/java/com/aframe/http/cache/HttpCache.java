package com.aframe.http.cache;

import java.util.List;

import com.aframe.Loger;
import com.aframe.ui.WYActivityManager;
import com.aframe.database.WYDB;

/**
 * Http请求中对字符串信息的缓存类，通过Lru算法，对数据做两级缓存（内存缓存、数据库缓存）<br>
 * 
 * <b>说明</b> 系统默认的缓存时间为5分钟，你可以通过设置修改它<br>
 * <b>创建时间</b> 2014-9-3
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.0
 */
public class HttpCache implements I_HttpCache {
    private WYCacheConfig config;
    private HttpMemoryCache cache;
    private static HttpCache instance;
    private WYDB db;

    /******************** 使用静态内部类手段创建单例 **********************/
    private HttpCache(HttpMemoryCache cache) {
        this.cache = cache;
        config = new WYCacheConfig();
        db = WYDB.create(WYActivityManager.create().topActivity(),
                config.isDebug());
    }

    /**
     * 使用默认配置
     */
    public synchronized static HttpCache create(HttpMemoryCache cache) {
        if (instance == null) {
            instance = new HttpCache(cache);
        }
        return instance;
    }

    public static HttpCache create() {
        return create(HttpMemoryCache.create());
    }

    /************************** public method ****************************/

    /**
     * 添加一个缓存
     * 
     * @param url
     *            key
     * @param json
     *            value
     */
    @Override
    public void add(String url, String json) {
        CacheBean data = new CacheBean();
        long currentTime = System.currentTimeMillis();
        data.createTime = currentTime;
        data.effectiveTime = config.getEffectiveTime();
        data.overdueTime = data.createTime + data.effectiveTime;
        data.url = url;
        data.json = json;
        CacheBean dataInDb = getCacheBean(url);
        if (dataInDb != null) {
            if ((dataInDb.overdueTime - currentTime) > 0) {
                /* 还没过期，继续用 */
            } else {
                showDebug("update to database cache for " + data.url);
                db.update(data);
            }
        } else {
            showDebug("add to database cache for " + data.url);
            db.save(data);
        }
        cache.add(data);
    }

    /**
     * 通过指定url读取缓存，首先会从内存缓存中查找，再去数据库中查找
     * 
     * @param url
     *            key
     * @return 若缓存中没有key对应的cache，返回null，若key对应的cache过期，返回null
     */
    @Override
    public String get(String url) {
        String res = cache.get(url);
        if (res == null) { // 内存缓存中没有
            CacheBean data = getCacheBean(url);
            if (data != null
                    && (data.overdueTime - System.currentTimeMillis()) > 0) {
                res = data.json;
                cache.add(data);
                showDebug("get cache from database");
            }
        } else {
            showDebug("get cache from memory");
        }
        return res;
    }

    /**
     * 忽略缓存的有效期读取缓存
     * 
     * @param url
     * @return 若url没有对应的cache，返回null
     */
    @Override
    public String getDataFromCache(String url) {
        String res = cache.getDataFromCache(url);
        if (res == null) {
            CacheBean data = getCacheBean(url);
            if (data != null) {
                res = data.json;
                showDebug("get a cache from database");
            }
        } else {
            showDebug("get a cache from memory");
        }
        return res;
    }

    /**
     * 内部方法，从数据库读取一个CacheBean
     * 
     * @param url
     * @return
     */
    private CacheBean getCacheBean(String url) {
        CacheBean data = null;
        List<CacheBean> datas = db.findAllByWhere(CacheBean.class,
                "url='" + url + "';");
        if (datas != null && !datas.isEmpty()) {
            data = datas.get(0);
        }
        return data;
    }

    private void showDebug(String msg) {
        if (config.isDebug()) {
            Loger.debugLog(getClass().getName(), "---" + msg);
        }
    }

    /************************** config method ****************************/

    /**
     * 获取当前缓存配置器
     */
    public WYCacheConfig getConfig() {
        return this.config;
    }

    /**
     * 设置当前缓存配置器
     */
    public void setConfig(WYCacheConfig config) {
        this.config = config;
    }

    /**
     * 设置Cache有效期，单位:秒
     */
    public void setEffectiveTime(long effectiveTime) {
        this.config.setEffectiveTime(effectiveTime);
    }

    /** Cache有效期，单位:秒 */
    public long getEffectiveTime() {
        return this.config.getEffectiveTime();
    }

    /**
     * 调试模式
     * 
     * @return
     */
    public boolean isDebug() {
        return this.config.isDebug();
    }

    /**
     * 调试模式
     */
    public void setDebug(boolean isDebug) {
        this.config.setDebug(isDebug);
    }
}
