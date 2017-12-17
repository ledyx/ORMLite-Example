package io.github.xeyez.ormliteexample.persistence.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import lombok.Data;

/**
 * Created by Administrator on 2017-06-05.
 */

@Data
/*@ToString(exclude = "company")*/
@DatabaseTable
public class Product {
    @DatabaseField(generatedId = true)
    private int pid;

    @DatabaseField(foreign = true, columnName = "cid")
    protected Company company;

    @DatabaseField
    protected String name;

    @DatabaseField
    protected DateTime birth;
}