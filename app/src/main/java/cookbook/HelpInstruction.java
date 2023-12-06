package cookbook;

public class HelpInstruction {
  
  private String heading;
  private String step;
  private String imageID;

  public HelpInstruction(){

  }

  /**
   * Crates a help a help instruction object.
   *
   * @param d description of the step
   * @param p image id of the step
   */
  public HelpInstruction(String h,String d, String p){
    heading = h;
    step = d;
    imageID = p;
  }

  public String getStep() {
    return step;
  }

  public String getImageID() {
    return imageID;
  }

  public void setStep(String step) {
    this.step = step;
  }

  public void setImageID(String imageID) {
    this.imageID = imageID;
  }

  public String getHeading() {
    return heading;
  }

  public void setHeading(String heading) {
    this.heading = heading;
  }


}
