# Technical test

## Context

This is an implementation of a technical test. Instructions can be found [here](https://gist.github.com/sfriquet/55b18848d6d58b8185bbada81c620c4a).

## Building and running

In order to build the project, clone the repository to your computer. You will need maven to build it and execute it.

Run `mvn package` to build the project. It will run the tests.  
Run `mvn package -DskipTests` to skip the tests execution.

If you want to generate the javadoc, run `mvn javadoc:javadoc`. It will be generated in the current directory, in target/site/apidocs`.

Then run the project: `mvn exec:java -Dexec.args="/path/to/file.tsv"`. This will read your file and run a jetty web server on port 8080 on your machine
(make sure port 8080 is not already used).

As per the instructions, you can then use your browser or any other web client, and try some queries out.  
Ex :  
`http://localhost:8080/1/queries/popular/2015-08-02?size=5`  
`http://localhost:8080/1/queries/popular/2015-08?size=3`  
`http://localhost:8080/1/queries/count/2015-08-02 02`

When you are done, close your mvn prompt to close the web server.

## What's inside

This program builds a tree at launch time with the provided tsv file. It takes a few seconds and takes memory, but the queries on the dates
are consequently faster. Each node has a key and a value, the key being a timestamp, and the value is a list of query:count.

This tree (I use the red-black tree TreeMap implementation of the Java standard library) allows for log(n) search of a timestamp. 

The count operation of unique queries is implemented this way :
- Iterates on the nodes that start from the beginning of the queried interval to the end of the queried interval,
which is O(n) if n is the number of timestamps that will be visited.
- When iterating, the queries in each node are added to a set, which is O(1). Adding all queries is O(m),
if m is the number of queries.
- Then we just return the size of the set, which is O(1).
- Overall, the count operation is thus O(nm). 

Note: If N is the total number of nodes, getting to the two start/end nodes is O(NlogN), contrary to O(N) for a linked list, for instance.
Even though the O notation is the same in the end, one is faster than the other.

The popular operation (return top n queries based on their count on a time interval) is implemented this way :
- Like previously, we iterate on the nodes from the time interval, which is O(n), if n 
is the number of timestamps that will be visited.
- We maintain a hashmap a global {query:count}. For each visited node, and for each query in the node, we do a "get" and
a "put", which are O(1) operations. So, for each visited node, the time complexity is O(m),
if m is the number of queries.
- At the end, we sort the HashMap and create a LinkedHashMap. This is a worst case O(m * logm) operation.
- Overall, the popular operation is O(m * n + m *logm) operation.

## Thoughts

### Memory
This only works as long as your whole tree can fit in memory. For memory problems, except distributing the memory in multiple nodes (ex: Redis),
we could pre-sort our input file, and depending on the time interval, build a structure based on this time interval only.

### Time/memory tradeoffs
Other choices could be made, that encompass tradeoffs between memory and time.

For instance, instead of using a binary tree for storing timestamps, we could store the timestamps in an array, and have a HashMap acting as an index.
This way, getting to a specific timestamp is O(1) instead of O(logn), but this takes more memory.

 