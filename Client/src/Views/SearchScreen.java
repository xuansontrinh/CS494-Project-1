package Views;

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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Common.Image;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SearchScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8212101664340554765L;
	private JPanel contentPane;
	private int _type;
	private JFrame _parent;
	private JPanel pane;
	private JLabel lblQuery;
	private JTextField searchBox;
	private JButton btnSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SearchScreen frame = new SearchScreen();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	/**
	 * Create the frame.
	 */
	public SearchScreen(int type, JFrame parent) {
		setResizable(false);
		_type = type;
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

		lblQuery = new JLabel("Query");
		lblQuery.setBounds(10, 11, 51, 14);
		contentPane.add(lblQuery);
		
		searchBox = new JTextField();
		searchBox.setBounds(71, 8, 614, 20);
		contentPane.add(searchBox);
		searchBox.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSearch.setEnabled(false);
				loadImages(searchBox.getText());
			}
		});
		btnSearch.setBounds(695, 7, 89, 23);
		contentPane.add(btnSearch);
		
		pane = new JPanel();
		JScrollPane scrollPane = new JScrollPane(pane);
		pane.setLayout(new GridLayout(0, 4, 0, 0));
		scrollPane.setBounds(10, 36, 774, 524);
		contentPane.add(scrollPane);
	}
	
	private void loadImages(String query) {
		String hostName = "127.0.0.1";
		int portNumber = 9000;

		try (Socket server = new Socket(hostName, portNumber);
				ObjectInputStream in = new ObjectInputStream(server.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
				PrintWriter commandOut = new PrintWriter(server.getOutputStream(), true);) {

			if (_type == 0)
				commandOut.println("FetchImages");
			else if (_type == 1)
				commandOut.println("Search");

			if (in.readBoolean()) {
				commandOut.println(query);

				String path = "Query/" + query;
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
						
						String fileName = "Query/" + query + "/" + img.get_imageName();
						ImageIcon i = new ImageIcon(fileName); // create the image icon
						java.awt.Image tmp = i.getImage(); // get the image to transform
						double ratio = (double) image.getHeight() / tmp.getHeight(null);
						tmp = tmp.getScaledInstance((int) (tmp.getWidth(null) * ratio), image.getHeight(),
								java.awt.Image.SCALE_SMOOTH); // scale the image
						
						image.setIcon(new ImageIcon(tmp)); // set image icon
						
						pane.add(image);

						out.writeBoolean(true);
						out.flush();
						
						++count;
					}
					
					JOptionPane message = new JOptionPane();
					message.setMessage(count + " images found.");
					JDialog dialog = message.createDialog(null, "Alert");
					dialog.setVisible(true);
					
					pane.validate();
					btnSearch.setEnabled(true);
					
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
