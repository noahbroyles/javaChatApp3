# javaChatApp3
A Java Chat App with a beautiful GUI to make things nicer.

This app is very lightweight and easy to use. It comes with a server and a client. The client will not work without the server running first.


## How to run:
#### If you have Python3 installed, run the chat app as follows:
First jun `python3 RUNFIRST.py`. This program will update all the java code to a consistent version with the one you have installed. 
Next run `python3 Chat\ App.py`. The app will then guide you through either running the chat sever or connecting to it. Before connecting to the server you must make sure that there is one running and that you know the IP address of the host machine.
After either setting up your own server or learning the IP of another, you can connect the client. The Python script will ask you what you want your username to be and what IP to find the sever at. After giving it that, you will be ready to chat!

#### If you do not have Python3:
First run this fat juicy command:
`cd source; javac -d ../classes/. *.java; cd ../classes; jar -cvmf manifest.txt SimpleChatClient.jar *`
This will recomile everything and get your version of java ready for binness.
Start a server by typing `cd classes` and then `java -jar Server.jar`. Then to run the client open a new terminal / command prompt window, make sure you are in the `classes` folder, and type `java SimpleChatClient <IP address of server> <username>`. 
If you get errors trying to run the Server jar, that is because you have a different version of Java than my *GOOD* one. If that happens, run the server as follows:
`java VerySimpleChatServer`

Pardon my ugly .DS_Stores. They do absolutly *NOTHING*.