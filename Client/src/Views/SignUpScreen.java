package Views;

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Common.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUpScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 360258625773865195L;
	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// EventQueue.invokeLater(new Runnable() {
		// public void run() {
		// try {
		// SignUpScreen frame = new SignUpScreen();
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
	public SignUpScreen(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut) {
		setTitle("Sign up");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(82, 54, 68, 14);
		contentPane.add(lblUsername);

		username = new JTextField();
		username.setBounds(82, 74, 280, 28);
		contentPane.add(username);
		username.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(82, 113, 68, 14);
		contentPane.add(lblPassword);

		password = new JPasswordField();
		password.setBounds(82, 133, 280, 28);
		contentPane.add(password);

		JButton btnNewButton = new JButton("Sign up");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSignUp(in, out, commandOut, username.getText(), String.valueOf(password.getPassword()));
			}
		});

		btnNewButton.setBounds(177, 181, 89, 23);
		contentPane.add(btnNewButton);
	}

	private void onSignUp(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut, String _username,
			String _password) {
		// get object from server; blocks until object arrives.
		User user = new User(_username, _password);

		try {
			commandOut.println("SignUp");

			if (in.readBoolean()) {
				// writeUnshared() is like writeObject(), but always writes
				// a new copy of the object. The flush (optional) forces the
				// bytes out right now.
				out.writeUnshared(user);
				out.flush();

				int response = in.readInt();

				if (response == 1) {
					System.out.println("Successfully signed up.");
					HomeScreen us = new HomeScreen(in, out, commandOut, user.getName());
					us.setVisible(true);
					setVisible(false);
					dispose();
				} else {
					System.out.println("Username existed.");
					JOptionPane pane = new JOptionPane();
					pane.setMessage("Username existed.");
					JDialog dialog = pane.createDialog(null, "Alert");
					dialog.setVisible(true);
				}

			} else {
				System.out.println("Server not accepting sign up request.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
