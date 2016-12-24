package Views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HomeScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -818207248292691849L;
	private JPanel contentPane;
	private String _username;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					HomeScreen frame = new HomeScreen();
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
	public HomeScreen(String username) {
		setResizable(false);
		_username = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnUploadImage = new JButton("Upload Image");
		JFrame that = this;
		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UploadScreen us = new UploadScreen(_username, that);
				us.setVisible(true);
				setVisible(false);
			}
		});
		btnUploadImage.setBounds(120, 61, 203, 23);
		contentPane.add(btnUploadImage);
		
		JButton btnViewUploadedImages = new JButton("View Uploaded Images");
		btnViewUploadedImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewUploadedScreen vus = new ViewUploadedScreen(_username, that);
				vus.setVisible(true);
				setVisible(false);
			}
		});
		btnViewUploadedImages.setBounds(120, 95, 203, 23);
		contentPane.add(btnViewUploadedImages);
		
		JButton btnNewButton = new JButton("Search Images by Theme");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchScreen ss = new SearchScreen(1, that);
				ss.setVisible(true);
				setVisible(false);
			}
		});
		btnNewButton.setBounds(120, 129, 203, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Search Images by User");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchScreen ss = new SearchScreen(0, that);
				ss.setVisible(true);
				setVisible(false);
			}
		});
		btnNewButton_1.setBounds(120, 163, 203, 23);
		contentPane.add(btnNewButton_1);
	}
}
