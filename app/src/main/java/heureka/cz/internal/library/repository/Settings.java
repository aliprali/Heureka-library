package heureka.cz.internal.library.repository;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Created by tomas on 18.5.16.
 */

@Table(name = "settings")
public class Settings extends Model {

    @Column(name = "api_address")
    public String apiAddress;

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    public String email;

    @Column(name = "active")
    public Integer active;

    public Settings() {
        super();
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Settings get() {
        return new Select()
                .from(Settings.class)
                .executeSingle();
    }
}
