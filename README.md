EasyGreen 
===

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
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

- [ ] User can login with a Facebook account 
- [ ] User are able to select items from a list of ingridents
- [ ] User are able to see and update a shopping list on a seperate activity 
- [ ] User can see a list of recipes which they can select and read from given ingridents 
- [ ] User can share a post on Facebook from the app

**Optional Nice-to-have Stories**

- [ ] Keep track of user food items expiration dates and send app notifications to alert user  
- [ ] Have a nice graph of current inventory broken down in categories 
- [ ] Have an activity for video content from community x 

### 2. Screen Archetypes

*  Login Screen 
   * Login via a Facebook Button 
   * Sign up via a Facebook Button
   * Skip button - User straight into app but no recipes or shopping list is stored 
* Ingridents List Screen 
    * User can scroll up to see list of categories via CardViews of food categories 
        * User can open up the CardView and select food items via Chips 
        * User click on item to add or reclick to delete 
    * Horizontal LinearLayout above the bottom navigation for two buttons
        * Inventory -> InventoryActivity: See current items and update or add to shopping list
        * See Recipes -> RecipeActivity: Call an intent to RecipesActivity and passing list of inventory

* Recipes List Screen (not part of bottom navigation)
    * User can scoll up to see list of recipes and when user clicks on one 
    * RecipeDetailsActivity is launched 
        * See prep time, serving, etc.

* Shopping List Screen 
    * User can create a list by adding items (SimpleTodo) but with button interaction


* Discover Screen 
   * User can post their meals to a specific group via FAB
   * User can scoll up to see videos of people cooking, sharing tips to be eco-friendly, or related topics 
   * User can comment or like video
 

### 3. Navigation

**Bottom Navigation** (Tab to Screen)

* Inventory
* Recipes
* Shopping List
* Discover
  
## Wireframes
<p align=center>
  <img src="https://i.imgur.com/cNdekOE.jpg" height=400>
  
  <img src="https://i.imgur.com/05FMoyN.jpg" height=400>
  
   <img src="https://i.imgur.com/wkcmZDD.jpg" height=400>
</p>

<p align=center>
  <img src="https://i.imgur.com/nS8tcdP.jpg" height=400>
  
  <img src="https://i.imgur.com/4Ejjtpw.jpg" height=400>
  
   <img src="https://i.imgur.com/3IwnbAr.jpg" height=400>
</p>

<p align=center>
  <img src="https://i.imgur.com/Eav6c6b.jpg" height=400>
  
  <img src="https://i.imgur.com/k2G3Wcj.jpg" height=400>
  
   <img src="https://i.imgur.com/AkURDLX.jpg" height=400>
</p>

### SiteMap
<img src="https://i.imgur.com/7RHbtvF.jpg" width=600 height=500>

### Interactive Prototype
[Online Mockup](https://marvelapp.com/eg24a14/screen/70858799)

* Press H to restart from Login Screen

## Weekly Update of Application 
<p align="center">
<img src="EasyGreen1.gif" height="50%">

GIF created with [KAP](https://getkap.co/).    
</p> 

## Schema 

<br> **User** *(user_id, name, email, phone_number, zip_code)*
<br> **Opens** *(user_id, account_num)*
<br> **Account** *(account_num, date_opened)*
<br> **Shares** *(account_num, post_id)*
<br> **Post** *(post_id, timestamp, media, caption)*
<br> **Creates** *(account_num, iList_id)*
<br> **Inventory** *(iList_id, count, [Ingredients])*
<br> **Holds** *(account_num, rList_id)*
<br> **Recipes** *(rList_id, count, [Recipes])*


### Models
<img src="https://i.imgur.com/nSZxnte.jpg">

### Networking
**Spoonacular API**
<br> Base URL: https://spoonacular.com/food-api/docs#Search-Recipes-by-Ingredients


| HTTP Verb | Endpoint | Description|
| -------- | -------- | -------- |
| ` GET `    | /findByIngredients     | Search Recipes by Ingredients    |
