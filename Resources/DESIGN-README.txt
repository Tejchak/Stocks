    Gui Changes:
    We created a new interface called GUIFeatures that extends our StockController interface.
    Similarly, we also created a new interface call IStockViewGUI and a new class called StockViewGUI.
    To interact with and read inputs from a GUI we had to change our controller design from how it
    was previously set up to work with a text based view. We chose to use the Command Callbacks
    design. This is what our GUIFeatures interface is for. GUIFeatures represents the functions
    our view should be allowed to call. We let the view call those function by calling a function
    called setView in our controller that passes in an object of type GUIFeatures to the view. This
    way it has access to only the necessary functions in the GUIFeatures. By doing it this way we
    also prevent any unwanted changes from occuring to our text-based controller that already works
    fine and the overall readability of the code is improved by having a controller for each type
    of view.


    Changes to our project:
    For this assignment we were enhancing our previous project, so we wanted to adhere to SOLID
    principals and minimize changes to our previous project as much as possible. To accomplish this
    we created a new interface called StockModelTrader that implements our old StockModel interface.
    This interface has more public methods that are implemented in the StockModelNew class. We then
    changed our controlled to take in a model of type StockModelTrader(the new interface). This way
    the controller can call all of the public methods, but we didn't have to change the old
    StockModelImpl. We also created a new class called StockControllerNew that extends the previous
    controller. Most methods in our controller stayed the same, but we did have to add new cases
    to account for the user being able to do more things (bar chart, distribution, buy/sell). This
    way our old StockControllerImpl could stay virtually the same while minimizing duplicate
    code in our new implementation. We did not create any new classes or interfaces for the
    StockView as none of the new enhancements warranted changes to the view, but we did change the
    view to take in an appendable rather than just using System.ou as we believed our previous
    design was too tightly couple with System.out and not great for testing. We also changed some
    methods in our controller to take in a LocalDate rather than a String because we realized
    converting it was more efficient than manually parsing through every single time.

    Our StockModelNew design contains an arraylist of betterPortfolios. This is similar to our
    orginal stockModel design except the betterPortfolio is class that had been enhanced to allow
    buy and selling. Each better portfolio is comprised of a String representing its name, a
    Map<String, StockPurchase> called purchases, and a Map<String, StockSale> called sales. The
    String represents the name of the stock being bought or sold and each StockPurchase/Sale
    contains the number of shares bought/sold as a double and the date it was bought/sold as
    a localDate.


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


