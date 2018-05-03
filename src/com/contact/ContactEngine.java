package com.contact;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.contact.service.IContactServiceImpl;

@Path("/contact")
public class ContactEngine {
	private static final String PING_SUCCESS_MSG = "connection is alive.";
	@GET
	public String getContactById() {
		String response = PING_SUCCESS_MSG;
		return response;
	}
	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContactById(@PathParam("id") String id) 
			throws IOException, ClassNotFoundException {
		System.out.println("[Server] get contact by id: " + id);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.getContactById(id);
		return response;
	}
	@GET
	@Path("/email/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContactByEmail(@PathParam("email") String email)
			throws IOException, ClassNotFoundException {
		System.out.println("[Server] get contact by email: " + email);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.getContactByEmail(email);
		return response;
	}
	@GET
	@Path("/number/{number}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContactByNumber(@PathParam("number") String number)
			throws IOException, ClassNotFoundException {
		System.out.println("[Server] get contact by number: " + number);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.getContactByNumber(number);
		return response;
		
	}
	@GET
	@Path("/state/{state}")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchContactsByState(@PathParam("state") String state)
			throws IOException, ClassNotFoundException {
		System.out.println("[Server] get contacts by state: " + state);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.getContactsByState(state);
		return response;
		
	}
	@GET
	@Path("/city/{city}")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchContactByCity(@PathParam("city") String city)
			throws IOException, ClassNotFoundException {
		System.out.println("[Server] get contacts by city: " + city);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.getContactsByCity(city);
		return response;
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON) 
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String createContact(@FormParam("action") String action, 
		      @FormParam("data") String data,
		      @Context HttpServletResponse servletResponse) throws ClassNotFoundException {
		System.out.println("[Server] create contact: " + action);
		System.out.println(data);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.createContact(data);
		return response;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON) 
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateContact(@FormParam("action") String action, 
		      @FormParam("data") String data,
		      @Context HttpServletResponse servletResponse) throws ClassNotFoundException {
		System.out.println("[Server] update contact: " + action);
		System.out.println(data);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.updateContact(data);
		return response;
	}
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteContact(@PathParam("id") String id) throws ClassNotFoundException {
		System.out.println("[Server] delete contact: " + id);
		IContactServiceImpl util = new IContactServiceImpl();
		String response = util.deleteContact(id);
		return response;
	}
}
