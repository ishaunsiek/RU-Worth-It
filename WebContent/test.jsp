<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style>
.multiselect-container>li>a>label {
  padding: 4px 20px 3px 20px;
}
</style>
</head>
<body>

<form id="form1">

<div style="padding:20px">

<select id="chkveg" multiple="multiple">

<option value="cheese">Cheese</option>

<option value="tomatoes">Tomatoes</option>

<option value="mozarella">Mozzarella</option>

<option value="mushrooms">Mushrooms</option>

<option value="pepperoni">Pepperoni</option>

<option value="onions">Onions</option>

</select><br /><br />

<input type="button" id="btnget" value="Get Selected Values" />

</div>

</form>

</body>
</html>

<script>
$(function() {

	$('#chkveg').multiselect({

	includeSelectAllOption: true

	});

	$('#btnget').click(function() {

	alert($('#chkveg').val());

	})

	});
</script>