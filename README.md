# GogolLibrary
+ Java (Spring) -CSS-HTML-JavaScript implementation of Library Management System web application.
+ Introduction to Programming project by students of Innopolis University of BS1-2 group.
+ Team: Zhuchkov Alexey, Mazan Ilya, Galeev Farit, Zaynullin Ruslan

# Contents
  + <a href="#main">Main</a>
    + <a href="#arcp">Architecture of the server</a>
    + <a href="#arcd">Architecture of the database</a>
    + <a href="#arcw">Architecture of the website</a>
  + <a href="#imp">Implementation</a>
    + <a href="#doc">Documents</a>
    + <a href="#user">Users</a>
    + <a href="#book">Booking system</a>
  + <a href="#installation">Usage</a>
    + <a href="#inst">Installation of software</a>
    + <a href="#launch">Launching the project</a>
    + <a href="#entry">Entering</a>
    + <a href="#browse">Browsing web pages</a>
  + <a href="#soft">Soft in use</a>
  + <a href="#issue">Issues?</a>
<a name="main">

# Main
+ Library Management System (LMS) are used in libraries to track the different items in the library. 
> The system contains all information about books, magazines, audio/video materials, as well as people allowed to check out the materials or those in charge of the management.LMS enable users to search for documents, check out or enter
new materials, manage users of the library, among other functionalities.

<a name="arcp"> 

## Architecture of the server
</a> 

![Server](https://github.com/HiGal/GogolLibrary/blob/master/src/main/resources/rmres/packDep.png "Server") 
 
<a name="arcw"> 
  
## Architecture of the database
</a>

![Database](https://github.com/HiGal/GogolLibrary/blob/master/src/main/resources/rmres/database.png "DB")

<a name="arcd">
  
## Architecture of the website
</a> 

## Working on it
 ![Websites](https://github.com/HiGal/GogolLibrary/blob/master/src/main/resources/rmres/1.jpg "W")
 
<a name="imp">
   
# Implementation
</a>

<a name="doc">
   


<a name="user">
   
## Users
</a>

+ There are two types of users:
1. **Patron** - can *search for, check out and return documents* 
    + **Faculty** (professors, instructors, TAs): 
      *Have permission to сheck out document for ***4*** weeks (regardless the book is best seller) or ***2*** (if AV or journal article)*
    + **Students**:
      *Have permission to сheck out documents for ***3*** weeks (if book, but if bestseller - ***1***) or ***2*** (if AV or journal article)*
    + **Visiting Professors**:
      *Have permission to сheck out documents for ***1*** week only*
2. **Librarians**- can *check overdue documents, manage patrons, and
add/delete/modify documents*       

<a name="book">
  
## Documents
</a>

+ The main asset of the library are documents
  + ***Books***: Books are written by one or more authors and published by a publisher.
Books have a title and may exist in different editions – each
published in a certain year. For example, “Introduction to Algorithms”
is a book written by Thomas H. Cormen, Charles E. Leiserson, Ronald
L. Rivest and Clifford Stein. It was published by the MIT Press. The
third edition was published in 2009.  
![Book table](https://github.com/HiGal/GogolLibrary/blob/master/src/main/resources/rmres/bookHead.png "Book table")

  + ***Journal Articles***: Journal articles are written by one or more authors, have a title, and are published in a certain journal. Journals have a title and are published by a publisher in issues. Issues have editors and a publication date. For example, “Communication ACM” is a journal. The article “Go to Statement Considered Harmful” written by Edsger W. Dijkstra was published in the “March 1968” issue of this journal and Edward Nash Yourdon was the editor of the issue.
![Journal table](https://github.com/HiGal/GogolLibrary/blob/master/src/main/resources/rmres/journalHead.png "Journal table")

  + **Audio/Video materials (AV)**: AV materials have a title and the list of authors
![AV table](https://github.com/HiGal/GogolLibrary/blob/master/src/main/resources/rmres/avHead.png "AV table")

Documents also have the price value (in Rubles)


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
  + Spring Framework, Maven and SQLite are already preinstalled in Intellij IDEA
  
<a name="launch"> 
   
## Launching the project
</a>

#### Download and launch the project
  
  + use <a href="ProvidedDoc/project.pdf">this guide</a>
    
<a name="entry">

## Entrying
</a>
There are some pre-signed up users:

|     User           |     Login          | Password |
| ------------------ |:------------------:| :-------:|
| Student            | a.zhuchkov         |    123   |
| Student            | r.zaunullin        |    123   |
| Student            | f.galeev           |    123   |
| Instructor         | i.mazan            |    123   |
| Professor          | v.rivera           |    123   |
| Teacher Assistant  | h.aslam            |    123   |
| Visiting Professor | b.meyer            |    123   |
| Librarian          | a.admin            |    666   |
     
<a name="browse">

##Browsing Web pages
### There are some scenarios

<a name="soft">

# Software in use:
</a>

  + <a href="http://maven.apache.org/POM/4.0.0">Maven</a>
  + <a href="https://spring.io/docs">Spring Framework</a>
  + <a href="http://www.sqlite.org/docs.html">SQLite</a>
  + <a href="https://www.jetbrains.com/idea/">Intellij IDEA</a>
  + <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java JDK</a>
  
<a name="issue">
    
## Issues ?
 If something goes wrong, please, contact one of us via Telegram:
   + Zhuchkov Alexey ***@Aleksey_zhu***
   + Mazan Ilya ***@HardLight***
   + Galeev Farit ***@FirstOfAll***
   + Zaynullin Ruslan ***@BloodyTag51***
