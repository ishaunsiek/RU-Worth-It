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
