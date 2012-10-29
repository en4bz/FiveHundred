Design decisions:

	1) A Team object composed of two Player objects
	2) An abstract class called APlayer which abstracts over both human and robot players
	3) Different thresholds for trump and no trump bids
	4) Players should only be able to add or remove cards from their hand one at a time. I.E no setHand() method
	
Testing decisions:

	1) When testing card exchanges, cards must always be exchanged in a particular manner, and thus it was possible to use assert equals.
	2) When testing bidding, bids follow a specific algorithm and thus although it may have been difficult to determine the strength of the
	bid, it was usually possible to determine the correct suit.
	3) As far as playing was concerned, even though the algorithm returns a single card, given the same situation multiple times, the algorithm
	has the possibility of returning different cards, and thus each test was run 32 times to prevent any standard deviation from interrupting