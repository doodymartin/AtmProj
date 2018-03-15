/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.datamodel;

/**
 * Class used to hold the following information of notes in the ATM
 *
 * 1) The Denomination of the note
 * 2) The number of the notes held in ATM
 * 3) The total amount available of this type of denomination in ATM
 */
public class AtmEngineCurrency {

    int demonination;
    int currentNumberOfNotes;
    int currentAmount;

    public AtmEngineCurrency(int demonination, int currentNumberOfNotes) {
        this.demonination = demonination;
        this.currentNumberOfNotes = currentNumberOfNotes;
        this.currentAmount = (demonination * currentNumberOfNotes);
    }

    public int getDemonination() {
        return demonination;
    }
    public void setDemonination(int demonination) {
        this.demonination = demonination;
    }
    public int getCurrentNumberOfNotes() {
        return currentNumberOfNotes;
    }
    public void setCurrentNumberOfNotes(int currentNumberOfNotes) {
        this.currentNumberOfNotes = currentNumberOfNotes;
    }
    public int getCurrentAmount() {
        return currentAmount;
    }
    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }
    public void recalculateCurrentAmount() {
        this.currentAmount = this.demonination * this.currentNumberOfNotes;
    }

}
