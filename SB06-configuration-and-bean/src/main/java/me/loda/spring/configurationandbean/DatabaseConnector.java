package me.loda.spring.configurationandbean;


// Tạo ra một Abstract Class DatabaseConnector chịu trách nhiệm kết nối tới Database.
public abstract class DatabaseConnector {
    private String url;
    /**
     * Hàm này có nhiệm vụ Connect tới một Database bất kỳ
     */
    public abstract void connect();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
