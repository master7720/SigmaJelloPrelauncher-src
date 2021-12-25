package info.sigmaclient.jelloprelauncher;

import info.sigmaclient.jelloprelauncher.Utils;
import info.sigmaclient.jelloprelauncher.gui.DownloadFrame;
import info.sigmaclient.jelloprelauncher.ressources.RessourceManager;
import info.sigmaclient.jelloprelauncher.ressources.type.Library;
import info.sigmaclient.jelloprelauncher.versions.Version;
import info.sigmaclient.jelloprelauncher.versions.VersionManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.SwingUtilities;

public class JelloPrelauncher {
    public static JelloPrelauncher shared;
    DownloadFrame df;
    private static String[] launchArgs;
    private static File sigmaDir;
    private static File jreDir;
    private Version toLaunch;
    private VersionManager versionManager;

    public static void main(String[] args) {
        shared = new JelloPrelauncher(args);
    }

    public static RessourceManager getRessourceManager(Version version) {
        if (version.isOffline()) {
            return new RessourceManager(new File(version.getUrl()));
        }
        return new RessourceManager(version.getUrl());
    }

    public JelloPrelauncher(String[] args) {
        if (!sigmaDir.exists()) {
            sigmaDir.mkdirs();
        }
        System.out.println("Starting...");
        File file = new File(sigmaDir, "SigmaJello.jar");
        launchArgs = args;
        this.versionManager = new VersionManager("https://jelloprg.sigmaclient.info/version_manifest.json");
        this.setupWindow();
        Iterator<Map.Entry<String, Version>> iterator = this.versionManager.getVersions().entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, Version> v = iterator.next();
            this.toLaunch = v.getValue();
        }
        this.df.setVersions(this.versionManager.getVersions());
    }

    public void setupRuntime() {
        String platform = "";
        boolean macos = false;
        boolean windows = false;
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            platform = "mac";
            macos = true;
        } else if (osName.toLowerCase().contains("windows")) {
            platform = "windows";
            windows = true;
        } else {
            platform = "linux";
        }
        if (!jreDir.exists()) {
            try {
                File temporaryJreFile = File.createTempFile("sigma", "jre");
                Utils.downloadFileFromUrl("https://jelloprg.sigmaclient.info/download/" + platform + "/jre", temporaryJreFile, (totalDownloadedSize, totalFileSize) -> this.df.setProgress((int)(100L * totalDownloadedSize / totalFileSize), "Updating Runtime"));
                byte[] buffer = new byte[1024];
                ZipInputStream zis = new ZipInputStream(new FileInputStream(temporaryJreFile));
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    File newFile = new File(sigmaDir, zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        newFile.mkdirs();
                    } else {
                        int len;
                        FileOutputStream fos = new FileOutputStream(newFile);
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                    zipEntry = zis.getNextEntry();
                }
                zis.closeEntry();
                zis.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            for (File f : this.getFilesRecursive(jreDir)) {
                f.setExecutable(true);
            }
        }
    }

    private void setupWindow() {
        if (this.df == null) {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    JelloPrelauncher.this.df = new DownloadFrame();
                }
            });
        }
        while (this.df == null) {
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    private ArrayList<File> getFilesRecursive(File pFile) {
        ArrayList<File> f = new ArrayList<File>();
        for (File files : pFile.listFiles()) {
            if (files.isDirectory()) {
                f.addAll(this.getFilesRecursive(files));
                continue;
            }
            f.add(files);
        }
        return f;
    }

    public static void launchGame(RessourceManager manager, String jreBinLoc, boolean macos, boolean windows) {
        String separator = System.getProperty("file.separator");
        String mainClass = "net.minecraft.client.main.Main";
        String cpseparator = windows ? ";" : ":";
        StringBuilder classPathBuilder = new StringBuilder();
        classPathBuilder.append(manager.getClientPath().getAbsolutePath());
        for (Library lib : manager.getLibraries()) {
            classPathBuilder.append(cpseparator);
            classPathBuilder.append(lib.getFilePath().getAbsolutePath());
        }
        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        ArrayList<String> jvmArgs = new ArrayList<String>();
        jvmArgs.add(jreBinLoc);
        if (macos) {
            jvmArgs.add("-XstartOnFirstThread");
        }
        if (launchArgs == null || launchArgs.length == 0) {
            String assets = new File(Utils.getWorkingDirectory(), "assets").getAbsolutePath();
            launchArgs = new String[]{"--version", "mcp", "--accessToken", "0", "--assetsDir", assets, "--assetIndex", manager.getClient().getAssetsVersion(), "--userProperties", "{}"};
        } else {
            jvmArgs.addAll(inputArguments);
        }
        jvmArgs.add("-cp");
        jvmArgs.add(classPathBuilder.toString());
        jvmArgs.add(mainClass);
        jvmArgs.addAll(Arrays.asList(launchArgs));
        StringBuilder sb = new StringBuilder("Launching game");
        for (String arg : jvmArgs) {
            sb.append(" ").append(arg);
        }
        System.out.println(sb.toString());
        try {
            String line;
            ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        this.df.setProgress(0, "Launching Client");
        new Thread(() -> {
            String jreBinLoc;
            this.setupRuntime();
            RessourceManager ressourceManager = JelloPrelauncher.getRessourceManager(this.toLaunch);
            if (!this.toLaunch.isOffline()) {
                try {
                    ressourceManager.download((current, total) -> this.df.setProgress((int)(100L * current / total), "Updating Client"));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.df.setVisible(false);
            boolean macos = false;
            boolean windows = false;
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
                jreBinLoc = jreDir.getAbsolutePath() + File.separator + "Contents" + File.separator + "Home" + File.separator + "bin" + File.separator + "java";
                macos = true;
            } else if (osName.toLowerCase().contains("windows")) {
                jreBinLoc = jreDir.getAbsolutePath() + File.separator + "bin" + File.separator + "java.exe";
                windows = true;
            } else {
                jreBinLoc = jreDir.getAbsolutePath() + File.separator + "bin" + File.separator + "java";
            }
            JelloPrelauncher.launchGame(ressourceManager, jreBinLoc, macos, windows);
            System.exit(0);
        }).start();
    }

    public void setVersion(String s) {
        for (Map.Entry<String, Version> entry : this.versionManager.getVersions().entrySet()) {
            if (!s.equals(entry.getValue().getDisplayName())) continue;
            this.toLaunch = entry.getValue();
        }
    }

    static {
        sigmaDir = Utils.getSigmaDirectory();
        jreDir = new File(sigmaDir, "jre1.8.0_202");
    }
}
