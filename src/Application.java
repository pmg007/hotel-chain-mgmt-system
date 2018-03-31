import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
			case 8:
				addStaff();
				break;
				
			default:
				this.prompt("f");
				
				
		}
		
	}
	
	public void updateRoom() {
		// TODO Auto-generated method stub
		
		
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
			statefetched = rs.getString(5);
			emailfetched = rs.getString(6);
			phonefetched  =rs.getString(7);
			break;
		}
		
		int choice = this.scan.nextInt();
		switch (choice) {
		case 1:
			prompt("enter manager id:");		
			int managerid = scan.nextInt();
			UpdateHotelHelper(hotelid, managerid, namefetched, addressfetched, cityfetched, statefetched, emailfetched, phonefetched);
			break;
		case 2: 
			prompt("enter name");
			String name = scan.next();
			UpdateHotelHelper(hotelid, manageridfetched, name, addressfetched, cityfetched, statefetched, emailfetched, phonefetched);
			break;
		case 3:
			prompt("enter address");
			String address = scan.next();
			UpdateHotelHelper(hotelid, manageridfetched, namefetched, address, cityfetched, statefetched, emailfetched, phonefetched);			
			break;
		case 4: 
			prompt("enter city");
			String city = scan.next();
			UpdateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, city, statefetched, emailfetched, phonefetched);
			break;
		case 5: 
			prompt("enter state");
			String state = scan.next();
			UpdateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, cityfetched, state, emailfetched, phonefetched);
			break;
		case 6: 
			prompt("enter email");
			String email = scan.next();
			UpdateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, cityfetched, statefetched, email, phonefetched);
			break;
		case 7: 
			prompt("enter phone");
			String phone = scan.next();
			UpdateHotelHelper(hotelid, manageridfetched, namefetched, addressfetched, cityfetched, statefetched, emailfetched, phone);
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
		String sql = "INSERT into HOTEL(Name,Address,City,State,Email,Phone) values (?,?,?,?,?,?)";
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


	public int viewAdminOptions() {
		// TODO Auto-generated method stub
		prompt("1.Create Hotel 2.Get hotel info 3. update hotel 4.delete hotel");
		prompt("5.Create room  6. update room 7.delete room");
		prompt("8.Create staff 9.update staff 10.delete staff 11. get staff who served 12. get staff by role 13. get staff");
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
	
	 

	


	public void UpdateHotelHelper(int hotelid,int managerid, String name, String address, String city, String state, String email, String phone) throws SQLException {
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
