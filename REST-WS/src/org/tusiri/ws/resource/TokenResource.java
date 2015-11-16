package org.tusiri.ws.resource;

import java.io.IOException;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.tusiri.ws.db.DBConnection;
import org.tusiri.ws.model.Token;



@Path("/token")
public class TokenResource {
	
	public static boolean isTokenUnique(Token token){
		boolean unique = true;
		DBConnection dbc = new DBConnection();
		Statement stmt = dbc.getDBStmt();
		try{
			String sql = "SELECT access_token FROM token "
					+ "WHERE access_token='"+token.access_token+"'";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				unique = false;
			}
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return unique;
	}
	
	
	public static Token generateToken(String email, String password){
		Token token = new Token();
		//Check if email and password match
		DBConnection dbc = new DBConnection();
		Statement stmt = dbc.getDBStmt();
		try{
			String sql = "SELECT * FROM user "
					+ "WHERE email='"+email+"' AND password=MD5('"+ password +"')";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if(rs.next()){
				int user_id = rs.getInt("id_user");
				final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
				Random rnd = new Random();

				final int length = 50;
				StringBuilder sb = new StringBuilder( 50 );
				for( int i = 0; i < 50; i++ ) 
					sb.append(AB.charAt(rnd.nextInt(AB.length())));
				String random = sb.toString();
				//Insert token to database
				token.access_token=random;

				java.util.Date dt = new java.util.Date();
				java.text.SimpleDateFormat sdf = 
				     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(dt);
				
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(currentTime));
				c.add(Calendar.DATE, 2);
				
				dt.setTime (dt.getTime() + (2 * 24 * 3600 * 1000));//2 hari
				
				String cookieExpire = "expires=" + dt.toGMTString();
				token.expire = cookieExpire;
				System.out.println(token.expire);
				while(!(isTokenUnique(token))){
					token = generateToken(email,password);
				}
				sql = "INSERT INTO token(access_token,id_user,timestamp) " +
						"VALUES('"+random+"',"+user_id+",'"+currentTime+"');";
				stmt.executeUpdate(sql);
				
				System.out.println("OK");
				System.out.println(random);
				
			}
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}		
		return token;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Token post(@FormParam("email") String email,
	@FormParam("password") String password) {
		Token token = generateToken(email,password);
		System.out.println(token.access_token);
		return token;
	}
} 