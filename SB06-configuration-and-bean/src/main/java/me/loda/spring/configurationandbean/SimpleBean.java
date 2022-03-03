package me.loda.spring.configurationandbean;

/**
 * Một class cơ bản, không sử dụng `@Component`
 */
public class SimpleBean {

    private String username;

    public SimpleBean() {
    }

    public SimpleBean(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "This is a simple bean, name: " + username;
    }
}
