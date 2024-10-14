package app.libmgmt.dao;

import java.util.ArrayList;

public interface DAOInterface<T> {

    public void add(T t);

    public void update(T t);

    public void delete(T t);

    public ArrayList<T> selectAll();

    public ArrayList<T> selectByCondition(String condition);
}
