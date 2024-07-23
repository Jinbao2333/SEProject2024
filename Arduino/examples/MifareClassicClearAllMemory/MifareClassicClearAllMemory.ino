/*!
 *@file MifareClassicClearAllMemory.ino
 *@brief clear all data areas on MifareClassic card
 *@details  This demo runs on the arduino platform.
 *@n        Download this demo to clear all data areas.
 *@n        We can read the data on the card to see if the write is successful.
 *@copyright  Copyright (c) 2010 DFRobot Co.Ltd (http://www.dfrobot.com)
 *@license     The MIT license (MIT)
 *@author [fengli](li.feng@dfrobot.com)
 *@version  V1.0
 *@date  2019-7-3
 *@url https://github.com/DFRobot/DFRobot_PN532
 */
#include <DFRobot_PN532.h>

#define  PN532_IRQ      (2)
#define  INTERRUPT      (1)
#define  POLLING        (0)
//use this line for a breakout or shield with an I2C connection
//check the card by polling
DFRobot_PN532_IIC  nfc(PN532_IRQ, POLLING);
uint8_t data[16] = {0};
DFRobot_PN532:: sCard_t NFCcard;

void setup() {
  Serial.begin(115200);
  while (!nfc.begin()) {
    Serial.println("initial failure");
    delay (1000);
  }
  Serial.println("Clear all the data in the card.......");
  Serial.println("Waiting for a card......");
}

void loop() {
  /*Check if there is an IC card*/
  if (nfc.scan()) {
    /*!
      The 1024 × 8 bit EEPROM memory is organized in 16 sectors of 4 blocks. One block
      contains 16 bytes.
      Sector  0    block 0           (Manufacturer   block)              Manufacturer  Data
                   block 1           (data)
                   block 2           (data)
                   block 3           (Sector Trailer 0)                  keyA   Access Bits   KeyB
      Sector  1    block 4           (data)
                   block 5           (data)
                   block 6           (data)
                   block 7           (Sector Trailer 1)                  keyA   Access Bits   KeyB
      ...
      ...
      ...
      Sector 15    block 60           (data)
                   block 61           (data)
                   block 62           (data)
                   block 63           (Sector Trailer 15)                  keyA   Access Bits   KeyB
    */
    NFCcard = nfc.getInformation();
    if (nfc.scan() == true && (NFCcard.AQTA[1] == 0x02 || NFCcard.AQTA[1] == 0x04)) {
      Serial.println("Clear all the data in the card...");
      for (int i = 0; i < NFCcard.blockNumber; i++) {
        if (i == 0) {
          Serial.print("block:"); Serial.print(i); Serial.println(" Producer area, it has been written dead");
        }
        else if ((i + 1) % 4 == 0 && i < 128) {
          Serial.print("block:"); Serial.print(i); Serial.println(" Key area, please modify carefully");
        }
        else if ((i + 1) % 16 == 0 && i > 128) {
          Serial.print("block:"); Serial.print(i); Serial.println(" Key area, please modify carefully");
        }
        else {
          Serial.print("block:"); Serial.print(i);
          if (nfc.writeData(i, data))   Serial.println(" clear success");
          else Serial.println(" clear failure");
        }
      }
    }
    else {
      Serial.println("The card type is not mifareclassic...");
    }
  }
  else {
    //Serial.println("no card!");
  }
  delay(2000);
}
