package info.sigmaclient.jelloprelauncher.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class JelloProgressBar
extends BasicProgressBarUI {
    int width = 500;
    int height = 26;

    @Override
    protected Dimension getPreferredInnerVertical() {
        return new Dimension(this.height, this.width);
    }

    @Override
    protected Dimension getPreferredInnerHorizontal() {
        return new Dimension(this.width, this.height);
    }

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int iStrokWidth = 1;
        int width = this.progressBar.getWidth();
        int height = this.progressBar.getHeight();
        Rectangle2D.Double rect = new Rectangle2D.Double(0.0, 0.0, width, height);
        g2d.setColor(Color.BLACK);
        g2d.fill(rect);
        int iInnerHeight = height - iStrokWidth * 4;
        int iInnerWidth = width - iStrokWidth * 4;
        g2d.setColor(new Color(-11448236));
        RoundRectangle2D.Double fill = new RoundRectangle2D.Double(0.0, 0.0, iInnerWidth, iInnerHeight, iInnerHeight, iInnerHeight);
        g2d.fill(fill);
        g2d.setColor(Color.BLACK);
        RoundRectangle2D.Double fill1 = new RoundRectangle2D.Double(1.0, 1.0, iInnerWidth - 2, iInnerHeight - 2, iInnerHeight - 2, iInnerHeight - 2);
        g2d.fill(fill1);
        iInnerWidth = (int)Math.round((double)(iInnerWidth - 4) * this.getProgress());
        g2d.setColor(Color.WHITE);
        RoundRectangle2D.Double fill2 = new RoundRectangle2D.Double(2.0, 2.0, iInnerWidth, iInnerHeight - 4, iInnerHeight - 4, iInnerHeight - 4);
        g2d.fill(fill2);
        g2d.dispose();
    }

    private double getProgress() {
        double dProgress = this.progressBar.getPercentComplete();
        if (dProgress < 0.05) {
            return 0.05;
        }
        if (dProgress > 1.0) {
            return 1.0;
        }
        return dProgress;
    }

    @Override
    protected void paintIndeterminate(Graphics g, JComponent c) {
        super.paintIndeterminate(g, c);
    }
}
