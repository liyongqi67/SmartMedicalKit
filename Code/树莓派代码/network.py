import socket
import main
import re
import medicine
import schedule


class Network(object):
    # IP = '121.250.216.26'
    # PORT = 8889
    IP = '192.168.1.156'
    #IP = '192.168.1.130'
    PORT = 8889
    def __init__(self):
        pass

    def getMedicineList(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((Network.IP, Network.PORT))
        s.send(main.Main.DEVICE_CODE.encode('gbk')+';flash;\n'.encode('gbk'))
        buffer = []
        while True:

            d = s.recv(1024)
            if d:
                buffer.append(d)
            else:
                break
        data = b''.join(buffer)

        s.close()

        result = data.decode('gbk')
        list1 = re.split('\;',result)
        list2 = []
        for i in range(len(list1)-1):
            list3 = re.split('-',list1[i])
            print(list3)
            med = medicine.Medicine(list3[0], list3[1], list3[2], list3[3], list3[4], list3[5], list3[6], list3[7], list3[8], list3[9], list3[10],list3[11], list3[12], list3[13])
            list2.append(med)

        return list2

    def creat_by_code(self,code, manufacture = 'none', shelf = 'none', remaining = 'none'):
        if(isinstance(code, str) & isinstance(manufacture, str) & isinstance(shelf, str)& isinstance(remaining, str)):
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((Network.IP, Network.PORT))
            s.send(main.Main.DEVICE_CODE.encode('gbk') + ';n;'.encode('gbk') + code.encode('gbk') + '-'.encode('gbk') + manufacture.encode('gbk') + '-'.encode('gbk') + shelf.encode('gbk')+ '-'.encode('gbk') + remaining.encode('gbk') + ';\n'.encode('gbk'))
            buffer = []
            while True:

                d = s.recv(1024)
                if d:
                    buffer.append(d)
                else:
                    break
            data = b''.join(buffer)

            s.close()

            result = data.decode('gbk')

            return result

        else:
            raise ValueError('bad value!please check the enter！')

    def modify(self,code,name, manufacture = '', shelf = '',remaining = ''):
        if (isinstance(code, str) & isinstance(manufacture, str) & isinstance(shelf, str) & isinstance(remaining, str)):

            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.connect((Network.IP, Network.PORT))

            s.send(main.Main.DEVICE_CODE.encode('gbk') + ';m;'.encode('gbk')
                   + code.encode('gbk') + '-'.encode('gbk')+name.encode('gbk')+ '-'.encode('gbk')+ manufacture.encode('gbk')
                   + '-'.encode('gbk') + shelf.encode('gbk') + '-'.encode('gbk') + remaining.encode('gbk')+ ';\n'.encode('gbk'))


            buffer = []
            while True:

                d = s.recv(1024)
                if d:
                    buffer.append(d)
                else:
                    break
            data = b''.join(buffer)

            s.close()

            result = data.decode('gbk')

            return result

        else:
            raise ValueError('bad value!please check the enter！')

    def delete(self,code):
        if(not isinstance(code,str)):
            raise ValueError('bad value!please check the enter！')

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((Network.IP, Network.PORT))

        s.send(main.Main.DEVICE_CODE.encode('gbk') + ';delete;'.encode('gbk')
               + code.encode('gbk')+ ';\n'.encode('gbk'))


        buffer = []
        while True:

            d = s.recv(1024)
            if d:
                buffer.append(d)
            else:
                break
        data = b''.join(buffer)

        s.close()

        result = data.decode('gbk')

        return result

    def getSchedule(self):

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((Network.IP, Network.PORT))

        s.send(main.Main.DEVICE_CODE.encode('gbk') + ';schedule;\n'.encode('gbk'))

        buffer = []
        while True:

            d = s.recv(1024)
            if d:
                buffer.append(d)
            else:
                break
        data = b''.join(buffer)

        s.close()

        result = data.decode('gbk')
        list1 = re.split('\;',result)
        list2 = []
        for i in range(len(list1)-1):
            list3 = re.split('-',list1[i])
            print(list3)
            if not (list3[0] == "未设置" or list3[1] == "未设置" or list3[2] == "未设置" or list3[4] == "未设置"):
                sche = schedule.Schedule(list3[0], list3[1], list3[2], list3[3], list3[4])
                list2.append(sche)

        return list2

    def putMedicine(self,Str_rfid):

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((Network.IP, Network.PORT))

        s.send(main.Main.DEVICE_CODE.encode('gbk') + ';put;'.encode('gbk')
               + Str_rfid+ ';\n'.encode('gbk'))

        s.close()


    def bindingMedicine(self,Str_rfid):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((Network.IP, Network.PORT))

        s.send(main.Main.DEVICE_CODE.encode('gbk') + ';binding;'.encode('gbk')
               + Str_rfid + ';\n'.encode('gbk'))

        s.close()

