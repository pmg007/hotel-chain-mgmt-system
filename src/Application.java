import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;


public class Application {
	
	Connection connection;
	String jdbcURL = "jdbc:mysql://localhost:3306/wolfinns";
	String vclUserID = "root";
	String vclPassword = "root";
	Scanner scan;
	int dept = -1;
	
	public Application(Scanner scan) {
		// TODO Auto-generated constructor stub
		this.scan = scan;
	}
	
	
	public void prompt(String string){
		System.out.println(string);
	}

	public void getConnection() throws SQLException {
	        if (this.connection != null)
	            return;
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Network Issues");
	            e.printStackTrace();
	            return;
	        }
	        this.connection = DriverManager.getConnection(jdbcURL,vclUserID , vclPassword);
	    }
	
	
	public void start(int choice) throws SQLException {
		// TODO Auto-generated method stub
		String userName="", userPassword="";
		this.prompt("Enter id and password");
		try {
			userName = scan.next();
			userPassword = scan.next();
        } catch (Exception e) {
            this.prompt("Invalid input ");
            this.start(choice);
        }
		
		this.dept = this.getDepartment(userName, userPassword,choice);
		switch(this.dept){
			case -1: 
				this.prompt("Incorrect Credentials..back to main page.."); 
				Application.main(null); 
				break;
			case 1: 
				this.prompt("hello admin"); 
				this.adminHandler();
				break;
			case 2: 
				this.prompt("hello staff");
				this.staffHandler();
				break;
			default: 
				this.prompt("Something went wrong");
				this.start(choice);
				break;
            		
		}		
	}
	
	
	
	
	
	public void staffHandler() throws SQLException{
		// TODO Auto-generated method stub
		int choice  = this.viewStaffOptions();
		switch(choice) {
			
		}
		
	}


	public int viewStaffOptions() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void adminHandler() throws SQLException{
		// TODO Auto-generated method stub
		int choice  = this.viewAdminOptions();
		switch(choice) {
			case 1:
				addHotel();
				break;
			case 2:
				getHotelInfoByID();
				break;
			case 3:
				updateHotel();
				break;
			case 4:
				deleteHotel();
				break;
			case 5:
				addRoom();
				break;
			case 6:
				updateRoom();
			case 7:
				deleteRoom();
			case 8:
				addStaff();
				break;
			case 9:
				updateStaff();
				break;
			case 10:
				deleteStaff();
				break;
			case 11:
				getStaffWhoServed();
				break;
				
			case 12:
				getStaffByDept();
				break;
//			case 13:
//				getStaff();
				
			case 14:
				addService();
				break;
			case 15:
				updateService();
				break;
			case 16:
				deleteService();
				break;
			case 17:
				checkRoomAvailability();
			case 19:
				addCustomer();
				break;
			case 20:
				getCustomerInfoByEmail();
				break;
			case 21:
				updateCustomer();
				break;
			case 22:
				deleteCustomer();
				break;
			case 28:
				updateManager();
				break;
				
			default:
				this.prompt("f");
				
				
		}
		
	}
	
	public void updateManager() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter the hotel id");
		int hotelid = this.scan.nextInt();
		prompt("1. add new employee as manager 2. promote existing employee to manager");
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			addStaff();
			int staffid=0;
			String sql1 = "select max(StaffID) from STAFF";
			PreparedStatement ps1 = connection.prepareStatement(sql1);
			ResultSet rs1 = ps1.executeQuery();
			if(rs1.next()){
				staffid = rs1.getInt(1);
			}
			String sql2 = "select * from HOTEL where HotelID=?";
			PreparedStatement ps2 = this.connection.prepareStatement(sql2);
			ps2.setInt(1, hotelid);		
			ResultSet rs2= ps2.executeQuery();
			prompt("2");
			if(rs2.next()){
				updateHotelHelper(hotelid, staffid, rs2.getString("Name"),rs2.getString("Address") , rs2.getString("City"), rs2.getString("State"),rs2.getString("Email"), rs2.getString("Phone"));
			}
			
			break;
		case 2:
			updateStaff();
			prompt("enter the staff id(manager id) of the staff to be promoted to manager");
			int managerid = this.scan.nextInt();
			String sql3 = "update HOTEL set ManagerID =? where HotelID=?";
			PreparedStatement ps3 = connection.prepareStatement(sql3);
			ps3.setInt(1, managerid);
			ps3.setInt(2, hotelid);	
			ps3.executeQuery();
			break;

		default:
			break;
		}
		
	}


	public void checkRoomAvailability() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter hotel id");
		int hotelid = this.scan.nextInt();
		prompt("enter start date and end date (YYYY-MM-DD)");
		String requestedStartDate=this.scan.next(), requestedEndDate=this.scan.next();
		String sql = "";
		
		
		
	}


	public void getStaffByDept() throws SQLException{
		// TODO Auto-generated method stub
		prompt("1. get staff by dept for wolfinn 2.get staff by dept for a particular hotel 3. get staff grouped by dept");
		int choice=this.scan.nextInt();
		String dept;
		switch (choice) {
		case 1:
			prompt("enter the dept");
			dept = this.scan.next();
			String sql1 = "select Name from STAFF where Department =?";
			PreparedStatement ps1 = connection.prepareStatement(sql1);
			ps1.setString(1, dept);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()) {
				prompt(rs1.getString(1));
			}
			break;
		case 2:
			prompt("enter the dept");
			dept = this.scan.next();
			prompt("enter hotel id:");
			int hotelid = this.scan.nextInt();
			String sql2 = "select Name from STAFF where Department =? and HotelID=?";
			PreparedStatement ps2 = connection.prepareStatement(sql2);
			ps2.setString(1, dept);
			ps2.setInt(2, hotelid);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				prompt(rs2.getString(1));
			}
			break;
		case 3:
//			prompt("enter the dept");
//			dept = this.scan.next();
			String sql3 = "select Name, Department, HotelID from STAFF order by Department";
			PreparedStatement ps3 = connection.prepareStatement(sql3);
			//ps3.setString(1, dept);
			ResultSet rs3 = ps3.executeQuery();
			while(rs3.next()) {
				prompt("name:"+rs3.getString(1)+" dept:"+rs3.getString(2)+" hotel id:"+rs3.getInt(3));
			}
			break;
		default:
			prompt("invalid choice..please try again!");
			break;
		}		
	}


	public void getStaffWhoServed() throws SQLException {
		// TODO Auto-generated method stub
		prompt("Enter customer emailid");
		String customerEmail = this.scan.next();
		String sql1 = "select BookingID, RoomNumber, StartDate, EndDate  from BOOKING where CustomerEmail=?"; 
		PreparedStatement ps1 = connection.prepareStatement(sql1);
		ps1.setString(1, customerEmail);
		ResultSet rs1 = ps1.executeQuery();
		while(rs1.next()) {
			prompt("Booking id:"+rs1.getInt(1)+" RoomNumber:"+rs1.getInt(2)+" StartDate:"+rs1.getString(3)+ " End Date:"+rs1.getString(4));
		}
		prompt("enter the booking id for getting the staff who served the customer for that bookingid:");
		int bookingid = this.scan.nextInt();
		String sql2 = "select Name from STAFF where StaffID in (select sr.ServiceStaffID from SERVICE_REQUESTED sr Inner Join SERVICE_BOOKING sb on sr.ServiceNumber=sb.ServiceNumber Inner Join BOOKING b on sb.BookingID=b.BookingID where b.BookingID=? Union select ReceptingStaffID from SERVICE_REQUESTED sr Inner Join SERVICE_BOOKING sb on sr.ServiceNumber=sb.ServiceNumber Inner Join BOOKING b on sb.BookingID=b.BookingID where b.BookingID=?)  ";
		PreparedStatement ps2 = connection.prepareStatement(sql2);
		ps2.setInt(1, bookingid);
		ps2.setInt(2, bookingid);
		ResultSet rs2 = ps2.executeQuery();
		while(rs2.next()) {
			prompt(rs2.getString(1));
		}		
	}


	public void updateCustomer() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter customer email id");
		String customerEmail = scan.next();
		prompt("please select the field to be updated");
		prompt("1. name 2.email 3. DOB 4.Phone");
		String sql = "select * from CUSTOMER where Email=?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setString(1, customerEmail);
		ResultSet rs = ps.executeQuery();
		String customerNamefetched="", customerEmailfetched="", customerDOBfetched="", customerPhonefetched="" ;
		while(rs.next()) {
			customerNamefetched = rs.getString(1);
			customerEmailfetched = rs.getString(2);
			customerDOBfetched = rs.getString(3);
			customerPhonefetched = rs.getString(4);
			break;
		}		
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter name:");		
			String customerName = scan.nextLine();
			updateCustomerHelper(customerName, customerEmailfetched, customerDOBfetched, customerPhonefetched);
			break;
		case 2: 
			prompt("enter email");
			String email = scan.next();
			updateCustomerHelper(customerNamefetched, email, customerDOBfetched, customerPhonefetched);
			break;
		case 3:
			prompt("enter date(YYYY-MM-DD):");
			String customerDOB=scan.next();
			updateCustomerHelper(customerNamefetched, customerEmailfetched, customerDOB, customerPhonefetched);
			break;
		case 4:
			prompt("enter phone:");
			String customerPhone=scan.next();
			updateCustomerHelper(customerNamefetched, customerEmailfetched, customerDOBfetched, customerPhone);
			break;
		default:
			prompt("invalid option");
			break;
		}

		
	}


	public void updateCustomerHelper(String customerName, String customerEmail, String customerDOB,
			String customerPhone) throws SQLException{
		// TODO Auto-generated method stub
		String sql = "update CUSTOMER set Name=?, Email=?, DOB=?, Phone=? where Email=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setString(1, customerName);
		ps.setString(2, customerEmail);
		ps.setString(3,  customerDOB);
		ps.setString(4, customerPhone);	
		ps.setString(5, customerEmail);
		ps.executeQuery();		
		String sql2 = "select * from CUSTOMER where Email =? or Phone=?";
		PreparedStatement ps2 =connection.prepareStatement(sql2);
		ps2.setString(1, customerEmail);
		ps2.setString(2,customerPhone);
		ResultSet rs = ps2.executeQuery();
		if(rs.next()){
			//System.out.println("hello");
			String phonefetched = rs.getString(4);
			String sql1 = "update CUSTOMER set Email =? where Phone=?";
			PreparedStatement ps1 = connection.prepareStatement(sql1);
			ps1.setString(1, customerEmail );
			ps1.setString(2, phonefetched);
			ps1.executeQuery();			
		}
	}


	public void getCustomerInfoByEmail() throws SQLException{
		// TODO Auto-generated method stub
		prompt("please enter the csutomer email id");
		String customerEmail = this.scan.next();
		String sql = "select * from CUSTOMER where Email=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setString(1, customerEmail);		
		ResultSet rs= ps.executeQuery();
		while(rs.next()) {
			prompt("name="+rs.getString(1)+" email="+rs.getString(2)+" DOB="+rs.getString(3)
					+ " phone="+rs.getString(4));
		}
		
	}


	public void deleteCustomer() throws SQLException {
		// TODO Auto-generated method stub
		prompt("please enter the customer email id");
		String customerEmail = this.scan.next();
		String sql = "delete from CUSTOMER where Email=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setString(1, customerEmail);
		ps.executeQuery();		
	}


	public void addCustomer() throws SQLException{
		// TODO Auto-generated method stub
		
		
		String customerName, customerEmail, customerDOB, customerPhone;		
		prompt("Enter customer name:"); customerName = scan.next();
		prompt("Enter customer email:"); customerEmail = scan.next();
		prompt("Enter customer dob(YYYY-MM-DD):"); customerDOB = scan.next();
		prompt("Enter customer phone:"); customerPhone = scan.next();
		String sql = "INSERT into CUSTOMER values (?,?,?,?)";
		try {
			PreparedStatement ps = this.connection.prepareStatement(sql);			
			ps.setString(1, customerName);
			ps.setString(2, customerEmail);
			ps.setString(3, customerDOB);
			ps.setString(4, customerPhone);		
			ps.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}			

		
	}


	public void deleteService() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter service id"); int serviceid = this.scan.nextInt();
		prompt("please enter the hotel id");
		int hotelid = this.scan.nextInt();
		String sql = "delete from SERVICE where HotelID=? and ServiceID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ps.setInt(2, serviceid);
		ps.executeQuery();		
	}


	public void updateService() throws SQLException {
		// TODO Auto-generated method stub
//		int serviceid, cost, hotelid;
//		String serviceName="";	
		prompt("enter service id");
		int serviceid = scan.nextInt();
		prompt("enter hotel id");
		int hotelid = scan.nextInt();
		prompt("please select the field to be updated");
		prompt("1. cost 2.service name");
		String sql = "select * from SERVICE where HotelID=? and ServiceID=?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setInt(1, hotelid);
		ps.setInt(2, serviceid);
		ResultSet rs = ps.executeQuery();
		int costfetched=0;
		String serviceNamefetched="";
		while(rs.next()) {
			costfetched = rs.getInt(2);
			serviceNamefetched = rs.getString(3);
			break;
		}		
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter cost:");		
			int cost = scan.nextInt();
			updateServiceHelper(serviceid, cost, serviceNamefetched, hotelid);
			break;
		case 2: 
			prompt("enter service name:");
			String serviceName = scan.next();
			updateServiceHelper(serviceid, costfetched, serviceName, hotelid);
			break;
			
		default:
			prompt("invalid option");
			break;
		}

		
	}


	public void updateServiceHelper(int serviceid, int cost, String serviceName, int hotelid) throws SQLException{
		// TODO Auto-generated method stub
		String sql = "update SERVICE set ServiceID=?, Cost=?, ServiceName=?, HotelID=? where serviceID=? and hotelID=? ";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, serviceid);
		ps.setInt(2, cost);
		ps.setString(3,  serviceName);
		ps.setInt(4, hotelid);	
		ps.setInt(5, serviceid);
		ps.setInt(6, hotelid);
		ps.executeQuery();		
	}


	public void addService() throws SQLException {
		// TODO Auto-generated method stub
		int serviceid, cost, hotelid;
		String serviceName="";		
		prompt("Enter service id ( lim 3 digit)"); serviceid = scan.nextInt();
		prompt("Enter cost(lim 4 digit)"); cost = scan.nextInt();
		prompt("Enter hotelid"); hotelid = scan.nextInt();
		prompt("Enter service name"); serviceName = scan.nextLine();
		String sql = "INSERT into SERVICE values (?,?,?,?)";
		try {
			PreparedStatement ps = this.connection.prepareStatement(sql);			
			ps.setInt(1, serviceid);
			ps.setInt(2, cost);
			ps.setString(3, serviceName);
			ps.setInt(4, hotelid);		
			ps.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}			

	}


	@SuppressWarnings("null")
	public void deleteStaff() throws SQLException {
		// TODO Auto-generated method stub
		//prompt("enter room number"); int roomNumber = this.scan.nextInt();
		prompt("please enter the staff id");
		int staffid = this.scan.nextInt();
		String sql = "delete from STAFF where StaffID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, staffid);		
		ps.executeQuery();	
		String sql2 = "select * from HOTEL where ManagerID =?";
		PreparedStatement ps2 =connection.prepareStatement(sql2);
		ps2.setInt(1, staffid);
		ResultSet rs = ps2.executeQuery();
		if(rs.next()){
			System.out.println("hello");
			int hotelidfetched = rs.getInt(1);
			String sql1 = "update HOTEL set ManagerID =? where HotelID=?";
			PreparedStatement ps1 = connection.prepareStatement(sql1);
			ps1.setNull(1, Types.INTEGER);
			ps1.setInt(2, hotelidfetched);
			ps1.executeQuery();			
		}
			
	}


	public void updateStaff() throws SQLException{
//		// TODO Auto-generated method stub
//		String name, address, phone, dept, password;
//		//int  managerid=(Integer) null;		
//		//check if this manager id is already present in staff table	
//		int hotelid, age;
//		boolean availability,isactive;
		prompt("enter staff id");
		int staffid = scan.nextInt();
		prompt("please select the field to be updated");
		prompt("1.HotelID 2. name 3.age 4.address 5.phone 6.availability 7. dept 8. isactive");
		String sql = "select * from STAFF where StaffID=?";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setInt(1, staffid);
		ResultSet rs = ps.executeQuery();
		int agefetched = 0, hotelidfetched=0;
		String namefetched="", addressfetched="", phonefetched="",deptfetched="";
		Boolean availabilityfetched=false, isActivefetched=false;
		while(rs.next()) {
			hotelidfetched = rs.getInt(2);
			namefetched = rs.getString(3);
			agefetched = rs.getInt(4);
			addressfetched = rs.getString(5);
			phonefetched = rs.getString(6);
			availabilityfetched = rs.getBoolean(7);	
			deptfetched = rs.getString(8);
			isActivefetched = rs.getBoolean(9);
			break;
		}
		
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter hotelID:");		
			int hotelid = scan.nextInt();
			updateStaffHelper(staffid, hotelid, namefetched, agefetched, addressfetched, phonefetched,availabilityfetched, deptfetched, isActivefetched);
			break;
		case 2: 
			prompt("enter name:");
			String name = scan.next();
			updateStaffHelper(staffid, hotelidfetched, name, agefetched, addressfetched, phonefetched,availabilityfetched, deptfetched, isActivefetched);
			break;
		case 3:
			prompt("enter age");
			int age = scan.nextInt();
			updateStaffHelper(staffid, hotelidfetched, namefetched, age, addressfetched, phonefetched,availabilityfetched, deptfetched, isActivefetched);			
			break;
		case 4: 
			prompt("enter address:");
			String address = scan.next();
			updateStaffHelper(staffid, hotelidfetched, namefetched, agefetched, address, phonefetched,availabilityfetched, deptfetched, isActivefetched);
			break;
		case 5: 
			prompt("enter phone");
			String phone = scan.next();
			updateStaffHelper(staffid, hotelidfetched, namefetched, agefetched, addressfetched, phone,availabilityfetched, deptfetched, isActivefetched);
			break;
		case 6:
			prompt("enter availability:");
			Boolean availability = scan.nextBoolean();
			updateStaffHelper(staffid, hotelidfetched, namefetched, agefetched, addressfetched, phonefetched,availability, deptfetched, isActivefetched);
			break;
		case 7:
			prompt("enter dept:");
			String dept = scan.next();
			updateStaffHelper(staffid, hotelidfetched, namefetched, agefetched, addressfetched, phonefetched,availabilityfetched, dept, isActivefetched);
			break;
		case 8:
			prompt("is active status:");
			Boolean isActive = scan.nextBoolean();
			updateStaffHelper(staffid, hotelidfetched, namefetched, agefetched, addressfetched, phonefetched,availabilityfetched, deptfetched, isActive);
			break;
			
		default:
			prompt("invalid option");
			break;
		}

		
		
	}


	public void updateStaffHelper (int staffid, int hotelid, String name, int age, String address, String phone, Boolean availability, String dept, Boolean isActive) throws SQLException{
		// TODO Auto-generated method stub
		String sql = "update STAFF SET hotelID=?, Name=?, Age=?, Address=?, Phone=?, Availability=?, Department=?,isActive=?  where staffID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ps.setString(2, name);
		ps.setInt(3,  age);
		ps.setString(4, address);
		ps.setString(5, phone);
		ps.setBoolean(6, availability);
		ps.setString(7, dept);
		ps.setBoolean(8, isActive);
		ps.setInt(9, staffid);
		ps.executeQuery();		
	}


	public void deleteRoom() throws SQLException {
		// TODO Auto-generated method stub
		prompt("enter room number"); int roomNumber = this.scan.nextInt();
		prompt("please enter the hotel id");
		int hotelid = this.scan.nextInt();
		String sql = "delete from HOTEL where HotelID=? and RoomNumber=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ps.setInt(2, roomNumber);
		ps.executeQuery();
		
		
	}


	public void updateRoom() throws SQLException {
		// TODO Auto-generated method stub
		prompt("enter hotel id");
		int hotelid = scan.nextInt();
		prompt("enter room number");
		int roomNumber = scan.nextInt();
		prompt("please select the field to be updated");
		prompt("1. max Occupancy 2.rate 3.availability 4.service desc 5.category");
		String sql = "select * from Hotel where HotelID=? and RoomNumber=?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ps.setInt(2, roomNumber);
		ResultSet rs = ps.executeQuery();
		int maxOccupancyfetched = 0, ratefetched=0;
		String serviceDescfetched="", categoryfetched="";
		Boolean availabilityfetched=false;
		while(rs.next()) {
			serviceDescfetched = rs.getString(3);
			maxOccupancyfetched = rs.getInt(4);
			categoryfetched = rs.getString(5);
			availabilityfetched = rs.getBoolean(6);
			ratefetched = rs.getInt(7);			
			break;
		}
		
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter service desc:");		
			String serviceDesc = scan.nextLine();
			updateRoomHelper(roomNumber, hotelid, serviceDesc, maxOccupancyfetched, categoryfetched, availabilityfetched, ratefetched);
			break;
		case 2: 
			prompt("enter max occupancy");
			int maxOccupancy = scan.nextInt();
			updateRoomHelper(roomNumber, hotelid, serviceDescfetched, maxOccupancy, categoryfetched, availabilityfetched, ratefetched);
			break;
		case 3:
			prompt("enter category");
			String category = scan.next();
			updateRoomHelper(roomNumber, hotelid, serviceDescfetched, maxOccupancyfetched, category, availabilityfetched, ratefetched);			
			break;
		case 4: 
			prompt("enter availability");
			Boolean availability = scan.nextBoolean();
			updateRoomHelper(roomNumber, hotelid, serviceDescfetched, maxOccupancyfetched, categoryfetched, availability, ratefetched);
			break;
		case 5: 
			prompt("enter rate");
			int rate = scan.nextInt();
			updateRoomHelper(roomNumber, hotelid, serviceDescfetched, maxOccupancyfetched, categoryfetched, availabilityfetched, rate);
			break;
		default:
			prompt("invalid option");
			break;
		}

		
		
	}
	
	


	public void addRoom() throws SQLException{
		// TODO Auto-generated method stub
		String name, address, phone, dept, password;
		//int  managerid=(Integer) null;		
		//check if this manager id is already present in staff table	
		int roomNumber,hotelid, maxOccupancy,rate;
		boolean availability;
		String serviceDesc, category;
		prompt("Enter room number"); roomNumber = this.scan.nextInt();
		prompt("Enter hotelID"); hotelid = this.scan.nextInt();
		prompt("enter service desc"); serviceDesc = this.scan.nextLine();
		prompt("enter max occupancy"); maxOccupancy = this.scan.nextInt();
		prompt("Enter category"); category = scan.next();
		prompt("Enter availability"); availability = scan.nextBoolean();
		prompt("Enter rate"); rate = scan.nextInt();
		//prompt("Enter address"); address = scan.next();
		//prompt("Enter phone"); phone = scan.next();
		
		String sql = "Insert Into ROOM Values(?,?,?,?,?,?,?)";
		
		
		
//		ps.setInt(7, roomNumber);
//		ps.setInt(8, roomNumber);
		
		
		try {
			PreparedStatement ps=connection.prepareStatement(sql);			
			ps.setInt(1, roomNumber);
			ps.setInt(2, hotelid);
			ps.setString(3, serviceDesc);
			ps.setInt(4, maxOccupancy);
			ps.setString(5, category);
			ps.setBoolean(6, availability);
			ps.setInt(7, rate);
			ps.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}			
	}


	public void getHotelInfoByID() throws SQLException {
		// TODO Auto-generated method stub
		prompt("please enter the hotel id");
		int hotelid = this.scan.nextInt();
		String sql = "select * from HOTEL where HotelID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, hotelid);		
		ResultSet rs= ps.executeQuery();
		while(rs.next()) {
			prompt("hotelid="+rs.getInt(1)+" managerid="+rs.getInt(2)+" name="+rs.getString(3)
					+ " address="+rs.getString(4)+" city="+rs.getString(5)+" state="+rs.getString(6)
					+ " email="+rs.getString(7)+" phone="+rs.getString(8));
		}
	}


	public void deleteHotel() throws SQLException{
		// TODO Auto-generated method stub
		prompt("please enter the hotel id");
		int hotelid = this.scan.nextInt();
		String sql = "delete from HOTEL where HotelID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ps.executeQuery();	
	}


	public void updateHotel() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter hotel id");
		int hotelid = scan.nextInt();
		prompt("please select the field to be updated");
		prompt("1. ManagerID 2.Name 3.Address 4.City 5.State 6.email 7.phone");
		String sql = "select * from Hotel where hotelid=?";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ResultSet rs = ps.executeQuery();
		int manageridfetched = 0;
		String namefetched="", addressfetched="", cityfetched="", statefetched="", emailfetched="", phonefetched="";
		while(rs.next()) {
			manageridfetched = rs.getInt(2);
			namefetched = rs.getString(3);
			addressfetched = rs.getString(4);
			cityfetched = rs.getString(5);
			statefetched = rs.getString(6);
			emailfetched = rs.getString(7);
			phonefetched  =rs.getString(8);
			break;
		}
		
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter manager id:");		
			int managerid = scan.nextInt();
			updateHotelHelper(hotelid, managerid, namefetched, addressfetched, cityfetched, statefetched, emailfetched, phonefetched);
			break;
		case 2: 
			prompt("enter name");
			String name = scan.next();
			updateHotelHelper(hotelid, manageridfetched, name, addressfetched, cityfetched, statefetched, emailfetched, phonefetched);
			break;
		case 3:
			prompt("enter address");
			String address = scan.next();
			updateHotelHelper(hotelid, manageridfetched, namefetched, address, cityfetched, statefetched, emailfetched, phonefetched);			
			break;
		case 4: 
			prompt("enter city");
			String city = scan.next();
			updateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, city, statefetched, emailfetched, phonefetched);
			break;
		case 5: 
			prompt("enter state");
			String state = scan.next();
			updateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, cityfetched, state, emailfetched, phonefetched);
			break;
		case 6: 
			prompt("enter email");
			String email = scan.next();
			updateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, cityfetched, statefetched, email, phonefetched);
			break;
		case 7: 
			prompt("enter phone");
			String phone = scan.next();
			updateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, cityfetched, statefetched, emailfetched, phone);
			break;
		default:
			prompt("invalid option");
			break;
		}

	}
	 


	public void addStaff() throws SQLException{
		// TODO Auto-generated method stub
		String name, address, phone, dept, password;
		//int  managerid=(Integer) null;		
		//check if this manager id is already present in staff table	
		int hotelid, age;
		boolean availability,isactive;
		
		prompt("Enter hotelID"); hotelid = scan.nextInt();
		prompt("Enter name"); name = scan.next();
		prompt("Enter age"); age = scan.nextInt();
		prompt("Enter address"); address = scan.next();
		prompt("Enter phone"); phone = scan.next();
		prompt("Enter availability"); availability = scan.nextBoolean();
		prompt("Enter dept"); dept = scan.next();
		prompt("Enter isactive"); isactive = scan.nextBoolean();
		prompt("Enter password"); password = scan.next();
		String sql = "INSERT into STAFF(HotelID,Name,Age,Address,Phone,Availability,Department,IsActive,Password) values (?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = this.connection.prepareStatement(sql);
			
			ps.setInt(1, hotelid);
			ps.setString(2, name);
			ps.setInt(3, age);
			ps.setString(4, address);
			ps.setString(5, phone);
			ps.setBoolean(6, availability);
			ps.setString(7, dept);
			ps.setBoolean(8, isactive);
			ps.setString(9, password);			
			ps.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}			
	}


	public void addHotel() throws SQLException {
		// TODO Auto-generated method stub
		String name, address, city, state, email, phone;
		//int  managerid=(Integer) null;		
		//check if this manager id is already present in staff table		
		
		prompt("Enter name"); name = scan.next();
		prompt("Enter address"); address = scan.next();
		prompt("Enter city"); city = scan.next();
		prompt("Enter state"); state = scan.next();
		prompt("Enter email"); email = scan.next();
		prompt("Enter phone"); phone = scan.next();
		String sql = "INSERT into HOTEL(Name,Address,City,State,Email,Phone) values (?,?,?,?,?,?)";
		try {
			PreparedStatement ps = this.connection.prepareStatement(sql);
			//ps.setInt(1, managerid);
			ps.setString(1, name);
			ps.setString(2, address);
			ps.setString(3, city);
			ps.setString(4, state);
			ps.setString(5, email);
			ps.setString(6, phone);
			ps.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		
		prompt("now you need to add this manager to staff table");
		addStaff();
		adminHandler();

	}
	
	


	public Boolean isStaff(int managerid) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "select * from Staff where staffID =?";
		PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, managerid);
        //statement.setString(2, userPassword);
        ResultSet resultSet = statement.executeQuery();
        boolean valid = false;
        while (resultSet.next()) {
            valid = true;
            //this.userId = resultSet.getString("userid");
            break;
        }
        return valid;
	}


	


	public int getDepartment(String userName, String userPassword, int choice) throws SQLException {
		// TODO Auto-generated method stub
		if(choice==1) {
			//check for valid admin
			String admin_sql = "SELECT * FROM Admin WHERE adminID = ? AND password = ?";
			Boolean isAdmin = this.isUser(admin_sql, userName, userPassword);
	        if (isAdmin)
	            return 1;
		} else {
			//check for valid staff
			String staff_sql = "SELECT * FROM professor WHERE userid = ? AND password = ?";
			Boolean isStaff = this.isUser(staff_sql, userName, userPassword);
	        if (isStaff)
	            return 2;
		}
		// else return -1 for incorrect credentials
		return -1;
	}
	
	public Boolean isUser(String sql, String userName, String userPassword) throws SQLException {
        this.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, userName);
        statement.setString(2, userPassword);
        ResultSet resultSet = statement.executeQuery();
        boolean valid = false;
        while (resultSet.next()) {
            valid = true;
            //this.userId = resultSet.getString("userid");
            break;
        }
        return valid;
    }
	
	 

	


	public void updateHotelHelper(int hotelid,int managerid, String name, String address, String city, String state, String email, String phone) throws SQLException {
		String sql = "update HOTEL SET ManagerID=?, Name=?, Address=?, City=?, State=?, Email=?, Phone=? where HotelID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, managerid);
		ps.setString(2, name);
		ps.setString(3,  address);
		ps.setString(4, city);
		ps.setString(5, state);
		ps.setString(6, email);
		ps.setString(7, phone);		 
		ps.setInt(8, hotelid);
		ps.executeQuery();
	 }


	public int viewAdminOptions() {
		// TODO Auto-generated method stub
		prompt("1.Create Hotel 2.Get hotel info 3. update hotel 4.delete hotel");
		prompt("5.Create room  6. update room 7.delete room");
		prompt("8.Create staff 9.update staff 10.delete staff 11. get staff who served 12. get staff by dept 13. get staff");
		prompt("14.Create service 15.update service info 16.delete service");
		prompt("17.Check room availability 18.checkout room 3.");
		prompt("19.Create customer 20. get customer info 21.update customer 22.delete customer");
		prompt("23.Create booking  24. update booking 25.delete booking");
		prompt("26.generate bill 27. get revenue");
		prompt("28.update manager");
		prompt("29.assign staff to presidential");
		prompt("30.Create service request 31. update service request 32. delete service request");
		int option = scan.nextInt();	
		return option;
	}


	public void updateRoomHelper(int roomNumber, int hotelid, String serviceDesc, int maxOccupancy, String category, Boolean availability, int rate) throws SQLException{		
		String sql = "update ROOM SET ServiceDesc=?, MaxOccupancy=?, Category=?, Availability=?, Rate=? where HotelID=? and RoomNumber=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setString(1, serviceDesc);
		ps.setInt(2, maxOccupancy);
		ps.setString(3,  category);
		ps.setBoolean(4, availability);
		ps.setInt(5, rate);
		ps.setInt(6, hotelid);
		ps.setInt(7, roomNumber);			
		ps.executeQuery();
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
        Application app = new Application(scan);
        System.out.println("1.Admin Login 2.Staff Login  3.Exit");
        int choice = scan.nextInt();
        switch (choice) {
        case 1:
            try {
                app.start(choice);
            } catch (SQLException e) {
                app.prompt(e.toString());
            }
            break;
        case 2:
            try {
                app.start(choice);
            } catch (SQLException e) {
                app.prompt(e.toString());
            }
            break;
        case 3:
            return;
        default:
            app.prompt("Invalid Input");
            return;
        }
        try {
            app.connection.close();
        } catch (SQLException e) {
            app.prompt(e.toString());
        }
        scan.close();
    }
	

	


	

		



}
