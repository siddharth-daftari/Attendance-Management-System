# Attendance Management System
## Team Synergy:
* Madhur Khandelwal
* Siddharth Daftari
* Seema Rohilla
* Akshay Sonvane

## Objective:
To create a an automated system that eliminates the manual process of attendance management.

## System Components:
1. Raspberry Pi 
2. Android App
3. Django web application server
4. MySQL Database

## System Architecture Diagram:

![alt tag](https://github.com/siddharth-daftari/testRepo/blob/master/Architecture_Diagram.png)

## Features implemented:
* Fault tolerant using replication of Raspberry Pi.
* Dynamic leader election.
* Notification via playback of a short musical note for each attendance marked.
* Energy effecient mode; activates bluetooth scan only during class timing, by pulling class timings from the server periodically.
* Rapberry Pi denies service to unauthorized students.(i.e. students who have not yet registered for that class)
* E-Mail system integrated within the application for professor/authorised person.
* Google OAuth used, no need to remember passwords or re-register.
* Connection between android and Raspberry Pi using RFCOMM protocol.
* Android app dynamically obtains the MAC address from the server before marking the attendance.
* Protection against SQL injection on server side.
* Followed MTV (Model Template View) architecture on the server.

## License:

This project is released under the [MIT License](https://github.com/siddharth-daftari/Attendance-Management-System/blob/master/LICENSE.txt).


  
