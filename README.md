# University-Scheduler
Java Android Application that allows a user to keep track of university courses, terms, and assessments. In addition to new ones created by the user, each one can also be edited or removed altogether. Makes use of an SQL Room Database and organized by an MVVM methodology.

## Purpose of Application
The University Scheduler is a Java Android application designed to fulfill the requirements set forth by a prestigious regional university. The result of more students joining and applying in record numbers has facilitated the creation of a new scheduler app for use with Android or Google phones. An SQL Room Database is used for a more current, easier to use option to store scheduler data. Alerts/notifications can be set for university course start and end dates, as well as start and due dates of the course's given assessments.

## Author Details
Author: Harrison Thacker

Email: harrison.thacker69@gmail.com

Version Of Application: 1.0

Period Of Development: (October 2022 --- March 2023)

## IDE + Miscellaneous Dependencies
Android Studio Dolphin | 2021.3.1 Patch 1

Build #AI-213.7172.25.2113.9123335, built on September 29, 2022

Runtime version: 11.0.13+0-b1751.21-8125866 amd64

VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

AVD Of Choice: Google Pixel 3a

Compile SDK: 34

Room Version: 2.4.3

JUnit Version: 4.13.2

## Directions on how to run University Scheduler
1: Open up the App Release APK on your Google or Android Phone.

2: If there are any alerts, pay them no heed.

3: If successful, you should see the main page/activity showing stats about the students current schedule.


## Directions on how to operate University Scheduler
Upon starting the application, you'll be greeted with the main page. From here, you can view statistics on the current program progress of the student. By clicking the navigation tab on the top, the user can view the terms, courses, and assessments by clicking on the respective buttons. For purposes of testing, sample data is already provided for you. The Home Page button redirects the user back to the scheduler home/stats page.

When the Assessment button is clicked, you'll be redirected to a page where a RecyclerView shows a list of given assessments. A plus button below the list is what you click to add a new assessment to the database. To view an already existing assessment, click it in the list above. This will take you to the detail page. Info on the name, start and end dates, assessment types, and assigned course is listed above. The user can also toggle alerts/notifcations for the assessment start/end dates. Below are buttons to either make edits to the given assessment or to remove it from the scheduler entirely. On the addition page, you can enter the assessment name, select its type, select the start and end dates, and pick a course for the given assessment. Clicking the save button opens a dialog asking if the user is sure. Once that's clicked, the new assessment is now part of the database and can be viewed in the RecyclerView list.

When the Course button is clicked, you'll be redirected to a page where a RecyclerView shows a list of given courses. A plus button below the list is what you click to add a new course to the database. To view an already existing course, click it in the list above. This will take you to the detail page. Info on the name, start and end dates, term, status, assessments, course notes, and course instructor details are listed above. Included are alerts/notifcation toggles for the course start/end dates. A button below facilitates the sharing of course notes as a text file. The user can share the notes wherever. At the very bottom of the activity are buttons to either make edits to the given course or to remove it from the scheduler entirely. On the addition page, you can enter all the course attributes. Clicking the save button opens a dialog asking if the user is sure. Once clicked, the new course is now part of the database and can be viewed in the RecyclerView list.

Clicking the View Terms button redirects the user to a page where a RecyclerView shows a list of given terms. A plus button below the list is what you click to add a new term to the database. To view an already existing term, click it in the list above. This will take you to the detail page. Info on the name, start and end dates, and assigned courses is listed above. Below are buttons to either make edits to the given term or to remove it from the scheduler entirely. However, a term cannot be deleted if it currently has courses assigned to it. If you try to click the delete button, a dialog window will inform you that deletion is not possible. To rectify this, have the courses reassigned to another term or remove them altogether. All of this can be done within the Edit Course page/activity. After clicking the term deletion button, a confirmation dialog will ask if the user is sure. Upon a confirmation from the user, the term will be removed from the database/scheduler. On the addition page, you can enter the term's attributes. Clicking the save button opens a dialog asking if the user is sure. Once that's clicked, the new term is now part of the database and can be viewed in the RecyclerView list.
