//example class that shows the semantic tree being created
//and used... coudlve added the children without creating by
//passing value. Keeping a few test cases uncommented to show
//how it works

public class TreeTester {
  public static void main(String[] args) {
    semtreeNode root = new semtreeNode("root");

    semtreeNode c1d1 = new semtreeNode("home");
    semtreeNode c2d1 = new semtreeNode("Food");
    semtreeNode c3d1 = new semtreeNode("Outdoor activity");
    semtreeNode c4d1 = new semtreeNode("School");
    semtreeNode c5d1 = new semtreeNode("store");

    semtreeNode c1d2 = new semtreeNode("McDon");
    semtreeNode c2d2 = new semtreeNode("DQ");
    semtreeNode c3d2 = new semtreeNode("TacoBell");
    semtreeNode c4d2 = new semtreeNode("Park");
    semtreeNode c5d2 = new semtreeNode("Trail");
    semtreeNode c6d2 = new semtreeNode("RollaHigh");
    semtreeNode c7d2 = new semtreeNode("MST");
    semtreeNode c8d2 = new semtreeNode("walmart");
    semtreeNode c9d2 = new semtreeNode("Kmart");
    semtreeNode c10d2 = new semtreeNode("foodstores");

    semtreeNode c1d3 = new semtreeNode("aldi");
    semtreeNode c2d3 = new semtreeNode("kroger");

    root.addChild(c1d1);
    root.addChild(c2d1);
    root.addChild(c3d1);
    root.addChild(c4d1);
    root.addChild(c5d1);

    c2d1.addChild(c1d2);
    c2d1.addChild(c2d2);
    c2d1.addChild(c3d2);
    c3d1.addChild(c4d2);
    c3d1.addChild(c5d2);
    c4d1.addChild(c6d2);
    c4d1.addChild(c7d2);
    c5d1.addChild(c8d2);
    c5d1.addChild(c9d2);
    c5d1.addChild(c10d2);

    c10d2.addChild(c1d3);
    c10d2.addChild(c2d3);

    for(semtreeNode node : root.getChildren()) {
      System.out.println(node.getVal());
    }

    System.out.println("********");

    //System.out.println(c2d3.getParent().getChildren();
    for(semtreeNode node : c2d3.getParent().getChildren()) {
      System.out.println(node.getVal());
    }

    System.out.println(c2d3.getParent().getParent().getVal());
    System.out.println(c2d3.getParent().getParent().getParent().getVal());
  }
}
