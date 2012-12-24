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
	
	"""url = "http://onlineraceresults.com/race/view_race.php?race_id=16514&relist_record_type=result&lower_bound=0&upper_bound=1904&use_previous_sql=1&group_by=default#racetop"
	
	file = urllib.urlopen(url)
	print file.read()
	"""
	
	if(len(sys.argv) == 1):
		print "Please enter a filename"
		sys.exit()
		
	if(len(sys.argv) == 2):
		print "Please enter a race name"
		sys.exit()
	
	
	fileName = sys.argv[1]
	dateIn = sys.argv[2]
	raceName = sys.argv[3]
	
	date = dateIn
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
			outFile.write("	- !!models.RaceResultInfo \n");
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
			
			toFile  = "		raceName:	" + raceName + "\n"
			toFile += "		firstName:	" + firstName + "\n"
			toFile += "		lastName:	" + lastName + "\n"
			toFile += "		seconds:	" + str(seconds) + "\n"
			toFile += "		date:	" + str(date) + "\n"
			
			outFile.write(toFile + "\n")
			count+= 1
		
	outFile.close()
	
	print "Runners logged: " + str(count)
	print "File Created: " + fileName + "Out.yml"
	
	
	
		