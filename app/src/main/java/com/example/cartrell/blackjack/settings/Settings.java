package com.example.cartrell.blackjack.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class Settings implements Parcelable {
  //=========================================================================
  // static / const
  //=========================================================================
  public final static String INTENT_KEY = "settings";

  //=========================================================================
  // members
  //=========================================================================
  private int m_credits;
  private boolean m_isSoundOn;
  private boolean m_isMusicOn;
  private int m_totalGamesPlayed;
  private int m_totalGamesWon;
  private int m_numBj;
  private int m_numSplits;
  private int m_numSurrenders;
  private int m_numBlitzs;
  private int m_numDoubles;
  private int m_numThunderjacks;

  //=========================================================================
  // public
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  public Settings() {
  }

  //-------------------------------------------------------------------------
  // CREATOR
  //-------------------------------------------------------------------------
  public static final Parcelable.Creator<Settings> CREATOR = new Parcelable.Creator<Settings>() {

    //-------------------------------------------------------------------------
    // createFromParcel
    //-------------------------------------------------------------------------
    public Settings createFromParcel(Parcel in) {
      return(new Settings(in));
    }

    //-------------------------------------------------------------------------
    // newArray
    //-------------------------------------------------------------------------
    public Settings[] newArray(int size) {
      return(new Settings[size]);
    }
  };

  //-------------------------------------------------------------------------
  // describeContents
  //-------------------------------------------------------------------------
  @Override
  public int describeContents() {
    return(0);
  }

  //-------------------------------------------------------------------------
  // getCredits
  //-------------------------------------------------------------------------
  public int getCredits() {
    return(m_credits);
  }

  //-------------------------------------------------------------------------
  // getIsMusicOn
  //-------------------------------------------------------------------------
  public boolean getIsMusicOn() {
    return(m_isMusicOn);
  }

  //-------------------------------------------------------------------------
  // getIsSoundOn
  //-------------------------------------------------------------------------
  public boolean getIsSoundOn() {
    return(m_isSoundOn);
  }

  //-------------------------------------------------------------------------
  // getNumBj
  //-------------------------------------------------------------------------
  public int getNumBj() {
    return(m_numBj);
  }

  //-------------------------------------------------------------------------
  // getNumBlitzs
  //-------------------------------------------------------------------------
  public int getNumBlitzs() {
    return(m_numBlitzs);
  }

  //-------------------------------------------------------------------------
  // getNumDoubles
  //-------------------------------------------------------------------------
  public int getNumDoubles() {
    return(m_numDoubles);
  }

  //-------------------------------------------------------------------------
  // getNumSplits
  //-------------------------------------------------------------------------
  public int getNumSplits() {
    return(m_numSplits);
  }

  //-------------------------------------------------------------------------
  // getNumSurrenders
  //-------------------------------------------------------------------------
  public int getNumSurrenders() {
    return(m_numSurrenders);
  }

  //-------------------------------------------------------------------------
  // getNumThunderjacks
  //-------------------------------------------------------------------------
  public int getNumThunderjacks() {
    return(m_numThunderjacks);
  }

  //-------------------------------------------------------------------------
  // getTotalGamesPlayed
  //-------------------------------------------------------------------------
  public int getTotalGamesPlayed() {
    return(m_totalGamesPlayed);
  }

  //-------------------------------------------------------------------------
  // getTotalGamesWon
  //-------------------------------------------------------------------------
  public int getTotalGamesWon() {
    return(m_totalGamesWon);
  }

  //-------------------------------------------------------------------------
  // reset
  //-------------------------------------------------------------------------
  public void reset() {
    m_credits = m_totalGamesPlayed = m_totalGamesWon = m_numBj = m_numSplits = m_numSurrenders =
      m_numBlitzs = m_numDoubles = m_numThunderjacks = 0;
  }

  //-------------------------------------------------------------------------
  // setCredits
  //-------------------------------------------------------------------------
  public void setCredits(int value) {
    m_credits = value;
  }

  //-------------------------------------------------------------------------
  // setIsMusicOn
  //-------------------------------------------------------------------------
  public void setIsMusicOn(boolean value) {
    m_isMusicOn = value;
  }

  //-------------------------------------------------------------------------
  // setIsSoundOn
  //-------------------------------------------------------------------------
  public void setIsSoundOn(boolean value) {
    m_isSoundOn = value;
  }

  //-------------------------------------------------------------------------
  // setNumBj
  //-------------------------------------------------------------------------
  public void setNumBj(int value) {
    m_numBj = value;
  }

  //-------------------------------------------------------------------------
  // setNumBlitzs
  //-------------------------------------------------------------------------
  public void setNumBlitzs(int value) {
    m_numBlitzs = value;
  }

  //-------------------------------------------------------------------------
  // setNumDoublesWon
  //-------------------------------------------------------------------------
  public void setNumDoublesWon(int value) {
    m_numDoubles = value;
  }

  //-------------------------------------------------------------------------
  // setNumSplits
  //-------------------------------------------------------------------------
  public void setNumSplits(int value) {
    m_numSplits = value;
  }

  //-------------------------------------------------------------------------
  // setNumSurrenders
  //-------------------------------------------------------------------------
  public void setNumSurrenders(int value) {
    m_numSurrenders = value;
  }

  //-------------------------------------------------------------------------
  // setNumThunderjacks
  //-------------------------------------------------------------------------
  public void setNumThunderjacks(int value) {
    m_numThunderjacks = value;
  }

  //-------------------------------------------------------------------------
  // setTotalGamesPlayed
  //-------------------------------------------------------------------------
  public void setTotalGamesPlayed(int value) {
    m_totalGamesPlayed = value;
  }

  //-------------------------------------------------------------------------
  // setTotalGamesWon
  //-------------------------------------------------------------------------
  public void setTotalGamesWon(int value) {
    m_totalGamesWon = value;
  }

  //-------------------------------------------------------------------------
  // writeToParcel
  //-------------------------------------------------------------------------
  @Override
  public void writeToParcel(Parcel out, int flags) {
    out.writeInt(m_credits);
    out.writeInt(m_isSoundOn ? 1 : 0);
    out.writeInt(m_isMusicOn ? 1 : 0);
    out.writeInt(m_totalGamesPlayed);
    out.writeInt(m_totalGamesWon);
    out.writeInt(m_numBj);
    out.writeInt(m_numSplits);
    out.writeInt(m_numSurrenders);
    out.writeInt(m_numBlitzs);
    out.writeInt(m_numDoubles);
    out.writeInt(m_numThunderjacks);
  }

  //=========================================================================
  // private
  //=========================================================================

  //-------------------------------------------------------------------------
  // ctor
  //-------------------------------------------------------------------------
  private Settings(Parcel in) {
    m_credits = in.readInt();
    m_isSoundOn = in.readInt() == 1;
    m_isMusicOn = in.readInt() == 1;
    m_totalGamesPlayed = in.readInt();
    m_totalGamesWon = in.readInt();
    m_numBj = in.readInt();
    m_numSplits = in.readInt();
    m_numSurrenders = in.readInt();
    m_numBlitzs = in.readInt();
    m_numDoubles = in.readInt();
    m_numThunderjacks = in.readInt();
  }
}