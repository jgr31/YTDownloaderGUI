package gelabert.ytdownloadergui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Modal JDialog for Help > About.
 * REQUIRED: Must be modal, include author's name, course info, and citations.
 */
public class AboutDialog extends JDialog {

    public AboutDialog(JFrame owner) {
        super(owner, "About - YTDownloaderGUI", true); // modal
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setWrapStyleWord(true);
        ta.setLineWrap(true);
        ta.setText(
            "YTDownloaderGUI (prototype)\n" +
            "Author: Wi Kwi (Jordi Gelabert Roselló)\n" +
            "Course: DI01 - 2025\n\n" +
            "This GUI is a thin wrapper around yt-dlp.\n\n" +
            "Credits / Resources:\n" +
            "- yt-dlp (https://github.com/yt-dlp/yt-dlp)\n" +
            "- ffmpeg / ffprobe (https://ffmpeg.org/)\n" +
            "- Java Swing / NetBeans GUI Builder\n" +
            "- Process management via ProcessBuilder\n" +
            "- MIT Integrity guidelines on citing code (http://integrity.mit.edu/handbook/writing-code)\n\n" +
            "Notes:\n" +
            "- Downloads are executed in a background SwingWorker.\n" +
            "- When extracting audio, we pass --no-keep-video to avoid intermediate files.\n" +
            "- Optionally write a simple .m3u for playlists or single downloads.\n"
        );
        add(new JScrollPane(ta), BorderLayout.CENTER);
        JButton ok = new JButton(new AbstractAction("Close") {
            @Override public void actionPerformed(ActionEvent e) { dispose(); }
        });
        JPanel south = new JPanel();
        south.add(ok);
        add(south, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(520, 360));
        pack();
        setLocationRelativeTo(owner);
    }
}