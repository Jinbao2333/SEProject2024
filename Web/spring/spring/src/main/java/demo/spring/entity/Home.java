package demo.spring.entity;

public class Home {
    public Home() {
    }
    public Home(int count, int date) {
        this.count = count;
        Date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDate() {
        return Date;
    }

    public void setDate(int date) {
        Date = date;
    }

    private int count;
    private int Date;
}
