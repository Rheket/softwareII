package DAO;

public class Database {

    //public static String address = null;

    public static int getAddressId(String address) {

        int addressId = 0;

        for (int i = 0; i < 3; i++) {

            switch (address) {
                case "123 Main":
                    addressId = 1;
                    break;
                case "123 Elm":
                    addressId = 2;
                    break;
                case "123 Oak":
                    addressId = 3;
                    break;
            }

        }
        return addressId;
    }

    public static String getCityName(int addressId) {

        String city = null;

        if (addressId == 1) {
            city = "New York";
        } else if (addressId == 2) {
            city = "Toronto";
        } else if (addressId == 3) {
            city = "Oslo";
        }
        return city;
    }

    public static String getCountryName(int cityId) {

        String country = null;

        if (cityId == 1 || cityId == 2) {
            country = "US";
        } else if (cityId == 3 || cityId == 4) {
            country = "Canada";
        } else if (cityId == 5) {
            country = "Norway";
        }
        return country;
    }

    public static String getAddressName(int addressId) {

        String addressName = null;

        if (addressId == 1) {
            addressName = "123 Main";
        } else if (addressId == 2) {
            addressName = "123 Elm";
        } else if (addressId == 3) {
            addressName = "123 Oak";
        }
        return addressName;
    }

    public static String getZip(String address) {
        String zipCode = null;

        switch (address) {
            case "123 Main":
                zipCode = "11111";
                break;
            case "123 Elm":
                zipCode = "11112";
                break;
            case "123 Oak":
                zipCode = "11113";
                break;
        }
        return zipCode;

    }
}
