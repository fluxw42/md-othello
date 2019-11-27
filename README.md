# md-othello
Othello engine

This is an Othello engine in plain simple java I once hacked together in a few evenings. 
It actually plays rather good (according to my standards :))
It needs some cleaning up and a rest interface so we can talk to it.
But the api should be rather straight forward.

There used to be a crappy ui on top of it and it still runs here http://mdothello.appspot.com/othello
That frontend code is not in this repo. But it plays so you can have an impression of the engine.

The commits to this repo will automatically be deployed to  https://md-othello.herokuapp.com and 
you can look at the rest api on
https://md-othello.herokuapp.com/swagger-ui.html#/othello-restcontroller
A good thing is that the api actually tells you the legal moves to ease your frotnend development.

Getting this running should be a breeze.
Feel free to expand it and create pull requests for the api or clean up the code (because it works, but is 
damn ugly and there are no tests).

Happy hacking!
