package info.sigmaclient.jelloprelauncher.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

public class JelloButton
extends JButton {
    private static final long serialVersionUID = 1L;
    float alpha = 0.5f;

    public JelloButton(String label) {
        super(label);
        this.addMouseListener(new ML());
    }

    @Override
    protected void paintComponent(Graphics g) {
        int height;
        Rectangle originalSize = super.getBounds();
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        boolean iStrokWidth = true;
        int width = (int)originalSize.getWidth();
        int iInnerHeight = height = (int)originalSize.getHeight();
        int iInnerWidth = width;
        int color = JelloButton.blendColor(-1, -16777216, 0.05f + this.getAlpha() * 0.2f);
        g2d.setColor(new Color(color));
        RoundRectangle2D.Double fill = new RoundRectangle2D.Double(0.0, 0.0, iInnerWidth, iInnerHeight, 14.0, 14.0);
        g2d.fill(fill);
        g2d.setColor(new Color(-1118482));
        g2d.drawString("Play", 83, 19);
        g2d.dispose();
    }

    public static int blendColor(int color1, int color2, float perc) {
        int alpha1 = color1 >> 24 & 0xFF;
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int alpha2 = color2 >> 24 & 0xFF;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        float iratio = 1.0f - perc;
        float alpha = (float)alpha1 * perc + (float)alpha2 * iratio;
        float red = (float)r1 * perc + (float)r2 * iratio;
        float green = (float)g1 * perc + (float)g2 * iratio;
        float blue = (float)b1 * perc + (float)b2 * iratio;
        return (int)alpha << 24 | ((int)red & 0xFF) << 16 | ((int)green & 0xFF) << 8 | (int)blue & 0xFF;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        this.repaint();
    }

    public class ML
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
}
