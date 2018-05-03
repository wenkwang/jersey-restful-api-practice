package com.contact.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.contact.model.*;

public class IContactServiceImpl implements IContactService{
	//	status codes
	private final static String STATUS_CODE_SUCCESS = "200";
	private final static String STATUS_CODE_CREATED = "201";
	private final static String STATUS_CODE_NOTFOUND = "204";
	private final static String STATUS_CODE_FAILURE = "500";

	//	response message
	private final static String SUCCESS_MSG = "success";
	private final static String FAILURE_MSG = "failure";
	private final static String NOT_FOUND_MSG = "not found";
	
	//	other constants
	private final static String CLASS_NAME = "com.contact.service.IContactServiceImpl";
	private final static String DATA_FILE = "contacts.json";
	
	//	Use hashmap to store all the contact records. <(String) id, (Contact) contact>
	private Map<String, Contact> contactsMap;
	//	The data file path string
	private String dataFile;
	
	// The constructor, and retrieve data file path and initialize the contact map
	public IContactServiceImpl() throws ClassNotFoundException {
		// Search the data file resource and retrieve the file path.
		Class cls = Class.forName(CLASS_NAME);
		ClassLoader cLoader = cls.getClassLoader();
	    URL url = cLoader.getResource(DATA_FILE);
		this.dataFile = url.toString().substring(5);
		
		try {
			initializeContactRecords(this.dataFile);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}
	// Convert the data in .json file into contact map
	private void initializeContactRecords(String dataFile)
			throws ParseException, FileNotFoundException, IOException, ClassNotFoundException {
		this.contactsMap = new HashMap<String, Contact>();
		JSONParser parser = new JSONParser();
		JSONArray contacts = (JSONArray) parser.parse(new FileReader(this.dataFile));
		for (Object obj : contacts) {
			JSONObject record = (JSONObject) obj;
			Contact contact = convertJSONToContact(record);
			this.contactsMap.put(contact.getId(), contact);
		}
		return;
	}
	
	// Convert a JSONObject into a Contact object
	private Contact convertJSONToContact(JSONObject record) {
		String id = (String) record.get("id");
		Contact contact = new Contact(id);
		JSONObject name = (JSONObject) record.get("name");
		contact.setFirstName((String) name.get("first"));
		contact.setLastName((String) name.get("last"));
		contact.setCompany((String) record.get("company"));
		contact.setProfileImage((String) record.get("profile_image"));
		contact.setEmail((String) record.get("email"));
		contact.setBirthdate((String) record.get("birthday"));
		JSONObject number = (JSONObject) record.get("number");
		contact.setWorkNumber((String) number.get("work"));
		contact.setPersonalNumber((String) number.get("personal"));
		JSONObject address = (JSONObject) record.get("address");
		contact.setStreet((String) address.get("street"));
		contact.setCity((String) address.get("city"));
		contact.setState((String) address.get("state"));
		contact.setZip((String) address.get("zip"));
		return contact;
	}
	
	// Convert a Contact object into a JSONObject
	private JSONObject convertContactToJSON(Contact contact) {
		JSONObject record = new JSONObject();
		JSONObject name = new JSONObject();
		record.put("id", contact.getId());
		name.put("first", contact.getFirstName());
		name.put("last", contact.getLastName());
		record.put("name", name);
		record.put("company", contact.getCompany());
		record.put("profile_image", contact.getProfileImage());
		record.put("email",	contact.getEmail());
		record.put("birthday", contact.getBirthdate());
		JSONObject number = new JSONObject();
		number.put("work", contact.getWorkNumber());
		number.put("personal", contact.getPersonalNumber());
		record.put("number", number);
		JSONObject address = new JSONObject();
		address.put("street", contact.getStreet());
		address.put("city", contact.getCity());
		address.put("state", contact.getState());
		address.put("zip", contact.getZip());
		record.put("address", address);
		return record;
		
	}
	
	// Insert one Contact object into the map
	private boolean insertContact(String id, Contact contact) {
		if (contact == null) {
			return false;
		}
		if (this.contactsMap.isEmpty()) {
			return false;
		}
		if (this.contactsMap.containsKey(id)) {
			return false;
		}
		this.contactsMap.put(id, contact);
		return true;
	}
	
	// Generate a new UUID
	private String getNewId() {
		String id = UUID.randomUUID().toString();
		return id;
	}
	
	// Update the data file on disk
	private void updateDataFile() throws IOException {
		JSONArray jsonArray = new JSONArray();
		for (Contact contact: this.contactsMap.values()) {
			JSONObject jsonObj = convertContactToJSON(contact);
			jsonArray.add(jsonObj);
		}
		FileWriter file = new FileWriter(this.dataFile);
		file.write(jsonArray.toString());
		file.flush();
		file.close();
		return;
	}

	@Override
	public String createContact(String jsonStr) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_FAILURE);
		response.put("message", FAILURE_MSG);
		try {
			JSONParser parser = new JSONParser();
			JSONObject record;
			record = (JSONObject) parser.parse(jsonStr);
			String id = getNewId();
			record.put("id", id);
			Contact contact = convertJSONToContact(record);
			if (!insertContact(contact.getId(), contact)) {
				return response.toString();
			}
			updateDataFile();
			response.put("status", STATUS_CODE_CREATED);
			response.put("message", SUCCESS_MSG);
			response.put("id", id);
		} catch (ParseException | IOException e1) {
			e1.printStackTrace();
		}
		return response.toString();
	}

	@Override
	public String updateContact(String jsonStr) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_FAILURE);
		response.put("message", FAILURE_MSG);
		try {
			JSONParser parser = new JSONParser();
			JSONObject record;
			record = (JSONObject) parser.parse(jsonStr);
			// Quit if the map is empty.
			if (this.contactsMap.isEmpty()) {
				return response.toString();
			}
			String id = (String) record.get("id");
			// Quit if the map doesn't have the record.
			if (!this.contactsMap.containsKey(id)) {
				return response.toString();
			}
			Contact contact = convertJSONToContact(record);
			this.contactsMap.put(contact.getId(), contact);
			updateDataFile();
			response.put("status", STATUS_CODE_SUCCESS);
			response.put("message", SUCCESS_MSG);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	@Override
	public String deleteContact(String id) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_FAILURE);
		response.put("message", FAILURE_MSG);
		// Quit if the map is empty.
		if (this.contactsMap.isEmpty()) {
			return response.toString();
		}
		// Quit if the map doesn't have the record.
		if (!this.contactsMap.containsKey(id)) {
			return response.toString();
		}
		this.contactsMap.remove(id);
		try {
			updateDataFile();
			response.put("status", STATUS_CODE_SUCCESS);
			response.put("message", SUCCESS_MSG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}

	@Override
	public String getContactById(String id) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_NOTFOUND);
		response.put("message", NOT_FOUND_MSG);
		// Quit if the map is empty.
		if (this.contactsMap.isEmpty()) {
			return response.toString();
		}
		// Quit if the map doesn't have the record.
		if (!this.contactsMap.containsKey(id)) {
			return response.toString();
		}
		response.put("status", STATUS_CODE_SUCCESS);
		response.put("message", SUCCESS_MSG);
		Contact contact = this.contactsMap.get(id);
		JSONObject record = convertContactToJSON(contact);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(record);
		response.put("data", jsonArray);
		return response.toString();
	}

	@Override
	public String getContactByNumber(String number) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_NOTFOUND);
		response.put("message", NOT_FOUND_MSG);
		if (this.contactsMap.isEmpty()) {
			return response.toString();
		}
		JSONObject record = null;
		Map<String, Contact> map = this.contactsMap;
		// Search in the map and break the loop if found.
		for (String id: map.keySet()) {
			Contact contact = map.get(id);
			if (contact.getWorkNumber().equals(number)
					|| contact.getPersonalNumber().equals(number)) {
				record = convertContactToJSON(contact);
			}
		}
		// No match found.
		if (record == null) {
			return response.toString();
		}
		response.put("status", STATUS_CODE_SUCCESS);
		response.put("message", SUCCESS_MSG);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(record);
		response.put("data", jsonArray);
		return response.toString();
	}

	@Override
	public String getContactByEmail(String email) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_NOTFOUND);
		response.put("message", NOT_FOUND_MSG);
		if (this.contactsMap.isEmpty()) {
			return null;
		}
		JSONObject record = null;
		Map<String, Contact> map = this.contactsMap;
		// Search in the map and break the loop if found.
		for (String id: map.keySet()) {
			Contact contact = map.get(id);
			if (contact.getEmail().equalsIgnoreCase(email)) {
				record = convertContactToJSON(contact);
				break;
			}
		}
		// No match found.
		if (record == null) {
			return response.toString();
		}
		response.put("status", STATUS_CODE_SUCCESS);
		response.put("message", SUCCESS_MSG);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(record);
		response.put("data", jsonArray);
		return response.toString();
	}

	@Override
	public String getContactsByState(String state) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_NOTFOUND);
		response.put("message", NOT_FOUND_MSG);
		if (this.contactsMap.isEmpty()) {
			return null;
		}
		JSONArray jsonArray = new JSONArray();
		Map<String, Contact> map = this.contactsMap;
		for (String id: map.keySet()) {
			Contact contact = map.get(id);
			if (contact.getState().equalsIgnoreCase(state)) {
				JSONObject record = convertContactToJSON(contact);
				jsonArray.add(record);
			}
		}
		// No match found.
		if (jsonArray.size() == 0) {
			return response.toString();
		}
		response.put("status", STATUS_CODE_SUCCESS);
		response.put("message", SUCCESS_MSG);
		response.put("data", jsonArray);
		return response.toString();
	}

	@Override
	public String getContactsByCity(String city) {
		JSONObject response = new JSONObject();
		response.put("status", STATUS_CODE_NOTFOUND);
		response.put("message", NOT_FOUND_MSG);
		if (this.contactsMap.isEmpty()) {
			return null;
		}
		JSONArray jsonArray = new JSONArray();
		Map<String, Contact> map = this.contactsMap;
		for (String id: map.keySet()) {
			Contact contact = map.get(id);
			if (contact.getCity().equalsIgnoreCase(city)) {
				JSONObject record = convertContactToJSON(contact);
				jsonArray.add(record);
			}
		}
		// No match found.
		if (jsonArray.size() == 0) {
			return response.toString();
		}
		response.put("status", STATUS_CODE_SUCCESS);
		response.put("message", SUCCESS_MSG);
		response.put("data", jsonArray);
		return response.toString();
	}

}
