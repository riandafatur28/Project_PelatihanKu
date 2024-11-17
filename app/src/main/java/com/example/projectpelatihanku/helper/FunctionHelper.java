package com.example.projectpelatihanku.helper;

/**
 * Class Helper untuk metode yang digunakan dalam aplikasi
 */
public class FunctionHelper {

    /**
     * Metode Helper untuk memotong String dengan panjang yang ditentukan
     *
     * @param text      String yang akan dipotong
     * @param batasKata Jumlah maksimum kata yang akan ditampilkan
     * @return String yang telah dipotong
     */
    public static String potongString(String text, int batasKata) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        String[] kata = text.split("\\s+");
        if (kata.length <= batasKata) {
            return text;
        }
        return String.join(" ", java.util.Arrays.copyOfRange(kata, 0, batasKata)) + "...";
    }

}
