# RTL FE Simulator  
- System configuration: RTL_FE_SIM.json - just optional list of possible server URLs

## Versions History  
- 1.1.0.6
    - LAB DTO modified according to the lastes changes in BE: the CreatedOn field retyped to LONG, contains number of milliseconds since epoch start
- 1.1.0.7
    - LAB Creation / Deletion implemented
- 1.1.0.8
    - UI Improvements
- 1.1.0.9
    - In LAB section user role modification implemented
- 1.1.0.10
    - GUI Improvements
- 1.1.0.12
    - Sorting of users by Email added to their retrieving => big user lists are more user friendly
- 1.1.0.13
    - UI Change: FE Simulation tab moved under Testrack tabs
- 1.1.0.16
    - Fixed issue with JWT token decoding. Option for decoding token directly from the textbox added (token can be clipboarded directly to textarea)
- 1.2.0.0
    - Testrack editing implemented
- 1.2.0.1
    - Hand over of a testrack (FE simulation) did not work properly - fixed
- 1.3.0.0
    - Backup / Restore process implementation started (currently backup done)
- 1.4.0.0
    - Checking wrong LAB-USER assignments implemented
- 1.4.1.0
    - Added more details of response for HeartBeat in FE Simulation
- 1.4.1.1
    - Same HeartBeat details added also to TESTRACK | Services -> Send Single HeratBeat request
- 1.4.1.2
    - Error fixed in sending PWD update: missing content-type JSON
- 1.4.2.0
    - More logging in Janus monitoring
- 1.4.3.0
     final version, enabling o monitor STREAMING as well as AUDIOBRIDGE plugins
- 1.4.4.0
     Updating user profile fully implemented
- 1.4.5.0
     testrack data (DTO) extended with new ExtendedInfo and AVAIL / CONNECTION state
  


## Environment specific configuration  
As mentioned above various .

Backend section of the 'docker-compose.yml' file sample:

<pre>
  backend:  
    image: "rtl-backend-${CI_ENVIRONMENT_NAME}"  
    container_name: "rtl-backend-${CI_ENVIRONMENT_NAME}"  
</pre>

This configuration 

