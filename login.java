import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
public class login extends JFrame implements ActionListener{
	JLabel username,password;
	JTextField un;
	JPasswordField pw;
	JButton log,forgotpass,back;
	public login(){
		setLayout(null);
		username =new JLabel("Username");
		password=new JLabel("Password");
		un=new JTextField();
		pw=new JPasswordField();
		log=new JButton("Login");
		forgotpass=new JButton("Forgot Password");
		back=new JButton("Back");
		
		add(username);
		add(un);
		add(password);
		add(pw);
		add(log);
		add(forgotpass);
		add(back);
		
		username.setBounds(100,100,100,20);
		un.setBounds(200,100,100,20);
		password.setBounds(100,140,100,20);
		pw.setBounds(200,140,100,20);
		log.setBounds(150,180,100,20);
		forgotpass.setBounds(125,220,150,20);
		back.setBounds(150,260,100,20);
		
		log.addActionListener(this);
		forgotpass.addActionListener(this);
		back.addActionListener(this);
	}//constructor
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==log){
			try{
				String getun=un.getText();
				String getpw=pw.getText();
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
				String loginq="select * from bank_login where username = ?";
				PreparedStatement ps=c.prepareStatement(loginq);
				ps.setString(1,getun);
				ResultSet rs=ps.executeQuery();
				if(rs.next()){
					if(rs.getString(2).equals(getpw)){
						JOptionPane.showMessageDialog(this,"Login sucessfully","LOGIN",JOptionPane.INFORMATION_MESSAGE);
						//homepage home=new homepage(getun,getpw);
						mainbank mb=new mainbank(getun);
						mb.setVisible(true);
						mb.setTitle("Virtual Bank");
						mb.setSize(900,900);
						mb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						this.dispose();
					}
					else{
						JOptionPane.showMessageDialog(this,"Incorrect Password","ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
				else{
					JOptionPane.showMessageDialog(this,"Incorrect Username try to Sign-in","ERROR",JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(ae.getSource()==forgotpass){
			
			forgetpass forgot1=new forgetpass();
			forgot1.setTitle("Forget Password");
			forgot1.setVisible(true);
			forgot1.setSize(500,500);
			forgot1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.dispose();
		}
		
		if(ae.getSource()==back){
			signup sin1=new signup();
			sin1.setTitle("signin");
			sin1.setVisible(true);
			sin1.setSize(500,500);
			sin1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.dispose();
		}
	}
	
public static void main(String args[]){
login l1=new login();
l1.setTitle("login");
l1.setVisible(true);
l1.setSize(500,500);
l1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}