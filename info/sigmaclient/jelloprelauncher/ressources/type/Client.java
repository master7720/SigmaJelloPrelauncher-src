package info.sigmaclient.jelloprelauncher.ressources.type;

import info.sigmaclient.jelloprelauncher.versions.VersionManager;
import java.io.File;

public class Client {
    private String url;
    private String hash;
    private int size;
    private String id;
    private String assetsVersion;

    public Client(String url, String hash, int size, String id, String assetsVersion) {
        this.url = url;
        this.hash = hash;
        this.size = size;
        this.id = id;
        this.assetsVersion = assetsVersion;
    }

    public String getHash() {
        return this.hash;
    }

    public int getSize() {
        return this.size;
    }

    public String getName() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String toString() {
        return this.url + " : " + this.hash + " : " + this.size;
    }

    public String getAssetsVersion() {
        return this.assetsVersion;
    }

    public File getClientWorkingDir() {
        return new File(VersionManager.getVersionFolder(), this.id);
    }
}
