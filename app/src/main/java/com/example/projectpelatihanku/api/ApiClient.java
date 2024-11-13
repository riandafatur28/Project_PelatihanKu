
package com.example.projectpelatihanku.api;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projectpelatihanku.DashboardData;
import com.example.projectpelatihanku.Department;
import com.example.projectpelatihanku.DetailProgram;
import com.example.projectpelatihanku.MyNotification;
import com.example.projectpelatihanku.Program;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    public static final String BASE_URL = "http://192.168.1.6:80/";
    public static final String BASE_URL_PUBLIC = "api/v1/public";
    // Inisialisasi OkHttpClient
    private OkHttpClient client = new OkHttpClient();


    public interface RegisterHelper {
        void onSuccess(String response);
        void onFailed(IOException e);
    }

    public void Register(String endPoint, String nama, String jk, String ttl, String tlp, String email, String password, String konfirmPass, Bitmap fotoProfil, String alamat, RegisterHelper callback) throws JSONException {
        // Membuat JSON untuk dikirim
        JSONObject dataReg = new JSONObject();
        dataReg.put("name", nama);
        dataReg.put("jenis_kelamin", jk);
        dataReg.put("tanggal_lahir", ttl);
        dataReg.put("phone", tlp);
        dataReg.put("email", email);
        dataReg.put("password", password);
        dataReg.put("alamat", alamat);

        // Konversi gambar (Bitmap) menjadi Base64
        if (fotoProfil != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            fotoProfil.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // Format JPEG, kompresi 100%
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT); // Encode ke Base64
            dataReg.put("pas_foto", encodedImage); // Tambahkan ke JSON
        } else {
            dataReg.put("pas_foto", JSONObject.NULL); // Jika gambar tidak ada, kirim null
        }

        // Log JSON yang dikirim untuk debugging
        Log.d("RegisterDebug", "JSON sent to server: " + dataReg.toString());

        // Konversi JSON menjadi RequestBody
        RequestBody body = RequestBody.create(
                dataReg.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Membuat request dengan header Content-Type yang benar
        Request request = new Request.Builder()
                .url(BASE_URL + endPoint)
                .post(body)
                .addHeader("Content-Type", "application/json")  // Tambahkan header Content-Type
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("RegisterFailure", "onFailure: " + e.getMessage());
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String dataRegResponse = response.body().string();
                Log.d("Server Response", dataRegResponse);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonData = new JSONObject(dataRegResponse);
                        if (jsonData.has("status") && jsonData.getString("status").equalsIgnoreCase("success")) {
                            String data = jsonData.optString("data", "");
                            callback.onSuccess(data);
                        } else {
                            String errorMessage = jsonData.optString("message", "Unknown error");
                            callback.onFailed(new IOException(errorMessage));
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Parsing Error", e.getMessage());
                        callback.onFailed(new IOException("Failed to parse JSON response"));
                    }
                } else {
                    callback.onFailed(new IOException("Request failed with status " + response.code()));
                }
            }
        });
    }

    // Adapter Login API
    public interface LoginHelper {
        void onSuccess(String token);

        void onFailed(IOException e);
    }

    // Login
    public void oauthLogin(String endPoint, String email, String password, LoginHelper callback) throws JSONException {

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
                Log.d("Gagal kirim request", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String data = response.body().string();
                    if (response.isSuccessful()) {
                        try {
                            JSONObject JsonData = new JSONObject(data);
                            if (JsonData.getString("status").equalsIgnoreCase("success")) {
                                String token = JsonData.getString("token");
                                callback.onSuccess(token);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (IOException e) {
                    callback.onFailed(new IOException(response.message()));
                }
            }

        });
    }

    public interface DashboardDataHelper {
        void onSuccess(ArrayList<DashboardData> data);
        void onFailed(IOException e);
    }

    public void fetchDashboard(String token, String endPoint, DashboardDataHelper callback) {
        String url = BASE_URL + BASE_URL_PUBLIC + endPoint;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    try {
                        ArrayList<DashboardData> dashboardDataList = parseDashboardJson(jsonData);
                        callback.onSuccess(dashboardDataList);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("JSON Parsing Error", e));
                    }
                } else {
                    callback.onFailed(new IOException("Response not successful or body is null"));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(e);
            }
        });
    }

    private ArrayList<DashboardData> parseDashboardJson(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        if (!jsonObject.getBoolean("success")) {
            throw new JSONException("Response not successful");
        }

        JSONArray dataArray = jsonObject.getJSONArray("data");
        ArrayList<DashboardData> dashboardDataList = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            String tableName = dataObject.getString("table_name");
            int total = dataObject.getInt("total");
            dashboardDataList.add(new DashboardData(tableName, total));
        }

        return dashboardDataList;
    }


    public interface InstituteHelper {
        void onSuccess(String data[]);

        void onFailed(IOException e);
    }

    // institusi
    public void fetchInstitusi(String endPoint, String token, InstituteHelper resInstitute) {
        Log.d("fetch", "fetchInstitusi: " + BASE_URL + BASE_URL_PUBLIC + endPoint);
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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
                            // Kirim data ke views
                            resInstitute.onSuccess(dataInstitute);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.d("Gagal", "Gagal kirim permintaan");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                resInstitute.onFailed(e);
            }
        });
    }

    //    Department adapter
    public interface DepartmentHelper {
        void onSuccess(ArrayList<Department> data);

        void onFailed(IOException e);
    }

    // GET Departments
    public void fetchDepartment(String token, String endPoint, DepartmentHelper listener) {
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        ArrayList<Department> dataDepartments = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        if (!jsonObject.getBoolean("isEmpty")) {
                            JSONArray departments = jsonObject.getJSONArray("departments");
                            for (int i = 0; i < departments.length(); i++) {
                                JSONObject department = departments.getJSONObject(i);
                                String id = department.getString("id");
                                String nama = department.getString("nama");
                                String deskripsi = department.getString("deskripsi");
                                dataDepartments.add(new Department(id, nama, deskripsi, null));
                            }
                            listener.onSuccess(dataDepartments);
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

    //    Program adapter
    public interface ProgramHelper {
        void onSuccess(ArrayList<Program> data);

        void onFailed(IOException e);
    }

    // GET Programs
    public void fetchProgram(String token, String endPoint, ProgramHelper listener) {
        Log.d("Endpoint", "fetchProgram: " + BASE_URL + BASE_URL_PUBLIC + endPoint);
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        ArrayList<Program> dataPrograms = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        Log.d(TAG, "onResponse: fetch program" + jsonObject);
                        if (!jsonObject.getBoolean("isEmpty")) {
                            JSONArray programs = jsonObject.getJSONArray("programs");
                            for (int i = 0; i < programs.length(); i++) {
                                JSONObject program = programs.getJSONObject(i);
                                String id = program.getString("id");
                                String nama = program.getString("nama");
                                String deskripsi = program.getString("deskripsi");
                                dataPrograms.add(new Program(id, nama, deskripsi, null));
                            }
                            listener.onSuccess(dataPrograms);
                        }
                    } catch (JSONException e) {
                        Log.e("Network Error", "JSON Parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e("Network Error", "Respon gagal: " + response.message());
                }
            }
        });
    }

    //  Detail Program adapter
    public interface DetailProgramHelper {
        void onSuccess(ArrayList<DetailProgram> data);

        void onFailed(IOException e);
    }

    public void fetchDetailProgram(String departmentId, DetailProgramHelper resDetailProgram, String programId) {
        String endPoint = "/api/v1/public/departments/" + departmentId + "/programs/" + programId;

        // Debug log untuk memastikan URL yang dibangun benar
        Log.d("Endpoint", "fetchDetailProgram: " + BASE_URL + BASE_URL_PUBLIC + endPoint);

        // Membuat request untuk mengambil data program
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .build();

        // Mengirim permintaan secara asynchronous
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                resDetailProgram.onFailed(new IOException("Request Failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Mendapatkan response body dalam bentuk string
                    String data = response.body().string();
                    Log.d("API Response", data);  // Debug log untuk melihat response body

                    try {
                        // Inisialisasi ArrayList untuk menampung detail program
                        ArrayList<DetailProgram> detailPrograms = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);  // Parsing JSON response

                        // Debug log untuk melihat JSON response
                        Log.d(TAG, "onResponse: fetch detailprogram" + jsonObject);

                        // Cek jika data program ada dan tidak kosong
                        if (!jsonObject.getBoolean("isEmpty")) {
                            // Ambil array "programs" dari JSON
                            JSONArray programArray = jsonObject.getJSONArray("programs");

                            // Loop untuk mengambil setiap data detail program
                            for (int i = 0; i < programArray.length(); i++) {
                                JSONObject program = programArray.getJSONObject(i);

                                // Menyimpan data program ke dalam DetailProgram model
                                String id = program.getString("id");
                                String nama = program.getString("nama");
                                String deskripsi = program.getString("deskripsi");

                                // Ambil data lainnya sesuai dengan response API
                                String imageUrl = program.optString("imageUrl", "");
                                String statusPendaftaran = program.optString("statusPendaftaran", "Belum Pendaftaran");
                                String tanggalMulai = program.optString("tanggalMulai", "Tanggal Mulai");
                                String tanggalAkhir = program.optString("tanggalAkhir", "Tanggal Akhir");
                                String standar = program.optString("standar", "Standar");
                                String peserta = program.optString("peserta", "Peserta");

                                // Membuat objek DetailProgram dan menambahkannya ke dalam list
                                DetailProgram detailProgram = new DetailProgram(id, nama, deskripsi, imageUrl, statusPendaftaran,
                                        tanggalMulai, tanggalAkhir, standar, peserta);
                                detailPrograms.add(detailProgram);
                            }

                            // Mengirimkan data detail program ke listener jika berhasil
                            resDetailProgram.onSuccess(detailPrograms);
                        } else {
                            // Mengirimkan error jika data kosong
                            resDetailProgram.onFailed(new IOException("Data is empty"));
                        }
                    } catch (JSONException e) {
                        // Menangani error JSON
                        Log.e(TAG, "onResponse: JSON Parsing error", e);
                        resDetailProgram.onFailed(new IOException("JSON Parsing error", e));
                    }
                } else {
                    // Jika response tidak berhasil
                    resDetailProgram.onFailed(new IOException("Response not successful"));
                }
            }
        });
    }



    // Fungsi untuk parsing JSON
    private String[] parseDetailProgramJson(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        if (!jsonObject.getBoolean("success")) {
            throw new JSONException("Response success flag is false");
        }

        JSONArray detailProgram1 = jsonObject.getJSONArray("detailProgram1");
        JSONObject detailProgram = detailProgram1.getJSONObject(0);

        // Ambil data dari JSON dan simpan ke array
        String[] dataDetailProgram = new String[14];
        dataDetailProgram[0] = detailProgram.getString("id");
        dataDetailProgram[1] = detailProgram.getString("nama");
        dataDetailProgram[2] = detailProgram.getString("kejuruan");
        dataDetailProgram[3] = detailProgram.getString("standar");
        dataDetailProgram[4] = detailProgram.getString("peserta");
        dataDetailProgram[5] = detailProgram.getString("gedung");
        dataDetailProgram[6] = detailProgram.getString("deskripsi");
        dataDetailProgram[7] = detailProgram.getString("idInstructor");
        dataDetailProgram[8] = detailProgram.getString("namaInstructor");
        dataDetailProgram[9] = detailProgram.getString("alamatInstructor");
        dataDetailProgram[10] = detailProgram.getString("kontakInstructor");
        dataDetailProgram[11] = detailProgram.getString("status");
        dataDetailProgram[12] = detailProgram.getString("tglPendaftaran");

        return dataDetailProgram;
    }



// Notifikasi adapter
    public interface NotifikasiHelper {
        void onSuccess(ArrayList<MyNotification> data);

        void onFailed(IOException e);
    }

    // Notifikasi
    public void fetchNotifikasi(String token, String endPoint, NotifikasiHelper callback) {
        Log.d(TAG, "EndPoint : " + BASE_URL + BASE_URL_PUBLIC + endPoint);
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        // Melakukan request secara asynchronous
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Network Error", "Request failed: " + e.getMessage());
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
                            callback.onSuccess(notifications);
                        } else {
                            Log.e("Response Error", "Request was not successful.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFailed(new IOException("JSON parsing error"));
                    }// Menginformasikan keberhasilan
                } else {
                    Log.e("Request failed", "Code: " + response.code());
                }
            }
        });
    }

    // PROFILE ADAPTER
    public interface ProfileHelper {
        void onSuccess(String name, String jk, String ttl, String tlp, String email, String alamat);

        void onFailed(IOException e);
    }

    // PROFILE
    public void FetchProfile(String id, String token, String endPoint, ProfileHelper callback) {
        Log.d(TAG, "EndPoint : " + BASE_URL + BASE_URL_PUBLIC + endPoint);
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Gagal", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        JSONObject JsonData = new JSONObject(data);
                        if (!JsonData.getBoolean("isEmpty")) {
                            JSONObject users = JsonData.getJSONObject("users");
                            String name = users.getString("nama");
                            String gender = users.getString("jenis_kelamin");
                            String birth = users.getString("tanggal_lahir");
                            String phone = users.getString("tlp");
                            String email = users.getString("email");
                            String address = users.getString("alamat");
                            callback.onSuccess(name, gender, birth, phone, email, address);
                        } else {
                            callback.onFailed(new IOException(JsonData.getString("message")));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
