 function drawTable(tableName, colNames, tableData, tableID) {
	  var data = new google.visualization.DataTable();
	  var options = {showRowNumber: true, title: tableName,height: '550px', width: '100%'};
	  console.log(tableData);
	  var i = 0;
	  for(i = 0; i<colNames.length; i++){
		  data.addColumn('string', colNames[i]);
	  }
	  for(i = 0; i<tableData.length; i++){
		  data.addRow(tableData[i]);
	  }
	
	  var table = new google.visualization.Table(document.getElementById(tableID));
	  table.draw(data, options);
}
 
function drawBubbleChart(tableName, colNames, tableData, tableID){
	  var data = new google.visualization.DataTable();
	  var options = {
		        title: 'Mid-career Salaries for Jobs',
		        hAxis: {title: 'Most Common Major for Job'},
		        vAxis: {title: 'Mid-career Salary ($)'},
		        bubble: {textStyle: {fontSize: 11}}
		      };
	  console.log(tableData);
	  var i = 0;
	  for(i = 0; i<colNames.length; i++){
		  if(i == 0){
			  data.addColumn('string', colNames[i]);
		  }else{
			  data.addColumn('number', colNames[i]);
		  }
	  }
	  for(i = 0; i<tableData.length; i++){
		  data.addRow(tableData[i]);
	  }
	
	  var chart = new google.visualization.BubbleChart(document.getElementById(tableID));
	  chart.draw(data, options);
}

function drawCandlestickChart(tableName, colNames, tableData, tableID){
	  var data = new google.visualization.DataTable();
	  var options = {
		        title: 'Difference between Yearly Early-career Salary and Yearly Tuition',
		        hAxis: {title: 'US Dollars ($)'},
		        vAxis: {title: 'Colleges'}
		      };
	  console.log(tableData);
	  var i = 0;
	  var j = 0;
	  var row = [];
	  for(i = 0; i<colNames.length; i++){
		  if(i == 0){
			  data.addColumn('string', colNames[i]);
		  }else{
			  data.addColumn('number', colNames[i]);
		  }
	  }
	  for(i = 0; i<tableData.length; i++){
		  for(j = 0; j<tableData[i].length; j++){
			  if(j == 0){
				  row.push(tableData[i][j]);
			  }else{
				  row.push(parseInt(tableData[i][j], 10));
			  }
		  }
		  console.log(row);
		  data.addRow(row);
		  row = [];
	  }
	  
	  console.log(tableData);
//	  data =google.visualization.arrayToDataTable(tableData);
//	  
	  var chart = new google.visualization.CandlestickChart(document.getElementById(tableID));
	  chart.draw(data, options);
}