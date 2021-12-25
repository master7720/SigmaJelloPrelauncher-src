package info.sigmaclient.jelloprelauncher.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JelloButton$ML
extends MouseAdapter {
    @Override
    public void mouseExited(MouseEvent me) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                for (float i = 1.0f; i >= 0.5f; i -= 0.03f) {
                    JelloButton.this.setAlpha(i);
                    try {
                        Thread.sleep(10L);
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }).start();
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                for (float i = 0.5f; i <= 1.0f; i += 0.03f) {
                    JelloButton.this.setAlpha(i);
                    try {
                        Thread.sleep(10L);
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }).start();
    }

    @Override
    public void mousePressed(MouseEvent me) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                for (float i = 1.0f; i >= 0.6f; i -= 0.1f) {
                    JelloButton.this.setAlpha(i);
                    try {
                        Thread.sleep(1L);
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }).start();
    }
}
