package io.github.xeyez.ormliteexample.persistence;

import android.util.Log;

import com.annimon.stream.Stream;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Collection;

import io.github.xeyez.ormliteexample.persistence.vo.Company;
import io.github.xeyez.ormliteexample.persistence.vo.Product;
import lombok.Getter;

/**
 * Created by Administrator on 2017-06-09.
 */

public class CompanyDAOImpl extends BaseDaoImpl<Company, Integer> {

    @Getter
    private Dao<Product, ?> productDAO;

    protected CompanyDAOImpl(ConnectionSource connectionSource, Dao<Product, ?> productDAO) throws SQLException {
        super(connectionSource, Company.class);
        this.productDAO = productDAO;
    }

    public int create(Company company, Collection<Product> persons) throws SQLException {
        int result = super.create(company);
        productDAO.create(persons);


        Stream.of(productDAO.query(productDAO.queryBuilder().prepare())).forEach(product -> Log.wtf("dao create", product.toString()));

        return result;
    }

    public int update(Company company, Collection<Product> persons) throws SQLException {
        UpdateBuilder cub = productDAO.updateBuilder();
        cub.where().eq("cid", company.getCid());
        int result = productDAO.update(cub.prepare());

        // 지우고 다시 삽입
        DeleteBuilder deleteBuilder = productDAO.deleteBuilder();
        deleteBuilder.where().eq("cid", company.getCid());
        productDAO.delete(deleteBuilder.prepare());
        productDAO.create(persons);

        return result;
    }

    public int update(int cid, Company company, Collection<Product> persons) throws SQLException {
        UpdateBuilder cub = productDAO.updateBuilder();
        cub.where().eq("cid", cid);
        int result = productDAO.update(cub.prepare());

        // 지우고 다시 삽입
        DeleteBuilder deleteBuilder = productDAO.deleteBuilder();
        deleteBuilder.where().eq("cid", cid);
        productDAO.delete(deleteBuilder.prepare());
        productDAO.create(persons);

        return result;
    }

    @Override
    public int delete(Company company) throws SQLException {
        DeleteBuilder deleteBuilder = productDAO.deleteBuilder();
        deleteBuilder.where().eq("cid", company.getCid());
        productDAO.delete(deleteBuilder.prepare());

        Log.wtf("del query", deleteBuilder.prepareStatementString());

        return super.delete(company);
    }

    @Override
    public int deleteById(Integer cid) throws SQLException {
        DeleteBuilder deleteBuilder = productDAO.deleteBuilder();
        deleteBuilder.where().eq("cid", cid);
        productDAO.delete(deleteBuilder.prepare());

        Log.wtf("del query", deleteBuilder.prepareStatementString());

        return super.deleteById(cid);
    }
}
