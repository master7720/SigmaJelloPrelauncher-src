package info.sigmaclient.jelloprelauncher;

import info.sigmaclient.jelloprelauncher.gui.DownloadFrame;

class JelloPrelauncher$1
implements Runnable {
    JelloPrelauncher$1() {
    }

    @Override
    public void run() {
        JelloPrelauncher.this.df = new DownloadFrame();
    }
}
