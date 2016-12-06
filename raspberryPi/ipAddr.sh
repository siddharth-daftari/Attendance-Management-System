ifconfig | grep -A 1 wlan0 | grep "inet addr" | awk '{print $2}'
read "press any key to exit"
