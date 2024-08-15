package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@NoArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;
    private LocalDate birthday;
    private String title;

    public Customer(String id, String name, String address, String phoneNumber, LocalDate birthday, String title) {
        this.id = id;
        this.name = title+name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }
}
