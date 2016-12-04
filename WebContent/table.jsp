<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Table</title>
</head>
<body>

<div id="table_div_stream"></div>
<br><br>
<div id="table_div_college"></div>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['table']});
      google.charts.setOnLoadCallback(drawTableStream);
      google.charts.setOnLoadCallback(drawTableCollege);

      var options;
      //options = {'pageSize': 50};
      options = {showRowNumber: true, height: '100%', width: '100%'};
      function drawTableStream() {
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'stream');
          data.addColumn('string', 'rank');
          data.addColumn('string', 'job');
          data.addColumn('string', 'mid');
          data.addColumn('string', 'common_major');
          
          data.addRows([
			<c:forEach items="${streamTable}" var="i">
				['${i[0]}', '${i[1]}', '${i[2]}', '${i[3]}', '${i[4]}'],
			</c:forEach>
          ]);
          //var container = document.getElementById('table_div');
          //var tableQueryWrapper = new TableQueryWrapper(data, container, options);
          var table = new google.visualization.Table(document.getElementById('table_div_stream'));

          table.draw(data, options);
        }
      
      	function drawTableCollege() {
          var data = new google.visualization.DataTable();
          data.addColumn('string', 'state');
          data.addColumn('string', 'stream');
          data.addColumn('string', 'rank');
          data.addColumn('string', 'college');
          data.addColumn('string', 'early');
          data.addColumn('string', 'mid');
          data.addColumn('string', 'net_price');
              
          data.addRows([
    	  	<c:forEach items="${collegeTable}" var="i">
    			['${i[0]}', '${i[1]}', '${i[2]}', '${i[3]}', '${i[4]}', '${i[5]}', '${i[6]}'],
    	  	</c:forEach>
          ]);
             
          var table = new google.visualization.Table(document.getElementById('table_div_college'));

          table.draw(data, options);
         }
      
</script>

</body>
</html>