<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.io.*"%>
<%@ page import = "org.tusiri.ws.answer.AnswerService" %>
<%@ page import = "org.tusiri.ws.answer.Answer" %>
<%@ page import = "org.tusiri.ws.answer.AnswerItem" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<%
AnswerService aService = new AnswerService();
Answer a = aService.getAnswerPort();
List<AnswerItem> answerList = a.getAnswerList(49);
int n = answerList.size();
%>
Total jumlah pertanyaan: <%= n %></br>

Pada contoh ini, hanya answer dari question dengan question_id = 49 yang ditampilkan
<p>
<%  if(n > 0) {
		for(int i=0;i<n;i++){%>
Question ke-<%= i %> Isinya</br>
Id Question: <%= answerList.get(i).getIdQuestion() %></br>
Content: <%= answerList.get(i).getContent() %></br>
Username: <%= answerList.get(i).getUsername() %></br>
</br>
<% 	
		}
	} %>
</p>
</body>
</html>