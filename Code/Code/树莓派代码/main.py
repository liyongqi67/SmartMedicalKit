# -*- coding: utf-8 -*-
import network
import rfid
import medicine
import time
import threading
import sys
from PyQt5.QtWidgets import (QApplication, QAction, QWidget, QHBoxLayout, QLabel, QFrame, QTableWidget,
    QMainWindow, QTableWidgetItem, QPushButton, QVBoxLayout, qApp, QScrollBar, QScrollArea, QLineEdit, QMessageBox)
from PyQt5.QtGui import QPixmap, QPainter, QColor, QBrush, QFont, QRegExpValidator
from PyQt5.QtCore import Qt, QTimer,  QObject, pyqtSignal, QRegExp, QRect, QCoreApplication

class MyLabel(QLabel):
    def __init__(self, position):
        super().__init__()
        self.position = position

    def mouseReleaseEvent(self, e):
        Main.showDetail(main, self.position)

class Communicate(QObject):
    connectNetwork = pyqtSignal()

class Main (QMainWindow):

    DEVICE_CODE = 'D001'

    def __init__(self):
        super().__init__()
        self.flag = False
        self.c = Communicate()
        self.c.connectNetwork.connect(self.initNetwork)
        self.initUI()
        self.welcome()

        self.timer = QTimer(self) #初始化一个定时器
        self.timer.timeout.connect(self.nextUI) #计时结束调用closeFrame()，关闭frame
        self.timer.start(3000) #设置计时间隔并启动

        self.addMedicine = False

    def run(self):
        self.list = network.Network().getMedicineList()
        self.schedule = network.Network().getSchedule()
        self.flag = True

    def initNetwork(self):
        t = threading.Thread(target=self.run, name='networkThread')
        t.start()

    def initUI(self):
        self.setWindowFlags(Qt.FramelessWindowHint)  # 隐藏标题栏
        self.setGeometry(0, 0, 480, 320)
        self.setWindowTitle("SmartMedicineKit")
        self.show()


    def keyPressEvent(self, e):
        if e.key() == Qt.Key_Escape:
            self.close()

    # 欢迎界面
    def welcome(self):
        self.frame = QFrame(self)
        self.frame.setStyleSheet("background-image: url(SmartMedicineKit.png)")
        self.frame.setGeometry(0, 0, 480, 320)
        self.frame.show()

        self.c.connectNetwork.emit()

    def closeFrame(self):
        self.frame.setVisible(False)
        self.frame.close()

    def nextUI(self):
        self.rfid = rfid.RFID(self)
        self.closeFrame()
        self.timer.stop()  # 表格加载完成后将timer暂停
        if self.flag == True:
            self.showMedicine()
            self.showSchedule()
            self.lb0 = None
            # self.setVisible(False)  # 设底为不可见
        else:
            self.connectFailed()

    # 连接失败界面
    def connectFailed(self):
        self.lbt = QLabel("<b><font size=7>服务器连接失败</font></b>", self)
        self.lbt.setGeometry(0, 0, 480, 160)
        self.lbt.setWordWrap(True)  # 允许换行
        self.lbt.setAlignment(Qt.AlignHCenter | Qt.AlignBottom) #水平居中 垂直方向靠下
        self.lbt.setMargin(10) #设置边距
        self.lbt.show()

        self.lbb = QLabel("<b><font size=7>请检查网络连接并重启</font></b>", self)
        self.lbb.setGeometry(0, 160, 480, 160)
        self.lbb.setWordWrap(True)  # 允许换行
        self.lbb.setAlignment(Qt.AlignHCenter | Qt.AlignTop) #水平居中 垂直方向靠上
        self.lbb.setMargin(10) #设置边距
        self.lbb.show()

    #吃药提醒
    def showSchedule(self):
        self.scheduleFrame = QFrame(self)

        self.tableWidget = QTableWidget(self.scheduleFrame)
        self.tableWidget.setColumnCount(3)
        self.tableWidget.setSelectionBehavior(QTableWidget.SelectRows) #选择行
        self.tableWidget.setSelectionMode(QTableWidget.SingleSelection)  # 选择单行
        self.tableWidget.setEditTriggers(QTableWidget.NoEditTriggers)  # 不可编辑
        self.tableWidget.verticalHeader().setVisible(False)  # 隐藏垂直表头
        self.tableWidget.setColumnWidth(0, 140)
        self.tableWidget.setColumnWidth(1, 220)
        self.tableWidget.setColumnWidth(2, 120)
        self.tableWidget.resizeRowsToContents() #行高与内容相匹配
        self.tableWidget.setAutoScroll(False) #去掉自动滚动
        # 设置水平表头
        header1 = QTableWidgetItem("时间")
        header2 = QTableWidgetItem("药名")
        header3 = QTableWidgetItem("用药量")
        header1.setTextAlignment(Qt.AlignCenter)
        header2.setTextAlignment(Qt.AlignCenter)
        header3.setTextAlignment(Qt.AlignCenter)
        header1.setFont(QFont("SansSerif", 13))
        header2.setFont(QFont("SansSerif", 13))
        header3.setFont(QFont("SansSerif", 13))
        self.tableWidget.setHorizontalHeaderItem(0, header1)
        self.tableWidget.setHorizontalHeaderItem(1, header2)
        self.tableWidget.setHorizontalHeaderItem(2, header3)

        # self.tableWidget.setRowCount(10)
        #
        # item1 = QTableWidgetItem("8:30")
        # item1.setTextAlignment(Qt.AlignCenter)
        # item1.setFont(QFont("SansSerif", 10))
        # item2 = QTableWidgetItem("哈哈哈")
        # item2.setTextAlignment(Qt.AlignCenter)
        # item2.setFont(QFont("SansSerif", 10))
        # item3 = QTableWidgetItem("1")
        # item3.setTextAlignment(Qt.AlignCenter)
        # item3.setFont(QFont("SansSerif", 10))
        # self.tableWidget.setItem(0, 0, QTableWidgetItem(item1))
        # self.tableWidget.setItem(0, 1, QTableWidgetItem(item2))
        # self.tableWidget.setItem(0, 2, QTableWidgetItem(item3))

        self.tableWidget.setRowCount(len(self.schedule))

        for i in range(len(self.schedule)):
            sche = self.schedule[i]
            hour, minute = sche.last_time()

            item1 = QTableWidgetItem(sche.hour + ":" + sche.minute + "—" + str(hour) + ":" + str(minute))
            item1.setTextAlignment(Qt.AlignCenter)
            item1.setFont(QFont("SansSerif", 10))
            item2 = QTableWidgetItem(sche.name)
            item2.setTextAlignment(Qt.AlignCenter)
            item2.setFont(QFont("SansSerif", 10))
            item3 = QTableWidgetItem(sche.dosage)
            item3.setTextAlignment(Qt.AlignCenter)
            item3.setFont(QFont("SansSerif", 10))
            self.tableWidget.setItem(i, 0, QTableWidgetItem(item1))
            self.tableWidget.setItem(i, 1, QTableWidgetItem(item2))
            self.tableWidget.setItem(i, 2, QTableWidgetItem(item3))

        showMedicine = QPushButton("药品列表", self.scheduleFrame)
        showMedicine.setFont(QFont("SansSerif", 18))
        showMedicine.setGeometry(0, 0, 150, 40)
        showMedicine.clicked.connect(self.medList)

        refresh = QPushButton("刷新", self.scheduleFrame)
        refresh.setFont(QFont("SansSerif", 18))
        refresh.setGeometry(150, 0, 80, 40)
        refresh.clicked.connect(self.refreshSchedule)

        exit = QPushButton("退出", self.scheduleFrame)
        exit.setFont(QFont("SansSerif", 18))
        exit.setGeometry(230, 0, 80, 40)
        exit.clicked.connect(self.quit)

        self.tableWidget.setGeometry(0, 40, 480, 320)

        self.scheduleFrame.setStyleSheet("background-color:rgb(255,255,224)")
        self.scheduleFrame.setGeometry(0, 0, 480, 320)
        self.scheduleFrame.setAutoFillBackground(True)
        self.scheduleFrame.show()

    def medList(self):
        self.scheduleFrame.setVisible(False)

    def refreshSchedule(self):
        self.tableWidget.clearContents()
        self.schedule = network.Network().getSchedule()
        rowCount = self.tableWidget.rowCount()
        if rowCount > len(self.schedule) :
            for i in  range(rowCount - len(self.schedule)):
                self.tableWidget.removeRow(i)
        elif rowCount < len(self.schedule):
            self.tableWidget.setRowCount(len(self.schedule))

        for i in range(len(self.schedule)):
            sche = self.schedule[i]
            hour, minute = sche.last_time()

            item1 = QTableWidgetItem(sche.hour + ":" + sche.minute + "—" + str(hour) + ":" + str(minute))
            item1.setTextAlignment(Qt.AlignCenter)
            item1.setFont(QFont("SansSerif", 10))
            item2 = QTableWidgetItem(sche.name)
            item2.setTextAlignment(Qt.AlignCenter)
            item2.setFont(QFont("SansSerif", 10))
            item3 = QTableWidgetItem(sche.dosage)
            item3.setTextAlignment(Qt.AlignCenter)
            item3.setFont(QFont("SansSerif", 10))
            self.tableWidget.setItem(i, 0, QTableWidgetItem(item1))
            self.tableWidget.setItem(i, 1, QTableWidgetItem(item2))
            self.tableWidget.setItem(i, 2, QTableWidgetItem(item3))



    def quit(self):
        self.close()

    def backToSchedule(self):
        self.scheduleFrame.setVisible(True)

    # 药品列表
    def showMedicine(self):
        add = QAction("添加", self)
        add.setFont(QFont("SansSerif", 18))
        add.triggered.connect(self.add)

        refresh = QAction("刷新", self)
        refresh.setFont(QFont("SansSerif", 18))
        refresh.triggered.connect(self.refresh)

        back = QAction("返回", self)
        back.setFont(QFont("SansSerif", 18))
        back.triggered.connect(self.backToSchedule)

        toolbar = self.addToolBar("")
        toolbar.setMovable(False)
        toolbar.setAllowedAreas(Qt.TopToolBarArea)
        toolbar.addAction(add)
        toolbar.addAction(refresh)
        toolbar.addAction(back)
        toolbar.setStyleSheet("background-color:rgb(255,255,224)")

        scrollarea = QScrollArea(self)
        scrollarea.setWidgetResizable(True)
        scrollarea.setGeometry(0, 40, 480, 280)
        scrollarea.show()
        widget = QWidget()
        widget.setStyleSheet("background-color:rgb(174,255,172)")
        scrollarea.setWidget(widget)
        self.vbox = QVBoxLayout(widget)
        self.vbox.setAlignment(Qt.AlignTop)

        self.label = []
        if len(self.list) == 0:
            self.lb0 = QLabel("<b><font size=6>请点击上方的添加按钮添加药品</font></b>")
            # lb0.setGeometry(0, 40, 480, 70)
            self.lb0.resize(480, 100)
            self.lb0.setWordWrap(True)
            self.lb0.setAlignment(Qt.AlignCenter)
            self.lb0.setMargin(5)
            self.label.append(self.lb0)
            self.vbox.addWidget(self.lb0)
            self.lb0.show()
        else:
            length = len(self.list)
            for i in range(length):
                med = self.list[i]
                self.label.append(MyLabel(i))
                self.label[i].resize(480, 100)
                # self.label[i].setGeometry(0, 40+70*i, 480, 70)
                self.label[i].setWordWrap(True)
                self.label[i].setMargin(2)
                if med.remaining == "none":
                    if (med.cal_overdue() > 30):
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量请进入详情页面设置</FONT>')
                    elif med.cal_overdue() <= 30 and med.cal_overdue() >= 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量请进入详情页面设置</FONT>' + '[您的药品还有<FONT color=#FF0000>' + str(
                                int(med.cal_overdue())) + '</FONT>天过期]')
                    elif med.cal_overdue() < 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量请进入详情页面设置</FONT>' + '[您的药品<BLINK><FONT color=#FF0000>已过期</FONT></BLINK>]')
                else:
                    if (med.cal_overdue() > 30):
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量' + med.remaining + '</FONT>')
                    elif med.cal_overdue() <= 30 and med.cal_overdue() >= 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量' + med.remaining + '</FONT>' + '[您的药品还有<FONT color=#FF0000>' + str(
                                int(med.cal_overdue())) + '</FONT>天过期>]')
                    elif med.cal_overdue() < 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量' + med.remaining + '</FONT>' + '[您的药品<BLINK><FONT color=#FF0000>已过期</FONT></BLINK>]')

                self.vbox.addWidget(self.label[i])
                self.label[i].setStyleSheet("background-color:rgb(194,255,189)")
                self.label[i].show()

    # 显示详情
    def showDetail(self, position):
        self.detailFrame = QFrame(self)
        self.position = position

        regx = QRegExp("[0-9]+$")

        self.nameLabel = QLineEdit()
        self.nameLabel.setText(self.list[position].name)

        codeLabel = QLabel("条形码  ：" + self.list[position].code)
        typeLabel = QLabel("药品类型：" + self.list[position].type)
        indicationLabel = QLabel("适应症  ：")
        indicationLabel2 = QLabel(self.list[position].functions)
        indicationLabel2.setWordWrap(True)
        priceLabel = QLabel("药品定价：" + self.list[position].price)
        propertiesLable = QLabel("药品性状：" + self.list[position].properties)
        usageLable = QLabel("药品用法：")
        usageLable2 = QLabel(self.list[position].usage)
        attentionsLable = QLabel("注意事项：")
        attentionsLable2 = QLabel(self.list[position].attentions)
        attentionsLable2.setWordWrap(True)
        specificationsLable = QLabel("药品规格：" + self.list[position].specifications)

        self.manufactureLabel = QLineEdit()
        if self.list[position].manufacture != "none":
            self.manufactureLabel.setText(self.list[position].manufacture)
        validator1 = QRegExpValidator(regx, self.manufactureLabel)
        self.manufactureLabel.setValidator(validator1)

        self.shelfLabel = QLineEdit()
        if self.list[position].shelf != "none":
            self.shelfLabel.setText(self.list[position].shelf)
        validator2 = QRegExpValidator(regx, self.shelfLabel)
        self.shelfLabel.setValidator(validator2)

        self.remainingLabel = QLineEdit()
        if self.list[position].remaining != "none":
            self.remainingLabel.setText(self.list[position].remaining)
        validator3 = QRegExpValidator(regx, self.remainingLabel)
        self.remainingLabel.setValidator(validator3)

        dateLabel = QLabel("有效期还剩：" + str(int(self.list[position].cal_overdue())) + "天")

        quitButton = QPushButton("返回并保存信息")
        quitAndDrop = QPushButton("返回并放弃修改")
        deleteButton = QPushButton("  删除药品  ")

        nameWidget = QWidget()
        namehbox = QHBoxLayout()
        namehbox.addWidget(QLabel("药品名称："))
        namehbox.addWidget(self.nameLabel)
        self.nameLabel.setStyleSheet("background-color:rgb(255,255,255)")
        namehbox.addStretch(1)
        nameWidget.setLayout(namehbox)

        shelfWidget = QWidget()
        shelfhbox = QHBoxLayout()
        shelfhbox.addWidget(QLabel("保质期  ："))
        shelfhbox.addWidget(self.shelfLabel)
        self.shelfLabel.setStyleSheet("background-color:rgb(255,255,255)")
        shelfhbox.addStretch(1)
        shelfhbox.addWidget(quitAndDrop)
        quitAndDrop.setStyleSheet("background-color:rgb(255,255,255)")
        shelfWidget.setLayout(shelfhbox)

        remainingWidget = QWidget()
        remaininghbox = QHBoxLayout()
        remaininghbox.addWidget(QLabel("剩余量  ："))
        remaininghbox.addWidget(self.remainingLabel)
        self.remainingLabel.setStyleSheet("background-color:rgb(255,255,255)")
        remaininghbox.addStretch(1)
        remainingWidget.setLayout(remaininghbox)

        manufactureWidget = QWidget()
        manufacturehbox = QHBoxLayout()
        manufacturehbox.addWidget(QLabel("生产日期："))
        manufacturehbox.addWidget(self.manufactureLabel)
        self.manufactureLabel.setStyleSheet("background-color:rgb(255,255,255)")
        manufacturehbox.addStretch(1)
        manufactureWidget.setLayout(manufacturehbox)

        indicationWidget = QWidget()
        indicationhbox = QHBoxLayout()
        indicationhbox.addWidget(indicationLabel)
        indicationhbox.addWidget(indicationLabel2, 1)
        indicationhbox.addStretch(1)
        indicationWidget.setLayout(indicationhbox)

        codeWidget = QWidget()
        codehbox = QHBoxLayout()
        codehbox.addWidget(codeLabel)
        codehbox.addStretch(1)
        codehbox.addWidget(deleteButton)
        deleteButton.setStyleSheet("background-color:rgb(255,255,255)")
        codeWidget.setLayout(codehbox)

        priceWidget = QWidget()
        pricehbox = QHBoxLayout()
        pricehbox.addWidget(priceLabel)
        pricehbox.addStretch(1)
        pricehbox.addWidget(quitButton)
        quitButton.setStyleSheet("background-color:rgb(255,255,255)")
        priceWidget.setLayout(pricehbox)

        typeWidget = QWidget()
        typehbox = QHBoxLayout()
        typehbox.addWidget(typeLabel)
        typeWidget.setLayout(typehbox)

        dateWidget = QWidget()
        datehbox = QHBoxLayout()
        datehbox.addWidget(dateLabel)
        dateWidget.setLayout(datehbox)

        usageWidget = QWidget()
        usagehbox = QHBoxLayout()
        usagehbox.addWidget(usageLable)
        usagehbox.addWidget(usageLable2)
        usageLable2.setWordWrap(True)
        usagehbox.addStretch(1)
        usageWidget.setLayout(usagehbox)

        propertiesWidget = QWidget()
        propertieshbox = QHBoxLayout()
        propertieshbox.addWidget(propertiesLable)
        propertiesWidget.setLayout(propertieshbox)

        specificationsWidget = QWidget()
        specificationshbox = QHBoxLayout()
        specificationshbox.addWidget(specificationsLable)
        specificationsWidget.setLayout(specificationshbox)

        attentionsWidget = QWidget()
        attentionsHbox = QHBoxLayout()
        attentionsHbox.addWidget(attentionsLable)
        attentionsHbox.addWidget(attentionsLable2)
        attentionsHbox.addStretch(1)
        attentionsWidget.setLayout(attentionsHbox)

        scrollarea = QScrollArea(self.detailFrame)
        scrollarea.setWidgetResizable(True)
        scrollarea.setGeometry(0, 0, 480, 320)
        widget = QWidget()
        widget.setStyleSheet("background-color:rgb(174,255,172)")

        scrollarea.setWidget(widget)
        vbox = QVBoxLayout(widget)
        vbox.setAlignment(Qt.AlignTop)

        nameWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(nameWidget, 1)
        typeWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(typeWidget, 1)
        indicationWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(indicationWidget, 2)
        remainingWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(remainingWidget, 1)
        dateWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(dateWidget, 1)
        usageWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(usageWidget, 1)
        propertiesWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(propertiesWidget, 1)
        attentionsWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(attentionsWidget, 2)
        specificationsWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(specificationsWidget, 1)
        manufactureWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(manufactureWidget, 1)
        shelfWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(shelfWidget, 1)
        priceWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(priceWidget, 1)
        codeWidget.setStyleSheet("background-color:rgb(194,255,189)")
        vbox.addWidget(codeWidget, 1)

        self.detailFrame.setGeometry(0, 0, 480, 320)
        self.detailFrame.setAutoFillBackground(True)
        self.detailFrame.show()
        scrollarea.show()

        deleteButton.clicked.connect(self.delete)
        quitButton.clicked.connect(self.closeDetailFrame)
        quitAndDrop.clicked.connect(self.detailFrame.close)

    def closeDetailFrame(self):
        string = network.Network().modify(str(self.list[self.position].id), self.nameLabel.text(),
                                          self.manufactureLabel.text(), self.shelfLabel.text(),
                                          self.remainingLabel.text())
        if (string == 's'):
            reply = QMessageBox.question(self, "Message", "修改成功", QMessageBox.Ok)
            self.list[self.position].name = self.nameLabel.text()
            self.list[self.position].shelf = self.shelfLabel.text()
            self.list[self.position].manufacture = self.manufactureLabel.text()
            self.list[self.position].remaining = self.remainingLabel.text()
            if self.list[self.position].remaining == "none":
                if (self.list[self.position].cal_overdue() > 30):
                    self.label[self.position].setText(
                        '<b><FONT SIZE=4>' + self.list[self.position].name + '</b></FONT><br><FONT SIZE=3>' + self.list[self.position].indication + '<br>药品剩余量请进入详情页面设置</FONT>')
                elif self.list[self.position].cal_overdue() <= 30 and self.list[self.position].cal_overdue() >= 0:
                    self.label[self.position].setText(
                        '<b><FONT SIZE=4>' + self.list[self.position].name + '</b></FONT><br><FONT SIZE=3>' + self.list[self.position].indication + '<br>药品剩余量请进入详情页面设置</FONT>' + '[您的药品还有<FONT color=#FF0000>' + str(
                            int(self.list[self.position].cal_overdue())) + '</FONT>天过期]')
                elif self.list[self.position].cal_overdue() < 0:
                    self.label[self.position].setText(
                        '<b><FONT SIZE=4>' + self.list[self.position].name + '</b></FONT><br><FONT SIZE=3>' + self.list[self.position].indication + '<br>药品剩余量请进入详情页面设置</FONT>' + '[您的药品<BLINK><FONT color=#FF0000>已过期</FONT></BLINK>]')
            else:
                if (self.list[self.position].cal_overdue() > 30):
                    self.label[self.position].setText(
                        '<b><FONT SIZE=4>' + self.list[self.position].name + '</b></FONT><br><FONT SIZE=3>' + self.list[self.position].indication + '<br>药品剩余量' + self.list[self.position].remaining + '</FONT>')
                elif self.list[self.position].cal_overdue() <= 30 and self.list[self.position].cal_overdue() >= 0:
                    self.label[self.position].setText(
                        '<b><FONT SIZE=4>' + self.list[self.position].name + '</b></FONT><br><FONT SIZE=3>' + self.list[self.position].indication + '<br>药品剩余量' + self.list[self.position].remaining + '</FONT>' + '[您的药品还有<FONT color=#FF0000>' + str(
                            int(self.list[self.position].cal_overdue())) + '</FONT>天过期>]')
                elif self.list[self.position].cal_overdue() < 0:
                    self.label[self.position].setText(
                        '<b><FONT SIZE=4>' + self.list[self.position].name + '</b></FONT><br><FONT SIZE=3>' + self.list[self.position].indication + '<br>药品剩余量' + self.list[self.position].remaining + '</FONT>' + '[您的药品<BLINK><FONT color=#FF0000>已过期</FONT></BLINK>]')
        else:
            reply = QMessageBox.question(self, "Message", "修改失败", QMessageBox.Ok)

        self.detailFrame.close()
        self.position = 0

    # 删除
    def delete(self):
        reply = QMessageBox.question(self, "Message", "Are you sure to delete?", QMessageBox.Yes |
                                     QMessageBox.No, QMessageBox.No)
        if reply == QMessageBox.Yes:
            network.Network().delete(str(self.list[self.position].id))
            self.refresh()
            self.detailFrame.close()
            self.position = 0

    # 刷新
    def refresh(self):
        self.list = network.Network().getMedicineList()
        for i in range(len(self.label)):
            self.vbox.removeWidget(self.label[i])  # 遍历label删除布局中的label
            self.label[i].close()
        self.label.clear()  # 清空label
        if len(self.list) == 0:
            if self.lb0 == None:
                self.lb0 = QLabel("<b><font size=6>请点击上方的添加按钮添加药品</font></b>")
                self.lb0.resize(480, 100)
                self.lb0.setWordWrap(True)
                self.lb0.setAlignment(Qt.AlignCenter)
                self.lb0.setMargin(5)
                self.label.append(self.lb0)
                self.vbox.addWidget(self.lb0)
                self.lb0.show()
            elif self.lb0.isVisible() == False:
                self.lb0.setVisible(True)
                self.vbox.addWidget(self.lb0)
                self.label.append(self.lb0)
        else:
            length = len(self.list)
            for i in range(length):
                med = self.list[i]
                self.label.append(MyLabel(i))
                self.label[i].resize(480, 100)
                self.label[i].setWordWrap(True)
                self.label[i].setMargin(2)
                if med.remaining == "none":
                    if (med.cal_overdue() > 30):
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量请进入详情页面设置</FONT>')
                    elif med.cal_overdue() <= 30 and med.cal_overdue() >= 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量请进入详情页面设置</FONT>' + '[您的药品还有<FONT color=#FF0000>' + str(
                                int(med.cal_overdue())) + '</FONT>天过期]')
                    elif med.cal_overdue() < 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量请进入详情页面设置</FONT>' + '[您的药品<BLINK><FONT color=#FF0000>已过期</FONT></BLINK>]')
                else:
                    if (med.cal_overdue() > 30):
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量' + med.remaining + '</FONT>')
                    elif med.cal_overdue() <= 30 and med.cal_overdue() >= 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量' + med.remaining + '</FONT>' + '[您的药品还有<FONT color=#FF0000>' + str(
                                int(med.cal_overdue())) + '</FONT>天过期>]')
                    elif med.cal_overdue() < 0:
                        self.label[i].setText(
                            '<b><FONT SIZE=4>' + med.name + '</b></FONT><br><FONT SIZE=3>' + med.indication + '<br>药品剩余量' + med.remaining + '</FONT>' + '[您的药品<BLINK><FONT color=#FF0000>已过期</FONT></BLINK>]')

                self.vbox.addWidget(self.label[i])
                self.label[i].setStyleSheet("background-color:rgb(194,255,189)")
                self.label[i].show()

    # 添加
    def add(self):
        if len(self.list) == 0:
            if self.lb0 != None and self.lb0.isVisible() == True:
                self.label.remove(self.lb0)
                self.vbox.removeWidget(self.lb0)
                self.lb0.close()
            self.showCodeScan()
        else:
            self.showCodeScan()

    def showCodeScan(self):
        self.scanFrame = QFrame(self)
        vLayout = QVBoxLayout()
        label = QLabel("<b><FONT SIZE=5>请按下扫码器的按钮</b></FONT><br><b><FONT SIZE=5>并将药盒的条形码对准扫描器</b></FONT>")
        label.setAlignment(Qt.AlignCenter)
        self.quitButtoninScan = QPushButton("返回(30)")
        self.quitButtoninScan.clicked.connect(self.closeScanFrame)

        self.countNum = 30
        self.timerInScan = QTimer(self)
        self.timerInScan.timeout.connect(self.timerCounter)
        self.timerInScan.start(1000)

        self.codeedit = QLineEdit()
        regx = QRegExp("[0-9]{13}")
        validator = QRegExpValidator(regx, self.codeedit)
        self.codeedit.setValidator(validator)
        self.codeedit.returnPressed.connect(self.uploadMed)

        vLayout.addWidget(self.quitButtoninScan)
        vLayout.addWidget(label)
        vLayout.addWidget(self.codeedit)

        self.code = ''

        self.scanFrame.setLayout(vLayout)
        self.scanFrame.setGeometry(0, 0, 480, 320)
        self.scanFrame.setAutoFillBackground(True)
        self.scanFrame.show()
        self.codeedit.setFocus()

    def timerCounter(self):
        if (self.countNum > 0):
            self.countNum = self.countNum - 1
            string = "返回(" + str(self.countNum) + ")"
            self.quitButtoninScan.setText(string)
        else:
            self.closeScanFrame()

    def closeScanFrame(self):
        self.timerInScan.stop()
        self.scanFrame.close()

    def uploadMed(self):
        self.code = self.codeedit.text()
        self.addMedicine = True
        string = network.Network().creat_by_code(self.code, "none", "none", "none")

        if (string == 's'):
            reply1 = QMessageBox.question(self, "Message", "添加药品成功！", QMessageBox.Yes)
            if reply1 == QMessageBox.Yes:
                self.closeScanFrame()
                self.refresh()
        else:
            reply2 = QMessageBox.question(self, "Message", "添加药品失败，请尝试在手机端手动添加或检查网络连接", QMessageBox.Ok)
            if reply2 == QMessageBox.Ok:
                self.window.close()
                self.scanFrame.close()
                self.refresh()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    main = Main()

    sys.exit(app.exec_())
