## Download latest version

[Download Link](https://github.com/fervort/SuperMQL/files/3797547/SuperMQL.v1.0.0.beta.zip)
## Shall I learn Groovy to use SuperMQL ?
No :) . Almost all java synatx are valid groovy syntax. So you can use java code in the script as it is. If you know Groovy then you can combine both the syntax. Check some sample scripts present in github at path [Here ](https://github.com/fervort/SuperMQL/tree/master/Scripts ) 

## Why should I use SuperMQL ?
  - You can use as alternative to TCL . TCL is complex and old language but using SuperMQL, you can use java syntax to perform your ENOVIA tasks.
  - Once you write script, you don't need to compile it. Just change, save and execute.
  - You can execute native MQLs from SuperMQL (See How to use section)
  - You can create your own SMQL command (Under development)
  - Database connection can be created using JDBC from script which is not easy from TCL.
  - Multithreading can be used in scripting. 
  - There are some SuperMQL commands by which you could perform tasks which are not possible to perform using native MQL. Like list all types where XYZ attribute is used. (Under Development)
  
## Features:
  - Support execution of groovy script on ENOVIA kernal
  - Execute native MQLs
  - Execute groovy script in interactive mode
 
## How to deploy
1. Download SuperMQL ZIP package from link provided above.
2. Extract the ZIP package.
3. Copy `SuperMQL.jar` and `groovy-XXX.jar` files in MQL Classpath. 

For 3dspace classpath is :
```sh
# On Windows
3DSPACE_INSTALLED_DIRECTORY\win_b64\docs\javaserver
# On Linux
3DSPACE_INSTALLED_DIRECTORY/linux_b64/docs/javaserver
```
For studio classpath is   
```sh
# On Windows
STUDIO_INSTALLED_DIRECTORY\win_b64\docs\javaserver
# On Linux
STUDIO_INSTALLED_DIRECTORY/linux_b64/docs/javaserver
```

> Note : If you want to use SuperMQL using 3dspace MQL then copy JARs only in 3dspace classpath directory. Same for studio, if you want to use SuperMQL using studio MQL then copy JARs only in studio directory. Obviously, you could copy JARs in both the directory and use SuperMQL from 3dspace or studio. You could also copy this JARs in a common path and add this path as classpath in both 3dspace and studio directory.

4. Restart MQL if it is already running so that it will pickup the copied JARs.
5. As we can't modify ENOVIA kernal, we have to insert one supporting JPO to invoke the SuperMQL.jar file we copied in last step. This JPO ( `SuperMQL_mxJPO.java` ) is already provided in the ZIP package you have downloaded in last step.
6. Copy JPO SuperMQL_mxJPO.java to server where 3dspace or studio is running.
7. Insert and compile JPO using MQL command
```sh
insert program D:\Path\to\jpo\SuperMQL_mxJPO.java;

compile program SuperMQL force update;
```
8. That's All. 

## How to use SuperMQL
### Execute java/groovy script : 
- Start MQL and execute `SuperMQL` JPO like: 
```sh
execute program SuperMQL C:\Work\Scripts\ListVaults.groovy;
```
> `ListVaults.groovy` is provided in the ZIP package you have downloaded in last step. 


### Command line mode : 
- Start MQL and execute `SuperMQL` JPO like: 
```sh
execute program SuperMQL;
```
This command will start SuperMQL in command line mode. Like

![](Docs/images/SuperMQLCommandLineMode.png)

- You can also execute java/groovy script in command line mode using flag `f` (file) like
```sh
Smql<1>f D:\Project\script\RenameBus.groovy
```
- You can also execute java/groovy script in command line mode using flag `f` (file) like
- Native MQL can be executed from SuperMQL prompt. Use dot `.` to start a command.
```sh
Smql<2>. temp query bus Part A-* * select id; 
``` 
- Java/Groovy script can be executed in interactive mode Using flag `i` (interactive)
```sh
Smql<3>i def result=mql('list type *'); print result ; 
``` 
