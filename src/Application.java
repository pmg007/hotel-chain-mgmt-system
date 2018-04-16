import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;



public class Application {
	
	Connection connection;
	String jdbcURL = "jdbc:mariadb://localhost:3307/wolfinns";
	String vclUserID = "root";
	String vclPassword = "root";
	Scanner scan;
	int dept = -1;
	
	public Application(Scanner scan) {		// TODO Auto-generated constructor stub
		this.scan = scan;
	}
	
	
	public void prompt(String string){
		System.out.println(string);
	}

	public void getConnection() throws SQLException {
	        if (this.connection != null)
	            return;
	        try {
	            Class.forName("org.mariadb.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            System.out.println("Network Issues");
	            e.printStackTrace();
	            return;
	        }
	        this.connection = DriverManager.getConnection(jdbcURL,vclUserID , vclPassword);
	    }
	
	
	public void start(int choice) throws SQLException, ParseException {
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


	public void deleteBooking() throws SQLException{
		// TODO Auto-generated method stub
		prompt("please enter the booking id");
		int bookingid = this.scan.nextInt();
		String sql = "delete from BOOKING where BookingID=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, bookingid);
		ps.executeQuery();
	}


//	public void updateBooking() throws SQLException{
//		// TODO Auto-generated method stub
//				
//	}


	public void getRevenueByDateRange() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter hotel id");
		int hotelid = this.scan.nextInt();
		prompt("enter start date");
		String startDate=this.scan.next();
		prompt("enter end date");
		String endDate=this.scan.next();
		String sql = "select sum(TotalAmount) Revenue from BILL b, BOOKING bk where (b.BookingID=bk.BookingID and EndDate BETWEEN ? and ?) and HotelID=?";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1, startDate);
		ps.setString(2, endDate);
		ps.setInt(3, hotelid);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) {
			prompt("revenue for hotel with hotel id "+hotelid+" is:"+rs.getFloat(1));
		}
	}


	public void reportOccupancyByCity() throws SQLException{
		// TODO Auto-generated method stub		
		String sql="SELECT c.City, CASE  WHEN SUM(z.Occupied) IS NULL THEN 0 ELSE SUM(z.Occupied) END AS Occupancy, SUM(z.TotalRooms) AS TotalRooms,  CASE WHEN SUM(z.Occupied/z.TotalRooms*100) IS NULL THEN 0 ELSE SUM(z.Occupied/z.TotalRooms*100) END AS Percentage_occupancy_by_City FROM (SELECT t.HotelID, o.Occupied, t.TotalRooms FROM (SELECT   COUNT(roomNumber) AS Occupied , HotelID FROM room WHERE Availability=0  GROUP BY HotelID) o RIGHT OUTER JOIN (SELECT COUNT(roomNumber) AS TotalRooms, HotelID FROM room GROUP BY HotelID) t  ON o.HotelID=t.HotelID) z JOIN (SELECT City , HotelID FROM hotel) c ON c.HotelID=z.HotelID GROUP BY c.City;";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			prompt("city:"+rs.getString(1) + " occupied:"+rs.getInt(2)+" total rooms:"+rs.getInt(3)+" percentage occupancy by city:"+rs.getDouble(4));
		}		
	}


	public void reportOccupancyByDateRange() throws SQLException{
		// TODO Auto-generated method stub
		
		
	}


	public void reportOccupancyByRoomType() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter hotel id:");
		int hotelid = this.scan.nextInt();
		String sql="SELECT t.HotelID, t.Category, o.Occupied, t.TotalRooms, o.Occupied/t.TotalRooms*100 AS  Percentage_of_room_occupied FROM (SELECT    COUNT(roomNumber) AS Occupied, HotelID, Category FROM room WHERE availability=? AND HotelID=?  GROUP BY Category) o RIGHT OUTER JOIN (SELECT COUNT(roomNumber) AS TotalRooms, HotelID, Category FROM room WHERE   hotelID=? GROUP BY Category) t ON o.Category=t.Category ";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setBoolean(1, false);
		ps.setInt(2, hotelid);
		ps.setInt(3, hotelid);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			prompt("hotelid:"+rs.getInt(1) + " room type:"+rs.getString(2)+" occupied:"+rs.getInt(3)+" total rooms:"+rs.getInt(4)+" percentage of rooms:"+rs.getDouble(5));
		}
		
	}


	public void reportOccupancyByHotel() throws SQLException{
		// TODO Auto-generated method stub
		String sql = "SELECT t.HotelID, o.Occupied, t.TotalRooms, o.Occupied/t.TotalRooms*100 AS Percentage_of_room_occupied FROM (SELECT  COUNT(roomNumber)   AS Occupied, HotelID FROM room WHERE Availability=0  GROUP BY HotelID) o RIGHT OUTER JOIN (SELECT COUNT(roomNumber) AS TotalRooms, HotelID FROM room GROUP BY HotelID) t  ON o.HotelID=t.HotelID;";
		PreparedStatement ps=connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			prompt("hotelid:"+rs.getInt(1)+" occupied:"+rs.getInt(2)+" total rooms:"+rs.getInt(3)+" percentage:"+rs.getDouble(4));
		}
	}


	public void initBill(int bookingid, int hotelid, int roomNumber, long numberOfDays) throws SQLException{
		// TODO Auto-generated method stub
		scan.nextLine();
		String paymentMode="";
		prompt("enter paymentMode mode");
		paymentMode = this.scan.nextLine();
		long cardNumber;
		if(paymentMode.equalsIgnoreCase("CASH")){
			cardNumber=0;
		} else {
			prompt("enter card number");
			cardNumber =this.scan.nextLong();
		}
		//scan.nextLine();
		prompt("enter bill address");
		String billAddress = this.scan.nextLine();
		prompt("enter ssn number");
		long ssn = this.scan.nextLong();
		String sql = "SELECT Category, Rate from ROOM where HotelID=? and RoomNumber=?";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setInt(1, hotelid);
		ps.setInt(2, roomNumber);
		String roomType;
		int rate=0;
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			roomType=rs.getString(1);
			rate = rs.getInt(2);
		}
		double totalAmount = rate*numberOfDays;
		String sql1 = "INSERT into BILL (PaymentMode, CardNumber, BillAddress, SSN, TotalAmount, BookingID) values (?,?,?,?,?,?)";
		PreparedStatement ps1 = this.connection.prepareStatement(sql1);
		ps1.setString(1, paymentMode);
		ps1.setLong(2, cardNumber);
		ps1.setString(3, billAddress);
		ps1.setLong(4, ssn);
		ps1.setDouble(5, totalAmount);
		ps1.setInt(6, bookingid);
		ps1.executeUpdate();		
	}


	public void updateServiceRequested() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter the service number");
		int serviceNumber = this.scan.nextInt();
		String sql = "select * from SERVICE_REQUESTED where ServiceNumber=?";
		
		PreparedStatement ps = connection.prepareStatement(sql);
		
		ps.setInt(1, serviceNumber);
		ResultSet rs = ps.executeQuery();
		int receptingStaffidfetched=0;
		int serviceStaffidfetched=0;
		if (rs.next()) {
			serviceStaffidfetched = rs.getInt(2);
			receptingStaffidfetched = rs.getInt(3);
			
		}	
		prompt("1. change recepting staff 2. change service staff id");
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter recepting staff id:");		
			int receptingStaffid = scan.nextInt();
			updateServiceRequestedHelper(serviceNumber, receptingStaffid, serviceStaffidfetched);
			break;
		case 2: 
			prompt("enter service staff id:");
			int serviceStaffid = scan.nextInt();
			updateServiceRequestedHelper(serviceNumber, receptingStaffidfetched, serviceStaffid);
			break;
			
		default:
			prompt("invalid option");
			break;
		}
		

				
	}


	public void updateServiceRequestedHelper(int serviceNumber, int receptingStaffid, int serviceStaffid) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "update SERVICE_REQUESTED set ReceptingStaffID=?, ServiceStaffID=? where ServiceNumber=? ";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, receptingStaffid);
		ps.setInt(2, serviceStaffid);
		ps.setInt(3, serviceNumber);
		ps.executeUpdate();		
		
	}


	public void deleteServiceRequested() throws SQLException {
		// TODO Auto-generated method stub
		prompt("enter service number"); 
		int serviceNumber = this.scan.nextInt();
		String sql = "delete from SERVICE_REQUESTED where ServiceNumber=?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, serviceNumber);
		ps.executeQuery();				
	}


	public void addServiceRequested() throws SQLException{
		// TODO Auto-generated method stub
		int serviceid, hotelid,serviceStaffid, receptingStaffid;		
		prompt("Enter service id ( lim 3 digit)"); 
		serviceid = scan.nextInt();
		prompt("Enter hotelid");
		hotelid = scan.nextInt();
		prompt("Enter service staff id");
		serviceStaffid = scan.nextInt();
		prompt("Enter recepting staff id");
		receptingStaffid = scan.nextInt();
		prompt("enter booking id");
		int bookingid = this.scan.nextInt();
		String sql = "INSERT into SERVICE_REQUESTED (ServiceStaffID,ReceptingStaffid,ServiceID, HotelID) values (?,?,?,?)";
		try {
			PreparedStatement ps = this.connection.prepareStatement(sql);			
			ps.setInt(1, serviceStaffid );
			ps.setInt(2, receptingStaffid);
			ps.setInt(3, serviceid);
			ps.setInt(4, hotelid);		
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		//get max service number
		int serviceNumber=0;
		String sql11 = "select Max(ServiceNumber) from SERVICE_REQUESTED";
		PreparedStatement ps11 = connection.prepareStatement(sql11);
		ResultSet rs11 = ps11.executeQuery();
		if(rs11.next()) {
			serviceNumber = rs11.getInt(1);
		}
		
		
		//service_booking
		String sql1 = "insert into SERVICE_BOOKING values (?,?)";
		PreparedStatement ps1 = connection.prepareStatement(sql1);
		ps1.setInt(1, serviceNumber);
		ps1.setInt(2, bookingid);
		ps1.executeUpdate();
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
			ps3.executeUpdate();
			break;

		default:
			break;
		}
		
	}


	public void checkRoomAvailability() throws SQLException{
		// TODO Auto-generated method stub		
		int choice;
		prompt("1. filter by date and room type 2. filter by date");
		choice = this.scan.nextInt();
		switch(choice) {
		case 1:
			checkRoomAvailabilityByDateAndRoom();
			break;
		case 2:
			checkRoomAvailabilityByDate();
			break;
		default:
			prompt("wrong input");
				
		}
	}


	public void checkRoomAvailabilityByDate() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter hotel id");
		int hotelid = this.scan.nextInt();
		prompt("enter start date and end date (YYYY-MM-DD)");
		String requestedStartDate=this.scan.next(), requestedEndDate=this.scan.next();
		
		String sql = "select * from Room where RoomNumber not in(Select Distinct(RoomNumber) from Booking Where CheckOutTime is Null AND ((StartDate<=? and EndDate>=?) or(StartDate>=? and StartDate<=?))) and HotelID=?";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1, requestedStartDate);
		ps.setString(2, requestedStartDate);
		ps.setString(3, requestedStartDate);
		ps.setString(4, requestedEndDate);
		ps.setInt(5, hotelid);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			prompt("room number:"+rs.getInt(1)+" \n hotel id:"+rs.getInt(2)+" \n serviceDesc:"
					+rs.getString(3)+" \n max occupancy:"+rs.getInt(4)+" \n category:"+rs.getString(5)
					+" \n availability:"+rs.getString(6)+" \n rate:"+rs.getInt(7)+"\n");
		}	
		
	}


	public void checkRoomAvailabilityByDateAndRoom() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter hotel id");
		int hotelid = this.scan.nextInt();
		prompt("enter start date and end date (YYYY-MM-DD)");
		String requestedStartDate=this.scan.next(), requestedEndDate=this.scan.next();
		prompt("enter room type:");
		this.scan.nextLine();
		String roomType = this.scan.nextLine();		
		String sql = "select * from Room where RoomNumber not in(Select Distinct(RoomNumber) from Booking Where CheckOutTime is Null AND ((StartDate<=? and EndDate>=?) or(StartDate>=? and StartDate<=?))) and HotelID=? and Category=?";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1, requestedStartDate);
		ps.setString(2, requestedStartDate);
		ps.setString(3, requestedStartDate);
		ps.setString(4, requestedEndDate);
		ps.setInt(5, hotelid);
		ps.setString(6, roomType);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			prompt("room number:"+rs.getInt(1)+" \n hotel id:"+rs.getInt(2)+" \n serviceDesc:"
					+rs.getString(3)+" \n max occupancy:"+rs.getInt(4)+" \n category:"+rs.getString(5)
					+" \n availability:"+rs.getString(6)+" \n rate:"+rs.getInt(7)+"\n");
		}		
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
		ps.executeUpdate();		
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
			ps1.executeUpdate();			
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
		scan.nextLine();
		prompt("Enter customer name:"); customerName = scan.nextLine();
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
			ps.executeUpdate();
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
			scan.nextLine();
			String serviceName = scan.nextLine();
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
		ps.executeUpdate();		
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
			ps.executeUpdate();
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
			ps1.executeUpdate();		
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
			scan.nextLine();
			String name = scan.nextLine();
			updateStaffHelper(staffid, hotelidfetched, name, agefetched, addressfetched, phonefetched,availabilityfetched, deptfetched, isActivefetched);
			break;
		case 3:
			prompt("enter age");
			int age = scan.nextInt();
			updateStaffHelper(staffid, hotelidfetched, namefetched, age, addressfetched, phonefetched,availabilityfetched, deptfetched, isActivefetched);			
			break;
		case 4: 
			prompt("enter address:");
			scan.nextLine();
			String address = scan.nextLine();
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
		ps.executeUpdate();		
	}


	public void deleteRoom() throws SQLException {
		// TODO Auto-generated method stub
		prompt("enter room number"); int roomNumber = this.scan.nextInt();
		prompt("please enter the hotel id");
		int hotelid = this.scan.nextInt();
		String sql = "delete  from ROOM where HotelID=? and RoomNumber=?";
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
		prompt("1. service desc 2.max occupancy 3.category 4.availability 5.rate");
		String sql = "select * from ROOM where HotelID=? and RoomNumber=?";
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
			scan.nextLine();
			String category = scan.nextLine();
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
		this.scan.nextLine();
		prompt("enter service desc"); serviceDesc = this.scan.nextLine();
		prompt("enter max occupancy"); maxOccupancy = this.scan.nextInt();
		this.scan.nextLine();
		prompt("Enter category"); category = scan.nextLine();
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
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		
		
		if(category.equalsIgnoreCase("PRESIDENTIAL SUITE")) {
			String sql1="insert into PRESIDENTIAL_ROOM  (RoomNumber, HotelID) values (?,?)";
			PreparedStatement ps1 = connection.prepareStatement(sql1);
			ps1.setInt(1, roomNumber);
			ps1.setInt(2, hotelid);
			ps1.executeUpdate();
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
			scan.nextLine();
			String name = scan.nextLine();
			updateHotelHelper(hotelid, manageridfetched, name, addressfetched, cityfetched, statefetched, emailfetched, phonefetched);
			break;
		case 3:
			prompt("enter address");
			scan.nextLine();
			String address = scan.nextLine();
			updateHotelHelper(hotelid, manageridfetched, namefetched, address, cityfetched, statefetched, emailfetched, phonefetched);			
			break;
		case 4: 
			prompt("enter city");
			String city = scan.next();
			updateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, city, statefetched, emailfetched, phonefetched);
			break;
		case 5: 
			prompt("enter state");
			scan.nextLine();
			String state = scan.nextLine();
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
		scan.nextLine();
		prompt("Enter name"); name = scan.nextLine();
		prompt("Enter age"); age = scan.nextInt();
		scan.nextLine();
		prompt("Enter address"); address = scan.nextLine();
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
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}			
	}


	public void addHotel() throws SQLException, ParseException {
		// TODO Auto-generated method stub
		String name, address, city, state, email, phone;
		//int  managerid=(Integer) null;		
		//check if this manager id is already present in staff table		
		scan.nextLine();
		prompt("Enter name"); name = scan.nextLine();
		prompt("Enter address"); address = scan.nextLine();
		prompt("Enter city"); city = scan.next();
		scan.nextLine();
		prompt("Enter state"); state = scan.nextLine();
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
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		
		prompt("now you need to add manager to staff table");
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
			String admin_sql = "SELECT * FROM ADMIN WHERE adminID = ? AND password = ?";
			Boolean isAdmin = this.isUser(admin_sql, userName, userPassword);
	        if (isAdmin)
	            return 1;
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
		ps.executeUpdate();
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
		ps.executeUpdate();
	}


	public int viewAdminOptions() {
		// TODO Auto-generated method stub
		prompt("1.Create Hotel 2.Get hotel info 3. update hotel 4.delete hotel");
		prompt("5.Create room  6. update room 7.delete room");
		prompt("8.Create staff 9.update staff 10.delete staff 11. get staff who served 12. get staff by dept");
		prompt("14.Create service 15.update service info 16.delete service");
		prompt("17.Check room availability 18.checkout room ");
		prompt("19.Create customer 20. get customer info 21.update customer 22.delete customer");
		prompt("23.Create booking  24. update booking 25.delete booking");
		prompt("26.generate bill 27. get revenue by date range for a hotel");
		prompt("28.update manager");
		prompt("29.checkin room");
		prompt("30.Create service request 31. update service request 32. delete service request");
		prompt("33. Generate report(occupancy by hotel) 34. Generate report(occupancy by room type)");
		prompt("35. Generate report(occupancy by date range) 36. Generate report(occupancy by city)");
		prompt("37. total occupancy 38. percentage of room occupied 39. staff grouped by roles ");
		prompt("40. info of staff serving a customer on a stay");
		int option = scan.nextInt();
		return option;
	}


	public void adminHandler() throws SQLException, ParseException{
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
					break;
				case 7:
					deleteRoom();
					break;
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
					break;
				case 18:
					checkoutRoom();
					break;
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
				case 23:
					createBooking();
					break;
	//			case 24:
	//				updateBooking();
	//				break;
				case 25:
					deleteBooking();
					break;
//				case 26:
//					generateBill()	
				case 27:
					getRevenueByDateRange();
					break;
				case 28:
					updateManager();
					break;
				case 29:
					checkInRoom();
					break;
				case 30:
					addServiceRequested();
					break;
				case 31:
					updateServiceRequested();
					break;
				case 32:
					deleteServiceRequested();
					break;
				case 33:
					reportOccupancyByHotel();
					break;
				case 34:
					reportOccupancyByRoomType();
					break;
				case 35:
					reportOccupancyByDateRange();
					break;
				case 36:
					reportOccupancyByCity();
					break;
				case 37:
					getTotalOcupancy();
					break;
				case 38:
					getPercentageOfRoomOccupied();
					break;
					
				default:
					this.prompt("f");
					
					
			}
			prompt("do u want to perform any other operation? Enter y or n");
			String more = this.scan.next();
			if(more.equalsIgnoreCase("y")) {
				adminHandler();
			} else {
				prompt("bye!");
			}
		}


	public void checkInRoom() throws SQLException{
		// TODO Auto-generated method stub
		//take booking id
		prompt("please enter the booking id");
		int bookingid = this.scan.nextInt(), hotelid = 0,roomNumber = 0;
		
		
		
		String sql1 = "select RoomNumber, HotelID from BOOKING where BookingID=? ";
		PreparedStatement ps1 = connection.prepareStatement(sql1);
		ps1.setInt(1, bookingid);
		ResultSet rs1 = ps1.executeQuery();
		if(rs1.next()) {
			roomNumber = rs1.getInt(1);
			hotelid = rs1.getInt(2);
		}
		
		String sql2 = "update ROOM set Availability=? where HotelID=? and RoomNumber=?";
		PreparedStatement ps2 = connection.prepareStatement(sql2);
		ps2.setBoolean(1, false);
		ps2.setInt(2, hotelid);
		ps2.setInt(3, roomNumber);
		
		ps2.executeUpdate();
		
		
		String sql3 = "SELECT Category from ROOM where HotelID=? and RoomNumber=?";
		PreparedStatement ps3 = connection.prepareStatement(sql3);
		ps3.setInt(1, hotelid);
		ps3.setInt(2, roomNumber);
		ResultSet rs3 = ps3.executeQuery();
		String roomType="";
		if(rs3.next())
			roomType = rs3.getString(1);
		
		if(roomType.equalsIgnoreCase("PRESIDENTIAL SUITE")) {
			assignStaffToPresidential(bookingid,hotelid,roomNumber);
		}
		
	}


	public void assignStaffToPresidential(int bookingid, int hotelid, int roomNumber) throws SQLException{
		// TODO Auto-generated method stub
//		
		String sql1 = "select StaffID,Name from STAFF where hotelid=? and Department <> ? and Department<> ?";
		PreparedStatement ps1 = connection.prepareStatement(sql1);
		ps1.setInt(1, hotelid);
		ps1.setString(2, "Management");
		ps1.setString(3,"Receptionist");
		ResultSet rs1 = ps1.executeQuery();
		while(rs1.next()){
			prompt("staff id: "+rs1.getInt(1)+" name: "+rs1.getString(2));
		}
		int serviceStaffid, cleanerStaffid;
		prompt("enter serice staff id");
		serviceStaffid = this.scan.nextInt();
		prompt("enter cleaner staff id");
		cleanerStaffid = this.scan.nextInt();
		
		
		String sql2 = "update PRESIDENTIAL_ROOM set  RoomServiceStaffID=? and CleanerStaffID=?  ";
		PreparedStatement ps2 = connection.prepareStatement(sql2);
		ps2.setInt(1, serviceStaffid);
		ps2.setInt(2, cleanerStaffid);
		ps2.executeUpdate();
		
		
		String sql = "INSERT into SERVICE_REQUESTED (ServiceStaffID,ReceptingStaffid,ServiceID, HotelID) values (?,?,?,?)";
		try {
			PreparedStatement ps = this.connection.prepareStatement(sql);			
			ps.setInt(1, serviceStaffid );
			ps.setInt(2, cleanerStaffid);
			ps.setInt(3, 000);
			ps.setInt(4, hotelid);		
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
		//get max service number
		int serviceNumber=0;
		String sql11 = "select Max(ServiceNumber) from SERVICE_REQUESTED";
		PreparedStatement ps11 = connection.prepareStatement(sql11);
		ResultSet rs11 = ps11.executeQuery();
		if(rs11.next()) {
			serviceNumber = rs11.getInt(1);
		}
		
		
		//service_booking
		String sql123 = "insert into SERVICE_BOOKING values (?,?)";
		PreparedStatement ps123 = connection.prepareStatement(sql123);
		ps123.setInt(1, serviceNumber);
		ps123.setInt(2, bookingid);
		ps123.executeUpdate();
		
	}


	public void generateBill(int bookingid) throws SQLException{
		// TODO Auto-generated method stub
		String sql1 = "select * from BOOKING where BookingID=?";
		PreparedStatement ps1=connection.prepareStatement(sql1);
		ps1.setInt(1, bookingid);
		ResultSet rs1=ps1.executeQuery();
		String startDate="", endDate="", checkinTime="", checkoutTime="", hotelName="", customerEmail="", roomType="",hotelAddress="", hotelCity="", serviceName="";
		
		int roomNumber=0, hotelid=0,serviceCost=0;
		double totalAmount=0;
		while(rs1.next()) {
			startDate  = rs1.getString(3);
			endDate  = rs1.getString(4);
			checkinTime  = rs1.getString(5);
			checkoutTime  = rs1.getString(6);
			customerEmail = rs1.getString(7);
			hotelid=rs1.getInt(8);
			roomNumber=rs1.getInt(9);
		}
		
		String sql2 = "select Category from ROOM where RoomNumber=? and hotelid=?";
		PreparedStatement ps2=connection.prepareStatement(sql2);
		ps2.setInt(1, roomNumber);
		ps2.setInt(2, hotelid);
		ResultSet rs2=ps2.executeQuery();
		if(rs2.next()) {
			roomType=rs2.getString(1);
		}
		
		String sql3 = "select Name, Address, City from HOTEL where hotelid=?";
		PreparedStatement ps3=connection.prepareStatement(sql3);
		ps3.setInt(1, hotelid);
		ResultSet rs3=ps3.executeQuery();
		if(rs3.next()) {
			hotelName=rs3.getString(1);
			hotelAddress = rs3.getString(2);
			hotelCity = rs3.getString(3);
		}
		
		long billNumber=0,cardNumber=0;
		String paymentMode="", billAddress="";
		String sql4 = "select * from BILL where BookingID=?";
		PreparedStatement ps4=connection.prepareStatement(sql4);
		ps4.setInt(1, bookingid);
		ResultSet rs4=ps4.executeQuery();
		if(rs4.next()) {
			billNumber=rs4.getLong(1);
			paymentMode = rs4.getString(2);
			cardNumber = rs4.getLong(3);
			billAddress = rs4.getString(4);
			totalAmount = rs4.getDouble(6);
		}
		
		
		
		prompt(hotelName +", "+hotelAddress+", "+hotelCity);
		prompt("bill number:"+ billNumber + "\t booking id:" +bookingid + " payment mode:"+paymentMode);
		
		prompt("customer email:"+customerEmail);
		prompt("billing address:"+billAddress);
		prompt("start date:"+startDate +", "+" checkin time:"+checkinTime);
		prompt("end date:"+endDate+ ", "+ "checkout time"+checkoutTime);
		prompt("room number:"+roomNumber+ " room type:"+roomType+ " room cost:"+totalAmount);
		//prompt("room cost:"+totalAmount);
		
		
		
		String sql5 = "SELECT ServiceName, Cost FROM service WHERE ServiceId IN(SELECT ServiceID FROM Service_requested WHERE ServiceNumber IN(SELECT ServiceNumber FROM service_booking WHERE BookingID=?)) AND HotelID = (SELECT HotelID FROM booking WHERE BookingID=?);";
		PreparedStatement ps5 = connection.prepareStatement(sql5);
		ps5.setInt(1, bookingid);
		ps5.setInt(2, bookingid);
		ResultSet rs5 = ps5.executeQuery();
		prompt("service name \t service cost ");
		while(rs5.next()) {
			serviceName = rs5.getString(1);
			serviceCost = rs5.getInt(2);
			prompt(serviceName+"\t"+serviceCost);
			totalAmount+=serviceCost;			
		}
		
		if(paymentMode.equalsIgnoreCase("WOLFINN CREDIT CARD")) {
			totalAmount=0.95*totalAmount;
			prompt("5 % discount for you!!");
		}
		prompt("total= "+totalAmount);
		//String sql6 // to update total amount in bill table
		
		String sql6 = "update BILL set TotalAmount=? where BookingID=?";
		PreparedStatement ps6 = connection.prepareStatement(sql6);
		ps6.setDouble(1, totalAmount);
		ps6.setInt(2, bookingid);
		ps6.executeUpdate();
	}


	public void getPercentageOfRoomOccupied() throws SQLException{
		// TODO Auto-generated method stub
		String sql = "SELECT t.HotelID, o.Occupied, t.TotalRooms, o.Occupied/t.TotalRooms*100 AS Percentage_of_room_occupied FROM (SELECT  COUNT(roomNumber)   AS Occupied, HotelID FROM room WHERE Availability=0  GROUP BY HotelID) o RIGHT OUTER JOIN (SELECT COUNT(roomNumber) AS TotalRooms, HotelID FROM room GROUP BY HotelID) t  ON o.HotelID=t.HotelID;";
		PreparedStatement ps=connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			prompt("hotelid:"+rs.getInt(1)+" occupied:"+rs.getInt(2)+" total rooms:"+rs.getInt(3)+" percentage:"+rs.getDouble(4));
		}		
	}


	public void getTotalOcupancy() throws SQLException{
		// TODO Auto-generated method stub
		String sql = "SELECT rs.HotelID,CASE WHEN SUM(rs.GuestNumber) IS NULL THEN 0 ELSE SUM(rs.GuestNumber) END AS TotalOccupancy FROM (SELECT A.BookingID, A.GuestNumber, room.`HotelID`,room.`RoomNumber` FROM (SELECT * FROM Booking WHERE startDate<=? AND EndDate>=?) A RIGHT JOIN room ON A.HotelID = room.`HotelID` AND A.RoomNumber=room.`RoomNumber`) rs GROUP BY rs.HotelID;";
		PreparedStatement ps=connection.prepareStatement(sql);
		ps.setString(1,java.time.LocalDateTime.now().toString());
		ps.setString(2,java.time.LocalDateTime.now().toString());
		ResultSet rs=ps.executeQuery();
		while(rs.next()) {
			prompt("hotelid:"+rs.getInt(1)+" total occupancy(number of guests):"+rs.getInt(2));
		}				
	}


	public void createBooking() throws SQLException, ParseException{
		// TODO Auto-generated method stub
		prompt("enter customer email id"); 
		String checkCustomerEmail = this.scan.next();
		String sql = "SELECT Email from CUSTOMER where Email=?";
		PreparedStatement ps=this.connection.prepareStatement(sql);
		ps.setString(1, checkCustomerEmail);
		ResultSet rs=ps.executeQuery();
		if(!rs.next()){
			addCustomer();
		}
		checkRoomAvailability();
		int guestNumber, hotelid, roomNumber;
		String startDate, endDate, checkinTime, checkoutTime=null, customerEmail;		
		prompt("Enter number of guests"); guestNumber = scan.nextInt();
		prompt("Enter hotelid"); hotelid = scan.nextInt();
		prompt("Enter start date"); startDate = scan.next();
		prompt("Enter end date"); endDate = scan.next();
		prompt("enter customer email id"); customerEmail = this.scan.next();
		prompt("enter checkin time"); checkinTime = this.scan.next();
		prompt("Enter room number"); roomNumber = scan.nextInt();
		
		
		try {
			DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dstartDate = LocalDate.parse(startDate,dtFormatter);
			LocalDate dendDate = LocalDate.parse(endDate,dtFormatter);
			long numberOfDays = ChronoUnit.DAYS.between(dstartDate, dendDate)+1;
			connection.setAutoCommit(false);
			boolean flag1=false, flag2 = false;
			String sql1 = "INSERT into BOOKING (GuestNumber, StartDate, EndDate, CheckInTime,CheckOutTime, CustomerEmail, HotelID, RoomNumber) values (?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement ps1 = this.connection.prepareStatement(sql1);			
				ps1.setInt(1, guestNumber);
				ps1.setString(2, startDate);
				ps1.setString(3, endDate);
				ps1.setString(4, checkinTime);
				ps1.setString(5, checkoutTime);
				ps1.setString(6, customerEmail);
				ps1.setInt(7, hotelid);
				ps1.setInt(8, roomNumber);			
				ps1.executeUpdate();
				flag1 = true;
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			int bookingid;
			try {
				String sql2="select max(BookingID) from BOOKING";
				bookingid=0;
				PreparedStatement ps2=connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				if(rs2.next()) {
					bookingid = rs2.getInt(1);
				}
				initBill(bookingid,hotelid,roomNumber,numberOfDays);
				flag2=true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();;
			}
			
			if(flag1 && flag2 ) {
				connection.commit();
				prompt("booking is created successfully");
			} else {
				
				connection.rollback();
				prompt("booking not successful");
			}
			connection.setAutoCommit(true);
		} catch (Exception e) {
			// TODO: handle exception
			if(connection !=null){
				try{
					connection.rollback();
					connection.setAutoCommit(true);
				}
				catch (Exception e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}
			}
		}
		
		
		
		//handle presidential staff assignment
		// handled in checkinRoom
		
		
	}


	public void checkoutRoom() throws SQLException{
		// TODO Auto-generated method stub
		prompt("enter booking id");
		int bookingid=this.scan.nextInt();
		boolean f1=false,f3=false,f5=false,f6=false,f7=false;
		try {
			connection.setAutoCommit(false);
			try {
				String sql1="update BOOKING set CheckoutTime=? where BookingID=?";
				Calendar cal = Calendar.getInstance();
			    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			    //System.out.println( sdf.format(cal.getTime()) );//checkout time
				PreparedStatement ps1 = this.connection.prepareStatement(sql1);
				ps1.setString(1, sdf.format(cal.getTime()));
				ps1.setInt(2, bookingid);
				ps1.executeUpdate();
				f1=true;
			} catch (Exception e1) {
				// TODO: handle exception
				e1.printStackTrace();
			}
			
			
			String sql2="SELECT HotelID, RoomNumber from BOOKING where BookingID=?";
			PreparedStatement ps2 = this.connection.prepareStatement(sql2);
			ps2.setInt(1, bookingid);
			ResultSet rs=ps2.executeQuery();
			int hotelid=0, roomNumber=0;
			if(rs.next()){
				hotelid=rs.getInt(1);roomNumber=rs.getInt(2);
			}
			
			try {
				String sql3="update ROOM set Availability=? where HotelID=? and RoomNumber=?";//room status change
				PreparedStatement ps3=this.connection.prepareStatement(sql3);
				ps3.setBoolean(1, true);
				ps3.setInt(2, hotelid);
				ps3.setInt(3, roomNumber);
				ps3.executeUpdate();
				f3=true;
			} catch (Exception e3) {
				// TODO: handle exception
				e3.printStackTrace();
			}
			
			
			
			String sql4="SELECT Category from ROOM where RoomNumber=? and HotelID=?";//find room type
			PreparedStatement ps4=this.connection.prepareStatement(sql4);
			ps4.setInt(1, roomNumber);
			ps4.setInt(2, hotelid);		
			ResultSet rs4=ps4.executeQuery();		
			String roomType="";
			if(rs.next()){
				roomType=rs.getString(1);
				
			}
			
			String sql5 = "select RoomServiceStaffID, CleanerStaffID from PRESIDENTIAL_ROOM where HotelID=? and RoomNumber=?"; //update staff to be available
			PreparedStatement ps5= this.connection.prepareStatement(sql5);
			ps5.setInt(1, hotelid);
			ps5.setInt(2, roomNumber);
			ResultSet rs5 = ps5.executeQuery();
			try {
				while(rs5.next()) {
					int sid=rs5.getInt(1),sid2=rs5.getInt(2);
					String sql5i ="update STAFF set Availability=? where StaffID=?";
					PreparedStatement ps5i=connection.prepareStatement(sql5i);
					//PreparedStatement ps5ii=connection.prepareStatement(sql5i);
					ps5i.setBoolean(1, true);
					ps5i.setInt(2, sid);
					ps5i.executeUpdate();
					ps5i.setInt(2, sid2);
					ps5i.executeUpdate();
				}
				f5=true;
			} catch (Exception e5) {
				// TODO: handle exception
				e5.printStackTrace();
			}
			
			
			
			try {
				if(roomType.equalsIgnoreCase("Presidential Suite")){
					String sql6="update PRESIDENTIAL_ROOM set RoomServiceStaffID=? and CleanerStaffID=? where HotelID=? and RoomNumber=?"; //presidential staff isavailable status flag, cleaner, roomserviceid set null
					PreparedStatement ps6=connection.prepareStatement(sql6);
					ps6.setNull(1, Types.INTEGER);
					ps6.setNull(2, Types.INTEGER);
					ps6.setInt(3, hotelid);
					ps6.setInt(4, roomNumber);
					ps6.executeUpdate();			
				}
				f6=true;
			} catch (Exception e6) {
				// TODO: handle exception
				e6.printStackTrace();
			}
					
			
			try {
				generateBill(bookingid);
				f7=true;
			} catch (Exception e7) {
				// TODO: handle exception
				e7.printStackTrace();
			}
			
			if (f1 && f3 && f5 && f6 && f7){
				connection.commit();
				prompt("");
				prompt("Checkout and Bill is generated!");
			}
			else{
				connection.rollback();
				prompt("");
				prompt("Some error occured!");
			}
			connection.setAutoCommit(true);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(connection != null){
				try{
					connection.rollback();
					connection.setAutoCommit(true);
				}
				catch (Exception e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}
			}
		}
		
	}


	public static void main(String[] args) throws ParseException {
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
