package org.tusiri.ws.answer;
import javax.jws.WebMethod;
import javax.jws.WebService;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.sql.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tusiri.ws.db.DBConnection;
import org.tusiri.ws.question.QuestionItem;

public class Answer {
	
	
	public int createAnswer(String access_token,int id_question, String content) throws ClientProtocolException, IOException, ParseException{
		int status = 0;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(
				"http://localhost:8080/REST-WS/rest/token-validity/getUserID");
			StringEntity input = new StringEntity("{\"access_token\":\""+access_token+"\"}");
			input.setContentType("application/json");
			postRequest.setEntity(input);
			HttpResponse response = httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
			String output;
			boolean isTokenValid=false;
			int id_user;
			if ((output = br.readLine()) != null) {
				System.out.println(output);
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(output);
				isTokenValid = (boolean) jsonObject.get("valid");
				long id_user_long = (long) jsonObject.get("id_user");
				id_user = (int) id_user_long; //bahaya, tapi asumsi ga ada angka yang besar
				
				System.out.println(id_user);
				if(isTokenValid){
					//Masukkan ke database
					DBConnection dbc = new DBConnection();
					Connection conn = dbc.getConn();
					PreparedStatement stmt = dbc.getDBStmt();
					try{
						String sql = "INSERT INTO answer(id_question,id_user,content,answer_date,num_votes)"
								+ "VALUES(?,?,?,?,?)";
						stmt = conn.prepareStatement(sql);
						stmt.setInt(1, id_question);
						stmt.setInt(2, id_user);
						stmt.setString(3, content);
						java.util.Date dt = new java.util.Date();
						java.text.SimpleDateFormat sdf = 
						     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentTime = sdf.format(dt);
						stmt.setString(4, currentTime);
						stmt.setInt(5, 0);
						status = stmt.executeUpdate();
						ResultSet rs = stmt.getGeneratedKeys();
			            while (rs.next()) {
			            	status = rs.getInt(1);
			            }
						//res = 1;
					} catch(SQLException se){
						//Handle errors for JDBC
						se.printStackTrace();
					} catch(Exception e){
						//Handle errors for Class.forName
						e.printStackTrace();
					}
				}
			}
			httpClient.getConnectionManager().shutdown();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}
	
	@WebMethod
	public ArrayList<AnswerItem> getAnswerList(int id_question) {
		ArrayList<AnswerItem> questionItemList = new ArrayList<AnswerItem>();
		DBConnection dbc = new DBConnection();
		Statement stmt = dbc.getDBStmt();
		try{
			String sql = "SELECT * FROM answer WHERE id_question="+id_question;
			ResultSet rs = stmt.executeQuery(sql);
			
			// Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				
				int num_answer = rs.getInt("num_answers");
				int id_user = rs.getInt("id_user");
				String content = rs.getString("content");
				String answer_date = rs.getString("date");
				int num_votes = rs.getInt("num_votes");
				
				AnswerItem q = new AnswerItem();
				questionItemList.add(q);
			}
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return questionItemList;
	}
}
