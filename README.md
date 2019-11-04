# javaChatApp3
A Java Chat App with a beautiful GUI to make things nicer.

This app is very lightweight and easy to use. It comes with a server and a client. The client will not work without the server running first.


## How to run:
#### If you have Python3 installed, run the chat app as follows:
First run `javac java/*.java`. Then run `python3 Chat\ App.py`. The app will then guide you through either running the chat sever or connecting to it. Before connecting to the server you must make sure that there is one running and that you know the IP address of the host machine.
After either setting up your own server or learning the IP of another, you can connect the client. The Python script will ask you what you want your username to be and what IP to find the sever at. After giving it that, you will be ready to chat!

#### If you do not have Python3:
First compile all the java code (incase you have a different version of java):
`javac java/*.java`
Start a server by typing `java -jar Server.jar`. Then to run the client make sure you are in the `java` folder and tpye `java SimpleChatClient <IP address of server> <username>`. 
If you get errors trying to run the Server jar, that is because you have a different version of Java than my *GOOD* one. If that happens, run the server as follows:
`java VerySimpleChatServer`


Pardon my ugly .DS_Stores. They do absolutly *NOTHING*.
