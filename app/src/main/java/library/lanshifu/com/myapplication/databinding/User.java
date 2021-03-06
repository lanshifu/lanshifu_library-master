package library.lanshifu.com.myapplication.databinding;

/**
 * Created by lanxiaobin on 2017/8/4.
 */

public class User {
    private final String firstName;
    private final String lastName;
    private String displayName;
    private int icon;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private int age;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String firstName, String lastName, int age) {
        this(firstName, lastName);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    public boolean isAdult() {
        return age >= 18;
    }
}
