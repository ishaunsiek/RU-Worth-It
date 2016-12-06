

import java.io.IOException;

import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class Index
 */
@WebServlet("/Index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Index() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static void printTableArray(String[][] table){
    	for(int i = 0; i < table.length; i++){
    		System.out.println();
    		for(int j = 0; j < table[i].length; j++){
    			System.out.print(table[i][j] + "\t");
    		}
    	}
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			System.out.println("HI");
			String url = "jdbc:mysql://cs336.c7r2hjhvlcff.us-east-1.rds.amazonaws.com:3306/my_instance";
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection(url, "root", "password");
			Statement stmt = (Statement) con.createStatement();
			ResultSet res; 
			
			String[] streamIn;
			String[] stIn;
			String q = "";
			
			if((streamIn = request.getParameterValues("streamIn")) != null){
				for(int i = 0; i < streamIn.length; i++){
					System.out.println(streamIn[i]);
				}
			}
			
			if( (stIn = request.getParameterValues("stateIn")) != null){
				for(int i = 0; i < stIn.length; i++){
					System.out.println(stIn[i]);
				}
			}
			
			
			if(request.getParameterValues("streamIn") == null){
				q = "select stream, rank, job, mid, common_major from PayscaleJobs limit 5000";
				res = stmt.executeQuery(q);
			} else {
				streamIn = request.getParameterValues("streamIn");
				q = "select stream, rank, job, mid, common_major from PayscaleJobs where stream in (";
		
				for(int i = 0; i < streamIn.length - 1; i++){
					q += "?,";
				}
				q += "?) limit 5000";
			
				PreparedStatement ps = (PreparedStatement) con.prepareStatement(q);

				for(int i = 0; i < streamIn.length; i++){
					ps.setString(i+1, streamIn[i]);
				}
				System.out.println(q);
				res = ps.executeQuery();
			}
			
			int newcount = 0;
			while(res.next() != false)
			{
				newcount++;
			}
			
			int c = 0;
			res.beforeFirst();
			String[][] streamTable = new String[newcount][5];
			while(res.next() != false){
				streamTable[c][0] = res.getString(1).replaceAll("[-+.^:,']","");
				streamTable[c][1] = "" + res.getInt(2);
				streamTable[c][2] = res.getString(3).replaceAll("[-+.^:,']","");
				streamTable[c][3] = "" + res.getInt(4);
				streamTable[c][4] = res.getString(5).replaceAll("[-+.^:,']","");
				c++;
			}
			
			/*
			for(int i = 0; i < streamTable.length; i++){
				System.out.println(streamTable[i][0]+" "+streamTable[i][1]+" "+streamTable[i][2]+" "+streamTable[i][3]+" "+streamTable[i][4]);
			}
			*/
			
			request.setAttribute("streamTable", streamTable);
			
			String[] stateIn;
			String w = "";
			if(request.getParameterValues("stateIn") == null){
				w = "SELECT I.state, P.stream, P.rank, P.name, I.early, I.mid, I.net_price FROM  PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id ";
				if(request.getParameterValues("streamIn") != null){
					streamIn = request.getParameterValues("streamIn");
					w += " and stream in (";
					for(int i = 0; i < streamIn.length - 1; i++){
						w += "?,";
					}
					w += "?) order by P.rank LIMIT 5000";
		
					PreparedStatement ps = (PreparedStatement) con.prepareStatement(w);

					for(int i = 0; i < streamIn.length; i++){
						ps.setString(i+1, streamIn[i]);
					}
					System.out.println(w);
					res = ps.executeQuery();
				} else {
					w += "order by P.rank LIMIT 5000";
					res = stmt.executeQuery(w);
				}
			} else {
				stateIn = request.getParameterValues("stateIn");
				w = "SELECT I.state, P.stream, P.rank, P.name, P.early, P.mid, I.net_price FROM  PayscaleStreams P, IpedSchool I "
						+ "WHERE I.id = P.id and state in (";
		
				for(int i = 0; i < stateIn.length - 1; i++){
					w += "?,";
				}
				w += "?) ";
				
				
				if(request.getParameterValues("streamIn") != null){
					streamIn = request.getParameterValues("streamIn");
					w += " and stream in (";
					for(int i = 0; i < streamIn.length - 1; i++){
						w += "?,";
					}
					w += "?) ";
				}
				w += "order by P.rank LIMIT 5000";
				
				PreparedStatement ps = (PreparedStatement) con.prepareStatement(w);
				int x = 0;
				for(x = 0; x < stateIn.length; x++){
					System.out.println("state: "+stateIn[x] + " x:"+x);
					ps.setString(x+1, stateIn[x]);
				}
				if(request.getParameterValues("streamIn") != null){
					streamIn = request.getParameterValues("streamIn");
					for(int i = x; i < streamIn.length + x; i++){
						System.out.println("stream: "+streamIn[i - x] + " i:"+i);
						ps.setString(i+1, streamIn[i - x]);
					}
				}
				System.out.println("w: "+w);
				res = ps.executeQuery();
			}
			
			
			newcount = 0;
			while(res.next() != false)
			{
				newcount++;
			}
			
			c = 0;
			res.beforeFirst();
			String[][] collegeTable = new String[newcount][7];
			while(res.next() != false){
				collegeTable[c][0] = res.getString(1).replaceAll("[-+.^:,']","");
				collegeTable[c][1] = res.getString(2).replaceAll("[-+.^:,']","");
				collegeTable[c][2] = "" + res.getInt(3);
				collegeTable[c][3] = res.getString(4).replaceAll("[-+.^:,']","");
				collegeTable[c][4] = "" + res.getInt(5);
				collegeTable[c][5] = "" + res.getInt(6);
				collegeTable[c][6] = "" + res.getInt(7);
				c++;
			}
			/*
			for(int i = 0; i < collegeTable.length; i++){
				System.out.println(collegeTable[i][0]+" "+collegeTable[i][1]+" "+collegeTable[i][2]+" "+collegeTable[i][3]+" "+collegeTable[i][4]+" "+collegeTable[i][5]+" "+collegeTable[i][6]);
			}
			*/
			request.setAttribute("collegeTable", collegeTable);
			
			System.out.println("\n\nSTREAM TABLE==================================================");
			printTableArray(streamTable);
			System.out.println("\n\nCOLLEGE TABLE=================================================");
			printTableArray(collegeTable);
			HashMap<String, String[][]> responseMap = new HashMap<String, String[][]>();
			String[][] collegesHeader = {{"State", "Stream", "Rank", "College name", "Early-career salary ($)", "Mid-career salary ($)", "Net tuition($)"}};
			responseMap.put("CollegesTableHeader", collegesHeader);
			responseMap.put("CollegesTable", collegeTable);
			String[][] streamsHeader = {{"Stream", "Rank", "Job title", "Mid-career salary ($)", "Most common major"}};
			responseMap.put("StreamsTableHeader", streamsHeader);
			responseMap.put("StreamsTable", streamTable);
			String jsonOut = new Gson().toJson(responseMap);
//			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/table.jsp");
//			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher(responseMap);
//			RequetsDispatcherObj.forward(request, response);
			
			System.out.println("\n\n\n" + jsonOut);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonOut);
			
			res.close();
			stmt.close();
			con.close();
			
			System.out.print("table.jsp");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.print("failed table.jsp");
			
		}
	}
	

}
