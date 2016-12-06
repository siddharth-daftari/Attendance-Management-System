from django.http import HttpResponse, HttpResponseBadRequest
import json
import datetime
import logging

leaderMac = None
leaderLastHearbeat = None

TIMEOUT = datetime.timedelta(seconds=10)


def getLeader(request):
    global leaderMac
    if leaderMac and _timeoutReached():
        _resetLeader()
    body = {}
    body['data'] = leaderMac
    body['message'] = ""
    body['result'] = True if leaderMac else False
    return HttpResponse(json.dumps(body), content_type='application/json')


def setLeader(request):
    global leaderMac
    try:
        body = json.loads(request.body)
        mac = body['MAC']
    except ValueError:
        logging.exception("Error decoding body '%s'" % str(request.body))
        return HttpResponseBadRequest("Error decoding body")
    if leaderMac is None or _timeoutReached():
        _setNewLeader(mac)
        return HttpResponse(status=202)
    elif mac == leaderMac:
        _updateLeaserLastHearbeat(datetime.datetime.now())
        return HttpResponse(status=208)
    else:
        return HttpResponse(status=401)


def _timeoutReached():
    global leaderLastHearbeat
    global TIMEOUT
    if leaderLastHearbeat:
        return datetime.datetime.now() >= leaderLastHearbeat + TIMEOUT
    raise Exception("No leader set yet")


def _updateLeaserLastHearbeat(time):
    global leaderLastHearbeat
    leaderLastHearbeat = time


def _setNewLeader(mac):
    global leaderMac
    _updateLeaserLastHearbeat(datetime.datetime.now())
    leaderMac = mac
    logging.info("New leader elected: '%s'" % leaderMac)


def _resetLeader():
    global leaderMac
    global leaderLastHearbeat
    leaderMac = None
    leaderLastHearbeat = None
