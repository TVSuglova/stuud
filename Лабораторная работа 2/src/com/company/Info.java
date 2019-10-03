package com.company;

import java.time.LocalDate;

public class Info
{
    private String name, surname, gender;
    private LocalDate date;

    public String toString()
    {
        return name + " " + surname + " " + String.format("%d.%s.%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear()) + " " + gender;
    }

    public Info(String info)
    {
        String[] i = info.split("(\\s)|(\\.)");
        this.name = i[0];
        this.surname = i[1];
        date = LocalDate.of(Integer.parseInt(i[4]), Integer.parseInt(i[3]), Integer.parseInt(i[2]));
        this.gender = i[5];
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getGender()
    {
        return gender;
    }

    public int getAge()
    {
        int age = LocalDate.now().getYear() - date.getYear();
        if (LocalDate.now().getMonthValue() - date.getMonthValue() < 0)
            age--;
        else if (LocalDate.now().getDayOfMonth() - date.getDayOfMonth() < 0)
            age--;
        return age;
    }
}
