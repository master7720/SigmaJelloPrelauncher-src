package info.sigmaclient.jelloprelauncher.gui;

class JelloButton$ML$2
implements Runnable {
    JelloButton$ML$2() {
    }

    @Override
    public void run() {
        for (float i = 0.5f; i <= 1.0f; i += 0.03f) {
            ML.this.this$0.setAlpha(i);
            try {
                Thread.sleep(10L);
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}
