from djitellopy import Tello
import keyboard
import logging
import cv2

Tello.LOGGER.setLevel(logging.WARNING)
drone = Tello()
try:
    drone.connect()

    drone.streamon()
    frame_reader = drone.get_frame_read()

    speed = 40

    while not keyboard.is_pressed('l'):
        frame = frame_reader.frame
        cv2.imshow("Tello Camera", frame)
        cv2.waitKey(1)

        if keyboard.is_pressed('t'):
            drone.takeoff()
        if keyboard.is_pressed('c'):
             drone.curve_xyz_speed(50,50,0,100,0,0,30)
        if keyboard.is_pressed('f'):
            drone.flip_forward()

        left_right = 0
        forward_back = 0
        up_down = 0
        yaw = 0

        if keyboard.is_pressed('w'):
            forward_back = speed
        if keyboard.is_pressed('a'):
            left_right = -speed
        if keyboard.is_pressed('s'):
            forward_back = -speed
        if keyboard.is_pressed('d'):
            left_right = speed

        if keyboard.is_pressed('up'):
            up_down = speed
        if keyboard.is_pressed('down'):
            up_down = -speed
        if keyboard.is_pressed('left'):
            yaw = -speed
        if keyboard.is_pressed('right'):
            yaw = speed

        drone.send_rc_control(
            left_right,
            forward_back,
            up_down,
            yaw
        )
    drone.send_rc_control(0,0,0,0)
    drone.land()
finally:
    drone.end()