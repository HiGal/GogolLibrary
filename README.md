# GogolLibrary
+ Java (Spring) -CSS-HTML-JavaScript implemetion of Library Management System web application: 
Introduction to Programming project by students of Innopolis University of BS1-2 group.
+ Team: Zhuchkov Alexey, Mazan Ilya, Galeev Farit, Zaynullin Ruslan

# Contents
  + <a href="#main">Main scheme</a>
  + <a href="#arc">Architecture of the website</a>
  + <a href="#imp">Implementation</a>
    + <a href="#doc">Documentation</a>
    + <a href="#user">Users</a>
    + <a href="#lib">Librarian features</a>
    + <a href="#book">Booking system</a>
  + <a href="#installation">Usage</a>
    + <a href="#inst">Installation of software</a>
    + <a href="#launch">Launching the project</a>
    + <a href="#entry">Entrying</a>
    + <a href="#browse">Browsing web pages</a>
  + <a href="#soft">Soft in use</a>
  + <a href="#issue">Issues?</a>
<a name="main">

# Main scheme
</a>
Patrons are able survey diffrent documents on the main page and check them out for a certain time.
Librarins are used to manage their moves and them as objects. 
<a name="arc"> 
   
# Architecture of the website
</a> 
 <img src="ProvidedDoc/scheme.jpg" alt="ProvidedDoc/scheme.jpg"> 
<a name="imp">
   
# Implementation
</a>
<a name="doc">
   
## Documents
</a>
We store all documents in documents db. 

which is typicly the abstract class for all documents.
Below we have particular types of document extending from it: 

<a name="user">
   
## Users
</a>

  + Patron 
   *Could give requests to  ***search for, check out and return documents***.*
    + Student
   *Have permission to сheck out documents for ***3*** weeks* 
    + Faculty member 
   *Have permission to сheck out documents for ***4*** weeks* 
  + Librarian
   *Is allowed to ***modify/delete/add*** any document or patron.*

We assign the loged in user with new exemplar of appropriate class:

        
<a name="lib">

## Librarian features
</a>
Librarian is a user with manage abilities. One's 3special features
defined in class Librarian. 

        

<a name="book">

## Booking System (Document Copy)
</a>

         

Every time user check out document - mount of copies in the library decrease.
<a name="installation">
  
# Usage
</a>

<a name="inst">

## Installation accompanying soft:
</a>

#### Install Java JDK according to your operation system

  + use <a href="ProvidedDoc/java.pdf"> this guide </a>

#### Install Intellij IDEA 
  + use <a href="https://www.jetbrains.com/help/idea/install-and-set-up-intellij-idea.html"> this guide</a> 
  + Spring Framework and Maven are alreadu preinstalled in Intellij IDEA
  
<a name="launch"> 
   
## Launching the project
</a>

#### Download and launch the project
  
  + use <a href="ProvidedDoc/project.pdf">this guide</a>
    
<a name="entry">

## Entrying
</a>
There are some pre-signed up users:
<ul>
   <li> Student:
      <ul>
         <li> login - 
         <li> password - 123
      </ul>
   <li> Professor:
      <ul>
         <li> login - 
         <li> password - 123
      </ul>
   <li> Librarian:
      <ul>
         <li> login - 
         <li> password  - 123
     </ul>
 </ul>
     
<a name="browse">

##Browsing Web pages
### There are some scenarious

<a name="soft">

# Software in use:
</a>

  + <a href="http://maven.apache.org/POM/4.0.0">Maven</a>
  + <a href="https://spring.io/docs">Spring Framework</a>
  + <a href="https://www.jetbrains.com/idea/">Intellij IDEA</a>
  + <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java JDK</a>
  
<a name="issue">
    
## Issues ?
</a>

### If something goes wrong:
  + Still have some problems? Please contact one of us in Telegram:
    + Zhuchkov Alexey 
    + Mazan Ilya
    + Galeev Farit 
    + Zaynullin Ruslan
