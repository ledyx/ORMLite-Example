package io.github.xeyez.ormliteexample.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import io.github.xeyez.ormliteexample.persistence.vo.Company;
import io.github.xeyez.ormliteexample.persistence.vo.Product;
import lombok.Setter;

/**
 * Created by Administrator on 2017-06-05.
 */

public class OrmHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "mydb.db";
    private static final int DATABASE_VERSION = 1;

    private CompanyDAOImpl companiesDao;

    @Setter
    private static Context context;

    public static OrmHelper getInstance() {
        if(context == null)
            throw new NullPointerException("Must be set context!");

        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        public static final OrmHelper INSTANCE = new OrmHelper();
    }

    private OrmHelper() {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Company.class);
            TableUtils.createTableIfNotExists(connectionSource, Product.class);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Company.class, true);
            TableUtils.createTable(connectionSource, Company.class);

            TableUtils.dropTable(connectionSource, Product.class, true);
            TableUtils.createTable(connectionSource, Product.class);
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    public CompanyDAOImpl getCompaniesDao() throws SQLException {
        if(companiesDao == null)
            companiesDao = new CompanyDAOImpl(getConnectionSource(), getDao(Product.class));

        return companiesDao;
    }
}
