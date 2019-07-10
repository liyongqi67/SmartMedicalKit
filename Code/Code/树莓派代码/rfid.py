import serial
import threading
import time
import network
import struct

class RFID:

    def __init__(self,main):
        self.main = main
        self.count = 0
        self.read_max = 50
        self.rfid_list = []
        self.backup_list = []
        self.list_of_len = []

        self.change_backup_list = []
        self.change_detector = 0
        self.change_count = 0
        self.change_detector_mode = False

        #self.serial = serial.Serial('/dev/serial/by-id/usb-1a86_USB2.0-Serial-if00-port0', 115200, timeout=20)
        self.serial = serial.Serial('/dev/serial/by-id/usb-Prolific_Technology_Inc._USB-Serial_Controller-if00-port0', 115200, timeout=20)
        #self.serial.write(b'\xbb\x17\x02\x00\x00\x19\x0d\x0a')

        t = threading.Thread(target=self.run, name='rfid')
        t.start()



    def run(self):
        try:
             i = 0
             while True:
                while(not self.count>self.read_max):

                    while self.serial.inWaiting() == 0:
                        time.sleep(1)

                    response = self.serial.readline()
                    # print(2)
                    string1 = response.decode('unicode_escape')

                    # print(string1.encode('unicode_escape'))
                    string2 = string1.encode('unicode_escape')

                    if (not (string2 == b'\\xbb\\xff\\x02\\x00\\x05\\x06\\r\\n' or string2 == b'\\xbb\\xff\\x02\\x00\\x02\\x03\\r\\n' or string2 == b'')):
                        self.append_to_list(string2)

                    self.count = self.count + 1


                if(self.change_detector_mode):
                    if(self.change_count < 2):
                        self.change_count += 1
                    else:
                        
                        self.change_detector_mode = False
                        self.change_count = 0
                        if(self.change_detector == len(self.rfid_list)):
                            print('确定被更改，正在处理')
                            if(self.change_detector2<self.change_detector):
                                change_list = self.compareList()

                                if not self.main.addMedicine:
                                    for rfid in change_list:
                                        network.Network().putMedicine(rfid)
                                else:
                                    for rfid in change_list:
                                        network.Network().bindingMedicine(rfid)
                                        self.main.addMedicine = False


                if(i>0 and not len(self.rfid_list) == len(self.backup_list)):
                    print('开启侦测模式')
                    self.change_detector_mode = True
                    self.change_detector = len(self.rfid_list)
                    self.change_detector2 = len(self.backup_list)
                    self.change_backup_list = self.backup_list

                self.list_of_len.append(len(self.rfid_list))
                print('上一次backup有'+str(len(self.backup_list))+'个标签,这一次有'+str(len(self.rfid_list)))
                print(self.main.addMedicine)
                self.backup_list_len = len(self.rfid_list)
                self.backup_list = self.rfid_list
                self.count = 0
                self.rfid_list = []

                i+=1
             print (self.list_of_len)
        except KeyboardInterrupt:
             self.serial.close()

    def append_to_list(self,rfid):
        for r in self.rfid_list :
            if(rfid == r):
                return
        self.rfid_list.append(rfid)

    def compareList(self):
        RFIDlist = []
        print(self.change_backup_list)

        for rfid in self.rfid_list:
            find = False
            for r in self.change_backup_list:
                if (r == rfid):
                    find = True
                    break
            if (not find):
                RFIDlist.append(rfid)
        print(RFIDlist)
        return RFIDlist


if __name__ == '__main__':
    rfid = RFID()
    rfid.run()

