# ScheduleToSMS
Un programme qui enverra votre emploi du temps UVHC sur votre téléphone. Basé sur un cycle de vie évolutif.

# Fonctionnement
A l'aide de l'API de l'Application PushBullet, et de multiple requêtes GET et POST sur le site de l'UVHC, l'application parviendra à vous fournir par SMS votre emploi du temps de la journée.


*   Dans un premier temps, elle enverra l'emploi du temps entier par SMS 5 minutes avant le début du premier cours.
*   Dans un second temps, elle enverra 5 minutes avant le prochain cours un SMS informant l'étudiant du nom du cours, du type de cours, de la salle, du professeur, ainsi que la durée.


# Les nécessaires


*   L'application PushBullet sur votre téléphone.
*  	Un compte PushBullet.
*	Un Access-Token pour la connexion sur votre compte PushBullet.
