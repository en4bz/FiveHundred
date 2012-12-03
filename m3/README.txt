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

