package io.thrive.fs.help;

import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DataGenerator {
    private String baseEmailPrefix = "vladimir.pavlyukov";
    private String baseEmailSuffix = "@thrive.io";

    public DataGenerator(){
        timestamp = System.currentTimeMillis() - 1657479582677L;
        en_faker = new Faker(new Locale("en"), new RandomService());
        br_faker = new Faker(new Locale("pt-BR"), new RandomService());
        ru_faker = new Faker(new Locale("ru"), new RandomService());
    }

    private long timestamp;
    private final Faker en_faker;
    private final Faker br_faker;
    private final Faker ru_faker;
    private Faker getFaker(String locale){
        switch (locale) {
            case "pt-BR":
                return br_faker;
            case "ru":
                return ru_faker;
            default:
                return en_faker;
        }
    }

    public String generateCity(String locale){
        return getFaker(locale).address().city();
    }

    public  String generateFullName(String locale){
        return  getFaker(locale).name().fullName();
    }

    public  String getEmail(){
        return  baseEmailPrefix + "+" + timestamp + baseEmailSuffix;
    }

    public  String getPhone(){
        return  "+" + (5547479582677L +timestamp);
    }

    public String getPassword(){
        return "Aa@45678";
    }
}
