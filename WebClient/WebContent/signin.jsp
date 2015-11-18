<!DOCTYPE html>
<html>
    <head>
        <title>Sign In</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
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
		
		if((access_token != null) && (access_token.length()>0)){
			//check access_token validity to server
%>
		
		<script>
		function checkToken(){
			var tokenData = {access_token:"<%= access_token %>"}
			var checkTokenUrl = "http://localhost:8080/REST-WS/rest/token-validity";
			$.ajax({
                url: checkTokenUrl,
                data: tokenData,
                dataType: "json",
                type: "POST",
                success: function(data) {
                    var valid = data.valid;
                    if(valid)
                    	window.location.href = "index.jsp";
                },
                error: function(jqxhr, status, errorMsg) {
                    alert(status + ": " + errorMsg);
                }
            });
		}
		$(document).ready(function(){
		    checkToken();
		});
		</script>
		
		
		<%
	}
      
  }else{
      out.println("<h2>No cookies founds</h2>");
  }
%>
        <script>
            $(document).ready(function(){
                var url = "http://localhost:8080/REST-WS/rest/token";
                $("#submitBtn").click(function(e) {
                    e.preventDefault();
                    var formData = $("#loginForm").serialize();
                    $.ajax({
                        url: url,
                        data: formData,
                        dataType: "json",
                        type: "POST",
                        success: function(data) {
                            var token = data.access_token;
                            document.cookie="access_token="+token+"; expires="+data.expire;
                            if(token == null)
                            	$(".error").replaceWith( "Username and password not match" );
                            else
                            	window.location.href = "index.jsp";

                        },
                        error: function(jqxhr, status, errorMsg) {
                            alert(status + ": " + errorMsg);
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <div id="content">
        	<span class="error"></span>
            <form id="loginForm">
                Email: <input type="text" name="email"/><br/>
                Password : <input type="text" name="password"/><br/>
                <button id="submitBtn">Submit</button>
            </form>
        </div>
    </body>
</html>