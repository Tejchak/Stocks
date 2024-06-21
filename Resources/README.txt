GUI version:
Our GUI version of the stock program supports creating a portfolio, buying and selling specific
quantities of specific stocks on specific dates, query the value and composition of a portfolio,
and loading and saving portfolios. These features are all readily available as buttons when the
user runs the program. As such how to use them should be pretty self-explanatory for the user
(they just need to click on the button). If the user ever is in the middle of an action and they
want to cancel it they can always just hit cancel. For example, if a user click buy stock, and they
change their mind, they can just hit the cancel button. The portfolios are available to the user
as a dropdown menu. For example, if a user clicks queryPortfolio, they will be able to select from
a drop down of their portfolios. The date is also chosen via three dropdown menus (day, month, and
year). There is only ever one window available to the user at a time. The only exception to this
is when the user wants to save or load a portfolio a window prompting them to select the file from
the computer will appear and the user will not be able to do anything else until they've selected
the file or hit the cancel button.

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
for the given stock. When doing portfolio calculations, however, the value is calculated using the
most recent trading day (and the program tells the user this before asking for the date.
For actions requiring a date range the program performs a check to make sure
 the end-dat is after the start date. In all those cases the user is given a unique message
informing them of what the problem was. The program also continues to loop until the user presses
5 at the main menu. For example, after outputting for the user the gain or loss of a stock it will
return the user to the main menu where they can perform another action or type 5 to quit the
program. The x-day moving average is only calculated with the days available in Alphavantage so
we inform the user of the furthest back date available and inform them that any prior days will not
be used in its calculation. The same also for the x-day moving crossover.
With the new changes the user is now able to buy and sell shares of stock on given dates. They must
sell shares in strictly chronological order (i.e. 2015 then 2016). If the user tries to go back and
sell shares before their previous sell date a message will appear asking if the user would like to
get rid of the shares they have after the current sellDate, or if they want to type in a new date.
Our program can also give the distribution of a portfolio on a given date. The distribution will
contain the name of the Stocks in given portfolio, the value of each of those stocks in the
portfolio, and those value added up equal the total value of the portfolio. If the user wants to
see the total value of a portfolio over time they can graph it as a bar chart. The bar chart will
take in the name of the portfolio they want to see, and the start and end date they are looking at.
One of the best of features of our program is that a user can re-balance a portfolio to contain
the weighting of there choosing for every stock, and the program will automatically sell and
purchase the correct amount of each. The controller will ask the user to enter the weight for
each stock one by one and check that the weights add up to one.