import os
os.system("cd source; javac -d ../classes/. *.java; cd ../classes; jar -cvmf manifest.txt SimpleChatClient.jar *")