Design Decisions:
1.) Ditched the old logger for a much more robust one. Namely log4j.
2.) The GameFrame acts as a controller in the MVC model, as such it 
implements both Observer and Observable and is in charge of the following:
    * listen to the gameEngine for state changes (see the enum GameEngine.State)
    * update the views in GameBoard.java (see 3)
    * keep track of the prompt states (bid, discard, play or none) and change 
    the toolbar according to these states (unless the autoPlay boolean is true)  
3.) If a human is playing and the user must be prompted for input, the following happens:
    * notify the gameFrame of a prompt state change from the play method in HumanPlayer
    * put a lock on the HumanPlayer object by calling wait()
    * the notified GameFrame updates the ActionToolbar and the GameBoard so that the 
    user can now make an action
    * as soon as an action is completed, the gameFrame is notified
    * the gameFrame validates the input
    * if the input is valid, it unlocks the HumanPlayer by calling aHuman.notify() in
    a synchronized block and sets the promptState to none. The HumanPlayer's play method 
    can now return so that the game engine can proceed.
    * if the input is invalid (the user might be notified via a dialog), wait for the 
    user to do something
4.) Autoplay does not substitute the HumanPlayer by a RobotPlayer per se. Rather,
    instead of notifying the ActionToolbar and GameBoard to listen to actions it
    uses AdvancedRobot Strategy classes to obtain the "result" of the action. It
    then uses sync() extensively because it must unlock the HumanPlayer object.
5.) All action panels are actually anonymous classes implementing Observer.
6.) Used Locale for internationalization


AI Decisions:
1.) If the robot is leading and in a no trump game, it looks at the discards and the
widow (if the contractor). It then goes through the hand and finds any winning cards,
and if it cannot find any winning cards, it plays a random low card.
2.) If the robot is leading in a trump game, if the robot is the contractor it will
attempt to get rid of any trumps that are still in the game and not in the discards/it's hand.
It all the trumps are contained, he will try to win the hand with a non trump card, and if
it cannot, it will play a random low card.
3.) If the robot is leading in a trump game and it is not the contractor, it will try and
play a card that will win and if not will return a random low card.
4.) If the robot is following, there are two outcomes. The first outcome is if the robot
is the third player, in which case if it can beat the winning card, it will play its highest
card. However, if the robot is the second or the fourth player, the robot will play a card just
high enough to beat the winning card. If the robot cannot beat the card at all, then it
will play the lowest possible card that it is allowed to.
5.) For CardExchange in a trump game, the robot first keeps all jokers, then keeps all trumps. 
If the retained cards are not yet ten, it then attempts to keep all runs (From Ace to Queen) in
non trump suits. If this still does not satistfy the conditions, then if there is a possibility
of a King with a smaller card, it keeps those. It then keeps a card based on whether the partner
bid or not to at some point return a card to the partner should the partner have the ace of that suit.
Finally it gets rid of all Singletons and Doubletons. If all these conditions do not meet the requirements
it removes random low cards.

