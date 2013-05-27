### Can I bloody make OSGi work this time?


## Currently in this branch:

- an iPOJO api module
- an iPOJO implementation module
- an iPOJO jersey resource that uses the implementation

Goal: change the implementation at runtime without ever interrupting the web service


The rest resource works and loads fine with this bundle setup:

     ID|State      |Level|Name
        0|Active     |    0|System Bundle (4.2.1)
        1|Active     |    1|Apache Felix Bundle Repository (1.6.6)
        2|Active     |    1|Apache Felix Gogo Command (0.12.0)
        3|Active     |    1|Apache Felix Gogo Runtime (0.10.0)
        4|Active     |    1|Apache Felix Gogo Shell (0.10.0)
       19|Active     |    1|Apache Felix Configuration Admin Service (1.2.4)
       21|Active     |    1|Apache Felix Log Service (1.0.0)
       29|Active     |    1|jersey-core (1.17.1)
       30|Active     |    1|jersey-servlet (1.17.1)
       31|Active     |    1|OSGi R4 Compendium Bundle (4.0.0)
       32|Active     |    1|Apache Felix Declarative Services (1.6.0)
       33|Active     |    1|Apache Felix Shell Service (1.4.0)
       34|Active     |    1|Apache Felix Http Api (2.0.4)
       50|Active     |    1|Apache Felix File Install (3.0.2)
       51|Active     |    1|Apache Felix iPOJO (1.8.0)
       52|Active     |    1|Apache Felix Web Management Console (3.1.2)
       53|Active     |    1|Apache Felix iPOJO WebConsole Plugins (1.6.0)
       58|Active     |    1|hello-world-api (1.1.0.SNAPSHOT)
       59|Active     |    1|Apache Felix iPOJO Gogo Command (1.0.1)
       61|Active     |    1|Apache Felix iPOJO Temporal Service Dependency Handler (1.6.0)
       63|Active     |    1|hello-world-impl (1.1.0.SNAPSHOT)
       75|Active     |    1|hello-world-rest-api-impl (1.1.0.SNAPSHOT)
       81|Resolved   |    1|jersey-server (1.17.1)
       82|Active     |    1|Apache Felix Http Jetty (2.0.4)
       83|Resolved   |    1|HTTP Service (1.0.1)
       84|Installed  |    1|Apache Felix Http Base (2.0.4)
       85|Installed  |    1|Apache Felix Http Bundle (2.0.4)
       86|Installed  |    1|Apache Felix Http Proxy (2.0.4)
