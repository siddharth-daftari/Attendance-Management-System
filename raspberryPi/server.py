from bluetooth import SERIAL_PORT_CLASS, SERIAL_PORT_PROFILE
from WebServerConnector import WebServerConnector

import bluetooth
import datetime
import json
import logging
import os
import requests
import time

with open(os.path.join(os.getcwd(), "serverConf.json")) as fp:
    config = json.load(fp)


class BTServer(object):
    def __init__(self, port, uuid, backlog):
        self.port = port
        self.uuid = uuid
        self.backlog = backlog
        self.logFile = 'BTServer%s.log' % int(time.time())
        self.sleepSecs = config["sleepSecs"]
        self.timeoutSecs = config["sleepSecs"]

        self._setupLogging()
        self._createAndBindSocket()

    @property
    def leader(self):
        url = config["setLeaderURL"]
        data = json.dumps({"MAC": config["rpMac"]})
        try:
            r = requests.post(url, data=data)
        except requests.exceptions.ConnectionError:
            logging.error("Commuincaiton between the server failed")
            logging.error("URL: %s" % url)
            logging.error("DATA: %s" % data)
        else:
            if r.status_code == 202:
                logging.debug("I am the leader! Wohoo!!")
                return True
            elif r.status_code == 208:
                logging.debug("I am still the leader! hurray!!")
                return True
            elif r.status_code == 401:
                logging.debug("Damn! Someone else is the leader! :/")
                return False
            else:
                logging.debug("Status_code:%s" % r.status_code)
                logging.error("Something went wrong while determining leader"
                              "\nStatus code:%s" % r.status_code)

    def _setupLogging(self):
        self.logger = logging.getLogger('BTser')
        self.logger.setLevel(logging.DEBUG)

        fh = logging.FileHandler(self.logFile)
        fh.setLevel(logging.DEBUG)

        # ch = logging.StreamHandler()
        # ch.setLevel(logging.INFO)

        formatter = logging.Formatter('%(asctime)s - %(name)5s - '
                                      '%(levelname)7s - %(message)s')
        fh.setFormatter(formatter)
        # ch.setFormatter(formatter)

        self.logger.addHandler(fh)
        # self.logger.addHandler(ch)

    def _closeSocket(self):
        self.socket.close()

    def _createAndBindSocket(self):
        self.socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
        self.socket.settimeout(self.timeoutSecs)
        self.socket.bind(("", self.port))
        self.socket.listen(self.backlog)

    def _advertise(self):
        bluetooth.advertise_service(self.socket, "AquaPiServer",
                                    service_id=self.uuid,
                                    service_classes=[self.uuid,
                                                     SERIAL_PORT_CLASS],
                                    profiles=[SERIAL_PORT_PROFILE])

    def serve(self, endTime, callback):
        """
        Look for incoming connections on the socket till endTime and for each
        connection, make a call of callback(address). Address is the Bluetooth
        address of the client.
        """
        self._advertise()
        self.logger.info("Listening for requests...")

        while datetime.datetime.now() < endTime:
            if not self.leader:
                self.logger.info("Sleeping for %s sec" % self.sleepSecs)
                time.sleep(self.sleepSecs)
                continue
            try:
                clientsocket, address = self.socket.accept()
                self.logger.info("-" * 40)
                self.logger.info("Incoming MAC addr: %s" % address[0])
                data = clientsocket.recv(1024)
                self.logger.info("Student Name: %s", data)
                clientsocket.close()
                if address[0] in self.studentMacAddrList:
                    callback(address[0])
                else:
                    logging.warn("Unauthorized MAC addr '%s'" % address[0])
            except bluetooth.btcommon.BluetoothError as e:
                if e.message == "timed out":
                    # logging.debug("Bluetooth timed out [expected]")
                    continue
                self.logger.error("Bluetooth Error: '%s'" % e.message)
            self.logger.info("-" * 40)


def setupLogging():
    logger = logging.getLogger()
    logger.setLevel(logging.DEBUG)

    fh = logging.FileHandler('temp.txt')
    fh.setLevel(logging.DEBUG)

    ch = logging.StreamHandler()
    ch.setLevel(logging.DEBUG)

    formatter = logging.Formatter('%(asctime)s - %(name)5s - '
                                  '%(levelname)7s - %(message)s')
    fh.setFormatter(formatter)
    ch.setFormatter(formatter)

    logger.addHandler(fh)
    logger.addHandler(ch)


def getNextClassTime(classesInfo):
    # Get next class assuming non overlapping class timing
    now = datetime.datetime.now()
    nextClass, nextClassStartTime, nextClassEndTime = None, None, None
    for className in classesInfo["data"]:
        classEndTime = datetime.datetime.strptime(
            classesInfo["data"][className]['endTime'],
            "%m-%d-%y %H:%M:%S")
        classStartTime = datetime.datetime.strptime(
            classesInfo["data"][className]['startTime'],
            "%m-%d-%y %H:%M:%S")
        if classEndTime > now:
            if nextClass is None or nextClassEndTime > classEndTime:
                nextClassStartTime = classStartTime
                nextClassEndTime = classEndTime
                nextClass = className
    return nextClass, nextClassStartTime, nextClassEndTime


def getDummyDataForPresentation():
    x = datetime.datetime.now()
    x += datetime.timedelta(seconds=6)
    x = x.strftime("%m-%d-%y %H:%M:%S")
    y = datetime.datetime.now()
    y += datetime.timedelta(hours=2)
    y = y.strftime("%m-%d-%y %H:%M:%S")
    classesInfo = {"message": "Requested class info available in data "
                              "field",
                   "data": {"CMPE273": {"endTime": y,
                                        "enrolledStudents":
                                        ["00:0a:95:9d:68:00",
                                         "00:0a:95:9d:68:01",
                                         "64:BC:0C:F7:84:55",
                                         "C0:EE:FB:30:09:E8"],
                                        "startTime": x}},
                   "result": True}
    return classesInfo


if __name__ == "__main__":
    # TODO shorten the below lines of code or move them to functions
    global config
    setupLogging()

    webServerCon = WebServerConnector(config["classInfoURL"],
                                      config["attendanceMakerURL"])
    while True:
        # classesInfo = webServerCon.getClassesInfo()
        # logging.debug(classesInfo)
        # if not classesInfo['result']:
        #     logging.info("No class today... Sleeping for an hour")
        #     time.sleep(60 * 60)
        #     continue
        # TODO remove the below lines used for demo
        classesInfo = getDummyDataForPresentation()

        nextClass, nextClassStartTime, nextClassEndTime = \
            getNextClassTime(classesInfo)

        if nextClass is None:
            logging.warn("No classes to check attendance for")
            exit()
        else:
            logging.info("Next/Current class start time: %s" %
                         nextClassStartTime)
            logging.info("Next/Current class end time: %s" %
                         nextClassEndTime)
        if datetime.datetime.now() < nextClassStartTime:
            timeDelta = nextClassStartTime - datetime.datetime.now()
            timeDeltaSecs = timeDelta.total_seconds()
            logging.info("Sleeping for %s secs" % int(timeDeltaSecs))
            time.sleep(int(timeDeltaSecs))

        server = BTServer(config["port"], config["uuid"], config["backlog"])
        server.studentMacAddrList = classesInfo["data"][nextClass][
            "enrolledStudents"]
        server.serve(nextClassEndTime,
                     lambda stMac: webServerCon.markAttendance(stMac,
                                                               config["rpMac"],
                                                               nextClass))
