# EWallet - Android App

### Summary
This is a portfolio project that demonstrates the use of Compose, Coroutines, Hilt, Jetpack Navigation and ViewModel in a Clean Architecture with MVI for the presentation layer.

### App Functionality
The app fetches the top 50 Etherium tokens then displays the amount of each token you possess as you search. Searching for "p" will display all tokens from the top 50 that start with "p", typing additional letters will filter the list further.

The app handles all Api errors as stated in the API documentation
* https://info.etherscan.com/api-return-errors/
* https://github.com/EverexIO/Ethplorer/wiki/Ethplorer-API

### Technical Challenges
There is a rate limit of 5 network calls per minute. A two pronged solution has been created for this:
* A delay of 200ms is applied whenever more than 5 network calls are requested.
* If a 429 rate limit is still returned then a retry with exponential backoff is implemented. 

## Tech-Stack

* Kotlin
* Dagger Hilt
* Coroutines
* Compose
* Architecture
  * Clean Architecture
  * MVI
* Jetpack Navigation 
* Testing
  * JUnit5
  * Mockk

## Screen Shots

| Home Screen | Empty Search Screen | Populated Search Screen |
| :---:       |    :----:           |          :---:          |
|<img src="https://i.imgur.com/jZj8A7m.jpeg" width="60%" height="60%" align="centre">|<img src="https://i.imgur.com/wT4LRdS.jpeg" width="60%" height="60%" align="centre">|<img src="https://i.imgur.com/Pt4Ojac.jpeg" width="60%" height="60%" align="centre">|

 | Filtered Search Screen | No Results Search Screen | IO Exception Search Screen |
 |         :---:          |          :---:           |          :---:             |
 |<img src="https://i.imgur.com/EGkSJum.jpeg" width="60%" height="60%" align="centre">|<img src="https://i.imgur.com/lYJ1hJq.jpeg" width="60%" height="60%" align="centre">|<img src="https://i.imgur.com/QaDnCDf.jpeg" width="60%" height="60%" align="centre">|

## To Run The App
You will need to get an api key for etherscan and a wallet address: <br/>
```
ETHPLORER_API_KEY = freekey
ETHERSCAN_API_KEY = {api_key}
WALLET_ADDRESS = {wallet_address}
```

