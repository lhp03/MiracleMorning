# Miracle Morning
Term project for Mobile Programming
- Trailer Video: 

|Name|Part|task|
|---|---|---|
|박예진|Calendar|PPT|
|노인욱|Challenge|PPT|
|이현필|Alarm|Debugging|
|이정훈|Login, Community|Presenter|


## Brief Description
### Importance of Self-Management
self-management is essential in modern society. Therefore, varous advice related to this is pouring. One of tips is Miracle Morning

### What is Miracle Morning?
Miracle morning is a concept that appeared in 2016 self-development book.
Miracle morning is simply to awke up ealy in the morning and practice self-develop ment such as reading and exercise or self-advice as a routine.

### This application will help you to achieve your miracle morning**
- For someone who wants to live regular life, self-improvement, miracle morning
- By perform morning rouitnes with certain alarms.
- Share your routine with others and avoid th failure of Miracle Morning.

## Application Version
- minSdkVersion: 23

## Content
### Sign up and Login
- You can become a member of Miracle morning by sign up.
- You can use application with log in.
### Alarm
- You can make a rouitne.
- When it's time to perform routine, the alarm will be triggered.
- When it is time to end the routine, it checks whether or not the activity set in the routine is performed, alarm will inform about it.
### Calendar
- You can write on calendar.
- You can write daily notes such as the mood or condition of the day.  
### Commnunity
- You can upload photos, videos and writings on the community.
- You can share your daily rouitine.
### Challenge
- You can participate in a challenge related in their own routines.

## Structure
![flowchart_1](https://user-images.githubusercontent.com/60330965/173026354-85cc981b-ca80-4b91-818e-d878936c2d80.png)

![flowchart _2](https://user-images.githubusercontent.com/60330965/173026372-377c3762-74f9-4226-81a0-f15558fed9a4.png)

## Implementation in detail
### Sign up and Login and Check Information
![image_1](https://user-images.githubusercontent.com/60330965/173043534-ae2cbdd0-00fa-49cf-b78f-e93c69a4a6cb.png)
- When you run the application, the login page is displayed. you can login and enter the application, or you can go sign up page.
- In sign up page, you must enter an email and password to sign up.
- If you log in for the first time, the Profile Settings page is displayed. You cna set up a profile and view it on the profile page.


### Alarm
![image_2](https://user-images.githubusercontent.com/60330965/173047643-816b7eaa-f2e5-4cc4-a76b-a64c36491bce.png)
- After login, routine page is displayed. you can check the routines you added.
You can enter the add routine page by click '루틴추가' button. You must enter the routine's title, start time, end time and select days of week that you want to do on. Finally, select whether to start and end alarms. The start alarm notifies you to start your routine exactly at the start time of the routine. The end alarm notifies you whether the routine's detail activity is performed or not at the end time of routine before minutes that you selected (3min, 5min, 10min ...).
- In routine Detail, you can check the routine's detail activity that you added.
You can enter the add routine detail page by click '추가' button. You must enter detail's title, type. There are two types that are exactly time, timer.('읽기' is not updated...). If you want to add exactly time type detail, you must enter the time that you want to do it at. If you want to add timer type detail, you must enter the time that you want to do it for.

![image_3](https://user-images.githubusercontent.com/60330965/173043527-10faf989-f674-44f1-9b07-1a4994a6cdf5.png)
- When you want to do exatly time type detail, you click it. If the current time is less than 5 minutes from the stored time, the execution is completed and detail will be checked.
- When you want to do timer type detail, you can click it and timer page is displayed. You can click the play button and after the time you set, the detail activity is completed and the detail will be checked.

![image_4](https://user-images.githubusercontent.com/60330965/173043531-191b2b9a-fcfb-4ffb-a42f-0a15ce05eda2.png)
- Even if you are not running the application, the alarm notifies you that you should start the routine when it is time to start the routine that you set.
- When it is close to the end time, the alarm notifies you whether all detail activities of the routine have been completed.
### Calendar
![cal](https://user-images.githubusercontent.com/60330965/173051077-179a2a77-096f-4c88-9401-99b514eb9d27.png)
- In calendar page, you can write on calendar about daily mood, routine status...
- You can click a date in calendar and you can write notes(memo).
- If you click on the date you have alread written, you can check, modify, and delete previously written notes.

### Challenge
![challenge](https://user-images.githubusercontent.com/60330965/173054736-be140abb-c2f2-4290-bbd4-b2d2b65962db.png)
- Users can participate in a challenge related in their own routines
- You can write down the date and wake-up time on paper and take pictures to certify it.

### Community
![com](https://user-images.githubusercontent.com/60330965/173052453-968b7251-e865-4a9c-a9ef-2c96f0a8ce18.png)
- In community page, you can view posts posted by others, or you can create posts and share them with others.
- You can attach videos and pictures.
