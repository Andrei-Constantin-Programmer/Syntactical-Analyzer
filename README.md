# Syntactical Analyzer
## What is a Compiler?
*"A compiler is a computer program that translates computer code written in one programming language (the source language) into another language (the target language)."* ([Wikipedia](https://en.wikipedia.org/wiki/Compiler))  
  
These are the main components of a fully-fledged compiler:

<img src="https://github.com/Andrei-Constantin-Programmer/Syntactical-Analyzer/blob/master/images/Compiler.png?raw=true"/>

The syntactical analyzer that I created solves part of a compiler for the Boaz language. In particular, it contains the Lexer (called Tokenizer in the project, and will be used interchangeably) and part of the Parser.

## What is a Lexer?
The Lexer is the part of the compiler that creates tokens out of the code given as input.  
For example, the following Boaz code:
```
program example
int num, sum;
char ch;
```
will be tokenized as a list of strings: "program", "example", "int", "num", "," etc.

## What is a Parser?
The Parser is the part of the compiler that deals with syntatical errors and creating a syntax tree, based on the tokens received from the Lexer.  
The parser I created for this project only contains the former, checking if the given code is correct syntactically.  

## What is Boaz?
Boaz is a language invented by my University professor, [David Barnes](https://www.kent.ac.uk/computing/people/3070/barnes-david), with the purpose to test our understanding of creating a syntactical analyzer.  
The course is based on [Nand2Tetris](https://www.nand2tetris.org/), but it has some major modifications, such as the brand new Boaz language.  

---

# Install and Run

## Prerequisites
* Java installed on the machine
* (optional) An IDE that can run Java programs (IntelliJ, Eclipse etc.)

## How to install and run
### Through an IDE
1. Clone the project in your IDE of choice from this link: https://github.com/Andrei-Constantin-Programmer/Syntactical-Analyzer.git
2. Run the 'main' method from the Main class (make sure to supply the name of the file as a string in the parameters of the method e.g.: "filename.boaz")
3. The syntactical analyzer will print either "ok" or "error" to the console, depending on whether the Boaz code was correct syntactically or not.
4. Example files are included in the project's main folder.

### From command line
1. Download the project as zip from this link: https://github.com/Andrei-Constantin-Programmer/Syntactical-Analyzer
2. Extract the zip to a location of your choosing
3. Open the terminal in that location
4. Use those commands to run the "example.boaz" file:
```
bash compile
bash parse example.boaz
```
---

# Special thanks
* Special thanks to David Barnes for setting the assignment and creating the Boaz language
* The Nand2Tetris creators for building the foundation blocks for this project
