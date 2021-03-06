package Views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 659893169653693369L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					StartScreen frame = new StartScreen();
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
	public StartScreen(ObjectInputStream in, ObjectOutputStream out, PrintWriter commandOut) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("now closing");
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogInScreen lis = new LogInScreen(in, out, commandOut);
				lis.setVisible(true);
				setVisible(false);
				dispose();
			}
		});
		btnLogIn.setBounds(172, 89, 89, 23);
		contentPane.add(btnLogIn);
		
		JButton btnSignUp = new JButton("Sign up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SignUpScreen sus = new SignUpScreen(in, out, commandOut);
				sus.setVisible(true);
				setVisible(false);
				dispose();
			}
		});
		btnSignUp.setBounds(172, 123, 89, 23);
		contentPane.add(btnSignUp);
	}
}
