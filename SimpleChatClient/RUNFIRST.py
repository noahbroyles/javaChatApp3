import os

class Validate:
    def isWindows():
        if os.path.exists("C:"):
            return True
def main():
    if Validate.isWindows():
        os.system("cd source && javac -d ../classes/. *.java && cd ../classes && jar -cvmf manifest.txt SimpleChatClient.jar *")
    else:
        # Now I know that this works the other way, but better safe than sorry
        os.system("cd source; javac -d ../classes/. *.java; cd ../classes; jar -cvmf manifest.txt SimpleChatClient.jar *")

if __name__ == "__main__":
    main()