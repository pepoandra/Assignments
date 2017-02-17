// @author Sebastian Andrade
// COMP206
// ID 260513637



#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MSG_LENGTH 1024
#define NAME_LENGTH 512
#define ENCRYPTED_LENGTH 2000
#define PREFERED_KEY  100


char last_msg[ENCRYPTED_LENGTH]= " ";
int start = 0;
int key = 0;


void append(char* s, char c)
{
        int len = strlen(s);
        s[len] = c;
        s[len+1] = '\0';
}


void strrev(char *p)
{
  char *q = p;
  while(q && *q) ++q;
  for(--q; p < q; ++p, --q)
    *p = *p ^ *q,
    *q = *p ^ *q,
    *p = *p ^ *q;
}


int send_msg(char* msg, char* fileName, char* name){
	FILE* file = fopen(fileName, "w"); 
	char enc_msg[ENCRYPTED_LENGTH];	
	start =1;
	int i;	
	if(file)
	{

		snprintf( enc_msg, sizeof( enc_msg ), "%s%s%s%s", "[", name, "]", msg );

		for(i = 0; i< (strlen(enc_msg)); ++i){
			enc_msg[i] = (enc_msg[i] + key) % 256;
		}


		strrev(enc_msg);
		//printf(" \n %s \n" ,enc_msg);
		fprintf(file, "%s", enc_msg);
		fclose(file);	
		return 1;
	} else { return 0; } 
}


int read_msg(char* fileName){
	char msg[MSG_LENGTH];
	char c;
	int msgLength=0;
	int end_msg = 0;
	int invalid = 0;
	int closing_bracket = 0;

	FILE* file = fopen(fileName, "r");
	if(file == NULL) {
		if(start==0)
		{ 		
			//start=1;
			return 0; 
		} else { return 1; } 
	}
	
	if(file){
		while ((c = getc(file)) != EOF) {
				c-=key;
				if(c<0) c+=256;
				msg[msgLength] = c;
				//printf("%c_%i \t", c, msgLength);
			
				if(c == ']'){
					invalid =0;
					closing_bracket = msgLength;
				}

				msgLength++;
			}

		msg[msgLength]='\0';

 		strrev(msg);

		if(msg[0]!= '['){
			 invalid=1;
		}	
		

		if(msgLength==0 || invalid ==1 || msg[closing_bracket + 1] == '\n' )
			{
			//printf("Invalid msg, bud");
				if(start==0){	
					start =1;	
					return 0;
				} else {
					return 1;	
				}
			
			}

		 if(msgLength > 0) {
			fclose(file);
			
			start =1;	
			if(strcmp(msg, last_msg) != 0)
			{
				strcpy(last_msg, msg); //remember last msg
				

				printf("\nReceived: %s", msg);



				

				return 0;  //if msg received, send msg
				} else { return 1; } //if equal to last msg, keep waiting for msg
			}	
			} else {
				if(start ==0) 
				{	
					return 0; //if first run, send msg
				} else { return 1; } //if msg sent, wait for msg

		 } //if no input file created, send msg
				
}

int main(int argc, char *argv[]) 
{
    int turn; // Turn system: 0 sender, 1 receiver
    char*  input ;
    char*  output ;
    char*  name;
   // int counter=0;
    char msg[MSG_LENGTH];
 
   
   if(argc>1) {
		input  = argv[1]; /* should check that argc > 1 */
	} else {
		input = "1";
	}

   if(argc>2) {
		output   = argv[2]; /* should check that argc > 1 */
	} else {
		output = "2";
	}


   if(argc>3) {
		name   = argv[3]; 
	} else {
		name = "seb";
	}

   if(argc>4) {
		key   = atoi(argv[4]); 
	} else {
		key = PREFERED_KEY;
	}


//Print Chosen set-up
printf("\n Input file: %s", input);
printf("\n Output file: %s", output);
printf("\n Name: %s", name);
printf("\n Key: %i \n", key);


FILE* input_file = fopen(input, "r"); 
  
//If input file does not exist, start by sending a msg
if(input_file == NULL){
	turn = 0 ;
} else {  
	turn = 1 ;
	fclose(input_file);
}


 while(1)
{
	while(turn==1){
		turn = read_msg(input);
	} 

	while (turn ==0)
	{
		fflush(stdin);
		printf("\nSend: ");
		fgets (msg, MSG_LENGTH, stdin);	
		turn  = send_msg(msg, output, name);
	}
	
}

}
