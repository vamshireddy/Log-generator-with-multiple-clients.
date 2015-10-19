# LoggingClient
* A tool for generating specified number of log messages to a logging server. Its purpose is to check the scalability of the logging server in handling the network logs. 
* It uses a logging protocol to interact with the server.
* This tool can simulate multiple clients sending logs to a single server.
* Reliability of the logs is achieved with the help of sequence numbers and retransmissions. 
## How to run
* Compile the program. 
* Run with the following command line arguments.
* IP, port, timeout in ms, no of clients
* Timeout = the time the client has to wait for retransmitting the packet.
* no of client = no of clients that has to be simulated.
