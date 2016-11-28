import logging
import time
import requests
import json


class WebServerConnector(object):
    def __init__(self, classInfoURL, attendanceMakerURL):
        self.classInfoURL = classInfoURL
        self.attendanceMakerURL = attendanceMakerURL
        self.logFile = 'WebServerConnector%s.log' % int(time.time())
        self._setupLogging()

    def _setupLogging(self):
        logging.getLogger('urllib3.connectionpool').setLevel(logging.ERROR)
        self.logger = logging.getLogger('WebSr')
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

    def getClassesInfo(self):
        """
        Returns a JSON object conatining all classes and their info

        TODO: test
        """
        self.logger.info("Getting classes info")
        try:
            response = requests.get(self.classInfoURL)
            self.logger.debug("Response: '%s'" % response.json())
        except:
            logging.exception("Failed to get classes info")

        return response.json()

    def markAttendance(self, stMac, rpMac, className):
        """
        Marks attendacne for a the given bluetooth MAC address for the given
        class name

        TODO: test
        """
        payload = {}
        payload['studentMacAddress'] = stMac
        payload['raspPieMacAddress'] = rpMac
        payload['classId'] = className
        payload_json = json.dumps(payload)
        self.logger.debug("Payload: %s" % payload_json)
        self.logger.info("Marking attendace for '%s' in '%s'" % (stMac,
                                                                 className))
        try:
            r = requests.post(self.attendanceMakerURL, payload_json)
            self.logger.info("Response status code: %s" % r.status_code)
            assert r.status_code == 200
        except AssertionError:
            self.logger.error("Failed to mark attendace")
        except:
            self.logger.exception("Failed to mark attendace")
        return r.status_code


if __name__ == "__main__":
    print "Testing..."
    w = WebServerConnector("http://f1aba23f.ngrok.io",
                           "http://f1aba23f.ngrok.io/markattendance/")
    print "Got class info:\n", w.getClassesInfo()
    print "#" * 60
    w.markAttendance("00:0a:95:91:68:e4", "00:0a:95:9d:68:96", "CMPE273")
    print "Done testing!"
