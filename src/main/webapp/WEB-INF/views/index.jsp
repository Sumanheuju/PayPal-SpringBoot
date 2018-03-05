<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Online Home Page</title>
</head>
<body>
	<h1>Online Home Page</h1>
	<div>
	
		<c:forEach var="product" items="${products}">
			<h2>${product.name}</h2>
			<p>${product.description}</p>
			<h3><b>${product.price}</b></h3>
		
		
		<form action="pay/${product.price}" method="post">
			
			<button type="submit"><img src="/image/paypal-payments.png" style="width:80px;height:50px;"></button>
			
		</form>
		
		</c:forEach>
	</div>
	
</body>
</html>