package info.sigmaclient.jelloprelauncher.ressources;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import info.sigmaclient.jelloprelauncher.DownloadProgress;
import info.sigmaclient.jelloprelauncher.Utils;
import info.sigmaclient.jelloprelauncher.ressources.type.Asset;
import info.sigmaclient.jelloprelauncher.ressources.type.Client;
import info.sigmaclient.jelloprelauncher.ressources.type.Library;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RessourceManager {
    ArrayList<Library> libraries = new ArrayList();
    ArrayList<Asset> assets = new ArrayList();
    JsonObject versionJson;
    Client client;
    JsonObject assetsJson;

    public RessourceManager(File jsonFile) {
        try {
            Scanner scanner = new Scanner(jsonFile);
            String text = scanner.useDelimiter("\\A").next();
            scanner.close();
            this.versionJson = Json.parse(text).asObject();
            this.parseLibraries(this.versionJson);
            String versionName = this.versionJson.getString("id", null);
            String versionAssets = this.versionJson.getString("assets", null);
            this.client = new Client(null, null, 0, versionName, versionAssets);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public RessourceManager(String jsonUrl) {
        this.versionJson = Utils.queryJson(jsonUrl);
        String versionName = this.versionJson.getString("id", null);
        String versionAssets = this.versionJson.getString("assets", null);
        JsonObject assetsIndexJson = this.versionJson.get("assetIndex").asObject();
        String assetsUrl = assetsIndexJson.getString("url", null);
        String assetsIndex = assetsIndexJson.getString("id", null);
        this.assetsJson = Utils.queryJson(assetsUrl);
        JsonObject downloadsJson = this.versionJson.get("downloads").asObject();
        JsonObject clientObjectJson = downloadsJson.get("client").asObject();
        String clientSha1 = clientObjectJson.getString("sha1", null);
        String clientUrl = clientObjectJson.getString("url", null);
        int clientSize = clientObjectJson.getInt("size", 0);
        this.client = new Client(clientUrl, clientSha1, clientSize, versionName, versionAssets);
        this.assets = new ArrayList();
        JsonObject assetsJson2 = this.assetsJson.get("objects").asObject();
        for (String name : assetsJson2.names()) {
            JsonObject assetJson = assetsJson2.get(name).asObject();
            this.assets.add(new Asset(name, assetJson.getString("hash", null), assetJson.getInt("size", 0)));
        }
        this.parseLibraries(this.versionJson);
    }

    public void parseLibraries(JsonObject versionJson) {
        String osName = Utils.getPlatformName();
        String arch = System.getProperty("sun.arch.data.model");
        JsonArray librariesArrayJson = versionJson.get("libraries").asArray();
        for (JsonValue val : librariesArrayJson.values()) {
            JsonObject downloadsJson;
            JsonObject libraryJson = val.asObject();
            if (libraryJson.get("rules") != null) {
                boolean allowed = false;
                for (JsonValue ruleVal : libraryJson.get("rules").asArray().values()) {
                    JsonObject rule = ruleVal.asObject();
                    if (rule.get("os") != null && !osName.equals(rule.get("os").asObject().getString("name", null))) continue;
                    allowed = "allow".equals(rule.getString("action", null));
                }
                if (!allowed) continue;
            }
            if ((downloadsJson = libraryJson.get("downloads").asObject().get("artifact").asObject()).getString("path", null).contains("realms")) continue;
            this.libraries.add(new Library(downloadsJson.getString("path", null), downloadsJson.getString("sha1", null), downloadsJson.getInt("size", 0), downloadsJson.getString("url", null)));
            if (libraryJson.get("natives") == null) continue;
            String nativeName = libraryJson.get("natives").asObject().getString(osName, null);
            if (nativeName == null) {
                nativeName = libraryJson.get("natives").asObject().getString(osName + arch, null);
            }
            if (nativeName == null) continue;
            JsonObject classifierJson = libraryJson.get("downloads").asObject().get("classifiers").asObject();
            JsonObject nativeJson = classifierJson.get(nativeName).asObject();
            String path = nativeJson.getString("path", null);
            if (nativeJson.getString("url", null).endsWith(".zip")) {
                path = path + "natives.zip";
            }
            this.libraries.add(new Library(path, nativeJson.getString("sha1", null), nativeJson.getInt("size", 0), nativeJson.getString("url", null)));
        }
    }

    public boolean download(DownloadProgress progress) throws FileNotFoundException, IOException {
        File destination;
        int sizeToDownload = 0;
        for (Asset asset : this.assets) {
            sizeToDownload += asset.getSize();
        }
        for (Library library : this.libraries) {
            sizeToDownload += library.getSize();
        }
        int sizeToDownloadFinal = sizeToDownload += this.client.getSize();
        System.out.println("Size to download " + sizeToDownload + "B");
        int downloadedSize = 0;
        for (Asset asset : this.assets) {
            destination = new File(Utils.getWorkingDirectory(), "assets" + File.separator + "objects" + File.separator + asset.getHash().substring(0, 2) + File.separator + asset.getHash());
            if (destination.exists()) {
                System.out.println(destination);
                downloadedSize += asset.getSize();
                continue;
            }
            Utils.downloadFileFromUrl("https://resources.download.minecraft.net/" + asset.getHash().substring(0, 2) + "/" + asset.getHash(), destination, null);
            progress.update(downloadedSize += asset.getSize(), sizeToDownloadFinal);
        }
        for (Library library : this.libraries) {
            destination = new File(Utils.getWorkingDirectory(), "libraries" + File.separator + library.getPath().replaceAll("//", File.separator));
            if (destination.exists() && Utils.getFileSha1Sum(destination).equals(library.getSha1())) {
                downloadedSize += library.getSize();
                continue;
            }
            Utils.downloadFileFromUrl(library.getUrl(), destination, null);
            progress.update(downloadedSize += library.getSize(), sizeToDownloadFinal);
            if (!library.getUrl().endsWith(".zip")) continue;
            byte[] buff = new byte[4096];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(destination));
            Throwable throwable = null;
            try {
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    File dest;
                    if (ze.isDirectory()) {
                        dest = new File(destination.getParent(), ze.getName() + File.separator);
                        dest.mkdirs();
                        continue;
                    }
                    dest = new File(destination.getParent(), ze.getName());
                    try (FileOutputStream fos = new FileOutputStream(dest);){
                        int readed;
                        while ((readed = zis.read(buff)) > 0) {
                            fos.write(buff, 0, readed);
                        }
                    }
                    zis.closeEntry();
                }
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            finally {
                if (zis == null) continue;
                if (throwable != null) {
                    try {
                        zis.close();
                    }
                    catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                    continue;
                }
                zis.close();
            }
        }
        int downloadedSizePre = downloadedSize;
        if (!this.getClientPath().exists() || !Utils.getFileSha1Sum(this.getClientPath()).equals(this.client.getHash())) {
            this.getClientPath().delete();
            Utils.downloadFileFromUrl(this.client.getUrl(), this.getClientPath(), (size, totalsize) -> progress.update((long)downloadedSizePre + size, sizeToDownloadFinal));
        }
        if (!this.getClientJsonPath().getParentFile().exists()) {
            this.getClientJsonPath().getParentFile().mkdirs();
        }
        if (!this.getAssetsJsonPath().getParentFile().exists()) {
            this.getAssetsJsonPath().getParentFile().mkdirs();
        }
        Files.write(this.getClientJsonPath().toPath(), this.versionJson.toString().getBytes(), new OpenOption[0]);
        Files.write(this.getAssetsJsonPath().toPath(), this.assetsJson.toString().getBytes(), new OpenOption[0]);
        downloadedSize += this.client.getSize();
        return true;
    }

    public File getClientPath() {
        return new File(this.client.getClientWorkingDir(), this.client.getName() + ".jar");
    }

    public File getClientJsonPath() {
        return new File(this.client.getClientWorkingDir(), this.client.getName() + ".json");
    }

    public File getAssetsJsonPath() {
        return new File(Utils.getWorkingDirectory(), "assets" + File.separator + "indexes" + File.separator + this.client.getAssetsVersion() + ".json");
    }

    public ArrayList<Asset> getAssets() {
        return this.assets;
    }

    public ArrayList<Library> getLibraries() {
        return this.libraries;
    }

    public Client getClient() {
        return this.client;
    }
}
