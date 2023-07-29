package com.supremind.map.treemap;

public class CountryNameConverter {
    public static void main(String[] args) {
        System.out.println(CountryEnum.convertCountryName("china"));
        System.out.println(CountryEnum.convertCountryName("america"));
        System.out.println(CountryEnum.convertCountryName("japan"));
        System.out.println(CountryEnum.convertCountryName("england"));
        System.out.println(CountryEnum.convertCountryName("france"));
        System.out.println(CountryEnum.convertCountryName("germany"));
    }

}

enum CountryEnum {
    china("CN"),
    america("US"),
    japan("JP"),
    england("UK"),
    france("FR"),
    germany("DE"),
    ;

    public static String convertCountryName(String fullName) {
        return CountryEnum.valueOf(fullName).getFullName();
    }

    private String fullName;

    CountryEnum(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}