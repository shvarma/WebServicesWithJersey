package com;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ServicesWithJersey {

	public static void main(String args[]) throws URISyntaxException {
		ServicesWithJersey swj = new ServicesWithJersey();
		swj.testGetResponse();
	}
	
	public void jsonPathSample() throws URISyntaxException {
		String json = "{ data: { data2 : { value : 'hello'}}}";
		String hello = JsonPath.read(json, "$.data.data2.value");
		System.out.println(hello); //prints hello

	}

	public void testGetResponse() throws URISyntaxException {

		DefaultClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		URI userEndpoint = new URI("http://maps.google.com");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("address", "mumbai");
		queryParams.add("sensor", "false");

		WebResource webResource = client.resource(userEndpoint);

		String response = webResource.path("maps/api/geocode/json").queryParams(queryParams)
				.type(MediaType.APPLICATION_JSON_TYPE).get(String.class);

		System.out.println(response);
		JSONObject jsonObject;
		JSONArray jsonArray;
		try {
			jsonObject = new JSONObject(response);
			jsonArray = new JSONArray(jsonObject.get("results").toString());
			jsonObject = new JSONObject(jsonArray.get(0).toString());
			jsonArray = new JSONArray(jsonObject.get("address_components").toString());
			jsonObject = new JSONObject(jsonArray.get(0).toString());
//			System.out.println("City name searched in request : " + jsonObject.optString("long_name"));
//			System.out.println("City name searched in request : " + jsonObject.optString("short_name"));
//			System.out.println("City name searched in request : " + jsonObject.optString("types"));

		
//			String longName = JsonPath.read(jsonObject, "$.results[0].address_components[0].long_name").toString();
			String longName = JsonPath.read(jsonObject.toString(), "$.results[*].0.address_components[*].long_name").toString();

			System.out.println(longName);
			

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
