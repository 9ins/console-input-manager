# console-input-manager

 

![](./img/console.PNG)



## Introduction

This library help you to build Java console input process on your console application with easist way of way.

Also it's provide more effective and easy way of development console application.

You can define interactive queries which be need on the application as user input and set up each queries with full-managed way.



## How to install

To use this library on your application development, You already have to be installed Java Development Environment(JDK)  on your computer.

You need to download Jar file on **Release menu** in this project and set this library to classpath on your project.

Because of being used with **Snake YAML** library, you should be got the library on your project classpath.



## How to use

First you have to define a queries what you need at Application. To do this, you should make message file to be querying on the application.

That's something like below.

```yaml
# Console App messages

TRADEMARK_FILE: './trademark'
VERSION: '4.0.0'

TITLE: 'Title of Console Application'
PROLOGUE: 'This library help you to build your Console Application with very easy way \nAnd manage a queries for Application more easily'

QUERYS:
  MESSAGE001: 'This is message 001'
  QUERY001: 'This is query 001 : '
  QUERY002: 'This is query 002 : '
  MESSAGE002: 'This is message 002'
  CONTINUE: 'Do you wanna continue? (y/n) '
```

#### Definition of key of message file

* TRADEMARK_FILE : At the application start up, This file's contents be shown. This could be ASCII art which represent your console application.
* VERSION: Version of your console application. If you use **@version** variable on trademark file, The variable will be replaced with this key's value.
* TITLE: Title text which be printed below trademark. It's may be title of your console application.
* PROLOGUE: This key can be used  as long and detail information of your console application. It could be introduction or snippet of the console application. Line-break can be done by '\n' symbol.
* QUERYS: This key is section what the separate query be specified for your console application.
* MESSAGE001: This key will be printed as message while query process. It just print key's value. It must be starting with 'MESSAGE' string.
* QUERY001: This key is query what a user of console application can input what they choose or input. Later, input value will be included at result Map object.
* CONTINUE: This key represent whether it's gonna termination of query process. if user input 'n' or 'no', will be exit query and trigger process on  ConsoleTrigger implemented class.



To use trademark like ASCII fancy style, you can set the path of trademark at **TRADEMARK_FILE** key on the messages file. Default defined trademark content on this library example is down below.

```tex
╔╦╗┌─┐┬┌─┌─┐  ╦ ╦┌─┐┬ ┬┬─┐  ╔═╗┌─┐┌─┐                             
║║║├─┤├┴┐├┤   ╚╦╝│ ││ │├┬┘  ╠═╣├─┘├─┘                             
╩ ╩┴ ┴┴ ┴└─┘   ╩ └─┘└─┘┴└─  ╩ ╩┴  ┴                               
╔╦╗┌─┐┬─┐┌─┐  ╔═╗┌─┐┌─┐┬ ┬  ╔═╗┌┐┌┌┬┐  ╔═╗┌─┐┌─┐┌─┐┌─┐┌┬┐┬┬  ┬┌─┐ 
║║║│ │├┬┘├┤   ║╣ ├─┤└─┐└┬┘  ╠═╣│││ ││  ║╣ ├┤ ├┤ ├┤ │   │ │└┐┌┘├┤  
╩ ╩└─┘┴└─└─┘  ╚═╝┴ ┴└─┘ ┴   ╩ ╩┘└┘─┴┘  ╚═╝└  └  └─┘└─┘ ┴ ┴ └┘ └─┘o

Ver. @version  Authored by Kooin-Shin
```



### How to code

If you define message.yml file for your purpose, Now you are prepared to code with this library.

Fist, you have to implement object implementing ConsoleTrigger. ConsoleTrigger is interface for triggering workflow of the console application with using user input data.

Seconds,  you have to create File object for the YAML message file and you can create ConsoleInput object by ConsoleFactory.

Code of these is down below.

```java
import java.io.File;
import java.util.Map;

public class SimpleConsoleTest implements ConsoleTrigger { 

    ConsoleInput consoleInput;

    SimpleConsoleTest() throws Exception {
        //Create File object for message.yml
        File messageFile = new File(ConsoleFactory.class.getResource("messages.yml").toURI().getPath());
        //Create ConsoleInput object using ConsoleFactory
        this.consoleInput = ConsoleFactory.getDefaultConsoleInput(messageFile, this);
        //Start query process
        this.consoleInput.startQuery();
    }

    @Override
    public void trigger(Map<String, String> inputMap) throws Exception {
        //This will be executed when the query process is done if user didn't choose 'n' or 'no'
        System.out.println("========== Receive input map ==========");
        inputMap.entrySet().stream().forEach(e -> System.out.println("key: "+e.getKey()+"   value: "+e.getValue()));
    }

    @Override
    public void canceled() {
        //This will be executed if user choose 'n' or 'no'
        System.out.println("Console canceled...");        
    }   

    public static void main(String[] args) throws Exception {
        new SimpleConsoleTest();
    }
}
```



Your critical voice can make this project will be more thrive, and always elastic thinking is yours. 

Thanks.
