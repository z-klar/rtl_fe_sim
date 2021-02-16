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

## Environment specific configuration  
As mentioned above various .

Backend section of the 'docker-compose.yml' file sample:

<pre>
  backend:  
    image: "rtl-backend-${CI_ENVIRONMENT_NAME}"  
    container_name: "rtl-backend-${CI_ENVIRONMENT_NAME}"  
</pre>

This configuration 
