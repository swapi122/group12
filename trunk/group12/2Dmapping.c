/*
 ******************************************************************************
 *                          UNIVERSIDADE DE SAO PAULO                         *
 *             INSTITUTO DE CIENCIAS MATEMATICAS E DE COMPUTACAO              *
 *----------------------------------------------------------------------------*
 * DESENVOLVIDO POR: HEITOR DE FREITAS VIEIRA                DATA: 07/03/2009 *
 *                                                                            *
 * DEPENDENCIAS: PLAYER, STAGE e OPENCV                                       *
 *                                                                            *
 * PODE SER ALTERADO E DISTIBUIDO LIVREMENTE, DESDE QUE MENCIONADO O AUTOR.   *
 * TESTADO NO UBUNTU 8.10                                                     *
 *                                                                            *
 * Para compilar:                                                             *
 * gcc 2Dmapping.c -o 2Dmapping -I/usr/local/include/player-2.1 -L/usr/local/lib -lplayerc -lm -lplayerxdr -lplayererror -lcv -lhighgui -lcxcore -ljpeg
 ******************************************************************************
 */

#include <stdio.h>
#include <libplayerc/playerc.h>
#include <opencv/cv.h>
#include <opencv/highgui.h>
#include <math.h>

#define PI 3.14159265

//Convert angles into radians
float graus2rad (int graus) {
   float temp = ((graus*PI)/180);
   return temp;
}

//floating point into numbers then round
int arredonda (float num) {
    int temp = (int)(num*100);
    temp = temp%10;
    if (temp >= 5) return (int)((num*10)+1);
    else return (int)(num*10);
}

int main(int argc, const char **argv)
{
    //Variables
    int degrees,PosRelX,PosRelY;
    float radians,Dlaser,ODM_ang, ang;
    int width = 500, height = 500; //Create the size of the map here (in pixel)
    int centroX = (width / 2);
    int centroY = (height / 2);
    playerc_client_t *client;
    playerc_laser_t *laser;
    playerc_position2d_t *position2d;
    CvPoint pt,pt1,pt2;
    CvScalar cinzaE,preto,cinzaC;
    char window_name[] = "Map";

    IplImage* image = cvCreateImage( cvSize(width,height), 8, 3 );
    cvNamedWindow(window_name, 1 );
    preto = CV_RGB(0, 0, 0);        //for indicating obstacles
    cinzaE = CV_RGB(92, 92, 92);    //To indicate the stranger
    cinzaC = CV_RGB(150, 150, 150); //To indicate free spaces

    client = playerc_client_create(NULL, "localhost", 6665);
    if (playerc_client_connect(client) != 0)
    return -1;

    laser = playerc_laser_create(client, 0);
    if (playerc_laser_subscribe(laser, PLAYERC_OPEN_MODE))
    return -1;

    position2d = playerc_position2d_create(client, 0);
    if (playerc_position2d_subscribe(position2d, PLAYERC_OPEN_MODE) != 0) {
        fprintf(stderr, "error: %s\n", playerc_error_str());
        return -1;
    }

    if (playerc_client_datamode (client, PLAYERC_DATAMODE_PULL) != 0) {
        fprintf(stderr, "error: %s\n", playerc_error_str());
        return -1;
    }

    if (playerc_client_set_replace_rule (client, -1, -1, PLAYER_MSGTYPE_DATA, -1, 1) != 0) {
        fprintf(stderr, "error: %s\n", playerc_error_str());
        return -1;
    }

    playerc_position2d_enable(position2d, 1);  // initialise motors
    playerc_position2d_set_odom(position2d, 0, 0, 0);  // Set odometer to zero

    cvSet(image, cinzaE,0); //set the image colour to dark
    pt.x = centroX;  // Zero coordinate for x
    pt.y = centroY;  // Zero coordinate for y


    while(1) {
        playerc_client_read(client);
        cvSaveImage("mapa.jpg",image);
        playerc_client_read(client);

        for (degrees = 2; degrees <= 360; degrees+=2) {
            Dlaser = laser->scan[degrees][0];
            if (Dlaser < 8) {
                radians = graus2rad (degrees/2);      //Convert the angle of the laser to radians
                ODM_ang = position2d->pa;             //Obtain the angle relative to the robot
                ang = ((1.5*PI)+radians+ODM_ang);     //Converte the angle relative to the world
                PosRelX = arredonda(position2d->px);  //Position x relative to robot
                PosRelY = arredonda(position2d->py);  //Position y relative to robot
                pt1.y = (centroY-PosRelY);            //Co-ordinated global y of the robot
                pt1.x = (centroX+PosRelX);            //Co-ordinated global x of the robot

 //t converts polar coordinates for rectangular (global)
                pt.y = (int)(pt1.y-(sin(ang)*Dlaser*10));
                pt.x = (int)(pt1.x+(cos(ang)*Dlaser*10));

                //The free area draws cvline
                cvLine(image, pt1,pt,cinzaC, 1,4,0);

                //marks the object in the map
                cvLine(image, pt,pt,preto, 1,4,0);

                //Shows the result of the map to the screen
                cvShowImage(window_name, image );
                cvWaitKey(10);
            }
        }
    }

    //Disconnect player
    playerc_laser_unsubscribe(laser);
    playerc_laser_destroy(laser);
    playerc_client_disconnect(client);
    playerc_client_destroy(client);

    //Destroy the OpenCV window cvReleaseImage
    cvReleaseImage(&image);
    cvDestroyWindow(window_name);
    return 0;
}



