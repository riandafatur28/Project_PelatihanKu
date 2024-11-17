
package com.example.projectpelatihanku.api;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projectpelatihanku.Models.Dashboard;
import com.example.projectpelatihanku.Models.Department;
import com.example.projectpelatihanku.Models.DetailProgram;
import com.example.projectpelatihanku.Models.Requirements;
import com.example.projectpelatihanku.MyNotification;
import com.example.projectpelatihanku.Models.Program;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    public static final String BASE_URL = "https://alive-fluent-sponge.ngrok-free.app/";
    public static final String BASE_URL_PUBLIC = "api/v1/public";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * Callback Class
     */

    /**
     * Interface untuk Callback Login
     */
    public interface LoginHelper {
        void onSuccess(String token);

        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Department
     */
    public interface DepartmentHelper {
        /**
         * Callback untuk menerima hasil dari request
         *
         * @param data daftar department yang berhasil diambil
         * @see Department
         */
        void onSuccess(ArrayList<Department> data);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Program
     */
    public interface ProgramHelper {
        /**
         * Callback untuk menerima hasil dari request
         *
         * @param data daftar program yang berhasil diambil
         * @see Program
         */
        void onSuccess(ArrayList<Program> data);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Detail Program
     */
    public interface DetailProgramHelper {
        /**
         * Callback untuk menerima hasil dari request
         *
         * @param data daftar detail program yang berhasil diambil
         * @see DetailProgram
         */
        void onSuccess(ArrayList<DetailProgram> data);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Requirements
     */
    public interface requirementsHelper {
        /**
         * Callback untuk menerima hasil dari request
         *
         * @param data daftar requirements program yang berhasil diambil
         * @see Requirements
         */
        void onSuccess(ArrayList<Requirements> data);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Request Reset Password
     */
    public interface RequestResetPassword {
        /**
         * Callback untuk menerima hasil dari request.
         * Ambil dan gunakan token dari response untuk lanjut ke tahap verifikasi OTP
         *
         * @param token token yang berhasil diambil
         */
        void onSuccess(String token);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request.
         *          Gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    // Inisialisasi OkHttpClient
    private OkHttpClient client = new OkHttpClient();

    public interface RegisterHelper {
        void onSuccess(String response);

        void onFailed(IOException e);
    }


    public void Register(String endPoint, String nama, String gender, String tanggalLahir, String nomorTelepon, String email, String password, String konfirmasiPassword, byte[] fotoProfil, String alamat, RegisterHelper registerHelper) throws JSONException {

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // Menambahkan data form lainnya
        bodyBuilder.addFormDataPart("nama", nama);
        bodyBuilder.addFormDataPart("gender", gender);
        bodyBuilder.addFormDataPart("tanggalLahir", tanggalLahir);
        bodyBuilder.addFormDataPart("noTelp", nomorTelepon);
        bodyBuilder.addFormDataPart("email", email);
        bodyBuilder.addFormDataPart("password", password);
        bodyBuilder.addFormDataPart("konfirmasiPassword", konfirmasiPassword);
        bodyBuilder.addFormDataPart("alamat", alamat);

        // Menambahkan gambar sebagai byte array dengan media type yang sesuai
        if (fotoProfil != null) {
            bodyBuilder.addFormDataPart("fotoProfil", "profile_picture.jpg",
                    RequestBody.create(MediaType.parse("image/jpeg"), fotoProfil));
        }

        RequestBody requestBody = bodyBuilder.build();
        Request request = new Request.Builder()
                .url(BASE_URL + endPoint) // Ganti dengan endpoint API Anda
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                registerHelper.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    registerHelper.onSuccess(response.body().string());
                } else {
                    registerHelper.onFailed(new IOException("Failed to register"));
                }
            }
        });

    }

    /**
     * Service Metode untuk mengirim permintaan login ke server.
     *
     * @param endPoint URL endpoint untuk login
     * @param email    alamat email user
     * @param password password user
     * @param callback callback untuk hasil login
     * @throws JSONException jika terjadi kesalahan dalam parsing JSON
     */
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

    /**
     * Service Metode untuk mengirim permintaan kode OTP ke server.
     * Kode OTP dikirim ke email user yang dikirim.
     * Server memberikan response berisi token yang digunakan untuk verifikasi OTP
     *
     * @param email    alamat email user yang akan dikirimkan kode OTP.
     *                 email yang digunakan adalah email yang terdaftar di aplikasi.
     * @param callback callback untuk hasil request reset kode otp
     */
    public void sendRequestOtp(String email, RequestResetPassword callback) {
        String url = BASE_URL + "password-reset/request";

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal Mengirim Request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);
                    if (response.isSuccessful()) {
                        if (jsonResponse.has("token")) {
                            String token = jsonResponse.getString("token");
                            callback.onSuccess(token);
                        } else {
                            callback.onFailed(new IOException("Token Tidak ditemukan"));
                        }
                    } else if (response.code() == 401) {
                        String message = jsonResponse.getString("message");
                        callback.onFailed(new IOException(message));
                    } else {
                        callback.onFailed(new IOException("Gagal dengan code response: " + response.code()));
                    }
                } catch (JSONException e) {
                    callback.onFailed(new IOException("Error saat parsing JSON: " + e.getMessage()));
                } catch (IOException e) {
                    callback.onFailed(new IOException(e.getMessage()));
                }
            }
        });
    }

    public interface PassResetHelper {
        void onSuccess(String tempToken);

        void onFailed(IOException e);
    }

    public interface OtpVerificationHelper {
        void onSuccess(String finalToken);

        void onFailed(IOException e);
    }

    public void requestPasswordReset(String email, PassResetHelper callback) {
        String url = BASE_URL + "password-reset/request";
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Password Reset", "Request failed: " + e.getMessage());
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String tempToken = jsonResponse.getString("temp_token");
                        callback.onSuccess(tempToken);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Error parsing response"));
                    }
                } else {
                    callback.onFailed(new IOException("Unexpected response code " + response.code()));
                }
            }
        });
    }

    public void verifyOtp(String otp, OtpVerificationHelper callback) {
        String url = BASE_URL + "password-reset/verify";
        JSONObject json = new JSONObject();
        try {
            json.put("otp", otp);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer ")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("OTP Verification", "Request failed: " + e.getMessage());
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        String finalToken = jsonResponse.getString("token");
                        callback.onSuccess(finalToken);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Error parsing JSON"));
                    }
                } else {
                    callback.onFailed(new IOException("Unexpected response code " + response.code()));
                }
            }
        });
    }


    public interface ResendOtpHelper {
        void onSuccess(String message);

        void onFailed(IOException e);
    }

    // Metode untuk mengirim ulang OTP
    public void resendOtp(String finalToken, ResendOtpHelper callback) {
        String url = BASE_URL + "password-reset/resend";

        // Membuat JSON body kosong atau tambahkan field sesuai kebutuhan server
        JSONObject json = new JSONObject();
        try {
            json.put("action", "resend_otp"); // Sesuaikan dengan kebutuhan server jika ada data tambahan
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Membuat RequestBody
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        // Membuat Request dengan URL, Authorization header, dan body
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + finalToken) // Kirim token final di header Authorization
                .post(body)
                .build();

        Log.d("ResendOtp", "Request URL: " + url);
        Log.d("ResendOtp", "Request JSON: " + json.toString());

        // Melakukan request asinkron menggunakan enqueue
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("ResendOtp", "Request failed: " + e.getMessage());
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body() != null ? response.body().string() : "";
                Log.d("ResendOtp", "Response code: " + response.code());
                Log.d("ResendOtp", "Response data: " + responseData);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        if (jsonResponse.has("message")) {
                            String message = jsonResponse.getString("message");
                            callback.onSuccess(message);
                        } else {
                            callback.onFailed(new IOException("Message not found in response"));
                        }
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Error parsing JSON"));
                    }
                } else {
                    callback.onFailed(new IOException("Unexpected response code " + response.code()));
                }
            }
        });
    }

    public void resetPassword(String finalToken, String newPassword, RequestResetPassword callback) {
        String url = BASE_URL + "password-reset/new";

        // Membuat JSON body untuk permintaan reset password
        JSONObject json = new JSONObject();
        try {
            json.put("password", newPassword); // Ganti "password" jika server mengharuskan field berbeda
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Membuat RequestBody dari JSON
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        // Membuat Request dengan URL, Authorization header, dan menggunakan metode PUT
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + finalToken) // Mengirim token final dalam header Authorization
                .put(body) // Ubah dari .post(body) ke .put(body) untuk metode PUT
                .build();

        Log.d("PasswordReset", "Request URL: " + url);
        Log.d("PasswordReset", "Request JSON: " + json.toString());

        // Melakukan request asinkron menggunakan enqueue
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("PasswordReset", "Request failed: " + e.getMessage());
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body() != null ? response.body().string() : "";
                Log.d("PasswordReset", "Response code: " + response.code());
                Log.d("PasswordReset", "Response data: " + responseData);

                if (response.isSuccessful()) {
                    if (!responseData.isEmpty()) {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            String message = jsonResponse.has("message") ? jsonResponse.getString("message") : "Password reset successful.";
                            callback.onSuccess(message);
                        } catch (JSONException e) {
                            Log.d("PasswordReset", "JSON parsing error: " + e.getMessage());
                            callback.onSuccess("Password reset successful, but no additional message returned.");
                        }
                    } else {
                        callback.onSuccess("Password reset successful, but no additional message returned.");
                    }
                } else {
                    callback.onFailed(new IOException("Unexpected response code " + response.code() + " with data: " + responseData));
                }
            }
        });
    }


    public interface DashboardDataHelper {
        void onSuccess(ArrayList<Dashboard> data);

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
                        ArrayList<Dashboard> dashboardList = parseDashboardJson(jsonData);
                        callback.onSuccess(dashboardList);
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

    private ArrayList<Dashboard> parseDashboardJson(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        if (!jsonObject.getBoolean("success")) {
            throw new JSONException("Response not successful");
        }

        JSONArray dataArray = jsonObject.getJSONArray("data");
        ArrayList<Dashboard> dashboardList = new ArrayList<>();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataObject = dataArray.getJSONObject(i);
            String tableName = dataObject.getString("table_name");
            int total = dataObject.getInt("total");
            dashboardList.add(new Dashboard(tableName, total));
        }

        return dashboardList;
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

    /**
     * Service Metode untuk mengirim permintaan ke server
     * Gunakan untuk mengambil daftar department yang tersedia
     *
     * @param token    token JWT, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil daftar department
     * @param callback callback untuk menerima hasil dari permintaan
     * @see DepartmentHelper#onSuccess(ArrayList<Department>)
     * @see DepartmentHelper#onFailed(IOException)
     */
    public void fetchDepartment(String token, String endPoint, DepartmentHelper callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal Mengirim Request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        ArrayList<Department> dataDepartments = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);

                        if (jsonObject.has("isEmpty") && !jsonObject.getBoolean("isEmpty")) {
                            JSONArray departments = jsonObject.getJSONArray("departments");
                            for (int i = 0; i < departments.length(); i++) {
                                JSONObject department = departments.getJSONObject(i);
                                String id = department.getString("id");
                                String nama = department.getString("nama");
                                String deskripsi = department.getString("deskripsi");
                                String imageUrl = BASE_URL + department.getString("image_path");
                                dataDepartments.add(new Department(id, nama, deskripsi, imageUrl));
                            }

                            callback.onSuccess(dataDepartments);
                        } else {
                            callback.onFailed(new IOException("No departments found."));
                        }

                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan kode response: " + response.code()));
                }
            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan ke server
     * Gunakan untuk mengambil data daftar program dari berdasarkan deparment id
     * Kirim token ke header Authorization untuk mengakses endpoint
     *
     * @param token    token JWT, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil daftar program
     * @param callback callback untuk menerima hasil dari permintaan
     * @see ProgramHelper#onSuccess(ArrayList<Program>)
     * @see ProgramHelper#onFailed(IOException)
     */
    public void fetchProgram(String token, String endPoint, ProgramHelper callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.d(TAG, "onResponse: " + data);
                    try {
                        ArrayList<Program> dataPrograms = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        if (!jsonObject.getBoolean("isEmpty")) {
                            JSONArray programs = jsonObject.getJSONArray("programs");
                            for (int i = 0; i < programs.length(); i++) {
                                JSONObject program = programs.getJSONObject(i);
                                String id = program.getString("id");
                                String nama = program.getString("nama");
                                String deskripsi = program.getString("deskripsi");
                                String imageUri = BASE_URL + program.getString("image_path");
                                String departmentId = program.getString("department_id");
                                dataPrograms.add(new Program(id, nama, deskripsi, imageUri, departmentId));
                            }
                            callback.onSuccess(dataPrograms);
                        } else {
                            callback.onFailed(new IOException("No departments found."));
                        }
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan kode response: " + response.code()));
                }
            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan ke server
     * Gunakan untuk mengambil data detail program
     *
     * @param token    token JWT, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil data detail program
     * @param callback callback untuk menerima hasil dari permintaan
     * @see DetailProgramHelper#onSuccess(ArrayList)
     * @see DetailProgramHelper#onFailed(IOException)
     */
    public void fetchDetailProgram(String token, String endPoint, DetailProgramHelper callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal mengirim request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String data = response.body().string();
                        ArrayList<DetailProgram> detailPrograms = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray programs = jsonObject.getJSONArray("programs");
                        JSONObject program = programs.getJSONObject(0);

                        String id = program.getString("id");
                        String nama = program.getString("nama");
                        String statusPendaftaran = program.getString("status_pendaftaran");
                        String tanggalMulai = program.getString("tgl_mulai_pendaftaran");
                        String tanggalAkhir = program.getString("tgl_akhir_pendaftaran");
                        String standar = program.getString("standar");
                        String jmlPeserta = program.getString("jml_peserta");
                        String deskripsi = program.getString("deskripsi");
                        String programImageUri = BASE_URL + program.getString("image_path");
                        String instructorId = program.getString("instructor_id");
                        String buildingId = program.getString("building_id");
                        String departmentId = program.getString("department_id");
                        String instructorName = program.getString("instructor_name");
                        String instructorAddress = program.getString("instructor_address");
                        String instructorContact = program.getString("instructor_contact");
                        String instructorImageUri = BASE_URL + program.getString("instructor_image");
                        String buildingName = program.getString("building_name");
                        String departmentName = program.getString("department_name");

                        detailPrograms.add(new DetailProgram(id, nama, statusPendaftaran,
                                tanggalMulai, tanggalAkhir, standar, jmlPeserta, deskripsi,
                                programImageUri, instructorId, buildingId, departmentId,
                                instructorName, instructorAddress, instructorContact,
                                instructorImageUri, buildingName, departmentName));

                        callback.onSuccess(detailPrograms);
                    } catch (Exception e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Response gagal dengan status code: " + response.code()));
                }
            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan ke server
     * Gunakan untuk mengambil data requirements suatu program
     *
     * @param token    token JWT, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil data requirements
     * @param callback callback untuk menerima hasil dari permintaan
     * @see requirementsHelper#onSuccess(ArrayList)
     * @see requirementsHelper#onFailed(IOException)
     */
    public void fetchRequirements(String token, String endPoint, requirementsHelper callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String data = response.body().string();
                        ArrayList<Requirements> requirements = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray arrayRequirements = jsonObject.getJSONArray("requirements");
                        JSONObject ObjectRequirements = arrayRequirements.getJSONObject(0);
                        String requirement = ObjectRequirements.getString("requirement");
                        requirements.add(new Requirements(requirement));

                        callback.onSuccess(requirements);

                    } catch (Exception e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Response gagal dengan status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal mengirim request: " + e.getMessage()));
            }
        });
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
    public void FetchProfile(int id, String token, String endPoint, ProfileHelper callback) {
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

    public interface UserUpdateCallback {
        void onSuccess(String message);

        void onFailed(IOException e);
    }


    public void updateUserProfile(String userId, String nama, String noTelp, String alamat, UserUpdateCallback callback) {
        String url = BASE_URL + "user/updateUsers/" + userId;

        // Membuat JSON body untuk permintaan
        JSONObject json = new JSONObject();
        try {
            json.put("name", nama);
            json.put("phone", noTelp);
            json.put("address", alamat);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailed(new IOException("Error creating JSON"));
            return;
        }

        // Membuat RequestBody dari JSON
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        // Membuat Request dengan URL dan body
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // Melakukan request asinkron menggunakan enqueue
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body() != null ? response.body().string() : "";
                    callback.onSuccess(responseData);
                } else {
                    callback.onFailed(new IOException("Unexpected response code " + response.code()));
                }
            }
        });
    }

}