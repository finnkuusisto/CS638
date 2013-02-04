#!/usr/bin/env python
import urllib
import sys
import datetime
import time
import unittest
""" Used to get Race data from a text file that has the results from http://onlineraceresults.com
   @args Data File
   @args Date
   @args Name of Race
"""
class RunningData():
	

	
	if(len(sys.argv) == 1):
		print "Please enter a filename"
		sys.exit()
	
	raceName = raw_input('Enter the race name: ')

	
	date = raw_input('Enter the date(in long): ')

	distance = raw_input('Enter the distance:');

	done = False
	while(not done):
		unit = raw_input('Enter the unit(miles, kilometers, meters):')
		if(unit == "miles" or unit == "kilometers" or unit == "meters"):
			done = True
		
	
	
	fileName = sys.argv[1]
	file = None
	try:
		file = open(fileName + ".txt")
	except IOError:
		print "File Not Found"
		sys.exit()
  
	lines = file.readlines()
	file.close()
	lineIter = iter(lines)
	count = 1
	
	outFile = open(fileName + "Out.yml",'w')
	def convertTimeToSeconds(str):
		 x = time.strptime(str.split(',')[0],'%H:%M:%S')
		 seconds = datetime.timedelta(hours=x.tm_hour,minutes=x.tm_min,seconds=x.tm_sec).total_seconds()
		 return seconds
		 
	outFile.write("# RaceResultInfo \n \n");
	outFile.write("results: \n\n");
	
	
	lastTime = 0
	for index, line in enumerate(lines):
		
		lineList = line.rsplit();
		
		if(len(lineList)  != 0 and lineList[0] == str(count)):
			outFile.write(" - !!models.RaceResultInfo \n");
			name = lines[index - 2];
			nameList = name.rsplit();
			firstName = nameList[1]
			lastName = nameList[2]
			time = lineList[len(lineList) - 2]
			seconds = convertTimeToSeconds(time)
			
			if (seconds < lastTime):
				print "Parsing error has occurred. Numbers are may not be accurate"
			lastTime = seconds
			out = firstName + " " + lastName + " " + time + " " + str(seconds)
			
			toFile  = "     raceName: " + raceName + "\n"
			toFile += "     firstName: " + firstName + "\n"
			toFile += "     lastName: " + lastName + "\n"
			toFile += "     seconds: " + str(seconds) + "\n"
			toFile += "     date: " + str(date) + "\n"
			toFile += "     km: " + distance + "\n"
			toFile += "     displayUnit: " + unit + "\n"
			
			
			outFile.write(toFile + "\n")
			count+= 1
		
	outFile.close()
	
	print "Runners logged: " + str(count)
	print "File Created: " + fileName + "Out.yml"
	
	
	
		