package com.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		Client client = Client.create();
		String url="http://localhost:8080/RestServer/rest/getdetails";
		WebResource wb = client.resource(url);
		ClientResponse res = wb.accept("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : " + res.getStatus());
		}
		String rs = res.getEntity(String.class);
		System.out.println("client res " +rs);
		Map<String, Object> map = new HashMap<>();
		try {
			JSONObject jObject = new JSONObject(rs);
			Iterator<?> keys = jObject.keys();
			while( keys.hasNext() ){
	            String key = (String)keys.next();
	            if(jObject.get(key) instanceof String) {
	            	String value = jObject.getString(key); 
		            map.put(key, value);
	            } else if(jObject.get(key) instanceof JSONObject) {
	            	JSONObject childjObject = new JSONObject(jObject.get(key).toString());
	            	Iterator<?> Objkeys = childjObject.keys();
	            	Map<String, Object> submap = new HashMap<>();
	            	while( Objkeys.hasNext() ){
	            		String subkey = (String)Objkeys.next();
	            		String subvalue = childjObject.getString(subkey);
	            		submap.put(subkey, subvalue);
	            	}
	            	map.put(key, submap);
	            } else if(jObject.get(key) instanceof JSONArray) {
	            	JSONArray childArray = new JSONArray(jObject.get(key).toString());
	            	List<Map> list = new ArrayList<>();
	            	for (int i = 0; i < childArray.length(); i++) {
	            		JSONObject jsonobject = childArray.getJSONObject(i);
	            		Iterator<?> Objkeys = jsonobject.keys();
	            		Map<String, Object> submap = new HashMap<>();
	            		while( Objkeys.hasNext() ){
		            		String subkey = (String)Objkeys.next();
		            		String subvalue = jsonobject.getString(subkey);
		            		submap.put(subkey, subvalue);
		            	}
	            		list.add(submap);
	            	}
	            	map.put(key, list);
	            }
	        }
			System.out.println("json : "+jObject);
	        System.out.println("map : "+map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
