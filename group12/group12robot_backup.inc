define robot_laser laser
(
	range_min 0.0
	range_max 3.5
	fov 180
	pose [0.3125 0.0625 0]
	size [0.025 0.025]
)

define robot_fiducial fiducialfinder
(
)


define robot_eyes ptz
(
	blobfinder
	(
	channels_count 5
	channels ["orange" "blue" "green" "red" "pink"]
	image [160 120]
	range_max 10.00
	ptz [10 10 60]
	)
)

define robot_grip gripper
(
	size [0.3 0.3]
	pose [0.425 0.0 0]
)

define robot_sonar ranger
(
	 scount 8 # number of Sonars

	 # Sonar positions x, y, angle
	 spose[0] [ 0.375 0.1 40 ] #Front north
	 spose[1] [ 0.375 -0.1 320 ] # Front south
	 spose[2] [ 0.10 0.25 90 ]   #body North rear	
	 spose[3] [ 0.10 -0.25 270 ] #body South rear
	 spose[4] [ 0.25 0.20 65 ]  #body north front
	 spose[5] [ 0.25 -0.20 295 ] #body south front
	 spose[6] [ 0.25 0.05 12.5 ]   #Front middle top
	 spose[7] [ 0.25 -0.05 347.5]  #Front middle bottom
	
	# Sonar Device Size
	 ssize[0] [ 0.01 0.05 ]
	 ssize[1] [ 0.01 0.05 ] 	
	 ssize[2] [ 0.01 0.05 ]
	 ssize[3] [ 0.01 0.05 ]
	 ssize[4] [ 0.01 0.05 ]
	 ssize[5] [ 0.01 0.05 ] 	
	 ssize[6] [ 0.01 0.05 ]
	 ssize[7] [ 0.01 0.05 ]

	# Sonar Range
	 sview[0] [ 0.3 2.0 10 ]
	 sview[1] [ 0.3 2.0 10 ]
	 sview[2] [ 0.3 2.0 10 ]
	 sview[3] [ 0.3 2.0 10 ]
	 sview[4] [ 0.3 2.0 10 ]
	 sview[5] [ 0.3 2.0 10 ]
	 sview[6] [ 0.3 2.0 10 ]
	 sview[7] [ 0.3 2.0 10 ]
)




define robot position
(
	#size of robot
	size [0.625 0.5]

	#centre of rotation offset
	origin [0.125 0 0]

	#shape of robot
	
	polygons 1
	polygon[0].points 6
	polygon[0].point[0] [0 0]
	polygon[0].point[1] [0 1]
	polygon[0].point[2] [0.75 1]
	polygon[0].point[3] [1 0.75]
	polygon[0].point[4] [1 0.25]
	polygon[0].point[5] [0.75 0]

robot_laser()
robot_grip()
robot_eyes()
robot_sonar()
robot_fiducial()

)
