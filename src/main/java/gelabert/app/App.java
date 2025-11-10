package gelabert.app;

import gelabert.app.ui.MainFrame;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
