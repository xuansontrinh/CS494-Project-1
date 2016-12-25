package Views;

import java.io.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Common.User;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class LogInScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6895843959543676154L;
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
		// LogInScreen frame = new LogInScreen();
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
	public LogInScreen(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut) {
		setTitle("Log in");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		username = new JTextField();
		username.setBounds(82, 70, 280, 28);
		username.setColumns(10);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(82, 51, 69, 14);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(82, 109, 69, 14);

		JButton btnNewButton = new JButton("Log in");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLogIn(in, out, commandOut, username.getText(), String.valueOf(password.getPassword()));
			}
		});

		btnNewButton.setBounds(178, 173, 89, 23);
		contentPane.setLayout(null);
		contentPane.add(lblUsername);
		contentPane.add(username);
		contentPane.add(lblPassword);

		password = new JPasswordField();
		password.setBounds(82, 128, 280, 28);
		contentPane.add(password);
		contentPane.add(btnNewButton);
	}

	private void onLogIn(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut, String _username, String _password) {
		// get object from server; blocks until object arrives.
		User user = new User(_username, _password);

		try {
			commandOut.println("LogIn");

			if (in.readBoolean()) {
				// writeUnshared() is like writeObject(), but always writes
				// a new copy of the object. The flush (optional) forces the
				// bytes out right now.
				out.writeUnshared(user);
				out.flush();

				int response = in.readInt();

				if (response == 1) {
					System.out.println("Successfully logged in.");
					HomeScreen us = new HomeScreen(in, out, commandOut, user.getName());
					us.setVisible(true);
					setVisible(false);
					dispose();
				} else if (response == -1) {
					System.out.println("Username does not exist.");
					JOptionPane pane = new JOptionPane();
					pane.setMessage("Username does not exist.");
					JDialog dialog = pane.createDialog(null, "Alert");
					dialog.setVisible(true);
				} else if (response == 0) {
					System.out.println("Wrong password.");
					JOptionPane pane = new JOptionPane();
					pane.setMessage("Wrong password.");
					JDialog dialog = pane.createDialog(null, "Alert");
					dialog.setVisible(true);
				}

			} else {
				System.out.println("Server not accepting log in request.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
