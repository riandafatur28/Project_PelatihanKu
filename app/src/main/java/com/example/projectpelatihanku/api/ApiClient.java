package com.example.projectpelatihanku.api;

import static android.content.ContentValues.TAG;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.projectpelatihanku.Institusi;
import com.example.projectpelatihanku.MyNotification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    public static final String BASE_URL = "http://172.16.110.37:80/";
    // Inisialisasi OkHttpClient
    private OkHttpClient client = new OkHttpClient();

    // Login
    public void oauthLogin(String endPoint, String email, String password, CallBackHelper callback) throws JSONException {

        // Buat Object untuk menampung data user
        JSONObject data = new JSONObject();
        data.put("email", email);
        data.put("password", password);

        RequestBody body = RequestBody.create(
                data.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder().url(BASE_URL + endPoint).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Network Error", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Mendapatkan data sebagai String
                    String data = response.body().string(); // Ini akan memberikan konten JSON sebagai String
//                    Log.d(TAG, "onResponse: " + data);
                    try {
                        JSONObject userData = new JSONObject(data);
                        String status = userData.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            callback.onLoginSuccess(userData.getString("name"));
                        } else {
                            callback.onLoginFailed();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("Response Error", "Response code: " + response.code());
                }
            }
        });
    }

    public interface ResponseInstitute {
        void onSucces(String data[]);

        void onFailed(IOException e);
    }

    // institusi
    public void fetchInstitusi(String endPoint, GetResponese resInstitute) {
        Request request = new Request.Builder().url(BASE_URL + endPoint).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                resInstitute.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        final String data = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.getBoolean("success")) {
                                JSONArray institutes = jsonObject.getJSONArray("institutes");

                                JSONObject institute = institutes.getJSONObject(0);

                                    // Mengambil data dari setiap objek
                                    String id = institute.getString("id");
                                    String nama = institute.getString("nama");
                                    String pimpinan = institute.getString("pimpinan");
                                    String kepemilikan = institute.getString("kepemilikan");
                                    String status_beroperasi = institute.getString("status_beroperasi");
                                    String email = institute.getString("email");
                                    String no_tlp = institute.getString("no_tlp");
                                    String no_vin = institute.getString("no_vin");
                                    String no_sotk = institute.getString("no_sotk");
                                    String tipe = institute.getString("tipe");
                                    String no_fax = institute.getString("no_fax");
                                    String website = institute.getString("website");
                                    String deskripsi = institute.getString("deskripsi");
                                    String thnBerdiriString = institute.getString("thn_berdiri");

                                    // Simpan setiap data ke element array
                                    String dataInstitute[] = new String[14];
                                    dataInstitute[0] = id;
                                    dataInstitute[1] = nama;
                                    dataInstitute[2] = pimpinan;
                                    dataInstitute[3] = kepemilikan;
                                    dataInstitute[4] = status_beroperasi;
                                    dataInstitute[5] = email;
                                    dataInstitute[6] = no_tlp;
                                    dataInstitute[7] = no_vin;
                                    dataInstitute[8] = no_sotk;
                                    dataInstitute[9] = tipe;
                                    dataInstitute[10] = no_fax;
                                    dataInstitute[11] = website;
                                    dataInstitute[12] = deskripsi;
                                    dataInstitute[13] = thnBerdiriString;

//                                 Kirim data ke views
                                resInstitute.onSuccesArray(dataInstitute);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Log.d(TAG, "onResponse: failed disini");
                    }
                } catch (IOException e) {
                    Log.d(TAG, "onResponse: Pengambilan data " + e.getMessage());
                }

            }
        });
    }

    // ADAPTER
    public interface GetResponese {
        void onSuccesArray(String data[]);

        void onSuccessArrayList(ArrayList<String> data);

        void onSuccessFetchNotif(ArrayList<MyNotification> data);

        void onSuccessFetchDepartment(ArrayList<Institusi> data);

        void onFailure(IOException e);
    }

    // GET Departments
    public void fetchDepartment(String endPoint, GetResponese listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + endPoint)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        ArrayList<Institusi> dataDepartments = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        if(!jsonObject.getBoolean("isEmpty")){
                            JSONArray departments = jsonObject.getJSONArray("departments");
                            for (int i = 0; i < departments.length(); i++) {
                                JSONObject department = departments.getJSONObject(i);
                                String id = department.getString("id");
                                String nama = department.getString("nama");
                                String deskripsi = department.getString("deskripsi");
                                dataDepartments.add(new Institusi(id, nama, deskripsi,null));
                            }
                            listener.onSuccessFetchDepartment(dataDepartments);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("Network Error", "Response unsuccessful or empty body");
                }
            }
        });
    }

    // program

    public void fetchProgram(String endPoint) {
        Request request = new Request.Builder().url(BASE_URL + endPoint).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String data = response.body().string();
                    Log.d("Data program", data);
                    try {
                        // Inisialisasi JSONArray dari string response
                        JSONArray jsonArray = new JSONArray(data);

                        // Iterasi melalui setiap objek dalam JSONArray
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Mengambil data dari setiap objek
                            String id = jsonObject.getString("id");
                            String pesan = jsonObject.getString("nama");
                            String tipe = jsonObject.getString("deskripsi");
                            System.out.println(id);
                            System.out.println(pesan);
                            System.out.println(tipe);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // Notifikasi di ApiClient.java
    public void fetchNotifikasi(GetResponese listener, String endPoint) {
        // Membuat request ke server
        Request request = new Request.Builder()
                .url(BASE_URL + endPoint)
                .build();

        // Melakukan request secara asynchronous
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Network Error", "Request failed: " + e.getMessage());
                listener.onFailure(e); // Menginformasikan kegagalan
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Ambil response JSON dari server dalam bentuk String
                    // ex : {'success': true, 'data': [{id:1, 'message': 'oke'}, {id:1, 'message': 'oke'}]} type: String
                    final String responseData = response.body().string();
                    Log.d(TAG, "onResponse: " + responseData);

                    // Inisialisasi ArrayList untuk menyimpan notifikasi
                    ArrayList<MyNotification> notifications = new ArrayList<>();

                    try {
                        // Ubah responseData(String) menjadi JSONObject
                        // ex : {'success': true, 'notifications': [{id:1, 'message': 'oke'}, {id:1, 'message': 'oke'}]} type: JSON Object
                        JSONObject jsonObject = new JSONObject(responseData);
                        // Cek value property success dari Response JSON
                        if (jsonObject.getBoolean("success")) {

                            // Dapatkan array `notifications`
                            JSONArray notificationsArray = jsonObject.getJSONArray("notifications");
                            // Iterasi melalui setiap objek dalam array `notifications`
                            for (int i = 0; i < notificationsArray.length(); i++) {
                                JSONObject notificationObject = notificationsArray.getJSONObject(i); // {id:1, 'message': 'oke'}
                                // Ambil data dari setiap objek
                                String id = notificationObject.getString("id");
                                String pesan = notificationObject.getString("pesan");
                                String tipe = notificationObject.getString("tipe");
                                notifications.add(new MyNotification(id, pesan, false));
                            }
                            listener.onSuccessFetchNotif(notifications);
                        } else {
                            Log.e("Response Error", "Request was not successful.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onFailure(new IOException("JSON parsing error"));
                    }// Menginformasikan keberhasilan
                } else {
                    Log.e("Request failed", "Code: " + response.code());
                }
            }
        });
    }

    public void fetchDepartment(String s) {
    }

    public void onLog(String endPoint, CallBackHelper callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + endPoint)
                .build();

        OkHttpClient client = new OkHttpClient(); // Pastikan client sudah diinisialisasi

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Network Error", "Request failed: " + e.getMessage());
                callback.onProfileFetchFailure("Gagal mengambil data profil. Silakan coba lagi."); // Kirim pesan error
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.getBoolean("success")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String name = data.getString("name");
                            String email = data.getString("email");
                            String ttl = data.getString("ttl");
                            String phoneNumber = data.getString("phoneNumber");
                            String jenisKelamin = data.getString("jenisKelamin");
                            String alamat = data.getString("alamat");

                            callback.onProfileFetchSuccess(name, email, ttl, phoneNumber, jenisKelamin, alamat);
                        } else {
                            Log.e("Response Error", "Failed to fetch profile.");
                            callback.onProfileFetchFailure("Data profil tidak ditemukan.");
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Parsing Error", "Error parsing JSON: " + e.getMessage());
                        callback.onProfileFetchFailure("Kesalahan parsing data.");
                    }
                } else {
                    Log.e("Request failed", "Response Code: " + response.code());
                    callback.onProfileFetchFailure("Gagal mengambil data profil. Kode: " + response.code());
                }
            }
        });
}



    // Callback interface untuk mengirimkan data kembali
    public interface OnDataFetchedListener {
        void onProfileFetchSuccess(String name, String email, String ttl, String tlp, String jenisKelamin, String alamat);

        void onProfileFetchFailure(String errorMessage);

        void onSuccess(List<MyNotification> notifications);

        void onFailure(IOException e);
    }
}
