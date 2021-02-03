# RTL FE Simulator  
- System configuration: RTL_FE_SIM.json - just optional list of possible server URLs

## Versions History  
- 1.1.0.6
    - LAB DTO modified according to the lastes changes in BE: the CreatedOn field retyped to LONG, contains number of milliseconds since epoch start

## Environment specific configuration  
As mentioned above various .

Backend section of the 'docker-compose.yml' file sample:

<pre>
  backend:  
    image: "rtl-backend-${CI_ENVIRONMENT_NAME}"  
    container_name: "rtl-backend-${CI_ENVIRONMENT_NAME}"  
</pre>

This configuration 

