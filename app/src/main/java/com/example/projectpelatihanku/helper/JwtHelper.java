package com.example.projectpelatihanku.helper;

import com.auth0.android.jwt.JWT;

import java.util.Map;

/**
 * Kelas Helper untuk mengambil data dari token JWT
 */
public class JwtHelper {

    /**
     * Mendapatkan data spesifik dari token JWT
     * @param claim klaim yang ingin diambil (ex. users)
     * @param dataKey kunci data yang ingin diambil (ex. username)
     * @return data yang diambil atau pesan error jika gagal
     */
    public static String getUserData(String token, String claim, String dataKey) {
        try {
            JWT jwt = new JWT(token);
            Map<String, Object> users = jwt.getClaim(claim).asObject(Map.class);
            if (users != null && users.containsKey(dataKey)) {
                return users.get(dataKey).toString();
            } else {
                return "Data " + dataKey + " tidak ditemukan di klaim " + claim;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error decoding token: " + e.getMessage();
        }
    }
}
