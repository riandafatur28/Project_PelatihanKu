
package com.example.projectpelatihanku.api;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.projectpelatihanku.Models.Dashboard;
import com.example.projectpelatihanku.Models.Department;
import com.example.projectpelatihanku.Models.DetailProgram;
import com.example.projectpelatihanku.Models.Requirements;
import com.example.projectpelatihanku.Models.Notification;
import com.example.projectpelatihanku.Models.Program;
import com.example.projectpelatihanku.helper.FunctionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Service class untuk melakukan request ke server
 */
public class ApiClient {
    private OkHttpClient client = new OkHttpClient();
    private WebSocket webSocket;
    public static final String BASE_URL = "http://192.168.100.4:8080/";
    public static final String BASE_URL_PUBLIC = "api/v1/public";

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

    /**
     * Interface untuk Callback Verifikasi OTP
     */
    public interface OtpVerificationHelper {
        /**
         * Callback untuk menerima hasil dari request.
         * Ambil dan gunakan token dari response untuk lanjut ke tahap reset password
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

    /**
     * Interface untuk Callback Resend OTP
     */
    public interface ResendOtpHelper {
        /**
         * Callback untuk menerima hasil dari request
         *
         * @param message pesan dari server
         */
        void onSuccess(String message);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Register
     */
    public interface RegisterAccountHelper {
        /**
         * Callback untuk menerima hasil dari request.
         *
         * @param message pesan response dari server
         */
        void onSuccess(String message);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request.
         *          Gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback Profile
     */
    public interface ProfileHelper {
        /**
         * Callback untuk menerima hasil dari request GET data profile.
         *
         * @param name     Nama Pengguna
         * @param jk       Jenis Kelamin Pengguna
         * @param ttl      Tempat, Tanggal Lahir Pengguna
         * @param tlp      Telephone Pengguna
         * @param email    Email Pengguna
         * @param alamat   Alamat Pengguna
         * @param imageUri Uri Foto Profile Pengguna
         */
        void onSuccess(String name, String jk, String ttl, String tlp, String email,
                       String alamat, String imageUri);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface untuk Callback institusi
     */
    public interface InstituteHelper {
        /**
         * Callback untuk menerima hasil dari request.
         *
         * @param data data institusi yang terdiri dari beberapa informasi terkait institusi.
         *             Akses tiap data institusi dengan index yang telah ditentukan sebagai
         *             berikut:
         *             <ul>
         *               <li><b>dataInstitute[0]</b> = id (ID institusi)</li>
         *               <li><b>dataInstitute[1]</b> = nama (Nama institusi)</li>
         *               <li><b>dataInstitute[2]</b> = pimpinan (Nama pimpinan institusi)</li>
         *               <li><b>dataInstitute[3]</b> = kepemilikan (Jenis kepemilikan institusi)</li>
         *               <li><b>dataInstitute[4]</b> = status_beroperasi (Status apakah institusi sedang beroperasi)</li>
         *               <li><b>dataInstitute[5]</b> = email (Alamat email institusi)</li>
         *               <li><b>dataInstitute[6]</b> = no_tlp (Nomor telepon institusi)</li>
         *               <li><b>dataInstitute[7]</b> = no_vin (Nomor VIN, jika relevan)</li>
         *               <li><b>dataInstitute[8]</b> = no_sotk (Nomor SOTK institusi)</li>
         *               <li><b>dataInstitute[9]</b> = tipe (Tipe institusi)</li>
         *               <li><b>dataInstitute[10]</b> = no_fax (Nomor fax institusi)</li>
         *               <li><b>dataInstitute[11]</b> = website (Website institusi)</li>
         *               <li><b>dataInstitute[12]</b> = deskripsi (Deskripsi institusi)</li>
         *               <li><b>dataInstitute[13]</b> = thnBerdiriString (Tahun berdiri institusi dalam format string)</li>
         *             </ul>
         */

        void onSuccess(String data[]);

        /**
         * Callback untuk menerima error dari request
         *
         * @param e error yang terjadi saat request
         *          gunakan e.message untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Interface callback untuk menerima hasil dari permintaan
     */
    public interface updateProfileHelper {
        /**
         * Callback untuk menerima message dari permintaan
         *
         * @param message message dari permintaan
         */
        void onSuccess(String message);

        /**
         * Callback untuk menerima error dari permintaan
         *
         * @param e error dari permintaan. Gunakan e.getMessage() untuk mendapatkan pesan error
         */
        void onFailed(IOException e);
    }

    /**
     * Callback untuk menerima respons dari server
     */
    public interface RegistrationsProgramsHelper {
        /**
         * Callback untuk menerima data dari hasil request
         *
         * @param message            response pesan dari server
         * @param registrationNumber nomor pendaftaran yang diterima dari server
         */
        void onSuccess(String message, String registrationNumber);

        /**
         * Callback untuk menerima error
         *
         * @param e error yang terjadi saat request
         */
        void onFailed(IOException e);
    }

    /**
     * Callback untuk menerima respons dari server
     */
    public interface WebSocketCallback {
        /**
         * callback untuk menerima data notifikasi hasil dari request websocket
         *
         * @param data daftar notifikasi yang diterima dari server
         */
        void onMessageReceived(List<Notification> data);

        /**
         * callback untuk menerima error request websocket
         *
         * @param e error yang terjadi saat request websocket
         */
        void onFailure(IOException e);
    }

    /**
     * Callback untuk menerima respons dari server
     */
    public interface imageProfile {
        /**
         * Callback untuk menerima hasil dari request ke server
         *
         * @param uri Uri gambar yang berhasil didapatkan
         */
        void onSuccess(String uri);

        /**
         * Callback untuk menerima error dari request ke server
         *
         * @param e Exception yang terjadi saat request ke server
         */
        void onFailed(IOException e);
    }

    /**
     * Callback untuk menerima respons dari server
     */
    public interface notificationUpdateHelper {
        /**
         * Callback untuk menerima respons hasil dari request update value is_read atau
         * is_deleted ke server
         *
         * @param message pesan dari server
         */
        void onSuccess(String message);

        /**
         * Callback untuk menerima error dari request server
         *
         * @param e error dari request server
         */
        void onFailed(IOException e);
    }

    /**
     * Callback untuk menerima respons dari server
     */
    public interface DashboardDataHelper {
        /**
         * Callback untuk menerima hasil dari request ke server
         *
         * @param data data summary dashboard yang diterima dari server
         */
        void onSuccess(ArrayList<Dashboard> data);

        /**
         * Callback untuk menerima error dari request server
         *
         * @param e error dari request server
         */
        void onFailed(IOException e);
    }

    /**
     * Service Metode untuk mengirim permintaan registrasi account ke server
     *
     * @param endPoint  URL endpoint untuk registrasi
     * @param imagePart gambar yang akan dikirimkan
     * @param data      data yang akan dikirimkan (example. nama, email, dst..)
     * @param callback  callback untuk hasil request
     */
    public void Register(String endPoint, MultipartBody.Part imagePart, String data[],
                         RegisterAccountHelper callback) {

        MultipartBody.Builder builder =
                new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("name", data[0]);
        builder.addFormDataPart("tanggal_lahir", data[1]);
        builder.addFormDataPart("phone", data[2]);
        builder.addFormDataPart("alamat", data[3]);
        builder.addFormDataPart("email", data[4]);
        builder.addFormDataPart("password", data[5]);
        builder.addFormDataPart("jenis_kelamin", data[6]);
        builder.addFormDataPart("role", "pengguna");
        builder.addPart(imagePart);

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal Mengirim request " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(data);
                        String message = json.getString("message");
                        callback.onSuccess(message);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON" + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan code response: " + response.code()));
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

    /**
     * Service Metode untuk mengirim permintaan verifikasi kode OTP ke server.
     *
     * @param otp      kode OTP yang akan diverifikasi
     * @param token    token yang digunakan untuk verifikasi OTP. token di ambil dari response saat
     *                 request OTP
     * @param callback callback untuk hasil request verifikasi OTP
     */
    public void verifyOtp(String otp, String token, OtpVerificationHelper callback) {
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
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String responseData = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    if (response.isSuccessful()) {
                        String token = jsonResponse.getString("token");
                        callback.onSuccess(token);
                    } else if (response.code() == 400) {
                        callback.onFailed(new IOException("OTP tidak valid"));
                    } else if (response.code() == 401) {
                        callback.onFailed(new IOException("Token tidak valid atau kadaluarsa"));
                    } else {
                        callback.onFailed(new IOException("Gagal dengan code response: " + response.code()));
                    }
                } catch (JSONException e) {
                    callback.onFailed(new IOException("Error saat parsing JSON: " + e.getMessage()));
                }

            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan kirim ulang kode OTP ke server.
     *
     * @param token    Token yang diambil dari response saat request OTP
     * @param callback callback untuk hasil request kirim ulang OTP
     */
    public void resendOtp(String token, ResendOtpHelper callback) {
        String url = BASE_URL + "password-reset/resend";
        RequestBody body = RequestBody.create("", null);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(data);
                        String message = jsonResponse.getString("message");
                        callback.onSuccess(message);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Error parsing JSON"));
                    }
                } else if (response.code() == 401) {
                    callback.onFailed(new IOException("Token tidak valid atau kadaluarsa"));
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code:" + response.code()));
                }
            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan reset password ke server.
     *
     * @param token    Token yang diambil dari response saat Authentication Kode OTP, Sertakan di
     *                 header Authorization.
     * @param password Password baru.
     * @param callback callback untuk hasil request reset password
     */
    public void resetPassword(String token, String password, RequestResetPassword callback) {
        String url = BASE_URL + "password-reset/new";

        JSONObject json = new JSONObject();
        try {
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException(e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(data);
                        String message = json.getString("message");
                        callback.onSuccess(message);
                    } catch (JSONException e) {
                        callback.onSuccess("Gagal saat parsing Json: " + e.getMessage());
                    }
                } else if (response.code() == 401) {
                    callback.onFailed(new IOException("Token tidak valid atau kadaluarsa"));
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code: " + response.code()));
                }
            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan GET data institusi ke server
     *
     * @param endPoint endpoint untuk mengambil data institusi
     * @param token    token JWT, sertakan dalam header Authorization
     * @param callback callback untuk menerima hasil dari permintaan
     * @see InstituteHelper#onSuccess(String[])
     * @see InstituteHelper#onFailed(IOException)
     */
    public void fetchInstitusi(String endPoint, String token, InstituteHelper callback) {
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

                            callback.onSuccess(dataInstitute);
                        }
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(e);
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
                        Log.d(TAG, "onResponse: " + data);
                        ArrayList<Requirements> requirements = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray arrayRequirements = jsonObject.getJSONArray("requirements");
                        for (int i = 0; i < arrayRequirements.length(); i++) {
                            JSONObject objectRequirement = arrayRequirements.getJSONObject(i);
                            String requirement = objectRequirement.getString("requirement");
                            requirements.add(new Requirements(requirement));
                        }

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

    /**
     * Service Metode untuk mengirim permintaan GET data profile ke server
     *
     * @param token    token JWT, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil data profile
     * @param callback callback untuk menerima hasil dari permintaan
     * @see ProfileHelper#onSuccess(String, String, String, String, String, String, String)
     * @see ProfileHelper#onFailed(IOException)
     */
    public void FetchProfile(String token, String endPoint, ProfileHelper callback) {
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
                            String imageUri = BASE_URL + users.getString("profile_picture");
                            callback.onSuccess(name, gender, birth, phone, email, address, imageUri);
                        } else {
                            callback.onFailed(new IOException(JsonData.getString("message")));
                        }
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                }
            }
        });
    }

    /**
     * Service Metode untuk mengirim permintaan update profile ke server
     *
     * @param token    token JWT, sertakan dalam header Authorization
     * @param endPoint endpoint untuk memperbarui data profile
     * @param data     data yang akan dikirimkan ke server (example. ["nama", "tlp", "alamat"])
     * @param file     gambar yang akan dikirimkan ke server
     * @param callback callback untuk menerima hasil dari permintaan
     * @see updateProfileHelper#onSuccess(String)
     * @see updateProfileHelper#onFailed(IOException)
     */
    public void updateProfile(String token, String endPoint, String data[], File file,
                              updateProfileHelper callback) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("name", data[0]);
        builder.addFormDataPart("phone", data[1]);
        builder.addFormDataPart("address", data[2]);
        if (file != null) {
            RequestBody imageBody = RequestBody.create(file, MediaType.parse("image/*"));
            builder.addFormDataPart("profile_picture", file.getName(), imageBody);
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(data);
                        String message = json.getString("message");
                        callback.onSuccess(message);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Unexpected response code " + response.code()));
                }
            }
        });
    }

    /**
     * Mengirim permintaan untuk pendaftaran program ke server
     *
     * @param token     token JWT untuk otorisasi
     * @param endPoint  endpoint untuk pendaftaran program
     * @param nama      nama pengguna
     * @param programId kejuruan pengguna
     * @param program   program yang dipilih
     * @param uriKtp    Uri file KTP
     * @param uriKk     Uri file KK
     * @param uriIjazah Uri file Ijazah
     * @param callback  callback untuk menangani respons server
     */
    public void registerProgram(Context context, String token, String endPoint,
                                int userId, String nama,
                                String programId, String program, Uri uriKtp, Uri uriKk,
                                Uri uriIjazah,
                                RegistrationsProgramsHelper callback) {

        File fileKtp = FunctionHelper.getFileFromUri(context, uriKtp);
        File fileKk = FunctionHelper.getFileFromUri(context, uriKk);
        File fileIjazah = FunctionHelper.getFileFromUri(context, uriIjazah);

        if (fileKtp == null || fileKk == null || fileIjazah == null) {
            callback.onFailed(new IOException("Gagal membaca file. Pastikan file valid."));
            return;
        }

        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id_pengguna", String.valueOf(userId))
                .addFormDataPart("nama", nama)
                .addFormDataPart("id_program", programId)
                .addFormDataPart("nama_program", program)
                .addFormDataPart("ktp_pdf", fileKtp.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileKtp))
                .addFormDataPart("kk_pdf", fileKk.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileKk))
                .addFormDataPart("ijazah_pdf", fileIjazah.getName(), RequestBody.create(MediaType.parse("application/pdf"), fileIjazah))
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();
                try {
                    JSONObject json = new JSONObject(data);
                    if (response.isSuccessful()) {
                        try {
                            callback.onSuccess(json.getString("message"), json.getString("no_register"));
                        } catch (JSONException e) {
                            callback.onFailed(new IOException("Gagal parsing JSON: " + e.getMessage()));
                        }
                    } else if (response.code() == 400) {
                        callback.onFailed(new IOException(json.getString("message")));
                    } else {
                        callback.onFailed(new IOException("Response gagal: " + response.code()));
                    }
                } catch (JSONException e) {
                    callback.onFailed(new IOException("Gagal saat parsing json :" + e.getMessage()));
                }
            }
        });
    }

    /**
     * Mengirim permintaan untuk mengambil data notifikasi dari server menggunakan websocket.
     * kirim payload dalam bentuk JSON dengan format berikut:
     * {
     * "action": "fetch",
     * "userId": userId,
     * "token": token
     * }
     *
     * @param token    JWT untuk authentication, sertakan dalam request.
     * @param userId   ID pengguna yang akan menerima notifikasi
     * @param endPoint endpoint websocket
     * @param callback callback untuk menerima respons dari server
     */
    public void fetchNotification(String token, int userId, String endPoint, WebSocketCallback callback) {
        Request request = new Request.Builder()
                .url(endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                try {
                    JSONObject fetchRequest = new JSONObject();
                    fetchRequest.put("action", "fetch");
                    fetchRequest.put("userId", userId);
                    fetchRequest.put("token", token);
                    webSocket.send(fetchRequest.toString()); // Send request to server to fetch notifications
                } catch (Exception e) {
                    callback.onFailure(new IOException(e.getMessage()));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JSONObject json = new JSONObject(text);
                    List<Notification> notifications = new ArrayList<>();
                    if (json.getBoolean("success")) {
                        JSONArray notificationsArray = json.getJSONArray("notifications");
                        for (int i = 0; i < notificationsArray.length(); i++) {
                            JSONObject notificationObject = notificationsArray.getJSONObject(i);
                            String id = notificationObject.getString("id");
                            String pesan = notificationObject.getString("pesan");
                            int isRead = notificationObject.getInt("is_read");

                            notifications.add(new Notification(id, pesan, isRead));
                        }
                        callback.onMessageReceived(notifications);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "Closed: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                callback.onFailure(new IOException("Failed to send WebSocket request: " + t.getMessage()));
            }
        });
    }


    /**
     * Mengirim permintaan untuk menutup koneksi websocket
     */
    public void disconnectWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "User disconnected");
        }
    }

    /**
     * Mengirim permintaan untuk mengambil gambar profil dari server
     *
     * @param token    token JWT untuk otorisasi, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil gambar profil
     * @param callback callback untuk menerima respons dari server
     */
    public void fetchImageProfile(String token, String endPoint, imageProfile callback) {
        String url = BASE_URL + BASE_URL_PUBLIC + endPoint;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String pathUri = BASE_URL + jsonObject.getString("path");
                        callback.onSuccess(pathUri);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing json" + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(new IOException("Gagal mengirim request: " + e.getMessage()));
            }
        });

    }

    /**
     * Mengirim permintaan untuk mengubah status notifikasi ke sudah dibaca(is_read = 1)
     *
     * @param token    token JWT untuk otorisasi, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengubah status notifikasi
     * @param callback callback untuk menerima respons dari server
     */
    public void isReadNotification(String token, String endPoint,
                                   notificationUpdateHelper callback) {

        RequestBody body = RequestBody.create("", null);

        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal mengirim request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(data);
                        String message = json.getString("message");
                        callback.onSuccess(message);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code: " + response.code()));
                }
            }
        });
    }

    /**
     * Mengirim permintaan untuk mengubah status notifikasi ke sudah dihapus(is_deleted = 1)
     *
     * @param token    token JWT untuk otorisasi, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengubah status notifikasi
     * @param callback callback untuk menerima respons dari server
     */
    public void isDeletedNotification(String token, String endPoint,
                                      notificationUpdateHelper callback) {

        RequestBody body = RequestBody.create("", null);

        Request request = new Request.Builder()
                .url(BASE_URL + BASE_URL_PUBLIC + endPoint)
                .addHeader("Authorization", "Bearer " + token)
                .delete(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailed(new IOException("Gagal mengirim request: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(data);
                        String message = json.getString("message");
                        callback.onSuccess(message);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code: " + response.code()));
                }
            }
        });
    }

    /**
     * Mengirim permintaan untuk mengambil data summary dashboard dari server
     *
     * @param token    token JWT untuk otorisasi, sertakan dalam header Authorization
     * @param endPoint endpoint untuk mengambil data summary dashboard
     * @param callback callback untuk menerima respons dari server
     */
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
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        JSONObject jsonData = new JSONObject(data);
                        JSONArray dataArray = jsonData.getJSONArray("data");
                        ArrayList<Dashboard> dashboardList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            String tableName = dataObject.getString("table_name");
                            int total = dataObject.getInt("total");
                            dashboardList.add(new Dashboard(tableName, total));
                        }
                        callback.onSuccess(dashboardList);
                    } catch (JSONException e) {
                        callback.onFailed(new IOException("Gagal saat parsing JSON: " + e.getMessage()));
                    }
                } else {
                    callback.onFailed(new IOException("Gagal dengan status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailed(e);
            }
        });
    }
}