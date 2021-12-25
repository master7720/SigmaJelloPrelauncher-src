package info.sigmaclient.jelloprelauncher.gui;

class JelloButton$ML$3
implements Runnable {
    JelloButton$ML$3() {
    }

    @Override
    public void run() {
        for (float i = 1.0f; i >= 0.6f; i -= 0.1f) {
            ML.this.this$0.setAlpha(i);
            try {
                Thread.sleep(1L);
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}
