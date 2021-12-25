package info.sigmaclient.jelloprelauncher;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import info.sigmaclient.jelloprelauncher.DownloadProgress;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    private static char[] hexArray = "0123456789abcdef".toCharArray();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String queryUrl(String url) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(url).openConnection();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));){
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String string = sb.toString();
                return string;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject queryJson(String url) {
        String query = Utils.queryUrl(url);
        return query == null ? null : Json.parse(query.replace("\"size\": ,", "")).asObject();
    }

    public static String getFileSha1Sum(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            try (FileInputStream fis = new FileInputStream(file);){
                int n = 0;
                byte[] buffer = new byte[4096];
                while (n != -1) {
                    n = ((InputStream)fis).read(buffer);
                    if (n <= 0) continue;
                    digest.update(buffer, 0, n);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return Utils.bytesToHex(digest.digest());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0xF];
        }
        return new String(hexChars);
    }

    public static String getPlatformName() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            return "osx";
        }
        if (osName.toLowerCase().contains("windows")) {
            return "windows";
        }
        return "linux";
    }

    public static void downloadFileFromUrl(String url, File file, DownloadProgress progress) {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            int i = 0;
            long totalDownloadedSize = 0L;
            HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
            long totalFileSize = con.getContentLengthLong();
            try (InputStream is = con.getInputStream();
                 FileOutputStream fos = new FileOutputStream(file);){
                byte[] buff = new byte[8192];
                int readedLen = 0;
                while ((readedLen = is.read(buff)) > -1) {
                    fos.write(buff, 0, readedLen);
                    totalDownloadedSize += (long)readedLen;
                    if (i > 50) {
                        if (progress != null) {
                            progress.update(totalDownloadedSize, totalFileSize);
                        }
                        i = 0;
                    }
                    ++i;
                }
                if (progress != null) {
                    progress.update(totalFileSize, totalFileSize);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getWorkingDirectory() {
        File workingDirectory;
        String userHome = System.getProperty("user.home", ".");
        switch (Utils.getPlatformName()) {
            case "linux": {
                workingDirectory = new File(userHome, ".minecraft/");
                break;
            }
            case "windows": {
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, ".minecraft/");
                break;
            }
            case "osx": {
                workingDirectory = new File(userHome, "Library/Application Support/minecraft");
                break;
            }
            default: {
                workingDirectory = new File(userHome, "minecraft/");
            }
        }
        return workingDirectory;
    }

    public static File getSigmaDirectory() {
        return new File(Utils.getWorkingDirectory(), "Sigma");
    }
}
