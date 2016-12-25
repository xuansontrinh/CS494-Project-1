package Views;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class UploadScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4290665729846649452L;
	private JPanel contentPane;
	private JTextField filePathField;
	private File _selectedFile;

	private String _username;
	private JFrame _parent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// try {
		// UploadScreen frame = new UploadScreen("");
		// frame.setVisible(true);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// });
	}

	/**
	 * Create the frame.
	 */
	public UploadScreen(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut, String username,
			JFrame parent) {
		_parent = parent;
		_username = username;

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				_parent.setVisible(true);
			}
		});

		setResizable(false);
		setTitle("Upload");
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Image path");
		lblNewLabel_1.setBounds(10, 11, 73, 23);
		contentPane.add(lblNewLabel_1);

		filePathField = new JTextField();
		filePathField.setEditable(false);
		filePathField.setBounds(93, 11, 647, 23);
		contentPane.add(filePathField);
		filePathField.setColumns(10);

		JLabel image = new JLabel("");
		image.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		image.setBounds(10, 42, 570, 471);
		image.setHorizontalAlignment(JLabel.CENTER);
		contentPane.add(image);

		JButton openFileBtn = new JButton("");
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG/PNG Images", "jpg", "png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					_selectedFile = chooser.getSelectedFile();
					String fileName = chooser.getSelectedFile().getAbsolutePath();
					filePathField.setText(fileName);
					ImageIcon i = new ImageIcon(fileName); // create the image
															// icon
					Image tmp = i.getImage(); // get the image to transform
					double ratio = (double) image.getHeight() / tmp.getHeight(null);
					tmp = tmp.getScaledInstance((int) (tmp.getWidth(null) * ratio), image.getHeight(),
							Image.SCALE_SMOOTH); // scale the image
					image.setIcon(new ImageIcon(tmp)); // set image icon
					System.out.println("You chose to open this file: " + fileName);
				}
			}
		});
		openFileBtn.setIcon(
				new ImageIcon(UploadScreen.class.getResource("/com/sun/java/swing/plaf/windows/icons/Directory.gif")));
		openFileBtn.setBounds(750, 11, 34, 23);
		contentPane.add(openFileBtn);

		JLabel lblNewLabel = new JLabel("Themes");
		lblNewLabel.setBounds(590, 45, 46, 14);
		contentPane.add(lblNewLabel);

		JTextArea themesTxtArea = new JTextArea();
		themesTxtArea.setToolTipText("Line-separated list of themes");
		themesTxtArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		themesTxtArea.setBounds(590, 67, 194, 205);
		contentPane.add(themesTxtArea);

		JLabel lblNote = new JLabel("Note");
		lblNote.setBounds(590, 286, 46, 14);
		contentPane.add(lblNote);

		JTextArea note = new JTextArea();
		note.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		note.setBounds(590, 308, 194, 205);
		contentPane.add(note);

		JButton btnNewButton = new JButton("Upload");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadImage(in, out, commandOut, themesTxtArea.getText().split("\n"), note.getText());
			}
		});
		btnNewButton.setIconTextGap(8);
		btnNewButton.setIcon(new ImageIcon(
				UploadScreen.class.getResource("/com/sun/javafx/scene/control/skin/caspian/images/capslock-icon.png")));
		btnNewButton.setBounds(10, 524, 774, 36);
		contentPane.add(btnNewButton);
	}

	public void uploadImage(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut, String[] themes,
			String note) {
		
		if (_selectedFile == null)
		{
			JOptionPane pane = new JOptionPane();
			pane.setMessage("Please choose an image.");
			JDialog dialog = pane.createDialog(null, "Alert");
			dialog.setVisible(true);
			return;
		}
		
		try {
			File file = _selectedFile.getAbsoluteFile();
			byte[] fileContent;
			fileContent = Files.readAllBytes(file.toPath());

			BufferedImage bimg = ImageIO.read(file);

			// Create serializable image
			Common.Image image = new Common.Image(_selectedFile.getName(), themes, _username, note, bimg.getWidth(),
					bimg.getHeight(), fileContent);

			commandOut.println("UploadImage");

			if (in.readBoolean()) {
				// writeUnshared() is like writeObject(), but always writes
				// a new copy of the object. The flush (optional) forces the
				// bytes out right now.
				out.writeUnshared(image);
				out.flush();

				int response = in.readInt();

				if (response == 1) {
					System.out.println("Successfully uploaded image.");
					JOptionPane pane = new JOptionPane();
					pane.setMessage("Image uploaded successfully.");
					JDialog dialog = pane.createDialog(null, "Alert");
					dialog.setVisible(true);

					String path = "Upload/";
					if (!Files.exists(Paths.get(path)))
						Files.createDirectories(Paths.get(path));
					try (BufferedWriter bw = Files.newBufferedWriter(
							FileSystems.getDefault().getPath("Upload/" + _username + ".txt"), CREATE, APPEND)) {
						bw.write(image.toString());
					} catch (IOException x) {
						System.err.format("IOException: %s%n", x);
					}

				} else {
					System.out.println("Failed to upload image.");
					JOptionPane pane = new JOptionPane();
					pane.setMessage("Failed to upload image.");
					JDialog dialog = pane.createDialog(null, "Alert");
					dialog.setVisible(true);
				}

			} else {
				System.out.println("Server not image upload sign up request.");
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
