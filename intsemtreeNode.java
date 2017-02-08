import java.util.ArrayList;
import java.util.Collections;



public class intsemtreeNode {
  int value;
  intsemtreeNode parent = null;
  ArrayList<intsemtreeNode> children = new ArrayList<intsemtreeNode>();

  public intsemtreeNode(int val) {
    this.value = val;
  }

  //multiple ways to add children  both not needed

  public void addChild(intsemtreeNode child) {
    child.setParent(this);
    this.children.add(child);
  }

  public void addChild(int val) {
    intsemtreeNode newChild = new intsemtreeNode(val);
    newChild.setParent(this);
    children.add(newChild);
  }

  //adding multiple children at the same time. Not needed

  public void addChildren(ArrayList<intsemtreeNode> children) {
    for(intsemtreeNode t : children) {
      t.setParent(this);
    }
    this.children.addAll(children);
  }

  public ArrayList<intsemtreeNode> getChildren() {
    return children;
  }

  public int getVal() {
    return value;
  }

  public void setVal(int val) {
    this.value = val;
  }

  public void setParent(intsemtreeNode par) {
    this.parent = par;
  }

  public intsemtreeNode getParent () {
    return parent;
  }

  //TODO implement search function based on given value
}
