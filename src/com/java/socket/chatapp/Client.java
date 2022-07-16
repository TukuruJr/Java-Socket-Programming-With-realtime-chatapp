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
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;

public class Client implements ActionListener{

	private JFrame frmClientSide;
	private JTextField getip;
	private JTextField getport;
	private JTextField getmessage;
	private JTextArea textArea ;
	private DataInputStream in;
	private DataOutputStream out;
	private Socket socket;
	private JButton btnConnect;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frmClientSide.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmClientSide = new JFrame();
		frmClientSide.getContentPane().setBackground(Color.MAGENTA);
		frmClientSide.setTitle("CLIENT SIDE");
		frmClientSide.setResizable(false);
		frmClientSide.setBounds(100, 100, 668, 300);
		frmClientSide.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClientSide.getContentPane().setLayout(null);
		
		JLabel lblClient = new JLabel("CLIENT ");
		lblClient.setFont(new Font("Dialog", Font.BOLD, 15));
		lblClient.setForeground(Color.GREEN);
		lblClient.setBounds(234, 12, 70, 15);
		frmClientSide.getContentPane().add(lblClient);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "CONNECTION DETAILS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBackground(Color.PINK);
		panel.setBounds(24, 36, 243, 226);
		frmClientSide.getContentPane().add(panel);
		panel.setLayout(null);
		
		getip = new JTextField();
		getip.setBounds(32, 47, 184, 32);
		panel.add(getip);
		getip.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP ADDRESS");
		lblIpAddress.setForeground(Color.BLACK);
		lblIpAddress.setBounds(64, 26, 123, 15);
		panel.add(lblIpAddress);
		
		JLabel lblPort = new JLabel("PORT");
		lblPort.setForeground(Color.BLACK);
		lblPort.setBounds(64, 103, 77, 15);
		panel.add(lblPort);
		
		getport = new JTextField();
		getport.setColumns(10);
		getport.setBounds(32, 131, 184, 32);
		panel.add(getport);
		
		btnConnect = new JButton("CONNECT");
		btnConnect.setForeground(new Color(255, 255, 255));
		btnConnect.setBackground(new Color(128, 0, 0));
		btnConnect.setBounds(54, 189, 117, 25);
		btnConnect.addActionListener(this);
		panel.add(btnConnect);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "CHAT PHASE", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_1.setBackground(new Color(0, 139, 139));
		panel_1.setBounds(301, 12, 355, 250);
		frmClientSide.getContentPane().add(panel_1);
		
		getmessage = new JTextField();
		getmessage.setText("Type message and key enter to send");
		getmessage.setForeground(new Color(0, 255, 0));
		getmessage.setBackground(new Color(128, 0, 0));
		getmessage.setColumns(10);
		getmessage.setBounds(12, 206, 331, 32);
		getmessage.addActionListener(this);
		panel_1.add(getmessage);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Dialog", Font.BOLD, 12));
		textArea.setForeground(new Color(255, 255, 0));
		textArea.setBackground(new Color(153, 50, 204));
		textArea.setBounds(12, 23, 331, 171);
		panel_1.add(textArea);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		//connect
		if(ae.getSource().equals(btnConnect)) {
			try {
				socket = new Socket(getip.getText(),Integer.parseInt(getport.getText()));
				getmessage.setText("");
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
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if(ae.getSource().equals(getmessage)) {
			try {
				SendData(getmessage.getText());
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
		textArea.append("Server -> "+text);
	}
	private void SendData(String data) throws IOException {
		out = new DataOutputStream(socket.getOutputStream());
		out.writeUTF(data);
		textArea.append("Client -> "+data+"\n");
	}

}
