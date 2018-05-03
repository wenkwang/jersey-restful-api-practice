package com.contact.service;

public interface IContactService {
	public String createContact(String jsonStr);
	public String updateContact(String jsonStr);
	public String deleteContact(String id);
	public String getContactById(String id);
	public String getContactByNumber(String number);
	public String getContactByEmail(String email);
	public String getContactsByState(String state);
	public String getContactsByCity(String city);
}
