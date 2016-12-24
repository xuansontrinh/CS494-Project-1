package Views;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Common.Image;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ViewUploadedScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5949175171104934627L;
	private JPanel contentPane;
	private String _username;
	private JFrame _parent;
	private JPanel pane;
	private JLabel lblImagesUploaded;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// try {
		// ViewUploadedScreen frame = new ViewUploadedScreen();
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
	public ViewUploadedScreen(String username, JFrame parent) {
		setResizable(false);
		_username = username;
		_parent = parent;
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				_parent.setVisible(true);
			}
		});
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblImagesUploaded = new JLabel("Images uploaded");
		lblImagesUploaded.setBounds(10, 11, 288, 14);
		contentPane.add(lblImagesUploaded);

		pane = new JPanel();
		JScrollPane scrollPane = new JScrollPane(pane);
		pane.setLayout(new GridLayout(0, 4, 0, 0));
		scrollPane.setBounds(10, 36, 774, 524);
		contentPane.add(scrollPane);

		loadImages();
	}

	private void loadImages() {
		String hostName = "127.0.0.1";
		int portNumber = 9000;

		try (Socket server = new Socket(hostName, portNumber);
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				PrintWriter commandOut = new PrintWriter(server.getOutputStream(), true);) {

			commandOut.println("FetchImages");

			if (in.readBoolean()) {
				commandOut.println(_username);

//				GroupLayout groupLayout = new GroupLayout(pane);

//				contentPane.setLayout(groupLayout);
//				SequentialGroup horizontal = groupLayout.createSequentialGroup();
//				ParallelGroup vertical = groupLayout.createParallelGroup();

				String path = "Upload/" + _username;
				if (!Files.exists(Paths.get(path)))
					Files.createDirectories(Paths.get(path));
				try {
					
					int count = 0;
					while (in.readBoolean()) {
						Image img = (Image) in.readObject();
						FileOutputStream writer = new FileOutputStream(path + "/" + img.get_imageName());
						writer.write(img.get_imageData());
						writer.close();

						JLabel image = new JLabel("");
						image.setBounds(0, 0, 100, 100);
						image.setHorizontalAlignment(JLabel.CENTER);
						
						String fileName = "Upload/" + _username + "/" + img.get_imageName();						
						ImageIcon i = new ImageIcon(fileName); // create the image icon
						java.awt.Image tmp = i.getImage(); // get the image to transform
						double ratio = (double) image.getHeight() / tmp.getHeight(null);
						tmp = tmp.getScaledInstance((int) (tmp.getWidth(null) * ratio), image.getHeight(),
								java.awt.Image.SCALE_SMOOTH); // scale the image
						
						image.setIcon(new ImageIcon(tmp)); // set image icon
						
						pane.add(image);
//						horizontal.addComponent(image);
//						vertical.addComponent(image);

						out.writeBoolean(true);
						out.flush();
						
						++count;
					}

					lblImagesUploaded.setText("Images uploade by " + _username + ": " + count);
//					groupLayout.setHorizontalGroup(horizontal);
//					groupLayout.setVerticalGroup(vertical);
					
					
				} catch (Exception e) {
					System.err.println(e);
				}

			} else {
				System.out.println("Server not accepting fetch request.");
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}
