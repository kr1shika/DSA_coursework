package q_6;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;

public class KriImageDownloader extends JFrame {
    private JTextField kriUrlTextField;
    private JButton kriDownloadButton, kriPauseButton, kriResumeButton, kriCancelButton;
    private JProgressBar kriProgressBar;
    private ExecutorService kriExecutorService;
    private boolean kriPauseDownload = false;
    private boolean kriCancelDownload = false;

    public KriImageDownloader() {
        setTitle("Image Downloader"); 

      
        kriInitComponents();
        kriExecutorService = Executors.newFixedThreadPool(5);
        getContentPane().setBackground(new Color(48, 48, 48));
    }

    private void kriInitComponents() {
        setTitle("Kri Image Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1)); // 3 rows: 1 for URL, 1 for buttons, 1 for progress bar

        // Row 1: URL label and input field
        JPanel kriUrlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kriUrlPanel.setBackground(new Color(48, 48, 48));
        
        JLabel urlLabel = new JLabel("URL:");
        urlLabel.setForeground(new Color(255, 255, 255));
        urlLabel.setFont(urlLabel.getFont().deriveFont(Font.BOLD));
        kriUrlPanel.add(urlLabel);
        
        kriUrlTextField = new JTextField(30);
        kriUrlPanel.add(kriUrlTextField);
        add(kriUrlPanel);

        // Row 2: Buttons (Download, Pause, Resume, Cancel)
        JPanel kriButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        kriButtonPanel.setBackground(new Color(48, 48, 48));
        kriDownloadButton = new JButton("Download");
        kriDownloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kriStartDownload(kriUrlTextField.getText());
            }
        });
        kriButtonPanel.add(kriDownloadButton);

        kriPauseButton = new JButton("Pause");
        kriPauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kriPauseDownload();
            }
        });
        kriButtonPanel.add(kriPauseButton);

        kriResumeButton = new JButton("Resume");
        kriResumeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kriResumeDownload();
            }
        });
        kriButtonPanel.add(kriResumeButton);

        kriCancelButton = new JButton("Cancel");
        kriCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kriCancelDownload();
            }
        });
        kriButtonPanel.add(kriCancelButton);

        add(kriButtonPanel);

        // Row 3: Progress bar
        kriProgressBar = new JProgressBar();
        kriProgressBar.setPreferredSize(new Dimension(300, 20));
        kriProgressBar.setBackground(new Color(255, 255, 255));
        add(kriProgressBar);

        setPreferredSize(new Dimension(400, 200));

        pack();
        setLocationRelativeTo(null);
    }

    private void kriStartDownload(String kriImageUrl) {
        if (!kriImageUrl.isEmpty()) {
            JFileChooser kriFileChooser = new JFileChooser();
            kriFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = kriFileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                final String kriFilePath = kriFileChooser.getSelectedFile().getPath(); // Declare kriFilePath as final
                String kriFileExtension = kriGetFileExtension(kriImageUrl); // Get the file extension from the URL

                StringBuilder kriFilePathBuilder = new StringBuilder(kriFilePath); // Use StringBuilder to construct the file path
                kriFilePathBuilder.append(kriFileExtension); // Append the file extension to the file path

                kriExecutorService.execute(new Runnable() {
                    public void run() {
                        try {
                            URL kriUrl = new URL(kriImageUrl);
                            try (BufferedInputStream kriIn = new BufferedInputStream(kriUrl.openStream());
                                 BufferedOutputStream kriOut = new BufferedOutputStream(new FileOutputStream(kriFilePathBuilder.toString()))) {
                                byte[] kriBuffer = new byte[1024];
                                int kriBytesRead;
                                long kriTotalBytesRead = 0;
                                long kriFileSize = kriUrl.openConnection().getContentLength();

                                while ((kriBytesRead = kriIn.read(kriBuffer, 0, 1024)) != -1) {
                                    if (kriCancelDownload) {
                                        SwingUtilities.invokeLater(() -> {
                                            JOptionPane.showMessageDialog(KriImageDownloader.this, "Download canceled.");
                                            kriProgressBar.setValue(0);
                                        });
                                        return;
                                    }

                                    if (kriPauseDownload) {
                                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(KriImageDownloader.this, "Download paused."));
                                        while (kriPauseDownload) {
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    kriOut.write(kriBuffer, 0, kriBytesRead);
                                    kriTotalBytesRead += kriBytesRead;

                                    int kriPercentage = (int) ((kriTotalBytesRead * 100) / kriFileSize);
                                    SwingUtilities.invokeLater(() -> kriProgressBar.setValue(kriPercentage));

                                    // Introduce a delay to slow down the download
                                    try {
                                        Thread.sleep(100); // Adjust this value to control the download speed
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(KriImageDownloader.this, "Image downloaded successfully.");
                                    kriProgressBar.setValue(0);
                                });
                            }
                        } catch (IOException e) {
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(KriImageDownloader.this, "Error downloading image: " + e.getMessage()));
                        }
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid URL.");
        }
    }

    private String kriGetFileExtension(String kriUrl) {
        String kriExtension = "";
        int kriLastDotIndex = kriUrl.lastIndexOf('.');
        if (kriLastDotIndex > 0) {
            kriExtension = kriUrl.substring(kriLastDotIndex);
        }
        return kriExtension;
    }

    private void kriPauseDownload() {
        kriPauseDownload = true;
    }

    private void kriResumeDownload() {
        kriPauseDownload = false;
    }

    private void kriCancelDownload() {
        kriCancelDownload = true;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new KriImageDownloader().setVisible(true));
    }
}

