<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.io.*"%>
<%@ page import = "org.tusiri.ws.question.QuestionService" %>
<%@ page import = "org.tusiri.ws.question.Question" %>
<%@ page import = "org.tusiri.ws.question.QuestionItem" %>
<html>
<head>
<title>Using GET and POST Method to Read Form Data</title>
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

QuestionService qservice = new QuestionService();
Question q = qservice.getQuestionPort();
int result = q.deleteQuestion(access_token, 52);
if(result>0){//success
	String site = new String("question?id="+result);
	response.setStatus(response.SC_MOVED_TEMPORARILY);
	response.setHeader("Location", site); 
}


%>
<%= result %>
<center>
Berhasil menghapus question dengan id
   <%= 52%>
</body>
</html>