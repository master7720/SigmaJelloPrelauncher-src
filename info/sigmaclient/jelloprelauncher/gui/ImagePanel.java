package info.sigmaclient.jelloprelauncher.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import javax.swing.JPanel;

public class ImagePanel
extends JPanel
implements Serializable {
    Image image = null;

    public ImagePanel(Image image) {
        this.image = image;
    }

    public ImagePanel() {
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage(Image image) {
        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (this.image != null) {
            int height = this.getSize().height;
            int width = this.getSize().width;
            g.drawImage(this.image, 0, 0, width, height, this);
        }
    }
}
