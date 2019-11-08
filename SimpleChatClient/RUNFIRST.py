import os

class Validate:
    def isWindows():
        if os.path.exists("C:"):
            return True

if Validate.isWindows():
    os.system("cd source && javac -d ../classes/. *.java && cd ../classes && jar -cvmf manifest.txt SimpleChatClient.jar *")
else:
    os.system("cd source; javac -d ../classes/. *.java; cd ../classes; jar -cvmf manifest.txt SimpleChatClient.jar *")