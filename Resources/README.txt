

Upon running the main file our project can create a text-based user interface asking the user what
what exactly it is they would like to do. The user can choose to examine the gain/loss of stock over
a specified period, they examine the x-day moving average of a stock for a specified date and a
specified value of x, they can determine which days are x-day crossovers for a specified stock over
a specified date range and a specified value of x, and they can create
one or more portfolios with shares of one or more stock, and find the value of that portfolio on
a specific date. Whenever a user wants data for a stock our program will first try and fetch it from
the list of stocks we have saved in our StocksInfo database, and if it is not there we look for it
in AlphaVantage. Every time the programs fetches stock data, that data is automatically saved to a
field in the model, so that it can be recalled at any time without internet or an API request. Our
program can also perform input validation to account for any user mistakes. If the stock input
by the user cannot be found in both our StocksInfo folder and Alphavantage, the program informs the
user and asks them to type in a different stock. Dates can also be a little tricky so we give the
user an example of a valid date and ask them to try again if they type in an incorrect date. The
program also prompts the user to type a different date if they date they typed was not a trading day
for the given stock. For actions requiring a date range the program performs a check to make sure
 the end-dat is after the start date. In all those cases the user is given a unique message
informing them of what the problem was. The program also continues to loop until the user presses
5 at the main menu. For example, after outputting for the user the gain or loss of a stock it will
return the user to the main menu where they can perform another action or type 5 to quit the
program. The x-day moving average is only calculated with the days available in Alphavantage so
we inform the user of the furthest back date available and inform them that any prior days will not
be used in its calculation. The same also for the x-day moving crossover.