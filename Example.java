import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.File;

/**
 * A simple example showing how to use {@link FileDrop}
 *
 * @author Robert Harder, rob@iharder.net
 */
public class Example {
    private static final String FILE_EXTENSION = "zip";

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("Drop zip file to extract output mipmap");
        final javax.swing.JTextArea text = new javax.swing.JTextArea();
        frame.getContentPane().add(
                new javax.swing.JScrollPane(text),
                java.awt.BorderLayout.CENTER);

        new FileDrop(System.out, text, /*dragBorder,*/ new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                for (File file : files) {
                    try {
                        if (isExtensionAccepted(file.getAbsolutePath())) {
                            // Extract files
                            unzip(file.getAbsolutePath(), text);
                            System.out.println("DONE ----------------------------------------------");
                            JOptionPane.showMessageDialog(null, "Done unzip and rename to mipmap");
                        } else {
                            JOptionPane.showMessageDialog(null, "Not a zip file");
                        }
                    } catch (Exception e) {
                        text.append(e.getMessage() + "\n");
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Not a zip for drawable resource file or unknown error, see detail in the panel");
                    }
                }
            }
        });

        frame.setBounds(300, 300, 500, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static boolean isExtensionAccepted(String filePath) {
        return FilenameUtils.getExtension(filePath).equals(FILE_EXTENSION);
    }

    private static void unzip(String path, JTextArea text) throws Exception {
        String destination = new File(path).getParent();
        ZipFile zipFile = new ZipFile(path);
        zipFile.extractAll(destination);

        String pathToRes = destination + File.separator + "res";
        File[] files = new File(pathToRes).listFiles();
        for (File item : files) {
            String[] split = item.getName().split("-");
            String prefix = split[0];
            String suffix = split[1];
            if (prefix.equals("drawable")) {
                // Rename to mipmap
                File newItem = new File(item.getAbsoluteFile().getParent() + File.separator + "mipmap-" + suffix);
                boolean success = item.renameTo(newItem);
                if (success) {
                    String message = "Rename " + item.getName() + " to " + newItem.getName() + " successfully";
                    System.out.println(message);
                    text.append(message + "\n");
                }
            }
        }
    }


}
