
/////   Sebastian Andrade
///     ECSE 427
//      Assignment #1
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <dirent.h>
#include <sys/wait.h>
#include <errno.h>
#include <stdbool.h>
#include <sys/types.h>
#include <signal.h>
# include <time.h>

//// Constant declaration
#define ERROR 1
#define SUCCESS 0
#define BUFFER_SIZE 1024
 
/// Variable declaration
int static  out;
int static save_out; 
int static file_redirect;
pid_t child_id;
struct node *head = NULL; //Shell
struct node *current = NULL; // Buffer
int static keys=1;
int static foreground = -1;

////// Method declaration
void printList();
void insertFirst(pid_t pid, char *cmd);
bool isEmpty();
int length();
struct node* find(int key);
struct node* delete(int key);
void clean_list();
int getcmd(char *prompt, char *args[], int *background);
void open_redirect(char *output_file);
void close_redirect();
void initHandler(int num);
 
//////////////////////// Linked list for jobs implementation
struct node {
   pid_t pid;
   int key;
   char *cmd;
   struct node *next;
};

////////////// Signal handler methods
void initHandler(int dummy) {
    if(!isEmpty() && foreground != -1){
        struct node *buff;
        buff = find(foreground);
        foreground = -1;
        kill(buff->pid, SIGTERM);
    }
}

//display the list
void printList() {
   clean_list();
   struct node *ptr = head;
   printf("\n[");
	
   //start from the beginning
   while(ptr != NULL) {
      printf("(key:%d,pid:%ld,%s)\t",ptr->key,(long)ptr->pid, ptr->cmd);
      ptr = ptr->next;
   }
   printf("]");
}

//insert link at the first location
void insertFirst(pid_t pid, char *cmd) {
   //create a link
   struct node *link = (struct node*) malloc(sizeof(struct node));
	
   link->key = keys;
   keys++;
   link->cmd = cmd;
   link->pid = pid;
	
   //point it to old first node
   link->next = head;
	
   //point first to new first node
   head = link;
}

//is list empty
bool isEmpty() {
   return head == NULL;
}

int length() {
   int length = 0;
   struct node *current;
   for(current = head; current != NULL; current = current->next) {
      length++;
   }
   return length;
}


//find a link with given key
struct node* find(int key) {
   //start from the first link
   struct node* current = head;

   //if list is empty
   if(head == NULL) {
      return NULL;
   }

   //navigate through list
   while(current->key != key) {
	
      //if it is last node
      if(current->next == NULL) {
         return NULL;
      } else {
         //go to next link
         current = current->next;
      }
   }      
	
   //if data found, return the current Link
   return current;
}

//delete a link with given key
struct node* delete(int key) {

   //start from the first link
   struct node* current = head;
   struct node* previous = NULL;
	
   //if list is empty
   if(head == NULL) {
      return NULL;
   }

   //navigate through list
   while(current->key != key) {

      //if it is last node
      if(current->next == NULL) {
         return NULL;
      } else {
         //store reference to current link
         previous = current;
         //move to next link
         current = current->next;
      }
   }

   //found a match, update the link
   if(current == head) {
      //change first to point to next link
      head = head->next;
   } else {
      //bypass the current link
      previous->next = current->next;
   }    
   free(current);
   return current;
}

//deletes nodes reprensenting finished processes
void clean_list(){
    struct node *ptr = head;

    while(ptr != NULL) {
        //pid_t pid = waitpid(ptr->pid, NULL, WNOHANG);

        if(waitpid(ptr->pid, NULL, WNOHANG)!= 0){
            printf("deleted %d", ptr->key);
            delete(ptr->key);
        }
        ptr = ptr->next;
   }
}
//////////////////////// End Linked list for jobs implementation


int getcmd(char *prompt, char *args[], int *background)
{
    int length, i = 0;
    char *token, *loc;
    char *line = NULL;
    size_t linecap = 0;
    printf("%s", prompt);
    length = getline(&line, &linecap, stdin);
    if (length <= 0) {
        exit(-1);
    }
// Check if background is specified..
    if ((loc = index(line, '&')) != NULL) {
        *background = 1;
        *loc = ' ';
    } else
        *background = 0;
    

    while ((token = strsep(&line, " \t\n")) != NULL) {
        for (int j = 0; j < strlen(token); j++)
            if (token[j] <= 32)
        token[j] = '\0';
        if (strlen(token) > 0)args[i++] = token;
    }
return i;
}
 
/// Sets up output to specified file
void open_redirect(char *output_file){

                out = open(output_file, O_RDWR|O_CREAT|O_APPEND, 0600);
                if (-1 == out) { perror("opening file"); }
                save_out = dup(fileno(stdout));
                if (-1 == dup2(out, fileno(stdout))) { perror("cannot redirect stdout"); }
}

//////Resets output to standard out
void close_redirect(){
    fflush(stdout); 
    close(out);
    dup2(save_out, fileno(stdout));
    close(save_out);
    file_redirect = -1;
}

int main(void)
{
    char *args[20];
    int bg;
    file_redirect = -1;    
    int reset;
    time_t now;
    srand((unsigned int) (time(&now)));


    //Catching CTRL+C and ignoring CTRL+Z
    signal(SIGINT, initHandler);
    signal(SIGTSTP,SIG_IGN);



    while(1) {
    //clear all arguments and set-up variables before getting new cmd

        reset=0; //If last cmd redirected output, set stdout back to normal
        bg = 0;  
        foreground = -1;
        for (int i = 0; i < 20; i++){
            args[i] = NULL;
        }
        // Retrieve command 
        int cnt = getcmd("\n>> ", args, &bg);
        
        // If no input, ask for new command
        if(args[0]==NULL){
            continue;
        }

        // look for ">" in cmd        
        for(int i = 0; i < cnt; ++i){
            if( strcmp(args[i], ">") == 0){
                file_redirect = i + 1;
                break;
            }
        }

        //If no ">", add a NULL at the end of the cmd
        if(file_redirect == -1){
            args[cnt]=NULL;

        //else, add a NULL instead of ">"
        } else {
            args[file_redirect-1]=NULL;
            open_redirect(args[file_redirect]);
        }

       /*     if(bg){
                printf("Am I here?");
            int w, rem;
            w = rand() % 10;
           rem = sleep(w);
            //handles interruption by signal

            while(rem!=0){
                rem=sleep(rem);
            }      
            }   */
       // }
        // Built-in cmds
        if( strcmp("exit", args[0]) ==0) { 
            kill(0, SIGTERM);
        }
        
        // Print current working directory
        if( strcmp("pwd", args[0] ) ==0) { 
            char* cwd;
            char buff[BUFFER_SIZE];
            cwd = getcwd(buff, BUFFER_SIZE);
            printf("The current directory is  %s ", cwd);
            reset++;                
        }

        // Prints a list of all jobs in execution
        if( strcmp("jobs", args[0] ) ==0) { 
            printf("Current background jobs");
            printList();
        reset++;                
        }

        // Brings the specified job to foreground
            if( strcmp("fg", args[0] ) ==0) { 
            int num = atoi(args[1]);
            
            if(num > 0){
                foreground = num;
                struct node *c = find(num);
                pid_t new_fg = waitpid(c->pid, NULL, 0);
            } 
            reset++;                
        }

        // Changes directory
        if( strcmp("cd", args[0] ) ==0) { 
            char* path;
            path = args[1];
            if(path == NULL){
                path = getenv("HOME");
            }
            int cd = chdir(path);
        reset++;                
        }

        //If any built-in command was executed, restart the loop
        if(reset!=0){
            //Reset output redirection if necessary
            if(file_redirect!=-1){
                close_redirect();
            }
            continue;
        }
        ///End of built-in cmds          

        //External cmds
        child_id = fork();

        if( child_id ==  0) { //Child

            // Set-up signals handler before execvp
            signal(SIGINT, SIG_IGN);
            signal(SIGTSTP,SIG_IGN);

            //Execute command args[0]
            int c =  execvp(args[0], args);

            //Catch errors, if any
            if(c == -1){
                printf("Error during execvp\n");
                printf("\n error: %i", errno);
                exit(errno);
            }
        }   else 
        { //Parent
            
            //Reset output redirection if necessary
            if(file_redirect != -1){
                close_redirect();
            }
                // Foreground process
                if (bg == 0) { 
                    int status;
                    //Wait to child process to terminate
                    pid_t pid = waitpid(child_id, &status, WNOHANG | WUNTRACED);
                    pid = waitpid(child_id, &status, 0);

                //Backgground process
                } else {  
                    //Insert child to linked list and restart loop
                    insertFirst(child_id, args[0]);
                }
            }         
    }
}
