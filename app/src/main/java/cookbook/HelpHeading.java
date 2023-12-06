package cookbook;

import java.util.ArrayList;

public class HelpHeading {

  String heading;
  int headingId;
  ArrayList <HelpInstruction> setOfInstructions;

  public HelpHeading(){

  }

  public HelpHeading(String h, ArrayList <HelpInstruction> i){
    heading = h;
    setOfInstructions = i;
  }

  public String getHeading() {
    return heading;
  }

  public void setHeading(String heading) {
    this.heading = heading;
  }
  
  public ArrayList<HelpInstruction> getSetOfInstructions() {
    return setOfInstructions;
  }

  public void setSetOfInstructions(ArrayList<HelpInstruction> setOfInstructions) {
    this.setOfInstructions = setOfInstructions;
  }

  public int getHeadingId() {
    return headingId;
  }

  public void setHeadingId(int headingId) {
    this.headingId = headingId;
  }

  
}
