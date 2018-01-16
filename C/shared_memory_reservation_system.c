//Sebastian Andrade
// ID 260513637

#define _SVID_SOURCE
#define _BSD_SOURCE
#define _XOPEN_SOURCE 500
#define _XOPEN_SORUCE 600
#define _XOPEN_SORUCE 600

#include <semaphore.h>
#include <pthread.h>
#include <fcntl.h>
#include <sys/shm.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>

#define BUFF_SIZE 20
#define BUFF_SHM "/OS_BUFF_260513637"
#define BUFF_MUTEX_A "/OS_MUTEX_A_260513637"
#define BUFF_MUTEX_B "/OS_MUTEX_B_260513637"
#define NAME_LENGTH 10
#define SECTION_A_START 100
#define SECTION_A_END 109
#define SECTION_B_START 200
#define SECTION_B_END 209
#define MAX_SLEEP_TIME 1
#define MAX_CMD_LENGTH 100

//declaring semaphores names for local usage
sem_t *mutexA;
sem_t *mutexB;

//declaring the shared memory and base address
int shm_fd;
void *base;

//structure for indivdual table
struct table
{
    int num;
    char name[NAME_LENGTH];
};

void initTables(struct table *base)
{
    int i;
    //capture both mutexes using sem_wait
        sem_wait(mutexA);
        sem_wait(mutexB);
    
    //initialise the tables with table numbers
    for(i = 0; i < BUFF_SIZE/2; ++i){
        struct table section_a, section_b;
        struct table *ptr_a, *ptr_b, *sa, *sb;
    
        //initializing table numbers
        section_a.num = SECTION_A_START + i ;
        section_b.num = SECTION_B_START + i ;

        //initializing pointers to tables
        sa = &section_a;
        sb = &section_b;
        
        //Allocating memory in shared memory
        ptr_a =  (base + (i * sizeof(struct table)));
        ptr_b =  (base + (i * sizeof(struct table)) + (BUFF_SIZE/2) * sizeof(struct table));

        //copy initialized tables into shared memory
        memcpy(ptr_a, sa, sizeof(struct table));
        memcpy(ptr_b, sb, sizeof(struct table));

    }

    //perform a random sleep  
    sleep(rand() % MAX_SLEEP_TIME);

    //release the mutexes using sem_post
    sem_post(mutexA);
    sem_post(mutexB);

    return;
}

void printTableInfo(struct table *base)
{

    //capture both mutexes using sem_wait
        errno = 0;
        if (sem_wait(mutexA)==-1){
        printf("Sem_error:  %s", strerror(errno));
        }
        sem_wait(mutexB);

    //print the tables with table numbers and name
        int i;
        printf("\nSection A: \n");
        for(i = 0; i < BUFF_SIZE/2; ++i){
           struct table section_a;
            section_a =  *(base + (i * sizeof(struct table)));
            printf("\tTable: %d Name: %s \n" ,section_a.num, section_a.name);

        }

        printf("\nSection B: \n");
        for(i = 0; i < BUFF_SIZE/2; ++i){
           struct table section_b;
            section_b =  *(base + (i * sizeof(struct table)) + (BUFF_SIZE/2) * sizeof(struct table));
            printf("\tTable: %d Name: %s \n" ,section_b.num, section_b.name);
        }
    
    //perform a random sleep  
    sleep(rand() % MAX_SLEEP_TIME);
    
    //release the mutexes using sem_post
    sem_post(mutexA);
    sem_post(mutexB);

    return; 
}

void reserveSpecificTable(struct table *base, char *nameHld, char *section, int tableNo)
{
    switch (section[0])
    {
    case 'a':
    case 'A':
        //capture mutex for section A
         sem_wait(mutexA);
        
        //check if table number belongs to section specified
        if(tableNo > 109 || tableNo < 100){
            printf("\nInvalid table number.\n");
            sem_post(mutexA);
            return;
        }        

        //reserve table for the name specified
        //if cant reserve (already reserved by someone) : print "Cannot reserve table"
        if(strcmp((base + sizeof(struct table)* (tableNo - 100))->name, "\0")==0){
            strcpy((base + sizeof(struct table)* (tableNo - 100))->name, nameHld);
        } else {
            printf("Cannot reserve table");
            sem_post(mutexA);

            return;
        }
      
       // release mutex
       sem_post(mutexA);
        break;
    case 'b':
    case 'B':
        //capture mutex for section B
            sem_wait(mutexB);
        
        //check if table number belongs to section specified
        //if not: print Invalid table number 
        if(tableNo > 209 || tableNo < 200){
            printf("\nInvalid table number.\n");
            sem_post(mutexB);
            return;
        }        
        
        //reserve table for the name specified ie copy name to that struct
        //if cant reserve (already reserved by someone) : print "Cannot reserve table"
       if(strcmp( (base + (tableNo - 190)*sizeof(struct table))->name  , "\0")==0){
            strcpy( (base + (tableNo - 190)*sizeof(struct table))->name   , nameHld);
        } else {
            printf("Cannot reserve table");
            sem_post(mutexB);
            return;
        }

       // release mutex
       sem_post(mutexB);
       break;

       default: 
        printf("\nInvalid section.\n");
        break;    
    }
    return;
}

void reserveSomeTable(struct table *base, char *nameHld, char *section)
{
    int idx = -1;
    int i;
    switch (section[0])
    {
    case 'a':
    case 'A':
        //capture mutex for section A
            sem_wait(mutexA);
    
        //look for empty table and reserve it ie copy name to that struct

        for(i = 0; i < BUFF_SIZE /2 ; ++i){    
            if(strcmp((base + sizeof(struct table)* i)->name, "\0")==0){
                strcpy((base + sizeof(struct table)* i)->name, nameHld);
                 i+=100;
            //    printf("\nTable #%d has been reserved. \n", i);
            } 
        }

        //if no empty table print : Cannot find empty table
        if(i < 100){
            printf("\n. Cannot find empty table. \n");
        }
        //release mutex for section A
        sem_post(mutexA);
        break;
    case 'b':
    case 'B':
        //capture mutex for section B
            sem_wait(mutexB);
    
        //look for empty table and reserve it ie copy name to that struct
        for(i = 0; i < BUFF_SIZE /2 ; ++i){    
            if(strcmp((base + (BUFF_SIZE/2) * sizeof(struct table) + sizeof(struct table)* i)->name, "\0")==0){
                strcpy((base + (BUFF_SIZE/2) * sizeof(struct table) + sizeof(struct table)* i)->name, nameHld);
                i+=200;
             //   printf("\nTable #%d has been reserved. \n", i);
            } 
        }
      
        //if no empty table print : Cannot find empty table
        if(i < 100){
            printf("\n. Cannot find empty table. \n");
        }


        //release mutex for section B
        sem_post(mutexB);
        break;
    }
}

int processCmd(char *cmd, struct table *base)
{
    char *token;
    char *nameHld;
    char *section;
    char *tableChar;
    int tableNo;
    token = strtok(cmd, " ");
    switch (token[0])
    {
    case 'r':
        nameHld = strtok(NULL, " ");
        section = strtok(NULL, " ");
        tableChar = strtok(NULL, " ");

        if(nameHld == NULL || section == NULL){
            printf("Name and section cannot be empty. \n");
            return 1;
        }


        if (tableChar != NULL)
        {
            tableNo = atoi(tableChar);
            reserveSpecificTable(base, nameHld, section, tableNo);
        }
        else
        {
            reserveSomeTable(base, nameHld, section);
        }
        sleep(rand() % MAX_SLEEP_TIME);
        break;
    case 's':
        printTableInfo(base);
        break;
    case 'i':
        initTables(base);
        break;
    case 'e':
        return 0;
    }
    return 1;
}



int main(int argc, char * argv[])
{
    int fdstdin;
    int input;
    errno = 0;


    // file name specifed then rewire fd 0 to file 
    if(argc>1)
    {
        //store actual stdin before rewiring using dup in fdstdin
        fdstdin = dup(fileno(stdin));
        input = open(argv[1], O_RDONLY, 0666);

        //perform stdin rewiring as done in assign 1
        if (-1 == dup2(input, fileno(stdin))) { perror("cannot redirect stdin"); }
    }


    //open mutex BUFF_MUTEX_A and BUFF_MUTEX_B with inital value 1 using sem_open
    mutexA = sem_open (BUFF_MUTEX_A, O_CREAT, 0666, 1); 
    mutexB = sem_open (BUFF_MUTEX_B, O_CREAT , 0666, 1); 


    //opening the shared memory buffer ie BUFF_SHM using shm open
    shm_fd = shm_open(BUFF_SHM, O_RDWR | O_CREAT, 0666 ); //S_IRWXU 
    if (shm_fd == -1)
    {
        printf("prod: Shared memory failed: %s\n", strerror(errno));
        exit(1);
    }

    //configuring the size of the shared memory to sizeof(struct table) * BUFF_SIZE using ftruncate
    struct stat shm_info;
    fstat(shm_fd, &shm_info);
    if(shm_info.st_size <= 0)
    {
        ftruncate(shm_fd, BUFF_SIZE * sizeof(struct table));
    }

    //map this shared memory to kernel space
     base = mmap(NULL,  BUFF_SIZE * sizeof(struct table), PROT_READ | PROT_WRITE, MAP_SHARED, shm_fd, 0);


    if (base == MAP_FAILED)
    {
        printf("prod: Map failed: %s\n", strerror(errno));
        close(shm_fd);
        
        // close and shm_unlink?
        exit(1);
    }

    //intialising random number generator
    time_t now;
    srand((unsigned int)(time(&now)));

    //array in which the user command is held
    char cmd[MAX_CMD_LENGTH];
    int cmdType;
    int ret = 1;
    while (ret)
    {
        printf("\n>>");

       if( fgets(cmd, MAX_CMD_LENGTH , stdin)==NULL){
       }

        if(argc>1)
        {
            printf("Executing Command : %s\n",cmd);
        }
        ret = processCmd(cmd, base);
    }
    
    //close the semphores
    sem_close(mutexA);
    sem_close(mutexB);
    
    //reset the standard input
    if(argc>1)
    {
        //using dup2
        close(input);
        dup2(fdstdin, fileno(stdin));
        close(fdstdin);
    }

    //unmap the shared memory
    munmap(base, BUFF_SIZE * sizeof(struct table));
    close(shm_fd);
    return 0;
}
