package org.tusiri.ws.resource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tusiri.ws.db.DBConnection;
import org.tusiri.ws.model.Token;

@Path("/token-validity")
public class TokenValidity {
	
	public static class Identity{
		public boolean valid=false;
		public int id_user=0;
	}
	
	public static Identity getIdentity(String access_token){
		Identity identity = new Identity();
		
		//Check database
		DBConnection dbc = new DBConnection();
		Statement stmt = dbc.getDBStmt();
		try{
			String sql = "SELECT * FROM token "
					+ "WHERE access_token='"+access_token+"'";
			System.out.print(sql);
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				//cek tanngal expire
				java.util.Date date = rs.getTimestamp("timestamp");
				Date expire = new Date(date.getTime() + TimeUnit.DAYS.toMillis( 2 ));//2 days validity
				long expire_ms = expire.getTime();
				Date cur_date = new Date();
				long cur_date_ms = cur_date.getTime();
				if(expire_ms>cur_date_ms){
					identity.valid = true;
					identity.id_user = rs.getInt("id_user");
				}
			}
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return identity;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Identity post(@FormParam("access_token") String access_token) {
		//Input: FORM
		//Output: JSON
		Identity identity = getIdentity(access_token);
		return identity;
	}
	
	@POST
	@Path("/getUserID")
	@Consumes("application/json")
	@Produces("application/json")
	public Identity getInJSON(String access_token) throws ParseException {
		//Input: JSON
		//Output: JSON
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(access_token);
		String token = (String) jsonObject.get("access_token");
		
		System.out.println("a_t = " + token);
		Identity identity = getIdentity(token);
		System.out.println("id_user = " + identity.id_user);
		return identity;
	}
}