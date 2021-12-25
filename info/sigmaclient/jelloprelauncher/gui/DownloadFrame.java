package info.sigmaclient.jelloprelauncher.gui;

import info.sigmaclient.jelloprelauncher.JelloPrelauncher;
import info.sigmaclient.jelloprelauncher.gui.ImagePanel;
import info.sigmaclient.jelloprelauncher.gui.JelloButton;
import info.sigmaclient.jelloprelauncher.gui.JelloProgressBar;
import info.sigmaclient.jelloprelauncher.versions.Version;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class DownloadFrame
extends JFrame
implements ActionListener {
    private JProgressBar jProgressBar = new JProgressBar();
    private ImagePanel image = new ImagePanel();
    private JPanel pane = new JPanel();
    private JelloButton play = new JelloButton("Play!");
    private JLabel label = new JLabel();
    private JComboBox<String> comboBox;
    Thread autoPlay;

    public DownloadFrame() throws HeadlessException {
        this.setTitle("Sigma Jello Bootstrap");
        this.setResizable(false);
        this.setSize(580, 150);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.BLACK);
        this.jProgressBar.setUI(new JelloProgressBar());
        this.jProgressBar.setBounds(26, 80, this.getWidth() - 50, 25);
        this.jProgressBar.setBorderPainted(false);
        this.jProgressBar.setBorder(null);
        this.jProgressBar.setVisible(false);
        try {
            this.image.setImage(ImageIO.read(this.getClass().getClassLoader().getResource("logo.png")));
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.image.setBounds(26, 28, 221, 35);
        this.label.setText("Auto Play in 10s..");
        this.label.setBounds(this.getWidth() - 228, 44, 200, 20);
        this.label.setHorizontalAlignment(4);
        this.label.setForeground(Color.WHITE);
        this.autoPlay = new Thread(() -> {
            for (int i = 0; i < 10; ++i) {
                this.label.setText("Auto Play in " + (10 - i) + "s..");
                if (Thread.interrupted()) {
                    return;
                }
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e) {
                    return;
                }
                if (!Thread.interrupted()) continue;
                return;
            }
            SwingUtilities.invokeLater(() -> JelloPrelauncher.shared.play());
        });
        this.play.setBounds(360, 75, 195, 30);
        this.play.setVisible(true);
        this.play.setText("Play!");
        this.play.addActionListener(action -> SwingUtilities.invokeLater(() -> {
            this.autoPlay.interrupt();
            JelloPrelauncher.shared.play();
        }));
        this.comboBox = new JComboBox();
        this.comboBox.setBounds(26, 80, 195, 22);
        this.comboBox.addActionListener(this);
        this.play.setEnabled(false);
        this.pane.add(this.label);
        this.pane.add(this.image);
        this.pane.add(this.jProgressBar);
        this.pane.add(this.play);
        this.pane.add(this.comboBox);
        this.pane.setBackground(Color.BLACK);
        this.pane.setLayout(null);
        this.add(this.pane);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = (String)this.comboBox.getSelectedItem();
        if (JelloPrelauncher.shared != null) {
            JelloPrelauncher.shared.setVersion(s);
        }
        if (this.autoPlay != null) {
            this.autoPlay.interrupt();
            this.label.setText("Select version and play!");
        }
    }

    public void setVersions(HashMap<String, Version> hashMap) {
        this.play.setEnabled(true);
        for (Map.Entry<String, Version> entry : hashMap.entrySet()) {
            this.comboBox.addItem(entry.getValue().getDisplayName());
        }
        this.autoPlay.start();
    }

    public void setInvisible() {
        this.setVisible(false);
    }

    public void setProgress(int progress, String name) {
        this.play.setVisible(false);
        this.comboBox.setVisible(false);
        this.jProgressBar.setVisible(true);
        this.jProgressBar.setValue(Math.min(100, progress));
        this.label.setText(name + " " + progress + "%");
        this.repaint();
    }
}
