#!/usr/bin/python3
import sys
file = open(sys.argv[1], "r")
text = file.read()
print(text.count("L.marker"))
