package com.java.socket.chatapp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;

public class Server  implements ActionListener{

	private JFrame frmServerSide;
	private JTextField get_Message;
	private JTextField get_port;
	private JButton btnStartConnection;
	private DataInputStream in;
	private DataOutputStream out;
	private ServerSocket ssocket;
	private Socket socket;
	private JTextArea textArea ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frmServerSide.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServerSide = new JFrame();
		frmServerSide.setTitle("SERVER SIDE");
		frmServerSide.setResizable(false);
		frmServerSide.getContentPane().setBackground(Color.CYAN);
		frmServerSide.setBounds(100, 100, 432, 399);
		frmServerSide.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServerSide.getContentPane().setLayout(null);
		
		JLabel lblServerSide = new JLabel("server side ---> initialize/start connection first to listen to the given port");
		lblServerSide.setFont(new Font("MathJax_SansSerif", Font.BOLD | Font.ITALIC, 12));
		lblServerSide.setForeground(Color.RED);
		lblServerSide.setBounds(12, 22, 408, 15);
		frmServerSide.getContentPane().add(lblServerSide);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Chat Phase", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBackground(new Color(85, 107, 47));
		panel.setBounds(12, 131, 408, 230);
		frmServerSide.getContentPane().add(panel);
		panel.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		textArea.setForeground(Color.RED);
		textArea.setBackground(Color.GRAY);
		textArea.setBounds(12, 22, 384, 153);
		panel.add(textArea);
		
		get_Message = new JTextField();
		get_Message.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		get_Message.setText("Type your message here and key enter to send");
		get_Message.setForeground(new Color(0, 255, 0));
		get_Message.setBackground(new Color(0, 0, 0));
		get_Message.setBounds(22, 187, 374, 31);
		get_Message.addActionListener(this);
		panel.add(get_Message);
		get_Message.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.RED);
		separator.setBounds(12, 117, 408, 2);
		frmServerSide.getContentPane().add(separator);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.YELLOW);
		panel_1.setBounds(22, 37, 398, 68);
		frmServerSide.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		get_port = new JTextField();
		get_port.setBounds(30, 27, 114, 32);
		panel_1.add(get_port);
		get_port.setColumns(10);
		
		JLabel lblPort = new JLabel("port");
		lblPort.setBounds(59, 0, 70, 15);
		panel_1.add(lblPort);
		
		btnStartConnection = new JButton("START CONNECTION");
		btnStartConnection.setForeground(Color.WHITE);
		btnStartConnection.setBackground(Color.BLUE);
		btnStartConnection.setBounds(193, 27, 179, 25);
		btnStartConnection.setFocusable(false);
		btnStartConnection.addActionListener(this);
		panel_1.add(btnStartConnection);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		//handle clicks
		//start connection
		if(ae.getSource().equals(btnStartConnection)) {
			try {
				ssocket = new ServerSocket(Integer.parseInt(get_port.getText()));
				
				//accept connection from the client
				socket = ssocket.accept();
				get_Message.setText("");
				
				//set the app to recieve texts/data in every second
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							RecieveData();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}, 0,1000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if(ae.getSource().equals(get_Message)) {
			try {
				SendData(get_Message.getText());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void RecieveData() throws IOException {
		in = new DataInputStream(socket.getInputStream());
		String text = "";
		text = in.readUTF();
		textArea.append("Client -> "+text+"\n");
	}
	private void SendData(String data) throws IOException {
		out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(data);
		textArea.append("Server -> "+data+"\n");
	}
}
