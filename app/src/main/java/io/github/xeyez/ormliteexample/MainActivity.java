package io.github.xeyez.ormliteexample;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;

import io.github.xeyez.ormliteexample.persistence.CompanyDAOImpl;
import io.github.xeyez.ormliteexample.persistence.OrmHelper;
import io.github.xeyez.ormliteexample.persistence.vo.Company;
import io.github.xeyez.ormliteexample.persistence.vo.Product;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    TextView tv_result;

    @ViewById
    EditText et_deleteNo;

    private CompanyDAOImpl companyDAO;

    @AfterViews
    void afterViews() {
        OrmHelper.setContext(this);

        try {
            companyDAO = OrmHelper.getInstance().getCompaniesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    void btn_select() {
        try {
            ArrayList<Company> companies = (ArrayList<Company>) companyDAO.query(companyDAO.queryBuilder().prepare());
            workInBackground(companies);



            //Stream.of(companyDAO.getPersonDAO().query(companyDAO.getPersonDAO().queryBuilder().prepare())).forEach(person -> Log.wtf("???", person.toString()));
            Stream.of(companies).forEach(company -> {
                Log.wtf("??", company.toString());
                Log.wtf("fk items size", company.getPersons().size() + "");
                Stream.of(company.getPersons()).forEach(product -> Log.wtf("fk item", product.toString()));
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    void btn_insert() {
        try {
            Company company = new Company();
            company.setCompanyName("InsertCompany");

            ArrayList<Product> persons = new ArrayList<>();
            Product person1 = new Product();
            person1.setCompany(company);
            person1.setName("Jake");
            person1.setBirth(DateTime.now());

            Product person2 = new Product();
            person2.setCompany(company);
            person2.setName("Miguel");
            person2.setBirth(DateTime.now());

            persons.add(person1);
            persons.add(person2);

            companyDAO.create(company, persons);

            workInUI("insert!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    void btn_update() {
        try {
            UpdateBuilder<Company, Integer> updateBuilder = companyDAO.updateBuilder();
            updateBuilder.updateColumnValue("companyName", "UpdateCompany");
            updateBuilder.where().eq("companyName", "InsertCompany");
            updateBuilder.update();




            workInUI("update!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Click
    void btn_delete() {
        try {
            companyDAO.deleteById(Integer.parseInt(et_deleteNo.getText().toString()));

            workInUI("delete!");

            // fk 테이블 확인
            Stream.of(companyDAO.getProductDAO().query(companyDAO.getProductDAO().queryBuilder().prepare())).forEach(person -> Log.wtf("???", person.toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Background
    void workInBackground(ArrayList<Company> companies) {
        StringBuilder stringBuilder = new StringBuilder();
        Stream.of(companies).forEach(company -> {
            stringBuilder.append(company.toString()).append("\n");

            Stream.of(company.getPersons()).forEach(person -> stringBuilder.append(person.toString()).append(" "));
            stringBuilder.append("\n\n");
        });
        workInUI(stringBuilder.toString());

        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @UiThread
    void workInUI(String message) {
        tv_result.setText(message);
    }
}
