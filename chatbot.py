#ANDRADE - 260513637
import sys
import os
import stat
import re
import random

def msg_gen(last_word):
	global input_msg
	global full_dict
	chosen_word = ""
	if(len(input_msg) < 20):
		pair_list = []
		for word in full_dict:
			if (word.split("-")[0] == last_word):
				pair_list.append(word.split("-")[-1])
		if not pair_list:
			chosen_word = random.choice(full_dict.keys()).split("-")[0]
		else:
			chosen_word =  random.choice(pair_list)
		input_msg.append(chosen_word + " ")
		if not chosen_word.endswith(p):
			msg_gen(chosen_word)

full_dict = {}
if (len(sys.argv) < 2):
		raise Exception("Enter at least 1 (ONE) text file, please.  Thank you kindly.")
for textFile in sys.argv[1:]:
	fileContent = ""
	try:
		thisFile = open(textFile, "r")
		fileContent = (re.sub(r'([^\s\w.!?-]|_|\n)+', ' ', thisFile.read().lower()))
	except IOError:
		print "ERROR 404: FILE NOT FOUND"
	thisFile.close()
	prev_word = ''
	for word in fileContent.replace('-', ' ').split(" "):
		if "%s-%s" % (prev_word,word) in full_dict and word != '':
			full_dict["%s-%s" % (prev_word,word)]+= 1
		elif prev_word != '' and word != '':
			full_dict["%s-%s" % (prev_word,word)] = 1
		if word != '':
			prev_word = word
p = (".", "!", "?")
while(True):
	user_msg = raw_input("Send: " )
	input_msg = []
	msg_gen(user_msg.split(" ")[-1])
	print "Receive: " + (''.join(input_msg)).capitalize().strip(),
	if not input_msg[-1].strip().endswith(p):
		sys.stdout.write(".")
	print ""
