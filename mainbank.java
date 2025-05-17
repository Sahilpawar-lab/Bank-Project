import java.io.*;
import java.util.*;
import java.lang.Math;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.event.*;
import java.sql.*;
public class mainbank extends JFrame{
	JTabbedPane bankapp;
	//panles =====>
	panelAccountDetails accdetail;
	panelDeposit accdeposit;
	panelWithdraw accwithdraw;
	panelMoneyTransfer accmtransfer;
	panelTransactionHistory acchistory;
	EMIcal emicalculator;
	//banks own variables=====>
	JLabel bankname;
	String username;
	public mainbank(String username){
		setLayout(new BorderLayout());
		this.username=username;
		bankname=new JLabel("Revati bank");
		bankname.setForeground(Color.BLUE);
		bankname.setFont(new Font("Serif",Font.BOLD,40));
		bankname.setHorizontalAlignment(SwingConstants.CENTER);
		bankapp=new JTabbedPane();
		accdetail=new panelAccountDetails(username);
		accdeposit=new panelDeposit(username);
		accwithdraw=new panelWithdraw(username);
		accmtransfer=new panelMoneyTransfer(username);
		acchistory=new panelTransactionHistory(username);
		emicalculator=new EMIcal();
		bankapp.add("User Details",accdetail);
		bankapp.add("Deposit Money",accdeposit);
		bankapp.add("Withdraw Money",accwithdraw);
		bankapp.add("Transfer Money",accmtransfer);
		bankapp.add("Transaction History",acchistory);
		bankapp.add("EMI Calculator",emicalculator);
		add(bankapp,BorderLayout.CENTER);
		add(bankname,BorderLayout.NORTH);
	}//mainbank constructr
public static void main(String args[]){
	/*mainbank mb=new mainbank();
	mb.setVisible(true);
	mb.setTitle("Virtual Bank");
	mb.setSize(700,700);
	mb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
}
}
class panelAccountDetails extends JPanel implements ActionListener{
	JLabel usertxt,acctxt,balancetxt,askpass;
	JLabel userdisp,accdisp,balancedisp;
	JButton showbalance;
	JPasswordField passfill;
	String getusername;
	long getaccnumber=0;
	long getbalance=0;
	public panelAccountDetails(String getusername){
		setLayout(null);
		this.getusername=getusername;
		//System.out.println(getusername);
		try{
			usertxt=new JLabel("Wel-Come:");
			usertxt.setFont(new Font("Serif",Font.BOLD,20));
			userdisp=new JLabel(getusername);
			userdisp.setFont(new Font("Serif",Font.BOLD,15));
			acctxt=new JLabel("Account number :");
			accdisp=new JLabel("----------"); 
			balancetxt=new JLabel("Balance :");
			balancedisp=new JLabel("xxxxxxxxxx");
			askpass=new JLabel("Enter PIN :");
			passfill=new JPasswordField(10);
			showbalance=new JButton("Fetch balance");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
			String accnofetchq="select account_number from bank_login where username='"+getusername+"'";
			PreparedStatement ps=c.prepareStatement(accnofetchq);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				getaccnumber=rs.getLong(1);
				accdisp.setText(String.valueOf(getaccnumber));
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(this,e);
		}
		add(usertxt);
		add(userdisp);
		add(acctxt);
		add(accdisp);
		add(balancetxt);
		add(balancedisp);
		add(askpass);
		add(passfill);
		add(showbalance);
		usertxt.setBounds(100,100,100,20);
		userdisp.setBounds(220,100,100,20);
		acctxt.setBounds(100,140,100,20);
		accdisp.setBounds(220,140,100,20);
		balancetxt.setBounds(100,180,100,20);
		balancedisp.setBounds(220,180,100,20);
		askpass.setBounds(100,220,100,20);
		passfill.setBounds(220,220,100,20);
		showbalance.setBounds(120,260,150,20);
		showbalance.addActionListener(this);
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==showbalance){
			String getpass=passfill.getText();
			long getpassfill=Long.parseLong(getpass);
			try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
			String balancefetchq="select balance,password from bank_login where username='"+getusername+"'";
			PreparedStatement ps=c.prepareStatement(balancefetchq);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				if(rs.getLong(2)==getpassfill){
					getbalance=rs.getLong(1);
					balancedisp.setText(String.valueOf(getbalance));
				}
				else{
					JOptionPane.showMessageDialog(this,"Incorrect PIN ","ERROR",JOptionPane.ERROR_MESSAGE);
					balancedisp.setText("xxxxxxxxxx");
				}
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(this,e);
		}
		}
	}
}
class panelDeposit extends JPanel implements ActionListener{
	JLabel amttxt;
	JTextField enteramt;
	JButton do_deposit;
	String username="";
	public panelDeposit(String username){
		setLayout(null);
		this.username =  username;
		amttxt=new JLabel("Enter amount to deposit :");
		enteramt=new JTextField(10);
		do_deposit=new JButton("Deposit");
		add(amttxt);
		add(enteramt);
		add(do_deposit);
		amttxt.setBounds(100,100,200,30);
		enteramt.setBounds(320,100,150,30);
		do_deposit.setBounds(120,170,100,30);
		do_deposit.addActionListener(this);
	}
	public void actionPerformed(ActionEvent ae)
	{
		long dep_amt = Long.parseLong(enteramt.getText());
			try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
			// get deposit amount :
			String dep_amount_query="update bank_login  set balance = balance+ "+dep_amt+" where username='"+username+"'";
			PreparedStatement ps1=c.prepareStatement(dep_amount_query);
			int opt1 =ps1.executeUpdate();
			// fetch bank balance :
			String fetch_balance="select balance from bank_login where username='"+username+"'";
			PreparedStatement ps2=c.prepareStatement(fetch_balance);
			ResultSet rs =ps2.executeQuery();
			long balan=0;
			if(rs.next())
			{
				balan= rs.getLong(1);
			}
			//description :
			String desc="Amount deposited by self";
			//set record in bank_transaction :
			String dep_amount_transaction="insert into bank_transaction values(SYSDATE,'"+username+"',"+dep_amt+","+balan+",'"+desc+"' )";
			PreparedStatement ps3=c.prepareStatement(dep_amount_transaction);
			int opt3 =ps3.executeUpdate();
			// update checking :
			if(opt1 >0 && opt3>0)
			{
				JOptionPane.showMessageDialog(this,"Deposite succeful");
			}
			else
			{
				JOptionPane.showMessageDialog(this,"Deposite Failed");
			}
		}//<< try close 
		catch(Exception e){
			JOptionPane.showMessageDialog(this,e);
		}
	}
}
class panelWithdraw extends JPanel implements ActionListener{
	JLabel wamttxt,pintxt;
	JTextField enterwamt;
	JPasswordField pincheck;
	JButton do_withdraw;
	String username="";
	public panelWithdraw(String username){
		this.username=username;
		setLayout(null);
		wamttxt=new JLabel("Enter amount to Withdraw :");
		enterwamt=new JTextField(10);
		pintxt=new JLabel("Enter your pin :");
		pincheck=new JPasswordField(10);
		do_withdraw=new JButton("Withdraw");
		add(wamttxt);
		add(enterwamt);
		add(pintxt);
		add(pincheck);
		add(do_withdraw);
		wamttxt.setBounds(100,100,200,30);
		enterwamt.setBounds(320,100,150,30);
		pintxt.setBounds(100,150,200,30);
		pincheck.setBounds(320,150,150,30);
		do_withdraw.setBounds(150,200,150,30);
		do_withdraw.addActionListener(this);
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==do_withdraw){
			long withdrawamt=Long.parseLong(enterwamt.getText());
			long thispin=Long.parseLong(pincheck.getText());
			try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
			//fetch user pin >>>
			String fetch_pin="select password from bank_login where username='"+username+"'";
			PreparedStatement ps1=c.prepareStatement(fetch_pin);
			ResultSet rs1 =ps1.executeQuery();
			long checking_pin=0;
			long checking_balance=0;
			if(rs1.next())
			{
				checking_pin= rs1.getLong(1);
				if(thispin==checking_pin){
					//fetch user balance >>>
					String balance_check="select balance from bank_login where username='"+username+"'";
					PreparedStatement ps2=c.prepareStatement(balance_check);
					ResultSet rs2=ps2.executeQuery();
					if(rs2.next()){
						checking_balance=rs2.getLong(1);
						if(checking_balance>=withdrawamt){
							// get withdrawal amount :
							long balance_amt=checking_balance-withdrawamt;
							String wit_amount_query="update bank_login  set balance = balance- "+withdrawamt+" where username='"+username+"'";
							PreparedStatement ps3=c.prepareStatement(wit_amount_query);
							int opt1 =ps3.executeUpdate();
							//description :
							String desc="Amount creadited by self";
							//set record in bank_transaction :
							String wit_amount_transaction="insert into bank_transaction values(SYSDATE,'"+username+"',"+withdrawamt+","+balance_amt+",'"+desc+"' )";
							PreparedStatement ps4=c.prepareStatement(wit_amount_transaction);
							int opt2 =ps4.executeUpdate();
							// update checking :
							if(opt1 >0 && opt2>0){
								JOptionPane.showMessageDialog(this,"withdraw succefully");
								}
							else{
								JOptionPane.showMessageDialog(this,"withdrawal Failed");
								}	
							}
						else{
							JOptionPane.showMessageDialog(this,"Insufficient Balance","ERROR",JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else{
					JOptionPane.showMessageDialog(this,"Incorrect PIN ","ERROR",JOptionPane.ERROR_MESSAGE);
				}
			}
			}//try
			catch(Exception e){
				JOptionPane.showMessageDialog(this,e);
			}
		}
	}
}
class panelMoneyTransfer extends JPanel implements ActionListener{
	// GUI components >>>
	JLabel takeusernametxt,takepintxt,takeamttxt;
	JTextField takeusername,takeamt;
	JPasswordField takepin;
	JButton transfer;
	//Other variables >>>
	String username="";
	String recipient_username="";
	public panelMoneyTransfer(String username){
		setLayout(null);
		this.username=username;
		takeusernametxt=new JLabel("Enter Recipient User-Name :");
		takepintxt=new JLabel("Enter PIN :");
		takeamttxt=new JLabel("Enter Amount :");
		takeusername=new JTextField(10);
		takepin=new JPasswordField(10);
		takeamt=new JTextField(10);
		transfer=new JButton("Transfer");
		add(takeusernametxt);
		add(takeusername);
		add(takeamttxt);
		add(takeamt);
		add(takepintxt);
		add(takepin);
		add(transfer);
		takeusernametxt.setBounds(100,100,180,30);
		takeusername.setBounds(300,100,100,30);
		takeamttxt.setBounds(100,150,150,30);
		takeamt.setBounds(300,150,100,30);
		takepintxt.setBounds(100,200,100,30);
		takepin.setBounds(300,200,100,30);
		transfer.setBounds(150,250,100,30);
		transfer.addActionListener(this);
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==transfer){
			recipient_username=takeusername.getText();
			long transfer_amt=Long.parseLong(takeamt.getText());
			long pinfortransfer=Long.parseLong(takepin.getText());
			try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
			//checking user Exits or NOT >>>
			String recipient_check_q="SELECT * FROM bank_login WHERE username ='"+recipient_username+"' ";
			PreparedStatement ps1 = c.prepareStatement(recipient_check_q);
			ResultSet rs1 = ps1.executeQuery();
			if(rs1.next()){
				// checking pin >>>
				String pin_check_q="select password from bank_login where username='"+username+"'";
				PreparedStatement ps2=c.prepareStatement(pin_check_q);
				ResultSet rs2 =ps2.executeQuery();
				long checking_pin=0;
				if(rs2.next()){
					checking_pin=rs2.getLong(1);
						if(checking_pin==pinfortransfer){
							//checking senders balance >>>
							String sender_balanceq="select balance from bank_login where username='"+username+"'";
							PreparedStatement ps3=c.prepareStatement(sender_balanceq);
							ResultSet rs3=ps3.executeQuery();
							long senderbalance=0;
							if(rs3.next()){
								senderbalance=rs3.getLong(1);
								if(senderbalance>=transfer_amt){
									long recieverbalance=0;
									//fetch receiver balance >>
									String reciever_balanceq="select balance from bank_login where username='"+recipient_username+"'";
									PreparedStatement ps4=c.prepareStatement(reciever_balanceq);
									ResultSet rs4=ps4.executeQuery();
									if(rs4.next()){
										recieverbalance=rs4.getLong(1);
									}
									//sender balance update >>
									senderbalance-=transfer_amt;
									String s_balanceq="update bank_login set balance=balance-"+transfer_amt+" where username='"+username+"'";
									PreparedStatement ps5=c.prepareStatement(s_balanceq);
									int opt1=ps5.executeUpdate(); //<<check
									//recipient balance update >>
									String r_balanceq="update bank_login set balance=balance+"+transfer_amt+" where username='"+recipient_username+"'";
									PreparedStatement ps6=c.prepareStatement(r_balanceq);
									int opt2=ps6.executeUpdate(); //<<check
									recieverbalance+=transfer_amt;
									//setting reciever description >>
									String recipient_desc="Money received from "+username;
									//setting sender description >>
									String sender_desc="Money send to "+recipient_username;
									//set sender Record >>
									String s_setdata="insert into bank_transaction values(SYSDATE,'"+username+"',"+transfer_amt+","+senderbalance+",'"+sender_desc+"')";
									PreparedStatement ps7=c.prepareStatement(s_setdata);
									int opt3=ps7.executeUpdate(); //<<check
									//set receiver Record
									String r_setdata="insert into bank_transaction values(SYSDATE,'"+recipient_username+"',"+transfer_amt+","+recieverbalance+",'"+recipient_desc+"')";
									PreparedStatement ps8=c.prepareStatement(r_setdata);
									int opt4=ps8.executeUpdate(); //<<check
									if(opt1>0 && opt2>0 && opt3>0 && opt4>0){
										JOptionPane.showMessageDialog(this,"transfer succefully");
									}
									else{
										JOptionPane.showMessageDialog(this,"transfer failed");		
									}								
								}
								else{
								JOptionPane.showMessageDialog(this,"Insufficient Balance","ERROR",JOptionPane.ERROR_MESSAGE);
								}// balance check <<
							}
						}
						else{
							JOptionPane.showMessageDialog(this,"Incorrect PIN ","ERROR",JOptionPane.ERROR_MESSAGE);
						}
				    }//<<< pin checking
				}//<<< count username 
			else {
				JOptionPane.showMessageDialog(this, "User does not exist.");
			}
			}//try
			catch(Exception e){
				JOptionPane.showMessageDialog(this,e);
			}
		}
	}
}
class panelTransactionHistory extends JPanel{
	JTable table1;
	DefaultTableModel model;
	String username;
	panelTransactionHistory(String username)
	{
		this.username=username;
		setLayout(new BorderLayout());
		        String[] columns = {"Date", "Username", "Amount", "Balance Amount", "Description"};
		model =  new DefaultTableModel(columns,0);
		table1 = new JTable(model);
        add(new JScrollPane(table1), BorderLayout.CENTER);
	 try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection c=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","sahil");
			//checking user Exits or NOT >>>
			String recipient_check_q="SELECT * FROM bank_transaction WHERE username ='"+username+"' ";
			PreparedStatement ps1 = c.prepareStatement(recipient_check_q);
			ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                Object[] row = {
                    rs1.getDate("date_t"),
                    rs1.getString("username"),
                    rs1.getLong("amount"),
                    rs1.getDouble("balance_amount"),
                    rs1.getString("description")
                };
                model.addRow(row);
            }
            rs1.close();
            ps1.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
	}
}
class EMIcal extends JPanel implements ChangeListener{
	// gui >>
	JLabel loanamttxt,intresttxt,yearstxt;
	JSlider loanamt,intrestamt,setyears;
	JTextField enterloan,enterintrest,enteryears;
	 int loan_value =0;
	 int intrest_value =0;
	 int year_value =0;
	 //end user display >>
	 JLabel monthlyemitxt,totalpaymenttxt,totalintresttxt;
	 JTextField monthlyemi,totalpayment,totalintrest;
	 // calculation values >>
	 int current_loan_value,current_intrest_value,current_year_value;
	public EMIcal(){
		setLayout(null);
		loanamttxt=new JLabel("Loan Amount ");
		enterloan=new JTextField(10);
		loanamt=new JSlider(1,100000,50000);
		loanamt.setPaintTrack(true);
        loanamt.setPaintTicks(true);
        loanamt.setPaintLabels(true);
		loanamt.setMajorTickSpacing(20000);
        loanamt.setMinorTickSpacing(1000);
		intresttxt=new JLabel("Interest Rate (% P.A.)");
		enterintrest=new JTextField(10);
		intrestamt=new JSlider(1,15,8);
		intrestamt.setPaintTrack(true);
        intrestamt.setPaintTicks(true);
        intrestamt.setPaintLabels(true);
		intrestamt.setMajorTickSpacing(4);
        intrestamt.setMinorTickSpacing(1);
		yearstxt=new JLabel("Tenure (Years)");
		enteryears=new JTextField(10);
		setyears=new JSlider(1,30,1);
		setyears.setPaintTrack(true);
        setyears.setPaintTicks(true);
        setyears.setPaintLabels(true);
		setyears.setMajorTickSpacing(5);
        setyears.setMinorTickSpacing(1);
		monthlyemitxt=new JLabel("Monthly EMI: ");
		monthlyemi=new JTextField(20);
		totalintresttxt=new JLabel("Total Interest: ");
		totalintrest=new JTextField(20);
		totalpaymenttxt=new JLabel("Total Payment: ");
		totalpayment=new JTextField(20);
		add(loanamttxt);
		add(enterloan);
		add(loanamt);
		add(intresttxt);
		add(intrestamt);
		add(enterintrest);
		add(yearstxt);
		add(enteryears);
		add(setyears);
		add(monthlyemitxt);
		add(monthlyemi);
		add(totalintresttxt);
		add(totalintrest);
		add(totalpaymenttxt);
		add(totalpayment);
		loanamttxt.setBounds(100,100,100,30);
		enterloan.setBounds(250,100,100,30);
		loanamt.setBounds(100,150,500,50);
		intresttxt.setBounds(100,220,150,30);
		enterintrest.setBounds(250,220,100,30);
		intrestamt.setBounds(100,270,500,50);
		yearstxt.setBounds(100,340,100,30);
		enteryears.setBounds(250,340,100,30);
		setyears.setBounds(100,390,500,50);
		//user can see this >>
		monthlyemitxt.setBounds(150,500,100,30);
		monthlyemi.setBounds(270,500,150,30);
		totalintresttxt.setBounds(150,550,100,30);
		totalintrest.setBounds(270,550,150,30);
		totalpaymenttxt.setBounds(150,600,100,30);
		totalpayment.setBounds(270,600,150,30);
		//change Listener ::>>
		loanamt.addChangeListener(this);
		intrestamt.addChangeListener(this);
		setyears.addChangeListener(this);
		enterloan.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) {
        updateLoanSlider(e);
    }
    public void removeUpdate(DocumentEvent e) {
        updateLoanSlider(e);
    }
    public void changedUpdate(DocumentEvent e) { }
});// for loan 
	enterintrest.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) {
        updateLoanSlider(e);
    }
    public void removeUpdate(DocumentEvent e) {
        updateLoanSlider(e);
    }
    public void changedUpdate(DocumentEvent e) { }
});// for intrest 
	enteryears.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) {
        updateLoanSlider(e);
    }
    public void removeUpdate(DocumentEvent e) {
        updateLoanSlider(e);
    }
    public void changedUpdate(DocumentEvent e) {  }
});// for years
	}
public void updateLoanSlider(DocumentEvent e) {
    if(e.getDocument() == enterloan.getDocument()) {
        String text = enterloan.getText();
        try {
            int loan_value = Integer.parseInt(text);
            if (loan_value >= loanamt.getMinimum() && loan_value <= loanamt.getMaximum()) {
                loanamt.setValue(loan_value);
            } else {
                JOptionPane.showMessageDialog(this, "Not in range", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ee) {
           JOptionPane.showMessageDialog(this,ee);
        }
    }
	 if(e.getDocument() == enterintrest.getDocument()) {
        String text = enterintrest.getText();
        try {
            int intrest_value = Integer.parseInt(text);
            if (intrest_value >= intrestamt.getMinimum() && intrest_value <= intrestamt.getMaximum()) {
                intrestamt.setValue(intrest_value);
            } else {
                JOptionPane.showMessageDialog(this, "Not in range", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ee) {
           JOptionPane.showMessageDialog(this,ee);
        }
    }
	if(e.getDocument() == enteryears.getDocument()) {
        String text = enteryears.getText();
        try {
            int year_value = Integer.parseInt(text);
            if (year_value >= setyears.getMinimum() && year_value <= setyears.getMaximum()) {
                setyears.setValue(year_value);
            } else {
                JOptionPane.showMessageDialog(this, "Not in range", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ee) {
           JOptionPane.showMessageDialog(this,ee);
        }
    }
}
	//calculateEmi >>
	public void calculateEmi(){
		double p = current_loan_value;
        double annualRate = current_intrest_value;
        double r = annualRate / (12 * 100);
        int n = current_year_value * 12;
		double monthly_emi_amt=((p*r*Math.pow(1+r,n))/(Math.pow(1+r,n)-1));
		//monthlyemi.setText("₹ " +String.valueOf(monthly_emi_amt));
		monthlyemi.setText("Rs. " + String.format("%.2f", monthly_emi_amt));
		double total_interest_amt = ((monthly_emi_amt*n)-p);
		//totalintrest.setText("₹ " +String.valueOf(total_interest_amt));
		totalintrest.setText("Rs. " + String.format("%.2f", total_interest_amt));
		double total_payment_amt=(monthly_emi_amt*n);
		//totalpayment.setText("₹ " +String.valueOf(total_payment_amt));
		totalpayment.setText("Rs. " + String.format("%.2f", total_payment_amt));
	}
	public void stateChanged(ChangeEvent ce){
		if(ce.getSource()==loanamt){
			current_loan_value=loanamt.getValue();
		}
		if(ce.getSource()==intrestamt){
			current_intrest_value=intrestamt.getValue();
		}
		if(ce.getSource()==setyears){
			current_year_value=setyears.getValue();
		}
		calculateEmi();
	}
}