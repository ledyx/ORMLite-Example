package io.github.xeyez.ormliteexample.persistence.vo;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Administrator on 2017-06-05.
 */

@Data
@ToString(exclude = "persons")
@DatabaseTable
public class Company {
    @DatabaseField(generatedId = true)
    private int cid;

    @DatabaseField
    protected String companyName;

    @ForeignCollectionField
    private ForeignCollection<Product> persons;
}
