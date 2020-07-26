EasyGreen 
===

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Demo](#Demo)
2. [Schema](#Schema)

## Overview
### Description

> Theme: The ingredients of the recipes already in your fridge and save your Surplus Food 

> Technicality: Add list of ingredient into a recipe reverse search engine via natural language querying. It allows you to search for common recipes based on a list of Ingredients that you already have at home. 

Being stuck home made me realized that I wasn't the most environmentally friendly person. 

Knowing that I may not be the only one disregarding the environment, I decided to leverage technology to help others be more sustainable. The issues that I address is food waste. 

40% of the food in the U.S. today goes uneaten which is equivalent of $165 billion dollars worth of food each year. Food that decay in landfills can produces methane, a potent greenhouse gas. And the list of problems goes on....

EasyGreen is a fun and intuitive way for people to manage their food and waste by having a digital inventory of food items in their household and provide an array of tools to be eco-friendly:

* Find creative recipes given the food items you currently have in your home
* A nice visual graph of your inventory
* Create a shopping list 
* A discover feed within the app that allow other users to share their 10 second videos of meals or other related topics 
* Set reminders when an item is about to expire  

**Sources:**
https://www.npr.org/2012/09/21/161551772/the-ugly-truth-about-food-waste-in-america


### App Evaluation
- **Category:** Productivity
- **Mobile:** 
    - User engagement is best via mobile phones because user can check their fridge current state on the go and interact more with the community 
- **Story:** This app can appeal to different groups: 
    * User that want to keep track of their food to save money
    * User that are pro-sustainable 
    * User that want to find a recipe fast based on what they have available 
    * User can get inspired from watching other people's creation 
- **Market:** 
    - The app can have a big user base due to it's video content and cool features.
    -  I estimate the audience age range would be 16-25 years old 
- **Habit:** 
    - App notifications for expiration date can quickly gave user attention and the media content can keep user engagment. 
    - User get a hit of dopamine when scrolling vertically on the media page
- **Scope:** The challenges will be:
    - Having a dataset of recipes and ingridents 
    - Figuring out the best way of getting user input 
    - Figuring out the UI of the overall app 
    - Using Facebook SDK and testing if I can use single page of Facebook Watch within the app

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [X] User can login and sign up 
- [X] User are able to create an inventory from a list of ingridents
- [X] User are able to create a shopping list 
- [X] User can see a list of recipes which they can select and read from given ingridents 
- [X] User can share a post on Facebook from the app

**Optional Nice-to-have Stories**

- [ ] Keep track of user food items expiration dates and send app notifications to alert user  
- [ ] Have a nice graph of current inventory broken down in categories 
- [ ] Have an activity for video content from community  

### 2. Screen Archetypes

* Login Screen 
   * Login and Sign up for a EasyGreen account 
* Inventory Screen 
    * User can search for an ingredient, given suggestions
* Recipes Screen
    * User can see 20 recipes based on inventory
    * User can click on a recipe and get more details such as instructions and prep time
* Shopping List Screen 
    * User can type anything and creates a list of items 
* Discover Screen 
   * In progress
 

### 3. Navigation

**Bottom Navigation** (Tab to Screen)

* Inventory
* Recipes
* Shopping List
* Discover
* Account

## Demo
<p align="center">
<img src="EasyGreen3.5.gif" height=700>

GIF created with [KAP](https://getkap.co/)    
</p> 

## Schema 
**User** *(username, firstName, lastName, profileImage)*
<br> **Inventory** *(ingredient_list, **User**\*)*

### Networking
**Spoonacular API**
<br> Base URL: https://spoonacular.com/food-api/docs#Search-Recipes-by-Ingredients

| HTTP Verb | Endpoint | Description|
| -------- | -------- | -------- |
| ` GET `    | /findByIngredients     | Search Recipes by Ingredients    |
| ` GET `    | /{id}/information      | Get information about a recipe  |
