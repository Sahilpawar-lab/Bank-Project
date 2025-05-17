import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;
public class signup extends JFrame implements ActionListener{
	JLabel username,password,pnumber,email_id;
	JTextField un,pw,pnum,mailid;
	JButton sign,loginbtn;
	//for global acces to set database >>
	String getpw;
	String getun;
	String getemail;
	long getpnum=0;
	long getbalance=0;
	long getacc_num=0;
	boolean goahed=true;
	//accont number >>
    Random random = new Random();

	
	//constructor >>
	public signup(){
		setLayout(null);
		username =new JLabel("Username");
		password=new JLabel("Password");
		un=new JTextField();
		pw=new JTextField();
		sign=new JButton("signup");
		pnumber=new JLabel("Phone Number");
		pnum=new JTextField(10);
		email_id=new JLabel("Email Id");
		mailid=new JTextField();
		loginbtn=new JButton("Login");
		
		add(username);
		add(un);
		add(pnumber);
		add(pnum);
		add(email_id);
		add(mailid);
		add(password);
		add(pw);
		add(sign);
		add(loginbtn);
		
		username.setBounds(100,100,100,20);
		un.setBounds(200,100,100,20);
		pnumber.setBounds(100,140,100,20);
		pnum.setBounds(200,140,100,20);
		email_id.setBounds(100,180,100,20);
		mailid.setBounds(200,180,160,20);
		password.setBounds(100,220,100,20);
		pw.setBounds(200,220,100,20);
		sign.setBounds(160,260,100,20);
		loginbtn.setBounds(160,300,100,20);
		
		sign.addActionListener(this);
		loginbtn.addActionListener(this);
	}//constructor
	
	public long generateAccountNumber() {
        return 10000000L + random.nextInt(90000000);
    }//generate account number <=====
	
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==sign){
			getun=un.getText(); // username
			
			getemail=mailid.getText(); // emailid
			if (getemail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
		
			} else {
				JOptionPane.showMessageDialog(this,"Invalid ! Email address ","Invalid",JOptionPane.INFORMATION_MESSAGE);
				mailid.setText(" ");
				goahed=false;
			}

			String phonenumber=pnum.getText();
			if(phonenumber.matches("\\d{10}")){
				getpnum=Long.parseLong(phonenumber);  // phone number 
			}
			else{
				JOptionPane.showMessageDialog(this,"Invalid ! phone number must be contain 10 Digits ","Invalid",JOptionPane.INFORMATION_MESSAGE);
				goahed=false;
			}
			getpw=pw.getText(); // password
			getacc_num=generateAccountNumber();//account number 
			
				//database connection >>
			try{
				if(goahed==true){
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
				System.out.println("Database connected");
				String insq="insert into bank_login (username,password,account_number,email,phone,balance) values (?,?,?,?,?,?)";
				PreparedStatement ps=c.prepareStatement(insq);
				ps.setString(1,getun);
				ps.setString(2,getpw);
				ps.setLong(3,getacc_num);
				ps.setString(4,getemail);
				ps.setLong(5,getpnum);
				ps.setLong(6,getbalance);
				int rows=ps.executeUpdate();
				if(rows>0){
					JOptionPane.showMessageDialog(this,"Registered sucessfully","REGISTER",JOptionPane.INFORMATION_MESSAGE);
					JOptionPane.showMessageDialog(this,"Account number is :"+getacc_num,"Account info",JOptionPane.INFORMATION_MESSAGE);
				}
				}
				else{
					JOptionPane.showMessageDialog(this,"Incomplete information","Invalid",JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(ae.getSource()==loginbtn){
			login l1=new login();
			l1.setTitle("login");
			l1.setVisible(true);
			l1.setSize(500,500);
			l1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.dispose();
		}//loginbutto s
	}
	
public static void main(String args[]){
signup sin1=new signup();
sin1.setTitle("signup");
sin1.setVisible(true);
sin1.setSize(500,500);
sin1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}