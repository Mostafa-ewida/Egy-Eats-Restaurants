package com.itechpro.egyeatsrestaurant.Model;

public class Table {
    private long id;

    public Table(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                '}';
    }
}
