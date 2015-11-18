<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.io.*"%>
<%@ page import = "org.tusiri.ws.answer.AnswerService" %>
<%@ page import = "org.tusiri.ws.answer.Answer" %>
<%@ page import = "org.tusiri.ws.answer.AnswerItem" %>
<html>
<head>
<title>Inser Title Here</title>
</head>
<body>

<%
Cookie cookie = null;
Cookie[] cookies = null;
String access_token = null;
// Get an array of Cookies associated with this domain
cookies = request.getCookies();
if( cookies != null ){
	for (int i = 0; i < cookies.length; i++){
		cookie = cookies[i];
		if(cookie.getName().equals("access_token")){
			access_token = cookie.getValue();
			break;
		}
	}
} else {
	//Redirect to signin
}

AnswerService qservice = new AnswerService();
Answer a = qservice.getAnswerPort();
int result = a.createAnswer(access_token, 50, request.getParameter("content"));
if(result>0){//success
	String site = new String("question.jsp?id="+50);
	response.setStatus(response.SC_MOVED_TEMPORARILY);
	response.setHeader("Location", site); 
}


%>
<%= result %>
PADA TES INI, QUESTION ID 50
<center>
<ul>
<li><p><b>First Name:</b>
   <%= request.getParameter("topic")%>
</p></li>
<li><p><b>Last  Name:</b>
   <%= request.getParameter("content")%>
</p></li>
</ul>
</body>
</html>