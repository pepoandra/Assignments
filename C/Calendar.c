/*
* 	COMP 206 	ASSIGNMENT #2		PART 2		ANDRADE SEBASTIAN	260513637
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <ctype.h>


const int MONTHS_IN_YEAR = 12;
const int DAYS_IN_MONTH = 30;
const int DAYS_IN_WEEK = 7;
const int STARS_SPACE = 3;

char *months[12];
char *days[7];
int star_line;

int get_digits(int integer);
int is_number(char number[]);
void pad(int limit);
void calendar();
void starline();


// Returns # of digits of an integer
int get_digits(int integer) {
	int counter = 1;
	while (integer > 9) {
		counter++;
		integer /= 10;
	}
	return counter;
}

// Check whether argument is a positive number. 
int is_number(char number[]) {
	// Check for sign
	if (number[0] == '-')
		return 0;
	for (int i =0 ; number[i] != 0; i++) {
		if (!isdigit(number[i]))
			return 0;
	}
	return 1;
}




//Prints the whole calendar
void calendar(int DAY_SIZE, int DAY_OFFSET) {

	int counter = 0;
	int i = 0;
	int j = 0;
	int k = 0;

	while (counter < MONTHS_IN_YEAR) {

		// Set up counter variables
		i = 0;
		j = 0;
		k = 0;

		if (DAY_OFFSET > 7) {
			DAY_OFFSET = 1;
		}
		int difference;

		// Print one starline
		starline();
		printf("*");

		// Print months
		printf("\n* %s\n", months[counter]);

		// Print one starline
		starline();
		printf("*\n");

		// Print days of the week
		for (i = 0; i < DAYS_IN_WEEK; i++) {
			printf("*");
			if (strlen(days[i]) < DAY_SIZE && (i%6 != 0 )) {
				//If day length is less than desired, add padding
				printf(" %s", days[i]);
				difference = DAY_SIZE - strlen(days[i]);
				pad(difference);
			} else if (strlen(days[i]) >= DAY_SIZE && (i%6 != 0 )) {
				printf(" %s ", days[i]);
			}

			// Last day of the week, insert new line
			if (i == 6) {
				printf(" %s\n", days[i]);
			}
		}

		// Print line of stars
		starline();
		printf("*\n");
		int padded_days = 0;

		// Print out empty days before the start of next month
		for (padded_days = 0; padded_days < (DAY_OFFSET - 1); padded_days++) {
			printf("* ");
			for (i = 0; i < DAY_SIZE; i++) {
				printf(" ");
			}
			printf(" ");
		}
		k = 0;

		int days_current_month_offset = DAYS_IN_MONTH + padded_days;
		while (padded_days < days_current_month_offset) {
			padded_days++;
			k++;

			// Print day
			printf("* %i", k);

			// Pad spaces based on day size 
			int n_digits = get_digits(k);
			int pad_until = DAY_SIZE - n_digits;
			pad(pad_until);

			// Enter new line after week ends
			int mod_day = padded_days % DAYS_IN_WEEK;
			if (mod_day == 0 && k != DAYS_IN_MONTH) {
				printf(" \n");
			}

			// Pad last days of the month
			if (k == DAYS_IN_MONTH && mod_day != 0) {
				difference = DAYS_IN_WEEK - (mod_day);
				for (i = 0; i < (difference); i++) {
					printf("*  ");
					pad(DAY_SIZE - 1);
				}
			}
		}

		printf("\n");
		// Increase day offset & month counter
		counter++;
		DAY_OFFSET++;
	}
}



//Prints a line of starts
void starline() {
	int i = 0;
	for (i = 0; i < (star_line); i++) {
		printf("*");
	}
}

//Prints as many empty spaces as needed
void pad(int limit) {
	int j = 0;
	for (j = 0; j <= (limit); j++) {
		printf(" ");
	}
}


int main(int argc, char *argv[]) {

	int i = 0;
	int j = 0;
	int k = 0;
	char *token;

	// Make sure user entered valid arguments. 
	if (argc != 4 || is_number(argv[2])==0 ||  is_number(argv[3]) == 0 ) {
		printf("Invalid arguments.\n");
		return 1;
	}

	int DAY_SIZE = strtol(argv[2], NULL, 0);
	int DAY_OFFSET = argv[3][0] - '0';

	if (DAY_OFFSET > 7 || DAY_OFFSET < 1 ||DAY_SIZE < 2 ) {
		printf("Invalid arguments.\n");
		return 1;
	}

	FILE *idioms;
	int isFile = access(argv[1], F_OK);
	
	//Check if it's a valid file
	if (isFile == 0) {
		idioms = fopen(argv[1], "r");
		char line[100];

		//Parse information from language file line by line
		while (fgets(line, sizeof line, idioms) != NULL) {
			//First line
			if (j == 0) {
				token = strtok(line, " ,.-");
				for (i = 0; (i < sizeof(days)) && (token != NULL); i++) {
					int len = strlen(token);
					days[i] = malloc(len);
					strncpy(days[i], token, len);
					token = strtok(NULL, " ,.-\n");
				}
			}
			//Second line
			if (j == 1) {
				token = strtok(line, " ,.-");
				for (i = 0; (i < sizeof(months)) && (token != NULL); i++) {
					int len = strlen(token);
					months[i] = malloc(len);
					strncpy(months[i], token, len);
					token = strtok(NULL, " ,.-\n");
				}
			}
			++j;
		} 
		fclose(idioms);

	} 

	star_line = DAYS_IN_WEEK * (STARS_SPACE + DAY_SIZE) - 1;

	calendar(DAY_SIZE, DAY_OFFSET);

	for (i = 0; (i < sizeof(months)) && (token != NULL); i++) {
		free(months[i]);
	}
	for (i = 0; (i < sizeof(days)) && (token != NULL); i++) {
		free(days[i]);
	}

}

