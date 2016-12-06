<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<br>

<c:forEach items="${streamsList}" var="i">
	<p>${i}</p>
</c:forEach>

<form method="post" action="/DBCollege/Stream">
    
  <p>Pick the options you want:</p>
  <br>
  	<input type="checkbox" name="stateIn" value="CA"/>Cali
  <br>
  <br>
  	<input type="checkbox" name="stateIn" value="GA"/>Georgia
  <br>
  <br>
  	<input type="checkbox" name="stateIn" value="FL"/>Florida
  <br>
  <br>
  <br>
  	<input type="checkbox" name="streamIn" value="Engineering"/>Engineering
  <br>
  <br>
  	<input type="checkbox" name="streamIn" value="Art"/>Art
  <br>
  <br>
  	<input type="checkbox" name="streamIn" value="Humanities"/>Humanities
  <br>
  <br>
  <input type="submit" value="Submit" />
</form>

<br>
</body>
</html>
