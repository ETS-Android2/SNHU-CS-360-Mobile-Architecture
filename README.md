# SNHU-CS-360-Mobile-Architecture

# Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?
This Android application was developed to help users keep track of their events via an easy to use event list specific to their account, sorted by date which also notifies the user when the event date is the current date. To do this, I created an SQLite DB in local storage and created a hierarchy of activities with their views. The user logs in or creates an account, and instantly has the ability to choose notification settings and manage their events. 

# What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?
The screens implemented were a login screen, registration screen, event list screen, add event screen, view event screen, edit event screen, and edit notification settings. Each screen is intuitive to the user and links to the next seamlessly. My designs include labels for every input field, as well as input validation and warnings when the user's input is invalid.

# How did you approach the process of coding your app? What techniques or strategies did you use? How could those be applied in the future?
I first planned and created my database. This was important to start with, as I could easily develop the application around these tables. One technique I used when developing was lessening how much I copy logic, and instead create a function with that logic.

# How did you test to ensure your code was functional? Why is this process important and what did it reveal?
I tested probably just as much as I coded. I started my app via an emulator and and tested each activity one-by-one, essentially unit testing my activities. I then tested how all the activities worked together, and if the database was receiving and sending the right information.

# Considering the full app design and development process, from initial planning to finalization, where did you have to innovate to overcome a challenge?
A lot of the application was a challenge to me, as mobile development is very new to me. I ran into an issue with using the SMS manager, as it wanted real numbers to text and I couldn't get it to work for the life of me. I ended up adding all of the logic, so in theory it would work, but I also added Toast notifications which performed the same actions, but the users would have to be in the application to receive them. Of course, in a real-world environment I would get the user number and ensure this functionality was working.

# In what specific component from your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?
Generally, I'm quite proud of most of the application. The only exception is the SMS functionality. I'm happy with the login functionality, where it performs a DB query to check if the username and password are correct. Similarly, the registration portion checks the uniqueness of usernames, as these are the primary keys for the User table.
