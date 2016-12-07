

import java.io.IOException;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;

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
//			System.out.println("HI");
			String url = "jdbc:mysql://cs336.c7r2hjhvlcff.us-east-1.rds.amazonaws.com:3306/my_instance";
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection(url, "root", "password");
			Statement stmt = (Statement) con.createStatement();
			ResultSet res; 
			
			String[] streamIn;
			//String[] stIn;
			String q = "";
			/*
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
			*/
			
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
			String[][] streamChartTable = new String[newcount][5];
			while(res.next() != false){
				streamTable[c][0] = res.getString(1).replaceAll("[-+.^:,']","");
				streamTable[c][1] = "" + res.getInt(2);
				streamTable[c][2] = res.getString(3).replaceAll("[-+.^:,']","");
				streamTable[c][3] = "" + res.getInt(4);
				streamTable[c][4] = res.getString(5).replaceAll("[-+.^:,']","");
				
				streamChartTable[c][0] = res.getString(3).replaceAll("[-+.^:,']","");
				streamChartTable[c][1] = "" + res.getInt(4);
				streamChartTable[c][2] = res.getString(5).replaceAll("[-+.^:,']","");
				streamChartTable[c][3] = res.getString(1).replaceAll("[-+.^:,']","");
				streamChartTable[c][4] = streamChartTable[c][1];
				c++;
			}
			/*
			for(int i = 0; i < streamTable.length; i++){
				System.out.println(streamTable[i][0]+" "+streamTable[i][1]+" "+streamTable[i][2]+" "+streamTable[i][3]+" "+streamTable[i][4]);
			}
			*/
			
			request.setAttribute("streamTable", streamTable);
			String stateminmax = "select state, min(early), max(early), min(mid), max(mid), min(net_price), max(net_price) "
					+ "from IpedSchool "
					+ "group by state ";
			
			ResultSet smm = stmt.executeQuery(stateminmax);
			newcount = 0;
			while(smm.next() != false)
			{
				newcount++;
			}
			c = 0;
			smm.beforeFirst();
			HashMap<String, String[]> stateMinMaxMap = new HashMap<String, String[]>();
			String[] stateStats = new String[6];
			String[][] stateMinMaxTable = new String[newcount][7];
			while(smm.next() != false){
				stateStats[0] = smm.getString(2);	//early
				stateStats[1] = smm.getString(3);
				stateStats[2] = smm.getString(4);	//mid	
				stateStats[3] = smm.getString(5);	
				stateStats[4] = smm.getString(6);	//tuition
				stateStats[5] = smm.getString(7);
				System.out.println(stateStats);
//				stateMinMaxTable[c][0] = smm.getString(1).replaceAll("[-+.^:,']","");
//				stateMinMaxTable[c][1] = "" + smm.getInt(2); //early
//				stateMinMaxTable[c][2] = "" + smm.getInt(3);
//				stateMinMaxTable[c][3] = "" + smm.getInt(4); //mid
//				stateMinMaxTable[c][4] = "" + smm.getInt(5);
//				stateMinMaxTable[c][5] = "" + smm.getInt(6); //tuition
//				stateMinMaxTable[c][6] = "" + smm.getInt(7);
				stateMinMaxMap.put(smm.getString(1).replaceAll("[-+.^:,']",""), stateStats);
			}
//			printTableArray(stateMinMaxTable);
			
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
					ps.setString(x+1, stateIn[x]);
				}
				if(request.getParameterValues("streamIn") != null){
					streamIn = request.getParameterValues("streamIn");
					for(int i = x; i < streamIn.length + x; i++){
						ps.setString(i+1, streamIn[i - x]);
					}
				}
				res = ps.executeQuery();
			}
			
			
			newcount = 0;
			while(res.next() != false)
			{
				newcount++;
			}
			c = 0;
			res.beforeFirst();
			String[][] collegeTable = new String[newcount][8];
			String[][] collegeChartTable = new String[newcount][5];
			while(res.next() != false){
				collegeTable[c][0] = res.getString(1).replaceAll("[-+.^:,']","");
				collegeTable[c][1] = res.getString(2).replaceAll("[-+.^:,']","");
				collegeTable[c][2] = "" + res.getInt(3);
				collegeTable[c][3] = res.getString(4).replaceAll("[-+.^:,']","");
				collegeTable[c][4] = "" + res.getInt(5);
				collegeTable[c][5] = "" + res.getInt(6);
				collegeTable[c][6] = "" + res.getInt(7);
				collegeTable[c][7] = "" + (int) (((double)res.getInt(7))/((double)res.getInt(5))*100);
				collegeChartTable[c][0] = res.getString(4).replaceAll("[-+.^:,']","");
				stateStats = stateMinMaxMap.get(collegeTable[c][0]);
				System.out.println(collegeTable[c][0]);
				System.out.println("{ ");
				for(int l=0;l<stateStats.length;l++){
					System.out.print(stateStats[l] + ", ");
				}
				System.out.print(" }");
//				collegeChartTable[c][1] = "" + res.getInt(7);
//				collegeChartTable[c][2] = "" + res.getInt(7);
//				collegeChartTable[c][3] = "" + res.getInt(5);
//				collegeChartTable[c][4] = "" + res.getInt(5);
				collegeChartTable[c][1] = stateStats[4];
				collegeChartTable[c][2] = "" + res.getInt(7);
				collegeChartTable[c][3] = "" + res.getInt(5);
				collegeChartTable[c][4] = stateStats[2];
				c++;
				
			}
			
			
			request.setAttribute("collegeTable", collegeTable);
			
//			System.out.println("\n\nSTREAM TABLE==================================================");
//			printTableArray(streamTable);
//			System.out.println("\n\nCOLLEGE TABLE=================================================");
//			printTableArray(collegeTable);
//			System.out.println("\n\nSTREAM CHART TABLE=================================================");
//			printTableArray(streamChartTable);
			HashMap<String, String[][]> responseMap = new HashMap<String, String[][]>();
			String[][] collegesHeader = {{"State", "Stream", "Rank", "College name", "Early-career salary ($)", "Mid-career salary ($)", "Net tuition($)", "Tuition to Early Salary Ratio (%)"}};
			responseMap.put("CollegesTableHeader", collegesHeader);
			responseMap.put("CollegesTable", collegeTable);
			String[][] collegesChartHeader = {{"College Name", "Lowest tuition in state - Institution tuition; Starting salary for institution graduates - Highest starting salary in state", "Net Tuition($)", "Early-career Salary ($)", "Max Early-career Salary"}};
			responseMap.put("CollegesChartHeader", collegesChartHeader);
			responseMap.put("CollegesChartTable", collegeChartTable);
//			String[][] stateMinMaxHeader = {{"State", "Minimum Early-career Salary", "Maximum Early-career Salary", "Minimum Mid-career Salary", "Maximum Mid-career Salary", "Minimum Tuition Cost", "Maximum Tuition Cost"}};
//			responseMap.put("StateMinMaxHeader", stateMinMaxHeader);
//			responseMap.put("StateMinMaxTable", stateMinMaxTable);
			String[][] streamsHeader = {{"Stream", "Rank", "Job title", "Mid-career salary ($)", "Most common major"}};
			responseMap.put("StreamsTableHeader", streamsHeader);
			responseMap.put("StreamsTable", streamTable);
			String[][] streamChartHeader = {{"ID", "Mid-Career Salary ($)", "Most Common Major for Job", "Stream", "Mid-Career Salary"}};
			responseMap.put("StreamsChartHeader", streamChartHeader);
			responseMap.put("StreamsChartTable", streamChartTable);
			String jsonOut = new Gson().toJson(responseMap);
//			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/table.jsp");
//			RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher(responseMap);
//			RequetsDispatcherObj.forward(request, response);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonOut);
			
			res.close();
			stmt.close();
			con.close();
			
			System.out.print("Success in Index");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.print("failed in Index");
			
		}
	}
	

}
