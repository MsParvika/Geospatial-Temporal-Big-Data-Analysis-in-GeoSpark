# How to Set up Environment

### Passwordless ssh:
- Create id_rsa on Master: 
```
cat DDSHadoopSpark.pem > ~/.ssh/id_rsa
```

- Generate Public Key:
```
1. chmod 400 id_rsa
2. ssh-keygen -y -f ~/.ssh/id_rsa > ~/.ssh/id_rsa.pub
3. chmod 400 id_rsa.pub
```

- Copy Public Key to other instances:
```
cat ~/.ssh/id_rsa.pub | ssh ubuntu@172.32.abc.def 'cat >> .ssh/authorized_keys'
cat ~/.ssh/id_rsa.pub | ssh ubuntu@172.32.pqr.stu 'cat >> .ssh/authorized_keys'
```

### System Set- up:

- Download JAVA on all Instances:
```
In all instances:
- sudo apt-get update && sudo apt-get install openjdk-8-jdk openjdk-8-jre
(cd usr/lib/)
- Add JAVA_HOME to .bashrc
```

- Download Hadoop on all instances:
```
In all Instances:
wget http://apache.mirrors.lucidnetworks.net/hadoop/common/hadoop-2.7.7/hadoop-2.7.7.tar.gz
```

- Edit the following files:
```
etc/hadoop/hadoop-env.sh: In all instances, add JAVA_HOME to it
slaves: at master instances, run command touch slaves and add ip address of all slaves.
etc/hadoop/core-site.xml- 	Add	<fs.default.name>		<hdfs://192.168.0.1:54310>

Add HADOOP_HOME to .bashrc and Append $HADOOP_HOME/bin to path.
Run ./start-dfs.sh
```

- Download Spark :
```
https://go.gliffy.com/go/share/sis0f9q7sgfgno2h74ho
- Go to project root folder
- Run sbt assembly. You may need to install sbt in order to run this command.
- Submit the jar to Spark using Spark command "./bin/spark-submit"
( ./bin/spark-submit Geospark-Template-assembly-0.1.0.jar result/output rangequery src/resources/arealm10000.csv -93.63173,33.0183,-93.359203,33.219456 rangejoinquery src/resources/arealm10000.csv src/resources/zcta10000.csv distancequery src/resources/arealm10000.csv -88.331492,32.324142 1 distancejoinquery src/resources/arealm10000.csv src/resources/arealm10000.csv 0.1 )

```
