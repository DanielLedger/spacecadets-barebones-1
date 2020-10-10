# Barebones language
Challenge 2 of the University of Southampton Space Cadets program.


The barebones language is based on four instructions:

Set a variable to zero.
clear var;

Add one to a variable.
inc var;
  
Subtract one from a variable.
dec var;
  
Loop until var is 0.
while var not 0 do;
...
...
end
  
This project is an interpreter for this very simple language.

I may also include a compiler and bytecode runner, if I have time.

Running the program:
java -jar Challenge-2.jar filename 


Important notes:
The interpreter is fairly forgiving: if a variable doen't exist, it is assumed to equal zero.
Command line switches:
-v = Turns the extra printing OFF (this will only print at the end).
-t = Time the execution of a script.
