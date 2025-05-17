import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
public class forgetpass extends JFrame implements ActionListener{
JLabel userntxt,phonetxt,newpasstxt,confirmpasstxt;
JTextField username,phoneno,newpassword;
JPasswordField finalpassword;
JButton procced,back;
public forgetpass(){
	setLayout(null);
	userntxt=new JLabel("Enter Username :");
	phonetxt=new JLabel("Enter Phone-Number :");
	newpasstxt= new JLabel("Enter New Password :");
	confirmpasstxt=new JLabel("Re-write Password :");
	
	username=new JTextField(10);
	phoneno=new JTextField(10);
	newpassword=new JTextField(10);
	finalpassword=new JPasswordField(10);
	
	procced=new JButton("Proceed");
	back=new JButton("Back");
	
	add(userntxt);
	add(phonetxt);
	add(newpasstxt);
	add(confirmpasstxt);
	add(username);
	add(phoneno);
	add(newpassword);
	add(finalpassword);
	add(procced);
	add(back);
	
	userntxt.setBounds(100,100,100,20);
	username.setBounds(220,100,100,20);
	phonetxt.setBounds(100,140,150,20);
	phoneno.setBounds(250,140,100,20);
	newpasstxt.setBounds(100,180,150,20);
	newpassword.setBounds(250,180,100,20);
	confirmpasstxt.setBounds(100,220,150,20);
	finalpassword.setBounds(250,220,100,20);
	procced.setBounds(160,280,100,20);
	back.setBounds(160,320,100,20);
	
	procced.addActionListener(this);
	back.addActionListener(this);
}//constructor

public void actionPerformed(ActionEvent ae){
	if(ae.getSource()==procced){
		String getun=username.getText();
		String phone_num=phoneno.getText();
		String getpass=newpassword.getText();
		String getfinalpass=finalpassword.getText();
		long getphone=Long.parseLong(phone_num);
			
			if(!getpass.equals(getfinalpass)){
				JOptionPane.showMessageDialog(this,"Mismatched Password","ERROR",JOptionPane.ERROR_MESSAGE);
			}
			else{
				try{
					Class.forName("oracle.jdbc.driver.OracleDriver");
					Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
					String passq="select username,phone from bank_login where username=?";
					PreparedStatement ps=c.prepareStatement(passq);
					ps.setString(1,getun);
					ResultSet rs=ps.executeQuery();
					if(rs.next()){
						if(rs.getLong(2)==getphone){
							//String passchange="update registration set password='"+getpass+"' where username='"+getun+"'";
							String passchange="update bank_login set password=? where username=?";
							PreparedStatement ps1=c.prepareStatement(passchange);
							ps1.setString(1,getpass);
							ps1.setString(2,getun);
							int opt=ps1.executeUpdate();
								if(opt>0){
									JOptionPane.showMessageDialog(this,"Password change successfully","DONE",JOptionPane.INFORMATION_MESSAGE);
								}
							else{
								JOptionPane.showMessageDialog(this,"Failed ","ERROR",JOptionPane.ERROR_MESSAGE);
								}
							}
					else{
						JOptionPane.showMessageDialog(this,"Incorrect Phone Number ","ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
			else{
				JOptionPane.showMessageDialog(this,"User Not Found","ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}//try block <-----
		catch(Exception e){
			//e.printStackTrace();
			JOptionPane.showMessageDialog(this,e);
		}
	}//outer else bloc <-----
		
		
	}//procced button <<-----
	
	if(ae.getSource()==back){
		login l1=new login();
		l1.setTitle("login");
		l1.setVisible(true);
		l1.setSize(500,500);
		l1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.dispose();
	}//back button <<-----
}//actionPerformed


public static void main(String args[]){
forgetpass forgot1=new forgetpass();
forgot1.setTitle("Forget Password");
forgot1.setVisible(true);
forgot1.setSize(500,500);
forgot1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}