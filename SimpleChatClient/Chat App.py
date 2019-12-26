import os, sys, time, getpass, RUNFIRST
print()

windys = RUNFIRST.Validate.isWindows()

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

def getIp():
    print('The computer will now ask for the internet address of the chat server. This can be a DNS, IP Address, or hostname. It can either be local or over the internet. Push enter at the prompt if the chat server is being hosted on this computer.')
    # time.sleep(3)
    print()
    ip = input('Internet Address: ')
    if ip == '':
        ip = "127.0.0.1"
    return ip


try:
    server = input("Would you like to:\n[1] Host the Chat Server on your computer\n[2] or connect to the Chat Server server on a different computer? ")
    print()
    if server[0] == '1':
        print('Okay. You are the host. To see what people are sending, run: \'cd classes; java SimpleChatClient 127.0.0.1 username\'')
        if windys:
            os.system("cd classes/ && java VerySimpleChatServer")
        else:
            os.system("cd classes/;java VerySimpleChatServer")
    elif server[0] == '2':
        ip = getIp()
        userName = input('What do you want to be your user name? Spaces are not allowed: ')
        if userName == '':
            userName = getpass.getuser()
        if ' ' in userName:
            print('Hey man, just so you know, your username can\'t have spaces in it. So don\'t freak out when it isn\'t the same as you said.')
        print()
        print('Connecting to the chat server @',ip,'using', userName,'as the User Name. Please Wait...')
        if windys:
            os.system("cd classes/ && java SimpleChatClient " + ip + " "+ userName)
        else:
            os.system("cd classes/;java SimpleChatClient " + ip + " "+ userName)
except KeyboardInterrupt:
    sys.exit()
