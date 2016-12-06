 function drawTable(tableName, colNames, tableData, tableID) {
	  var data = new google.visualization.DataTable();
	  var options = {showRowNumber: true, title: tableName,height: '550', width: '100%'};
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