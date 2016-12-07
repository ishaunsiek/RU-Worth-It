

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class College
 */
@WebServlet("/College")
public class College extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public College() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("HI");
			String url = "jdbc:mysql://cs336.c7r2hjhvlcff.us-east-1.rds.amazonaws.com:3306/my_instance";
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection(url, "root", "password");
			Statement stmt = (Statement) con.createStatement();
			ResultSet res1;
			ResultSet res2;
			ResultSet res3;
			
			String states = "";
			String streams = "";
			String q1 = "";
			String q2 = "";
			String q3 = "";
			
			q1 = "SELECT I.state, P.stream, P.rank, P.name, P.early, P.mid, I.net_price, I.meaning FROM PayscaleStreams P, IpedSchool I "
					+ "WHERE I.id = P.id "
					+ "order by meaning desc "
					+ "Limit 10";
			
			q2 = "SELECT I.state, P.stream, P.rank, P.name, avg(P.early), avg(P.mid), avg(I.net_price) FROM PayscaleStreams P, IpedSchool I "
					+ "WHERE I.id = P.id " 
					+ "group by state, stream "
					+ "order by stream, rank "
					+ "Limit 5000";
			
			q3 = "SELECT P.state as state, P.stream as stream, P.rank as rank, P.name as name, P.early as early, P.mid as mid, P.net_price as net_price FROM ( "
					+ "SELECT *, @row_number:=CASE WHEN @stream = stream THEN @row_number+1 ELSE 1 END AS row_number,@stream:=stream AS stream2 "
					+ "FROM (SELECT I.state, PP.stream, PP.rank, PP.name, PP.early, PP.mid, I.net_price, I.meaning FROM  PayscaleStreams PP, IpedSchool I "
					+ "WHERE I.id = PP.id "
					+ ") As PS, (SELECT @row_number:=0,@stream:='') AS t "
					+ "ORDER BY stream asc, rank asc "
					+ ") AS P "
					+ "where P.row_number < 6 ";
			
			if(request.getParameterValues("streamIn") != null && request.getParameterValues("stateIn") != null){
				states = getStates(request);
				streams = getStreams(request);
				q1 = "SELECT I.state, P.stream, P.rank, P.name, P.early, P.mid, I.net_price, I.meaning FROM PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id and state in " + states + " and stream in " + streams
						+ "order by meaning desc "
						+ "Limit 10";
				
				q2 = "SELECT I.state, P.stream, P.rank, P.name, avg(P.early), avg(P.mid), avg(I.net_price) FROM PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id and I.state in" + states + "and P.stream in " +streams
						+ "group by state, stream "
						+ "order by stream, rank "
						+ "Limit 5000";
				
				q3 = "SELECT P.state as state, P.stream as stream, P.rank as rank, P.name as name, P.early as early, P.mid as mid, P.net_price as net_price FROM ( "
						+ "SELECT *, @row_number:=CASE WHEN @stream = stream THEN @row_number+1 ELSE 1 END AS row_number,@stream:=stream AS stream2 "
						+ "FROM (SELECT I.state, PP.stream, PP.rank, PP.name, PP.early, PP.mid, I.net_price, I.meaning FROM  PayscaleStreams PP, IpedSchool I "
						+ "WHERE I.id = PP.id and I.state in " + states + "and PP.stream in " + streams
						+ ") As PS, (SELECT @row_number:=0,@stream:='') AS t "
						+ "ORDER BY stream asc, rank asc "
						+ ") AS P "
						+ "where P.row_number < 6 and stream in " + streams;
				
				
			} else if(request.getParameterValues("streamIn") != null){
				streams = getStreams(request);
				q1 = "SELECT I.state, P.stream, P.rank, P.name, P.early, P.mid, I.net_price, I.meaning FROM PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id" + " and stream in " + streams
						+ "order by meaning desc "
						+ "Limit 10";
				
				q2 = "SELECT I.state, P.stream, P.rank, P.name, avg(P.early), avg(P.mid), avg(I.net_price) FROM PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id and P.stream in " +streams
						+ "group by state, stream "
						+ "order by stream, rank "
						+ "Limit 5000";
				
				q3 = "SELECT P.state as state, P.stream as stream, P.rank as rank, P.name as name, P.early as early, P.mid as mid, P.net_price as net_price FROM ( "
						+ "SELECT *, @row_number:=CASE WHEN @stream = stream THEN @row_number+1 ELSE 1 END AS row_number,@stream:=stream AS stream2 "
						+ "FROM (SELECT I.state, PP.stream, PP.rank, PP.name, PP.early, PP.mid, I.net_price, I.meaning FROM  PayscaleStreams PP, IpedSchool I "
						+ "WHERE I.id = PP.id and PP.stream in " + streams
						+ ") As PS, (SELECT @row_number:=0,@stream:='') AS t "
						+ "ORDER BY stream asc, rank asc "
						+ ") AS P "
						+ "where P.row_number < 6 and stream in " + streams;
				
			} else if(request.getParameterValues("stateIn") != null){
				states = getStates(request);
				q1 = "SELECT I.state, P.stream, P.rank, P.name, P.early, P.mid, I.net_price, I.meaning FROM PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id and state in " + states
						+ "order by meaning desc "
						+ "Limit 10";
				
				q2 = "SELECT I.state, P.stream, P.rank, P.name, avg(P.early), avg(P.mid), avg(I.net_price) FROM PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id and I.state in" + states
						+ "group by state, stream "
						+ "order by stream, rank "
						+ "Limit 5000";
				
				q3 = "SELECT P.state as state, P.stream as stream, P.rank as rank, P.name as name, P.early as early, P.mid as mid, P.net_price as net_price FROM ( "
						+ "SELECT *, @row_number:=CASE WHEN @stream = stream THEN @row_number+1 ELSE 1 END AS row_number,@stream:=stream AS stream2 "
						+ "FROM (SELECT I.state, PP.stream, PP.rank, PP.name, PP.early, PP.mid, I.net_price, I.meaning FROM  PayscaleStreams PP, IpedSchool I "
						+ "WHERE I.id = PP.id and I.state in " + states
						+ ") As PS, (SELECT @row_number:=0,@stream:='') AS t "
						+ "ORDER BY stream asc, rank asc "
						+ ") AS P "
						+ "where P.row_number < 6";
			}
			
			res1 = stmt.executeQuery(q1);
			
			//make tables of data
			int newcount = 0;
			while(res1.next() != false)
			{
				newcount++;
			}
			
			int c = 0;
			res1.beforeFirst();
			String[][] q1Table = new String[newcount][8];
			while(res1.next() != false){
				q1Table[c][0] = res1.getString(1).replaceAll("[-+.^:,']","");
				q1Table[c][1] = res1.getString(2).replaceAll("[-+.^:,']","");
				q1Table[c][2] = "" + res1.getInt(3);
				q1Table[c][3] = res1.getString(4).replaceAll("[-+.^:,']","");
				q1Table[c][4] = "" + res1.getInt(5);
				q1Table[c][5] = "" + res1.getInt(6);
				q1Table[c][6] = "" + res1.getInt(7);
				q1Table[c][7] = "" + res1.getInt(8);
				c++;
			}
			
			res2 = stmt.executeQuery(q2);
			
			newcount = 0;
			while(res2.next() != false)
			{
				newcount++;
			}
			
			c = 0;
			res2.beforeFirst();
			String[][] q2Table = new String[newcount][7];
			while(res2.next() != false){
				q2Table[c][0] = res2.getString(1).replaceAll("[-+.^:,']","");
				q2Table[c][1] = res2.getString(2).replaceAll("[-+.^:,']","");
				q2Table[c][2] = "" + res2.getInt(3);
				q2Table[c][3] = res2.getString(4).replaceAll("[-+.^:,']","");
				q2Table[c][4] = "" + res2.getInt(5);
				q2Table[c][5] = "" + res2.getInt(6);
				q2Table[c][6] = "" + res2.getInt(7);
				c++;
			}
			
			res3 = stmt.executeQuery(q3);
			
			newcount = 0;
			while(res3.next() != false)
			{
				newcount++;
			}
			
			c = 0;
			res3.beforeFirst();
			String[][] q3Table = new String[newcount][7];
			while(res3.next() != false){
				q3Table[c][0] = res3.getString(1).replaceAll("[-+.^:,']","");
				q3Table[c][1] = res3.getString(2).replaceAll("[-+.^:,']","");
				q3Table[c][2] = "" + res3.getInt(3);
				q3Table[c][3] = res3.getString(4).replaceAll("[-+.^:,']","");
				q3Table[c][4] = "" + res3.getInt(5);
				q3Table[c][5] = "" + res3.getInt(6);
				q3Table[c][6] = "" + res3.getInt(7);
				c++;
			}
			
		
			/*
			for(int i = 0; i < collegeTable.length; i++){
				System.out.println(collegeTable[i][0]+" "+collegeTable[i][1]+" "+collegeTable[i][2]+" "+collegeTable[i][3]+" "+collegeTable[i][4]+" "+collegeTable[i][5]+" "+collegeTable[i][6]);
			}
			*/
			
//			System.out.println("\n\nQ1 TABLE==================================================");
//			printTableArray(q1Table);
//			System.out.println("\n\nQ2 TABLE=================================================");
//			printTableArray(q2Table);
//			System.out.println("\n\nQ3 TABLE=================================================");
//			printTableArray(q3Table);
			HashMap<String, String[][]> responseMap = new HashMap<String, String[][]>();
			String[][] q1Header = {{"State", "Stream", "Rank", "College Name", "Early-career Salary ($)", "Mid-career Salary ($)", "Net Tuition ($)", "Meaningful Ratio (%)"}};
			responseMap.put("q1Header", q1Header);
			responseMap.put("q1Table", q1Table);
			String[][] q2Header = {{"State", "Stream", "Rank", "College Name", "Early-career Salary ($)", "Mid-career Salary ($)", "Average Tuition ($)"}};
			responseMap.put("q2Header", q2Header);
			responseMap.put("q2Table", q2Table);
			String[][] q3Header = {{"State", "Stream", "Rank", "College Name", "Early-career Salary ($)", "Mid-career Salary ($)", "Net Tuition ($)"}};
			responseMap.put("q3Header", q3Header);
			responseMap.put("q3Table", q3Table);
			String jsonOut = new Gson().toJson(responseMap);
			
			System.out.println("\n\n\n" + jsonOut);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonOut);
			
			
			res1.close();
			res2.close();
			res3.close();
			stmt.close();
			con.close();
			
			System.out.print("Success in College");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.print("failed in College");
			
		}
	}
	
	public static String getStates(HttpServletRequest request){
		String s = "";
		String [] stateIn = request.getParameterValues("stateIn");
		s += " (";
		int i = 0;
		for(i = 0; i < stateIn.length - 1; i++){
			s += "'" + stateIn[i]+"', ";
		}
		s += "'" + stateIn[i]+"') ";
		return s;
	}
	
	public static String getStreams(HttpServletRequest request){
		String s = "";
		String [] streamIn = request.getParameterValues("streamIn");
		s += " (";
		int i = 0;
		for(i = 0; i < streamIn.length - 1; i++){
			s += "'" + streamIn[i]+"', ";
		}
		s += "'" + streamIn[i]+"') ";
		return s;
	}
	
    public static void printTableArray(String[][] table){
    	for(int i = 0; i < table.length; i++){
    		System.out.println();
    		for(int j = 0; j < table[i].length; j++){
    			System.out.print(table[i][j] + "\t");
    		}
    	}
    }

}
