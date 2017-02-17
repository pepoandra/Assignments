# -*- coding: utf-8 -*-
# Python 2.7
# Alexander Bratyshkin
# 260684228

import os
import sys
import struct
import copy
#import pickle
from ctypes import *
import cPickle as pickle

history_file_name = "history.pickle"
result_file_name = "result.bmp"
# Libname
LibName = "libfast_filter.so"
os.environ['LD_LIBRARY_PATH'] = os.getcwd()

class History():
    def __init__(self):
        # Previous image from current (Undo), next image from current (Redo) 
        self.undo = []
        self.redo = []
        # Current active image
        self.active = None

    def getActiveBMP(self):
        return self.active

    def reset(self, bmp_bytes): # Load option
        self.undo = []
        self.redo = []
        self.active = copy.deepcopy(bmp_bytes)

    def setNewPoint(self, bmp_bytes): # Filter option
        self.redo = [] # Discar forwad history
        self.undo.append(self.active)
        self.active = copy.deepcopy(bmp_bytes)

    def backward(self): # Undo option
        if len(self.undo) == 0: return False
        self.redo.append(self.active)
        self.active = self.undo.pop()
        return True

    def forward(self): # Reado option
        if len(self.redo) == 0: return False
        self.undo.append(self.active)
        self.active = self.redo.pop()
        return True


def print_usage():
    print("Usage: ./" + os.path.basename(__file__) + " load | filter | undo | redo\n")
    print("\t -load <input image path> (loads the specified file, discards any existing history and makes this image the active image).\n")
    print("\t -filter <filter width> <filter weights> (applies the specified filter to the previously active image. The filtered result becomes the new active image and is placed at the next step in the history. Any previous history will be truncated).\n")
    print("\t -undo (moves the active image backwards by one step in history, if this is possible).\n")
    print("\t -redo (moves the active image forwards by one step in history, if this is possible).")


# Read the filter information from command line and 
# set it up to be used on the image  
def parseFilterCmdArgs( cmd_args ):

    filter_width = int( cmd_args[0] )
    filter_weights = []
    filter_offsets = []

    for i in range(0, filter_width * filter_width):
        filter_weights.append( float(cmd_args[1+i] ))

    return ( filter_width, filter_weights )

# Reads a BMP image from disk into a convenient array format
def loadBMPImage( img_file_name ):
    data = []
    with open( img_file_name, 'rb' ) as bmp_file:
        #data = bmp_file.read()
        byte = ' '
        while byte != "":
            # Do stuff with byte.
            byte = bmp_file.read(1)
            if byte != '': data.append(ord(byte))
    return data

def saveBMPImage( img_file_name, bmp_bytes ):
    with open( img_file_name, 'wb' ) as bmp_file:
        bmp_file.write( "".join(map(chr, bmp_bytes)) )
        #for byte in bmp_bytes:
        #    bmp_file.write(chr(byte))


def main():
    # Check user command line arguments
    if len(sys.argv) < 2:
        print_usage()
        return
    cmd = sys.argv[1]
    # Check that user input is valid command
    if cmd not in ["load", "filter", "undo", "redo"]:
        print("Unknow command: %s" % cmd)
        return
    # Create initialy empty history
    history = History()
    # Load history file or create empty
    try:
        with open("history.pickle", "rb") as history_file:            
            history = pickle.load(history_file)
    except Exception as e:
        print("Error reading history file. " + str(e))
        pass
    #print("Undo: ", len(history.undo))
    #print("Redo: ", len(history.redo))
    #print("Active: ", history.active != None)
    # Process command
    if cmd == "load":
        try:
            # Load input file (function return list of bytes)
            bmp_bytes = loadBMPImage(sys.argv[2])
            history.reset(bmp_bytes)
            saveBMPImage(result_file_name, bmp_bytes)

        except Exception as e:
            print("Loading new image error ! Details: " + str(e))

    elif cmd == "filter":
        try:
            # Parse arguments
            (filter_width, filter_weights) = parseFilterCmdArgs( sys.argv[2:] )
        except Exception as e:
            print("Error filter arguments ! Details: " + str(e))
            return
        try:
            # Load input file (function return list of bytes)
            bmp_bytes = loadBMPImage(result_file_name) # Load previous image
            # Create empty list for result image
            result_bytes = [0]* len(bmp_bytes)#copy.deepcopy(bmp_bytes)
            # Calculate path to library
            path_to_lib = os.path.join(os.environ['LD_LIBRARY_PATH'], LibName)
            # Load library
            filter_lib = cdll.LoadLibrary(path_to_lib)
            # Convert data to c types
            c_bmp_bytes = (c_ubyte * len(bmp_bytes))(*bmp_bytes)
            c_filter_weights = (c_float * len(filter_weights))(*filter_weights)    
            c_result_bytes = (c_ubyte * len(result_bytes))(*result_bytes)
            # Call external routine
            filter_lib.doFiltering(c_bmp_bytes, c_filter_weights, filter_width, c_result_bytes)
            # Save result image
            with open(result_file_name, "wb") as result_file:
                result_file.write(c_result_bytes)
            # Set new history point
            history.setNewPoint([c_result_bytes[i] for i in range(len(c_result_bytes))])

        except Exception as e:
            print("Filter apply error: " + str(e))
            return

    elif cmd == "undo":
        if history.backward() == False:
            print("Unable to process undo command")
            return
        saveBMPImage(result_file_name, history.getActiveBMP())

    elif cmd == "redo":
        if history.forward() == False:
            print("Unable to process redo command")
            return
        saveBMPImage(result_file_name, history.getActiveBMP())

    #print("Undo: ", len(history.undo))
    #print("Redo: ", len(history.redo))
    #print("Active: ", history.active != None)
    ## Save history
    with open(history_file_name, "wb") as history_file:
        pickle.dump(history, history_file)

if __name__ == "__main__":
    main()
