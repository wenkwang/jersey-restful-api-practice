# Assumptions: #

1. Contact records are stored in the service as a .json file\
2. Use jersey library to implement the RESTful API\
3. Use self-define response format for the client-server communication (JSON string)\
4. Haven't taken care of some of the client error (such as the empty input as the parameter)\
5. All contact information is stored as a string (validation and more proper data structures should be used in the real product)


# Package content: #

1. Solstice: contains the main source codes as a Java Dynamic Web Service project\
2. Solstice-challenge-test: java project to test the service\
3. solstice-challenge.war: service .war file\
4. solstice-challenge-test.jar: runnable .jar file for test\
5. contacts.json: contains the same initial contact records as one in the service (service does not use it)

# Instructions: #

1. Copy the solstice-challenge.war file into the web server application folder (if you use tomcat, it should be web apps under your tomcat installation directory)\
2. Start your web server(tomcat)\
3. You can use <localhost: port>/solstice-challenge/contact to ping the service, it will return "connection is alive." if success\
4. You can either test with your browser with the Get methods, or run the solstice-challenge-test.jar and follow the instructions


# Test with a browser or within test tool (solstice-challenge-test.jar) #

root url: <localhost:port>/solstice-challenge/contact

Retrieve a contact record by id:\
- url: <root url>/id/{id} | option: 4\
- sample ids:\
- "a208183f-5401-4a6d-8559-0dee7a174320"\
- "10af0341-1c48-420e-9820-722d548abfd4"\
- "4de74747-2d15-47e2-8501-d85e1d990d26"\

Retrieve a contact record by phone number:\
- url: <root url>/number/{number} | option: 5\
- sample numbers:\
- "+1 (872) 473-2818"\
- "+1 (811) 400-2056"\
- "+1 (840) 451-3753"\

Retrieve a contact record by email:\
- url: <root url>/email/{email} | option: 6\
- sample email:\
- "holdersuarez@pyramis.com"\
- "camposfrazier@isonus.com"\
- "jenningsbell@steeltab.com"\

Retrieve all contacts record by city:\
- url: <root url>/city/{city} | option: 7\
- sample cities:\
- "Washington"\
- "Winston"\
- "Rushford"\

Retrieve all contacts record by state:\
- url: <root url>/state/{state} | option: 8\
- sample states:\
- "Illinois"\
- "Nebraska"\
- "Kansas"\

# Test with test tool (solstice-challenge-test.jar) #

Create a contact\
- option: 1

Update a contact\
- option: 2

Delete a contact\
- option: 3

UnitTest for one endpoint (Retrieve contact by number):\
- option: 9
