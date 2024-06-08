Once you are in folder containing the jar file (Assignment4Stocks_jar), you can run it by typing
-jar Assignment4Stocks.jar.
Please make sure that the folder you are in contains the stocks info as that is our local
database to retrieve stocks from if the Alphavantage API is not working. To create a program when
the program starts type 4 for the portfolio option, then type 1 to create a new portfolio, enter
the name of your profile, enter the name of a stock to add to your portfolio, enter the amount of
shares of that stock that you want to add and you will have successfully created your portfolio. To
give it another stock again type 4, then type 2 to add stock to your newly created portfolio, type
the name of that portfolio, the stock you want to add and the amount of shares you want to add,
and repeat that process to have a portfolio with 3 different stocks with as many shares as you want.
You would follow the same process to create a portfolio with two different stocks. Our program
supports all the stocks available through Alphavantage's free api key. We also have in our
StocksInfo folder the stocks AAL.csv, AAPL.csv, ADBE.csv, AMC.csv, AMD.csv, AMZN.csv, BA.csv,
DIS.csv, F.csv, GE.csv, GME.csv, GOOG.csv, HD.csv, L.csv, LULU.csv, META.csv, MSFT.csv, NIO.csv,
NKE.csv, NVDA.csv, RIVN.csv, SBUX.csv, T.csv, TSLA.csv. These stocks are known to be quite popular
so we wanted a way to access their data even when the api key can't send querries and when there
is no wifi. Our stocks don't have any restriction on which data is available, the user can obtain
whatever data is available on Alphavantage, which is usually all the way back to 2013.