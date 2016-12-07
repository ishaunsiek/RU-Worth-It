

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
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class Stream
 */
@WebServlet("/Stream")
public class Stream extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Stream() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			
			//String states = "";
			String streams = "";
			String q1 = "";
			String q2 = "";
			String q3 = "";
			
			q1 = "select stream, job, avg(mid), common_major from PayscaleJobs "
					+ "group by stream "
					+ "limit 5000";
			
			q2 = "select stream, rank, job, avg(mid), common_major from PayscaleJobs "
					+ "group by stream, common_major "
					+ "limit 5000";
			
			q3 = "SELECT stream, rank, job, common_major, mid FROM ( "
					+ "SELECT *, @row_number:=CASE WHEN @common_major = common_major THEN @row_number+1 ELSE 1 END AS row_number,@common_major:=common_major AS common_major2 "
					+ "FROM PayscaleJobs, (SELECT @row_number:=0,@common_major:='') AS t "
					+ "ORDER BY common_major asc, mid desc "
					+ ") AS P "
					+ "where P.row_number < 6";
			
			if(request.getParameterValues("streamIn") != null){
				streams = getStreams(request);
				
				q1 = "select stream, job, avg(mid), common_major from PayscaleJobs "
						+ "where stream in " + streams
						+ "group by stream "
						+ "limit 5000";
				
				q2 = "select stream, rank, job, avg(mid), common_major from PayscaleJobs "
						+ "where stream in " + streams
						+ "group by stream, common_major "
						+ "limit 5000";
				
				q3 = "SELECT stream, rank, job, common_major, mid FROM ( "
						+ "SELECT *, @row_number:=CASE WHEN @common_major = common_major THEN @row_number+1 ELSE 1 END AS row_number,@common_major:=common_major AS common_major2 "
						+ "FROM PayscaleJobs, (SELECT @row_number:=0,@common_major:='') AS t "
						+ "ORDER BY common_major asc, mid desc "
						+ ") AS P "
						+ "where P.row_number < 6 "
						+ "and stream in " + streams;
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
			String[][] q1Table = new String[newcount][4];
			while(res1.next() != false){
				q1Table[c][0] = res1.getString(1).replaceAll("[-+.^:,']","");
				q1Table[c][1] = res1.getString(2).replaceAll("[-+.^:,']","");
				q1Table[c][2] = "" + res1.getInt(3);
				q1Table[c][3] = res1.getString(4).replaceAll("[-+.^:,']","");
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
			String[][] q2Table = new String[newcount][5];
			while(res2.next() != false){
				q2Table[c][0] = res2.getString(1).replaceAll("[-+.^:,']","");
				q2Table[c][1] = "" + res2.getInt(2);
				q2Table[c][2] = res2.getString(3).replaceAll("[-+.^:,']","");
				q2Table[c][3] = "" + res2.getInt(4);
				q2Table[c][4] = res2.getString(5).replaceAll("[-+.^:,']","");
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
			String[][] q3Table = new String[newcount][5];
			while(res3.next() != false){
				q3Table[c][0] = res3.getString(1).replaceAll("[-+.^:,']","");
				q3Table[c][1] = "" + res3.getInt(2);
				q3Table[c][2] = res3.getString(3).replaceAll("[-+.^:,']","");
				q3Table[c][3] = res3.getString(4).replaceAll("[-+.^:,']","");
				q3Table[c][4] = "" + res3.getInt(5);
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
			String[][] q1Header = {{"Stream", "Job Title", "Average Mid-career Salary ($)", "Most Common Major"}};
			responseMap.put("q1Header", q1Header);
			responseMap.put("q1Table", q1Table);
			String[][] q2Header = {{"Stream", "Rank", "Job Title", "Average Mid-career Salary ($)", "Most Common Major"}};
			responseMap.put("q2Header", q2Header);
			responseMap.put("q2Table", q2Table);
			String[][] q3Header = {{"Stream", "Rank", "Job Title", "Most Common Major", "Mid-Career Salary ($)"}};
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
			
			System.out.print("Success in Stream");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.print("failed in Stream");
			
		}
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
