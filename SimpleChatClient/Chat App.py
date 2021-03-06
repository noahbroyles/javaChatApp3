import os, sys, time, getpass, RUNFIRST
import readline

print()

windows = RUNFIRST.Validate.isWindows()

# isVaild(ip) is not used in this program, but is still USEFUL
def isValidIp(ip):
    i = 0
    for char in ip:
        if char == ".":
            i += 1
    if i != 3:
        return False
    else:
        return True

def getPort():
    port = int(input("Enter the Chat-code/port number for the server: "))
    while port < 4000:
        port = int(input("Try again. The port number must be greater than 4000: "))
    return port



def getIp():
    print('The computer will now ask for the internet address of the chat server. This can be a DNS, IP Address, or hostname. It can either be local or over the internet. Push enter at the prompt if the chat server is being hosted on this computer.')
    # time.sleep(3)
    print()
    ip = input('Internet Address: ')
    if ip == '':
        ip = "127.0.0.1"
    return ip

def getUsername():
    userName = input('What do you want to be your user name? Spaces are not allowed: ')
    if userName == '':
        userName = getpass.getuser()
    elif userName.lower() == "you":
        print('Hey man, just so you know, your username can\'t be "You". Sorry. ')
        userName = getUsername()
    if ' ' in userName:
        print('Hey man, just so you know, your username can\'t have spaces in it. So don\'t freak out when it isn\'t the same as you said.')
    return userName


try:
    os.system("clear")
    print("Would you like to:\n[1] Connect to the Chat Server server on a different computer\n[2] or host the Chat Server on your computer?", end=' ')
    server = input()
    if server[0] == '2':
        portNumber = str(getPort())
        print('Okay. You are the host. To see what people are sending, run: \'cd classes; java SimpleChatClient 127.0.0.1 username ' + portNumber+'\'')
        if windows:
            os.system("cd classes/ && java VerySimpleChatServer " + portNumber)
        else:
            os.system("cd classes/;java VerySimpleChatServer "  + portNumber)
    elif server[0] == '1':
        ip = getIp()
        portNumber = str(getPort())
        userName = getUsername()
        print()
        print('Connecting to the chat server @ ' + ip + ":"+ portNumber,'using', userName,'as the User Name. Please Wait...')
        if windows:
            os.system("cd classes/ && java SimpleChatClient " + ip + " "+ userName + " " + portNumber)
        else:
            os.system("cd classes/;java SimpleChatClient " + ip + " "+ userName  + " " + portNumber)
except KeyboardInterrupt:
    sys.exit("\n")
