#include <iostream>
#include <string>
#include <string.h>
#include <stdio.h>
#include <sstream>
#include <vector>
#include <iterator>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
using namespace std;
volatile sig_atomic_t flag = 0; // For Ctrl+C

// A string's array for commands "cd","help","exit"
string builtin[] =
{
  "cd",
  "help",
  "exit"
};
//The size of the array builtin_str
int num_builtins() {
  return 3;
}
// Change the signal Ctrl + C so that it works like the Linux-terminal
void catch_ctrlc(int sig_num)
{
  flag = 1;
}
// Change the directory/folder of the terminal's shell
int cd(char **args)
{
  char directory[1024];
  if (args[1] == NULL)
  {
    cout<<"Expected argument to \"cd\"\n"<<endl;;
  }
  else
  {
    if (chdir(args[1]) != 0)
    {
       perror("Error");
    }
    else
    {
        getcwd(directory, sizeof(directory));
        cout<<"In "<<directory<<endl;
    }
  }
  return 1;
}
//Display the built-ins commands
int help(char **args)
{
  int i;
  cout<<"The commands are: "<<endl;
  for (i = 0; i <num_builtins(); i++)
  {
    cout<<builtin[i]<<endl;
  }
  return 1;
}
//Close the MyShell program
int exit(char **args)
{
    cout<<"****************************"<<endl<<"   Exit From Shell    "<<endl<<"****************************"<<endl;
    exit(EXIT_SUCCESS);
}
// Splitting a string by whitespace
vector<string> split(string line)
{

      istringstream buf(line);
      istream_iterator<string> beg(buf), end;
      vector<string> tokens(beg, end);
      return tokens;
}
// Checking for '&'. Execution of program that user gave.
// We call fork to start process and take process ID.
// If ID is 0, we start the program. When it ends, we close process so we don't have Zombie Processes.
// If ID is smaller than 0, then we print Error
// If ID is greater than 0, then if '&' exists , the parent's process waits.
void LinuxCommand(char **args,int commandsize)
{
      int status=1;
      pid_t pid;
      int background = 0;
      if(strcmp(args[commandsize-1], "&") == 0)
      {
            background = 1;
            args[commandsize-1] = NULL;
      }
      pid = fork();
      if (pid == 0)
      {
        // Child process
        if (execvp(args[0], args) <0)
        {
           cout<<"-----> "<<args[0]<<" : Command Not Found"<<endl;
        }
        exit(EXIT_SUCCESS);
      }
      else if (pid < 0)
      {
        // Error forking
        perror("Error");
      }
      else
      {
        if(background==0)
        {
            while (wait(&status) != pid)
            {
                if(flag) // Ctrl+C
                {
                    exit(EXIT_SUCCESS);
                }
            }
        }
      }
}
// Check if the user's input is a built-in's or Linux's command.
// Call the matching functions
int execute(char **args,int commandsize)
{
  int i;

  if (args[0] == NULL) {
    // An empty command was entered.
    return 1;
  }

  for (i = 0; i < num_builtins(); i++)
  {
    if (strcmp(args[0], builtin[i].c_str()) == 0)
    {
       if(builtin[i]=="exit")
       {
           return exit(args);
       }
       else if(builtin[i]=="help")
       {
           return help(args);
       }
       else
       {
           return cd(args);
       }
    }
  }
  LinuxCommand(args,commandsize);
  return 1;
}
// Main Function
int main()
{
    signal(SIGINT, catch_ctrlc);
    string answer;
    vector<string> args;
    int commandsize=0; //The size of user's input
    while(1)
    {
        cout<<"MyShell$ ";
        getline(cin,answer); //User's input
        if(!answer.empty())
        {
                args=split(answer); //Splitting
                commandsize=args.size();
                char **argv =  new char*[args.size()+2];
                argv [0] = const_cast<char*>(args[0].c_str());
                for (int unsigned j = 0;  j < args.size();  ++j)
                     argv [j+1] = const_cast<char*>(args[j+1] .c_str()); // Convert string args to char **
                argv [args.size()] = NULL;
                execute(argv,commandsize); //Execution
                delete[] argv; //Clear Memory's space
                flag=0;
        }
    }
    return 0;
}
