package info.sigmaclient.jelloprelauncher.ressources.type;

public class Asset {
    private String name;
    private String hash;
    private int size;

    public Asset(String name, String hash, int size) {
        this.name = name;
        this.hash = hash;
        this.size = size;
    }

    public String getName() {
        return this.name;
    }

    public String getHash() {
        return this.hash;
    }

    public int getSize() {
        return this.size;
    }

    public String getUrl() {
        return "https://resources.download.minecraft.net/" + this.hash.substring(0, 2) + "/" + this.hash;
    }

    public String toString() {
        return this.name + " : " + this.hash + " : " + this.size;
    }
}
