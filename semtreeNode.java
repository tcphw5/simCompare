import java.util.ArrayList;
import java.util.Collections;



public class semtreeNode {
  String value;
  semtreeNode parent = null;
  ArrayList<semtreeNode> children = new ArrayList<semtreeNode>();

  public semtreeNode(String val) {
    this.value = val;
  }

  //multiple ways to add children  both not needed

  public void addChild(semtreeNode child) {
    child.setParent(this);
    this.children.add(child);
  }

  public void addChild(String val) {
    semtreeNode newChild = new semtreeNode(val);
    newChild.setParent(this);
    children.add(newChild);
  }

  //adding multiple children at the same time. Not needed

  public void addChildren(ArrayList<semtreeNode> children) {
    for(semtreeNode t : children) {
      t.setParent(this);
    }
    this.children.addAll(children);
  }

  public ArrayList<semtreeNode> getChildren() {
    return children;
  }

  public String getVal() {
    return value;
  }

  public void setVal(String val) {
    this.value = val;
  }

  public void setParent(semtreeNode par) {
    this.parent = par;
  }

  public semtreeNode getParent () {
    return parent;
  }
}
