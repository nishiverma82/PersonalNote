package com.example.nishchal.personalnote;

import java.util.ArrayList;
import java.util.HashMap;

public class ConversionStringArray {

    public static String strSeparator = "\n☐ ";

    public static String convertArrayToString(ArrayList<HashMap<String, String>> array){
        String str;
        if(array.size() != 0) {
            str = "☐ ";
            for (int i = 0; i < array.size(); i++) {
                str = str + array.get(i).get("value") + array.get(i).get("item");
                // Do not append comma at the end of last element
                if (i < array.size() - 1) {
                    str = str + strSeparator;
                }
            }
        } else{
            str = "";
        }
        return str;
    }
    public static ArrayList<HashMap<String, String>> convertStringToArray(String str){
        ArrayList<HashMap<String, String>> array = new ArrayList<>();
        String data = str.substring(2);
        String[] arr = data.split(strSeparator);
        try {
            if (arr.length != 0) {
                for (int i = 0; i < arr.length; i++) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("value", String.valueOf(arr[i].charAt(0)));
                    map.put("item", String.valueOf(arr[i].substring(1)));
                    array.add(map);
                }
            }
        }catch (StringIndexOutOfBoundsException e){

        }
        return array;
    }

}
