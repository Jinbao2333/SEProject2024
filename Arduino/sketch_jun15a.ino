/*!
 *@file NTAG21xReadWrite.ino
 *@brief read data and write data to nfc card
 *@details This demo runs on the arduino platform
 *@         Download this demo to write something into the card which type is NTAG21x,then reads what has been written
 *@copyright  Copyright (c) 2010 DFRobot Co.Ltd (http://www.dfrobot.com)
 *@license     The MIT license (MIT)
 *@author [fengli](li.feng@dfrobot.com)
 *@version  V1.0
 *@date  2019-7-3
 *@url https://github.com/DFRobot/DFRobot_PN532
 */
#include <DFRobot_PN532.h>

#define block          (5)

#define  PN532_IRQ      (2)
#define  INTERRUPT      (1)
#define  POLLING        (0)
//use this line for a breakout or shield with an I2C connection
//check the card by polling
DFRobot_PN532_IIC  nfc(PN532_IRQ, POLLING);
DFRobot_PN532:: sCard_t NFCcard;

void setup() {
  Serial.begin(115200);
  while (!nfc.begin()) {
    //Wait for NFC module activation
    // Serial.println("initial failure");
    delay (1000);
  }
  // Serial.println("Waiting for a card......");
}
