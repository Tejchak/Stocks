    We designed our program using the classic Model, View, Controller.
Our model contains the api key so the program can access the Alpha
vantage API. Our model also contains a list of portfolios so the
program can add and store multiple portfolios. Portfolios are a
class that we created to make the storage of a stock with its shares
much simpler. Finally our model also contains a map of stocks to
their corresponding data so that Csv files and the Api do not have to
be accessed everytime when getting the stock data. Our model loops
through the stock data to find correct dates and then calculates
the gain/loss, moving average, and x-day crossover based on the
information given. Our model will contain portfolios and can
calculate the value of a requested portfolio when told to do so.

Our view has no connection at all to the model and is only called
when needed by the controller. Every displayed message reaches the
view at some point.

Our controller is the glue of our program and connects the model
and view by talking them in along with a readable so it can
understand the user inputs. Our controller handles the menus
and calls the needed functions when it needs to.

All three of these classes have an interface with useful public
methods so it will be much simpler to add new features to our code
in the future.


