package ru.geekbrains.garmatin.lesson10.part2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneBook {
    Map<Long, String> phoneToSurname = new HashMap<>();

    public void add(String surname, long phoneNumber) {
        phoneToSurname.put(phoneNumber, surname);
    }

    public List<Long> get(String surname) {
        return phoneToSurname.entrySet().stream()
                .filter(pair -> pair.getValue().equals(surname))
                .collect(
                        ArrayList::new,
                        (list, pair) -> list.add(pair.getKey()),
                        ArrayList::addAll
                );
    }
}
